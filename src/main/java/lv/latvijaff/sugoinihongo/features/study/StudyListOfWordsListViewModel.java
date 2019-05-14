package lv.latvijaff.sugoinihongo.features.study;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.function.Consumer;

import javax.inject.Inject;

import lv.latvijaff.sugoinihongo.App;
import lv.latvijaff.sugoinihongo.base.BaseListViewModel;
import lv.latvijaff.sugoinihongo.constants.AppConstants;
import lv.latvijaff.sugoinihongo.di.services.SharedPreferencesService;
import lv.latvijaff.sugoinihongo.persistence.models.WordModel;
import lv.latvijaff.sugoinihongo.persistence.repos.WordRepo;

public class StudyListOfWordsListViewModel extends BaseListViewModel<WordModel> {

	@Inject
	SharedPreferencesService preferencesService;

	@Inject
	WordRepo repo;

	public StudyListOfWordsListViewModel(@NonNull App application) {
		super(application, false);

		getAppRepoComponent().inject(this);
	}

	@Override
	protected void fetchList(Consumer<List<WordModel>> consumer) {
		int studyEntriesCount = preferencesService.getInt(AppConstants.SharedPrefs.Keys.STUDY_ITEMS_COUNT);
		repo.getStudyWordList(studyEntriesCount, consumer);
	}
}
