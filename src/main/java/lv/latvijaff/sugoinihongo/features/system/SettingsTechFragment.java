package lv.latvijaff.sugoinihongo.features.system;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.android.material.textfield.TextInputLayout;

import butterknife.BindView;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import lv.latvijaff.sugoinihongo.R;
import lv.latvijaff.sugoinihongo.base.BaseDetailFragment;
import lv.latvijaff.sugoinihongo.databinding.FragmentSettingsTechBinding;
import lv.latvijaff.sugoinihongo.ui.events.CanNavigateBackEvent;
import lv.latvijaff.sugoinihongo.utils.DialogsUtils;
import lv.latvijaff.sugoinihongo.utils.EventBusUtils;
import lv.latvijaff.sugoinihongo.utils.TextInputLayoutUtils;

public class SettingsTechFragment extends BaseDetailFragment<SettingsTechViewModel, FragmentSettingsTechBinding> {

	private AlertDialog mAppRestartDialog;

	@BindView(R.id.settings_tech_input_layout_local_backup_file_count)
	TextInputLayout localBackupFileCountInputLayout;

	@BindView(R.id.settings_tech_input_layout_backup_creation_frequency)
	TextInputLayout backupCreationFrequencyInputLayout;

	public SettingsTechFragment() {
		super(SettingsTechViewModel.class, R.layout.fragment_settings_tech);
	}

	@Override
	protected void setupSubscriptions(SettingsTechViewModel viewModel, CompositeDisposable cleanUp) {
		super.setupSubscriptions(viewModel, cleanUp);

		Disposable backupCountErrorMsgDisp = viewModel.getLocalBackupFileCountItem().observeFirstErrorMessage()
			.subscribe(x -> TextInputLayoutUtils.setErrorMessage(localBackupFileCountInputLayout, x));

		Disposable backupFrequencyErrorMsgDisp = viewModel.getBackupCreationFrequencyDaysItem().observeFirstErrorMessage()
			.subscribe(x -> TextInputLayoutUtils.setErrorMessage(backupCreationFrequencyInputLayout, x));

		cleanUp.addAll(backupCountErrorMsgDisp, backupFrequencyErrorMsgDisp);
	}

	@Override
	public void beforeNavigatingFrom() {
		if (getViewModel().getIsTestDbInUseItem().isValueChanged()) {
			getAppRestartAlert().show();
		} else {
			super.beforeNavigatingFrom();
		}
	}

	@Override
	protected void applyBinding(final @NonNull FragmentSettingsTechBinding binding, final @NonNull SettingsTechViewModel viewModel) {
		binding.setViewModel(viewModel);
	}

	private AlertDialog getAppRestartAlert() {
		if (mAppRestartDialog == null) {
			mAppRestartDialog = DialogsUtils.createYesNoConfirmDialog(
				getContext(), R.string.general_restart, R.string.settings_tech_sync_confirm_restart,
				() -> getViewModel().restart(),
				() -> EventBusUtils.post(new CanNavigateBackEvent(true)));
		}

		return mAppRestartDialog;
	}
}
