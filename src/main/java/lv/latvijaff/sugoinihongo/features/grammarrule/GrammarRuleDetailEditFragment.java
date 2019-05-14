package lv.latvijaff.sugoinihongo.features.grammarrule;


import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import lv.latvijaff.sugoinihongo.R;
import lv.latvijaff.sugoinihongo.base.BaseDetailEditFragment;
import lv.latvijaff.sugoinihongo.databinding.FragmentGrammarRuleDetailEditBinding;
import lv.latvijaff.sugoinihongo.persistence.models.GrammarRuleModel;
import lv.latvijaff.sugoinihongo.ui.detailitems.DetailItemEditText;
import lv.latvijaff.sugoinihongo.utils.FragmentUtils;
import lv.latvijaff.sugoinihongo.utils.ObjectUtils;
import lv.latvijaff.sugoinihongo.utils.ObservableUtils;
import lv.latvijaff.sugoinihongo.utils.StringUtils;
import lv.latvijaff.sugoinihongo.utils.TextInputLayoutUtils;
import timber.log.Timber;

public class GrammarRuleDetailEditFragment extends BaseDetailEditFragment<GrammarRuleModel, GrammarRuleDetailEditViewModel, FragmentGrammarRuleDetailEditBinding> {

	private final HeaderItemLayout mHeaderItemLayout = new HeaderItemLayout();
	private Unbinder mUnbinder;

	@BindView(R.id.grammar_rule_detail_edit_input_layout_header)
	TextInputLayout headerInputLayout;

	@BindView(R.id.grammar_rule_detail_edit_input_layout_body)
	TextInputLayout bodyInputLayout;

	public GrammarRuleDetailEditFragment() {
		super(GrammarRuleDetailEditViewModel.class, R.layout.fragment_grammar_rule_detail_edit);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		headerInputLayout.requestFocus();
		mUnbinder = ButterKnife.bind(mHeaderItemLayout, headerInputLayout);
	}

	@Override
	public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
		FragmentUtils.inflateDetailEditFragmentMenu(menu, inflater);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public void onPrepareOptionsMenu(@NonNull Menu menu) {
		super.onPrepareOptionsMenu(menu);
		FragmentUtils.setSaveButtonEnabled(menu, isValid());
	}

	@Override
	public void onDestroyView() {
		ObjectUtils.invokeIfNotNull(mUnbinder, Unbinder::unbind);
		super.onDestroyView();
	}

	@Override
	protected void applyBinding(final @NonNull FragmentGrammarRuleDetailEditBinding binding, final @NonNull GrammarRuleDetailEditViewModel viewModel) {
		String id = getModelId();

		binding.setViewModel(viewModel);
		viewModel.processDocument(id);
	}

	@Override
	protected void setupSubscriptions(GrammarRuleDetailEditViewModel viewModel, CompositeDisposable cleanUp) {
		super.setupSubscriptions(viewModel, cleanUp);

		DetailItemEditText headerItem = viewModel.getHeaderItem();
		DetailItemEditText bodyItem = viewModel.getBodyItem();

		Disposable headerCursorDisp = ObservableUtils.setCursorOnce(
			headerItem.observeValue(),
			mHeaderItemLayout.editText);

		Disposable headerErrorMsgDisp = headerItem.observeFirstErrorMessage()
			.subscribe(x -> TextInputLayoutUtils.setErrorMessage(headerInputLayout, x));

		Disposable bodyErrorMsDisp = bodyItem.observeFirstErrorMessage()
			.subscribe(x -> TextInputLayoutUtils.setErrorMessage(bodyInputLayout, x));

		Disposable isValidDisp = Observable
			.combineLatest(
				headerItem.observeIsValid(),
				bodyItem.observeIsValid(),
				(x, y) -> x && y)
			.distinctUntilChanged()
			.doOnEach(x -> Timber.d("Is grammar rule valid: %s", x.getValue()))
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(this::setIsValid);

		cleanUp.addAll(headerCursorDisp, headerErrorMsgDisp, bodyErrorMsDisp, isValidDisp);
	}

	@Override
	protected String createErrorMessage() {
		String value = getViewModel().getHeaderItem().getValue();

		return Objects.requireNonNull(getContext())
			.getString(R.string.validation_grammar_rule_already_exists, StringUtils.trim(value));
	}

	@Nullable
	@Override
	protected Runnable getOnMenuItemSelectedAction(int id) {
		if (id == R.id.menu_item_detail_edit_base_save) {
			Timber.i("Menu item clicked: R.id.menu_item_detail_edit_base_save");
			return this::onSaveAction;
		}

		return null;
	}

	private void onSaveAction() {
		getViewModel().saveDetail();
	}

	static class HeaderItemLayout {

		@BindView(R.id.detail_item_edit_text)
		EditText editText;
	}
}
