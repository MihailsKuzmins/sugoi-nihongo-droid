package lv.latvijaff.sugoinihongo.features.system;


import android.content.Context;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.android.material.textfield.TextInputLayout;

import butterknife.BindView;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import lv.latvijaff.sugoinihongo.R;
import lv.latvijaff.sugoinihongo.base.BaseDetailFragment;
import lv.latvijaff.sugoinihongo.databinding.FragmentSettingsStudyBinding;
import lv.latvijaff.sugoinihongo.utils.DialogsUtils;
import lv.latvijaff.sugoinihongo.utils.TextInputLayoutUtils;

public class SettingsStudyFragment extends BaseDetailFragment<SettingsStudyViewModel, FragmentSettingsStudyBinding> {

	private AlertDialog mResetProgressAlertDialog;

	@BindView(R.id.settings_study_input_layout_study_items)
	TextInputLayout studyItemsInputLayout;

	@BindView(R.id.settings_study_input_layout_min_options_count)
	TextInputLayout minOptionsCountInputLayout;

	@BindView(R.id.settings_study_input_layout_max_options_count)
	TextInputLayout maxOptionsCountInputLayout;

	@BindView(R.id.settings_study_input_layout_unused_word_days)
	TextInputLayout unusedWordsDaysInputLayout;

	public SettingsStudyFragment() {
		super(SettingsStudyViewModel.class, R.layout.fragment_settings_study);
	}

	@Override
	public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.menu_settings_study, menu);
	}

	@Override
	protected void applyBinding(final @NonNull FragmentSettingsStudyBinding binding, final @NonNull SettingsStudyViewModel viewModel) {
		binding.setViewModel(viewModel);
	}

	@Override
	protected void setupSubscriptions(final SettingsStudyViewModel viewModel, final CompositeDisposable cleanUp) {
		super.setupSubscriptions(viewModel, cleanUp);

		Disposable studyItemsErrorMsgDisp = viewModel.getStudyItemsItem().observeFirstErrorMessage()
			.subscribe(x -> TextInputLayoutUtils.setErrorMessage(studyItemsInputLayout, x));

		Disposable minQuizOptionsErrorMsgDisp = viewModel.getMinQuizOptionsItem().observeFirstErrorMessage()
			.subscribe(x -> TextInputLayoutUtils.setErrorMessage(minOptionsCountInputLayout, x));

		Disposable maxQuizOptionsErrorMsgDisp = viewModel.getMaxQuizOptionsItem().observeFirstErrorMessage()
			.subscribe(x -> TextInputLayoutUtils.setErrorMessage(maxOptionsCountInputLayout, x));

		Disposable unusedWordDaysErrorMsgDisp = viewModel.getUnusedWordDaysItem().observeFirstErrorMessage()
			.subscribe(x -> TextInputLayoutUtils.setErrorMessage(unusedWordsDaysInputLayout, x));

		cleanUp.addAll(studyItemsErrorMsgDisp, minQuizOptionsErrorMsgDisp, maxQuizOptionsErrorMsgDisp, unusedWordDaysErrorMsgDisp);
	}

	@Nullable
	@Override
	protected Runnable getOnMenuItemSelectedAction(int id) {
		if (id == R.id.menu_item_settings_study_reset_progress) {
			return this::onResetProgressAction;
		}

		return null;
	}

	private void onResetProgressAction() {
		final Context context = getContext();

		if (mResetProgressAlertDialog == null) {
			mResetProgressAlertDialog = DialogsUtils.createYesNoConfirmDialog(
				context, R.string.general_reset_warning, R.string.settings_study_reset_all_marks_confirm,
				() -> getViewModel().resetWordProgress(() -> Toast
					.makeText(context, R.string.settings_study_marks_are_reset, Toast.LENGTH_SHORT)
					.show()));
		}

		mResetProgressAlertDialog.show();
	}
}
