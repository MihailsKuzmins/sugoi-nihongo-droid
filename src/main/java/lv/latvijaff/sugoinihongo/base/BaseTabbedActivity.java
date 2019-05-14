package lv.latvijaff.sugoinihongo.base;

import android.os.Bundle;
import android.util.Pair;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.jakewharton.rxbinding3.view.RxView;
import com.jakewharton.rxbinding3.viewpager.RxViewPager;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import lv.latvijaff.sugoinihongo.R;

public abstract class BaseTabbedActivity extends BaseActivity {

	@BindView(R.id.base_tabbed_toolbar)
	Toolbar toolbar;

	@BindView(R.id.base_tabbed_tab_layout)
	TabLayout tabLayout;

	@BindView(R.id.base_tabbed_view_pager)
	ViewPager viewPager;

	@BindView(R.id.base_tabbed_fab)
	FloatingActionButton fab;

	private Disposable mFabDisposable;
	private PagerAdapter mAdapter;

	public BaseTabbedActivity() {
		super(R.layout.activity_base_tabbed);
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setSupportActionBar(toolbar);
		viewPager.setAdapter(mAdapter = getPagerAdapter());
		tabLayout.setupWithViewPager(viewPager);

		initConfiguration();
	}

	@NonNull
	protected abstract PagerAdapter getPagerAdapter();

	@Override
	protected void setupSubscriptions(final CompositeDisposable cleanUp) {
		Disposable viewPagerDisp = RxViewPager.pageSelections(viewPager)
			.subscribeOn(AndroidSchedulers.mainThread())
			.subscribe(i -> {
				if (mFabDisposable != null) {
					cleanUp.remove(mFabDisposable);
				}

				Fragment fragment = mAdapter.mFragments.get(i).second;
				final HasFabFunctionality fabFragment = fragment instanceof HasFabFunctionality
					? (HasFabFunctionality)fragment
					: null;

				boolean hasFab = fabFragment != null;

				int visibility = hasFab
					? View.VISIBLE
					: View.GONE;

				fab.setVisibility(visibility);

				if (hasFab) {
					mFabDisposable = RxView.clicks(fab)
						.observeOn(AndroidSchedulers.mainThread())
						.subscribe(x -> fabFragment.onFabClickedAction(this));
					cleanUp.add(mFabDisposable);
				}
			});
		cleanUp.add(viewPagerDisp);
	}

	public static abstract class PagerAdapter extends FragmentPagerAdapter {

		private final List<Pair<String, Fragment>> mFragments;

		protected PagerAdapter(
			@NonNull final FragmentManager fm,
			@NonNull final List<Pair<String, Fragment>> fragments) {
			super(fm);

			mFragments = fragments;
		}

		@NonNull
		@Override
		public final Fragment getItem(int position) {
			return mFragments.get(position).second;
		}

		@Nullable
		@Override
		public final CharSequence getPageTitle(int position) {
			return mFragments.get(position).first;
		}

		@Override
		public final int getCount() {
			return mFragments.size();
		}
	}
}
