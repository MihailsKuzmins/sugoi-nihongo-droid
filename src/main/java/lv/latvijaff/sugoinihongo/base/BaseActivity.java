package lv.latvijaff.sugoinihongo.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Objects;

import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import lv.latvijaff.sugoinihongo.R;
import lv.latvijaff.sugoinihongo.features.android.activities.SettingsListContentActivity;
import lv.latvijaff.sugoinihongo.ui.events.CanNavigateBackEvent;
import lv.latvijaff.sugoinihongo.utils.ActivityUtils;
import lv.latvijaff.sugoinihongo.utils.EventBusUtils;
import timber.log.Timber;

public abstract class BaseActivity extends AppCompatActivity {

	private final int mLayoutId;
	private final CompositeDisposable mCleanUp = new CompositeDisposable();
	private ActivityConfiguration mCfg;

	BaseActivity(final @LayoutRes int layoutId) {
		mLayoutId = layoutId;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// Use utils because Dagger 2 cannot inject into base classes
		ActivityUtils.setAppTheme(this);

		super.onCreate(savedInstanceState);
		setContentView(mLayoutId);

		ButterKnife.bind(this);
	}

	@Override
	public final boolean onCreateOptionsMenu(Menu menu) {
		boolean isMenuDisplayed = mCfg.isMainMenuDisplayed();

		if (isMenuDisplayed) {
			getMenuInflater().inflate(R.menu.menu_base, menu);
		}

		return isMenuDisplayed;
	}

	@Override
	public final boolean onOptionsItemSelected(MenuItem item) {
		if (mCfg.isMainMenuDisplayed()) {
			int id = item.getItemId();
			return menuItemClickedAction(id);
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public final void onBackPressed() {
		beforeNavigatingFrom();
	}

	// method is overridden child classes
	protected void beforeNavigatingFrom() {
		EventBusUtils.post(new CanNavigateBackEvent(true));
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public final void onBackPressed(CanNavigateBackEvent event) {
		if (event.canNavigateBack()) {
			super.onBackPressed();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		setupSubscriptions(mCleanUp);
		EventBus.getDefault().register(this);
	}

	@Override
	protected void onPause() {
		mCleanUp.clear();
		EventBus.getDefault().unregister(this);
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		mCleanUp.dispose();
		super.onDestroy();
	}

	ActivityConfiguration getActivityConfiguration() {
		return mCfg;
	}

	void initConfiguration() {
		setupConfiguration(mCfg = new ActivityConfiguration());
		handleConfiguration(mCfg);
	}

	protected void setupSubscriptions(final CompositeDisposable cleanUp) {}

	protected void setupConfiguration(final ActivityConfiguration cfg) {}

	private void handleConfiguration(ActivityConfiguration cfg) {
		if (cfg.isBackButtonDisplayed()) {
			showBackButton();
		}
	}

	private void showBackButton() {
		ActionBar actionBar = Objects.requireNonNull(getSupportActionBar());

		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);
	}

	private boolean menuItemClickedAction(int id) {
		if (id == R.id.menu_item_main_settings) {
			Timber.i("Menu item clicked: R.id.menu_item_main_settings");
			Intent intent = new Intent(this, SettingsListContentActivity.class);
			startActivity(intent);
			return true;
		}

		return false;
	}
}
