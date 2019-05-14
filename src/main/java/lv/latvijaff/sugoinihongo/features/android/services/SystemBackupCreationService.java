package lv.latvijaff.sugoinihongo.features.android.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Pair;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import kotlin.Unit;
import lv.latvijaff.sugoinihongo.App;
import lv.latvijaff.sugoinihongo.constants.AppConstants;
import lv.latvijaff.sugoinihongo.constants.AppConstants.SharedPrefs.Keys;
import lv.latvijaff.sugoinihongo.di.services.SharedPreferencesService;
import lv.latvijaff.sugoinihongo.persistence.repos.GrammarRuleRepo;
import lv.latvijaff.sugoinihongo.persistence.repos.SentenceRepo;
import lv.latvijaff.sugoinihongo.persistence.repos.WordRepo;
import lv.latvijaff.sugoinihongo.persistence.repos.WordSecondaryPropsRepo;
import lv.latvijaff.sugoinihongo.utils.ContextUtils;
import lv.latvijaff.sugoinihongo.utils.FileUtils;
import lv.latvijaff.sugoinihongo.utils.LocalDateUtils;
import lv.latvijaff.sugoinihongo.utils.helpers.CustomSorters;
import timber.log.Timber;


public class SystemBackupCreationService extends IntentService {

	private final Gson mGson;
	private final CompositeDisposable mCleanUp = new CompositeDisposable();

	private final String BACKUP_FILE_WORDS = "words.json";
	private final String BACKUP_FILE_WORD_SECONDARY_PROPS = "wordSecondaryProps.json";
	private final String BACKUP_FILE_SENTENCES = "sentences.json";
	private final String BACKUP_FILE_GRAMMAR_RULES = "grammarRules.json";

	private final Subject<Unit> mWordsDoneSubject = PublishSubject.create();
	private final Subject<Unit> mSecondaryPropsDoneSubject = PublishSubject.create();
	private final Subject<Unit> mSentencesDoneSubject = PublishSubject.create();
	private final Subject<Unit> mGrammarRulesDoneSubject = PublishSubject.create();
	private final Subject<Unit> mBackupCreatedSubject = PublishSubject.create();

	private File mBackupDir;
	private File mTmpBackupDir;

	@Inject SharedPreferencesService preferencesService;
	@Inject WordRepo wordRepoLazy;
	@Inject WordSecondaryPropsRepo wordSecondaryPropsRepo;
	@Inject SentenceRepo sentenceRepo;
	@Inject GrammarRuleRepo grammarRuleRepo;

