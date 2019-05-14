package lv.latvijaff.sugoinihongo.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import lv.latvijaff.sugoinihongo.App;
import lv.latvijaff.sugoinihongo.persistence.repos.GrammarRuleRepo;
import lv.latvijaff.sugoinihongo.persistence.repos.SentenceRepo;
import lv.latvijaff.sugoinihongo.persistence.repos.SettingsRepo;
import lv.latvijaff.sugoinihongo.persistence.repos.WordRepo;
import lv.latvijaff.sugoinihongo.persistence.repos.WordSecondaryPropsRepo;

@Module
public class RepoModule {

	private final boolean mIsTestDbInUse;
	private final App mApp;

	public RepoModule(boolean isTestDbInUse, App app) {
		mIsTestDbInUse = isTestDbInUse;
		mApp = app;
	}

	@Provides
	@Singleton
	GrammarRuleRepo provideGrammarRuleRepo() {
		return new GrammarRuleRepo(mIsTestDbInUse);
	}

	@Provides
	@Singleton
	SentenceRepo provideSentenceRepo() {
		return new SentenceRepo(mIsTestDbInUse);
	}

	@Provides
	@Singleton
	WordSecondaryPropsRepo provideWordSecondaryPropsRepo() {
		return new WordSecondaryPropsRepo(mIsTestDbInUse);
	}

	@Provides
	@Singleton
	WordRepo provideWordRepo() {
		return new WordRepo(mIsTestDbInUse, mApp);
	}

	@Provides
	@Singleton
	SettingsRepo provideSettingsRepo() {
		return new SettingsRepo();
	}
}
