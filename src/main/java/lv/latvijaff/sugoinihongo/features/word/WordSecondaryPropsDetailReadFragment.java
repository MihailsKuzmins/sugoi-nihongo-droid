package lv.latvijaff.sugoinihongo.features.word;


import android.app.Activity;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import lv.latvijaff.sugoinihongo.R;
import lv.latvijaff.sugoinihongo.base.BaseDetailReadFragment;
import lv.latvijaff.sugoinihongo.constants.AppConstants;
import lv.latvijaff.sugoinihongo.databinding.FragmentWordSecondaryPropsDetailReadBinding;
import lv.latvijaff.sugoinihongo.persistence.models.WordModel;
import lv.latvijaff.sugoinihongo.utils.DialogsUtils;
import lv.latvijaff.sugoinihongo.utils.MenuItemUtils;
import lv.latvijaff.sugoinihongo.utils.ObjectUtils;
import lv.latvijaff.sugoinihongo.utils.StringUtils;
import timber.log.Timber;

public class WordSecondaryPropsDetailReadFragment extends BaseDetailReadFragment<WordModel.SecondaryProps, WordSecondaryPropsDetailReadViewModel, FragmentWordSecondaryPropsDetailReadBinding> {

	private boolean mCanResetMark;
	private AlertDialog mResetMarkDialog;

	public WordSecondaryPropsDetailReadFragment() {
		super(WordSecondaryPropsDetailReadViewModel.class, R.layout.fragment_word_secondary_props_detail_read);
	}

	@Override
	public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);

		inflater.inflate(R.menu.menu_detail_read_word_secondary_props, menu);
	}

	@Override
	public void onPrepareOptionsMenu(@NonNull Menu menu) {
		super.onPrepareOptionsMenu(menu);

		MenuItemUtils.setEnabled(menu, R.id.menu_item_detail_read_word_secondary_props_reset_progress, mCanResetMark);
	}

	@Override
	public void onPause() {
		getViewModel().saveModel();
		super.onPause();
	}

	@Override
	protected void setupSubscriptions(WordSecondaryPropsDetailReadViewModel viewModel, CompositeDisposable cleanUp) {
		super.setupSubscriptions(viewModel, cleanUp);

		Observable<Boolean> isMarkNotMinObservable = viewModel.getMarkItem().observeValue()
			.map(x -> StringUtils.toInt(x) > AppConstants.Models.MIN_MARK);

		Disposable canResetMarkDisp = Observable.combineLatest(
				isMarkNotMinObservable,
				viewModel.getIsIncludedInStudyItem().observeValue(),
				(x, y) -> x && y)
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(x -> {
				mCanResetMark = x;
				ObjectUtils.invokeIfNotNull(getActivity(), Activity::invalidateOptionsMenu);
			});

		Disposable markResetDisp = viewModel.observeMarkReset()
			.subscribe(x -> Snackbar.make(Objects.requireNonNull(getView()), R.string.models_word_mark_is_reset, Snackbar.LENGTH_LONG)
				.setAction(R.string.general_undo, v -> getViewModel().undoResetMark())
				.show());

		Disposable markResetUndoneDisp = viewModel.observeMarkResetUndone()
			.subscribe(x -> Toast.makeText(getContext(), R.string.general_previous_value_restored, Toast.LENGTH_SHORT)
				.show());

		cleanUp.addAll(canResetMarkDisp, markResetDisp, markResetUndoneDisp);
	}

	@Override
	protected void applyBinding(@NonNull FragmentWordSecondaryPropsDetailReadBinding binding, @NonNull WordSecondaryPropsDetailReadViewModel viewModel) {
		String wordId = getModelId();

		binding.setViewModel(viewModel);
		viewModel.processDocument(wordId);
	}

	@Nullable
	@Override
	protected Runnable getOnMenuItemSelectedAction(int id) {
		if (id == R.id.menu_item_detail_read_word_secondary_props_reset_progress) {
			Timber.i("Menu item clicked: R.id.menu_item_detail_read_word_secondary_props_reset_progress");
			return this::onResetProgressAction;
		}

		return null;
	}

	private void onResetProgressAction() {
		if (mResetMarkDialog == null) {
			mResetMarkDialog = DialogsUtils.createYesNoConfirmDialog(
					getContext(), R.string.general_reset_warning, R.string.models_reset_mark_confirm,
					() -> getViewModel().resetMark());
		}

		mResetMarkDialog.show();
	}
}