	public SystemBackupCreationService() {
		super(SystemBackupCreationService.class.getName());

		mGson = new GsonBuilder()
			.registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (src, typeOfSrc, context) -> {
				final long epoch = src.toEpochDay();
				return context.serialize(epoch);
			})
			.registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>) (src, typeOfSrc, context) -> {
				final long epoch = LocalDateUtils.toEpoch(src);
				return context.serialize(epoch);
			})
			.create();
	}

	@Override
	public void onCreate() {
		super.onCreate();

		App app = (App) getApplication();
		app.getAppRepoComponent().inject(this);

		String name = LocalDateUtils.toStringDefaultFormat(LocalDateTime.now());
		mBackupDir = ContextUtils.getBackupDir(this);
		mTmpBackupDir = new File(mBackupDir, "tmp_" + name);

		setupSubscriptions();
	}

	@Override
	protected void onHandleIntent(@Nullable Intent intent) {
		Timber.i("Creating backup");

		int localBackupsCount = preferencesService.getInt(Keys.LOCAL_BACKUP_FILES_COUNT);

		Arrays.stream(mBackupDir.listFiles((dir, name) -> name.endsWith(ContextUtils.BACKUP_EXTENSION)))
			.map(this::getFileCreationTime)
			.sorted(CustomSorters.sort(x -> x.second, CustomSorters.SortingExpression.DESCENDING))
			.skip(localBackupsCount)
			.map(x -> x.first)
			.forEach(FileUtils::deleteFile);

		if (mTmpBackupDir.mkdir()) {
			wordRepoLazy.getCollection(list -> {
				createFile(list, BACKUP_FILE_WORDS);
				mWordsDoneSubject.onNext(Unit.INSTANCE);
			});
		}
	}

	private void setupSubscriptions() {
		Disposable wordsDoneDisp = mWordsDoneSubject
			.subscribe(x -> wordSecondaryPropsRepo.getCollection(list -> {
				createFile(list, BACKUP_FILE_WORD_SECONDARY_PROPS);
				mSecondaryPropsDoneSubject.onNext(Unit.INSTANCE);
			}));

		Disposable secondaryPropsDoneDisp = mSecondaryPropsDoneSubject
			.subscribe(x -> sentenceRepo.getCollection(list -> {
				createFile(list, BACKUP_FILE_SENTENCES);
				mSentencesDoneSubject.onNext(Unit.INSTANCE);
			}));

		Disposable sentencesDoneDisp = mSentencesDoneSubject
			.subscribe(x -> grammarRuleRepo.getCollection(list -> {
				createFile(list, BACKUP_FILE_GRAMMAR_RULES);
				mGrammarRulesDoneSubject.onNext(Unit.INSTANCE);
			}));

		Disposable grammarRulesDoneDisp = mGrammarRulesDoneSubject
			.subscribe(x -> zipFiles());

		Disposable backupCreatedDisp = mBackupCreatedSubject
			.doOnEach(x -> Timber.i("Disposing cleanup"))
			.subscribe(x -> {
				mCleanUp.dispose();
				deleteTmpBackupDir();
			});

		mCleanUp.addAll(wordsDoneDisp, secondaryPropsDoneDisp, sentencesDoneDisp, grammarRulesDoneDisp, backupCreatedDisp);
	}

	private Pair<File, FileTime> getFileCreationTime(File file) {
		try {
			BasicFileAttributes attrs = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
			return new Pair<>(file, attrs.creationTime());
		} catch (IOException e) {
			Timber.e(e);
		}

		Instant instant = LocalDateUtils.toInstant(AppConstants.Models.MIN_DATE_TIME);
		return new Pair<>(file, FileTime.from(instant));
	}

	private void createFile(Object object, String fileName) {
		File file = new File(mTmpBackupDir, fileName);
		try (FileOutputStream stream = new FileOutputStream(file)) {
			byte[] bytes = mGson.toJson(object).getBytes();
			stream.write(bytes);
			Timber.i("Created file: %s", file.getName());
		} catch (IOException e) {
			Timber.e(e);
		}
	}

	private void zipFiles() {
		String name = LocalDateUtils.toStringDefaultFormat(LocalDateTime.now());
		File backupFile = new File(mBackupDir, name + ContextUtils.BACKUP_EXTENSION);

		try (FileOutputStream stream = new FileOutputStream(backupFile); ZipOutputStream zipStream = new ZipOutputStream(stream)) {
			List<File> files = getFilesForBackup();
			for (File file : files) {
				ZipEntry zipEntry = new ZipEntry(file.getName());
				zipStream.putNextEntry(zipEntry);

				writeFile(file, zipStream);
			}
		} catch (IOException e) {
			Timber.e(e);
		} finally {
			mBackupCreatedSubject.onNext(Unit.INSTANCE);
		}

		copyBackupFileToSyncDir(backupFile);
	}

	private void copyBackupFileToSyncDir(File backupFile) {
		File backupSyncFile = new File(ContextUtils.getBackupDirForSync(this), backupFile.getName());

		try {
			FileUtils.copyFile(backupFile, backupSyncFile);
		} catch (IOException e) {
			Timber.e(e);
		}
	}

	private List<File> getFilesForBackup() {
		List<String> backupFiles = Arrays.asList(BACKUP_FILE_WORDS, BACKUP_FILE_WORD_SECONDARY_PROPS, BACKUP_FILE_SENTENCES, BACKUP_FILE_GRAMMAR_RULES);

		return Arrays.stream(mTmpBackupDir.listFiles())
			.filter(x -> backupFiles.contains(x.getName()))
			.collect(Collectors.toList());
	}

	private void writeFile(File file, ZipOutputStream zipStream) throws IOException {
		try (FileInputStream stream = new FileInputStream(file)) {
			FileUtils.writeFile(stream, zipStream);
		}
	}

	private void deleteTmpBackupDir() {
		for (File file : mTmpBackupDir.listFiles()) {
			FileUtils.deleteFile(file);
		}

		if (mTmpBackupDir.delete()) {
			Timber.d("Deleted the temporary backup directory");
		} else {
			Timber.e("Could not delete the temporary backup directory");
		}
	}
}
