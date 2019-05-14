package lv.latvijaff.sugoinihongo.features.android.activities;

import android.os.Bundle;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import lv.latvijaff.sugoinihongo.R;
import lv.latvijaff.sugoinihongo.base.ActivityConfiguration;
import lv.latvijaff.sugoinihongo.base.BaseTabbedActivity;
import lv.latvijaff.sugoinihongo.features.grammarrule.GrammarRuleListFragment;
import lv.latvijaff.sugoinihongo.features.sentence.SentenceListFragment;
import lv.latvijaff.sugoinihongo.features.word.WordListFragment;

public class LanguageMainTabbedActivity extends BaseTabbedActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setTitle(R.string.app_name);
	}

	@NonNull
	@Override
	protected BaseTabbedActivity.PagerAdapter getPagerAdapter() {
		List<Pair<String, Fragment>> fragments = new ArrayList<>(3);
		fragments.add(new Pair<>(getString(R.string.general_words), new WordListFragment()));
		fragments.add(new Pair<>(getString(R.string.general_sentences), new SentenceListFragment()));
		fragments.add(new Pair<>(getString(R.string.general_grammar_rules), new GrammarRuleListFragment()));

		return new LanguageMainTabbedActivityPagerAdapter(getSupportFragmentManager(), fragments);
	}

	@Override
	protected void setupConfiguration(final ActivityConfiguration cfg) {
		cfg.setBackButtonDisplayed(false);
	}

	public static class LanguageMainTabbedActivityPagerAdapter extends BaseTabbedActivity.PagerAdapter {

		LanguageMainTabbedActivityPagerAdapter(
			@NonNull FragmentManager fm,
			@NonNull List<Pair<String, Fragment>> fragments) {
			super(fm, fragments);
		}
	}
}
