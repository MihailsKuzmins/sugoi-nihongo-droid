package lv.latvijaff.sugoinihongo.di;

import javax.inject.Singleton;

import dagger.Component;
import lv.latvijaff.sugoinihongo.features.grammarrule.GrammarRuleDetailEditViewModel;
import lv.latvijaff.sugoinihongo.features.grammarrule.GrammarRuleListViewModel;
import lv.latvijaff.sugoinihongo.features.grammarrule.GrammarRuleSearchListViewModel;
import lv.latvijaff.sugoinihongo.features.sentence.SentenceDetailEditViewModel;
import lv.latvijaff.sugoinihongo.features.sentence.SentenceListViewModel;
import lv.latvijaff.sugoinihongo.features.sentence.SentenceSearchListViewModel;
import lv.latvijaff.sugoinihongo.features.system.SettingsAboutListViewModel;
import lv.latvijaff.sugoinihongo.features.system.SettingsListViewModel;
import lv.latvijaff.sugoinihongo.features.word.WordDetailEditViewModel;
import lv.latvijaff.sugoinihongo.features.word.WordFavouritesListViewModel;
import lv.latvijaff.sugoinihongo.features.word.WordListViewModel;
import lv.latvijaff.sugoinihongo.features.word.WordSearchListViewModel;
import lv.latvijaff.sugoinihongo.persistence.repos.WordRepo;

@Singleton
@Component(modules = RepoModule.class)
public interface RepoComponent {

	void inject(WordRepo repo);

	void inject(GrammarRuleListViewModel viewModel);
	void inject(SentenceListViewModel viewModel);
	void inject(WordListViewModel viewModel);
	void inject(GrammarRuleDetailEditViewModel viewModel);
	void inject(SentenceDetailEditViewModel viewModel);
	void inject(WordDetailEditViewModel viewModel);
	void inject(SettingsListViewModel viewModel);
	void inject(SettingsAboutListViewModel viewModel);
	void inject(GrammarRuleSearchListViewModel viewModel);
	void inject(SentenceSearchListViewModel viewModel);
	void inject(WordSearchListViewModel viewModel);
	void inject(WordFavouritesListViewModel viewModel);
}
