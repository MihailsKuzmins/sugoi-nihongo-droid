package lv.latvijaff.sugoinihongo.di;

import javax.inject.Singleton;

import dagger.Component;
import lv.latvijaff.sugoinihongo.features.android.services.SystemBackupCreationService;
import lv.latvijaff.sugoinihongo.features.android.services.WordUnusedWordMarkReductionService;
import lv.latvijaff.sugoinihongo.features.grammarrule.GrammarRuleDetailReadViewModel;
import lv.latvijaff.sugoinihongo.features.sentence.SentenceDetailReadViewModel;
import lv.latvijaff.sugoinihongo.features.study.StudyListOfWordsListViewModel;
import lv.latvijaff.sugoinihongo.features.study.StudyQuizViewModel;
import lv.latvijaff.sugoinihongo.features.study.StudySelectTypeViewModel;
import lv.latvijaff.sugoinihongo.features.system.SettingsStudyViewModel;
import lv.latvijaff.sugoinihongo.features.word.WordDetailReadViewModel;
import lv.latvijaff.sugoinihongo.features.word.WordSecondaryPropsDetailReadViewModel;

@Singleton
@Component(modules = {ServicesModule.class, RepoModule.class})
public interface AppRepoComponent {

	void inject(WordUnusedWordMarkReductionService service);
	void inject(SystemBackupCreationService service);

	void inject(SettingsStudyViewModel viewModel);
	void inject(StudyListOfWordsListViewModel viewModel);
	void inject(StudySelectTypeViewModel viewModel);
	void inject(StudyQuizViewModel viewModel);
	void inject(WordDetailReadViewModel viewModel);
	void inject(SentenceDetailReadViewModel viewModel);
	void inject(GrammarRuleDetailReadViewModel viewModel);
	void inject(WordSecondaryPropsDetailReadViewModel viewModel);
}
