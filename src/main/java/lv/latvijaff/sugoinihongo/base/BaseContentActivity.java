package lv.latvijaff.sugoinihongo.base;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.jakewharton.rxbinding3.appcompat.RxToolbar;

import java.util.Objects;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import lv.latvijaff.sugoinihongo.R;

public abstract class BaseContentActivity<TFragment extends Fragment> extends BaseActivity {

	private final int mFragmentPlaceholderId = R.id.base_content_view_placeholder;

	@BindView(R.id.base_content_toolbar)
	Toolbar toolbar;

	public BaseContentActivity() {
		super(R.layout.activity_base_content);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setSupportActionBar(toolbar);
		TFragment fragment = createFragment();
		initFragment(getSupportFragmentManager(), fragment);

		initConfiguration();
	}

	@NonNull
	protected abstract TFragment createFragment();

	@Override
	protected void beforeNavigatingFrom() {
		BaseFragment fragment = (BaseFragment) getSupportFragmentManager().findFragmentById(mFragmentPlaceholderId);
		Objects.requireNonNull(fragment).beforeNavigatingFrom();
	}

	@Override
	protected void setupSubscriptions(final CompositeDisposable cleanUp) {
		if (getActivityConfiguration().isBackButtonDisplayed()) {
			Disposable backArrowDisp = RxToolbar.navigationClicks(toolbar)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(x -> super.onBackPressed());
			cleanUp.add(backArrowDisp);
		}
	}

	private void initFragment(FragmentManager fragmentManager, Fragment fragment) {
		Fragment existingFragment = fragmentManager.findFragmentById(mFragmentPlaceholderId);

		// when screen is rotated, a fragment is not removed from the placeholder, so
		// its ViewModel is recreated on each rotation. Checking for null prevents it
		if (existingFragment != null) {
			return;
		}

		fragmentManager.beginTransaction()
			.replace(mFragmentPlaceholderId, fragment)
			.commit();

		// make fragment available in the fragmentManager by findFragmentByTag()
		fragmentManager.executePendingTransactions();
	}
}
