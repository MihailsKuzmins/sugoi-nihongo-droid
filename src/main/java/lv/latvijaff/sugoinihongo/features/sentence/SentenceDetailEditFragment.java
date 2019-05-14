package lv.latvijaff.sugoinihongo.features.sentence;


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
import lv.latvijaff.sugoinihongo.databinding.FragmentSentenceDetailEditBinding;
import lv.latvijaff.sugoinihongo.persistence.models.SentenceModel;
import lv.latvijaff.sugoinihongo.ui.detailitems.DetailItemEditText;
import lv.latvijaff.sugoinihongo.utils.FragmentUtils;
import lv.latvijaff.sugoinihongo.utils.ObjectUtils;
import lv.latvijaff.sugoinihongo.utils.ObservableUtils;
import lv.latvijaff.sugoinihongo.utils.StringUtils;
import lv.latvijaff.sugoinihongo.utils.TextInputLayoutUtils;
import timber.log.Timber;

public class SentenceDetailEditFragment extends BaseDetailEditFragment<SentenceModel, SentenceDetailEditViewModel, FragmentSentenceDetailEditBinding> {

	private final EnglishItemLayout mEnglishItemLayout = new EnglishItemLayout();
	private Unbinder mUnbinder;

	@BindView(R.id.sentence_detail_edit_input_layout_english)
	TextInputLayout englishInputLayout;

	@BindView(R.id.sentence_detail_edit_input_layout_translation)
	TextInputLayout translationInputLayout;

	@BindView(R.id.sentence_detail_edit_input_layout_transcription)
	TextInputLayout transcriptionInputLayout;

	public SentenceDetailEditFragment() {
		super(SentenceDetailEditViewModel.class, R.layout.fragment_sentence_detail_edit);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		englishInputLayout.requestFocus();
		mUnbinder = ButterKnife.bind(mEnglishItemLayout, englishInputLayout);
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
	protected void applyBinding(final @NonNull FragmentSentenceDetailEditBinding binding, final  @NonNull SentenceDetailEditViewModel viewModel) {
		String id = getModelId();

		binding.setViewModel(viewModel);
		viewModel.processDocument(id);
	}

	@Override
	protected void setupSubscriptions(SentenceDetailEditViewModel viewModel, CompositeDisposable cleanUp) {
		super.setupSubscriptions(viewModel, cleanUp);

		DetailItemEditText englishItem = viewModel.getEnglishItem();
		DetailItemEditText translationItem = viewModel.getTranslationItem();
		DetailItemEditText transcriptionItem = viewModel.getTranscriptionItem();

		Disposable englishCursorDisp = ObservableUtils.setCursorOnce(
			englishItem.observeValue(),
			mEnglishItemLayout.editText);

		Disposable englishErrorMsgDisp = englishItem.observeFirstErrorMessage()
			.subscribe(x -> TextInputLayoutUtils.setErrorMessage(englishInputLayout, x));

		Disposable translationErrorMsgDisp = translationItem.observeFirstErrorMessage()
			.subscribe(x -> TextInputLayoutUtils.setErrorMessage(translationInputLayout, x));

		Disposable transcriptionFirstErrorMsgDisp = transcriptionItem.observeFirstErrorMessage()
			.subscribe(x -> TextInputLayoutUtils.setErrorMessage(transcriptionInputLayout, x));

		Disposable isValidDisp = Observable
			.combineLatest(
				englishItem.observeIsValid(),
				translationItem.observeIsValid(),
				transcriptionItem.observeIsValid(),
				(x, y, z) -> x && y && z)
			.distinctUntilChanged()
			.doOnEach(x -> Timber.i("Is sentence valid: %s", x.getValue()))
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(this::setIsValid);

		cleanUp.addAll(englishCursorDisp, englishErrorMsgDisp, translationErrorMsgDisp, transcriptionFirstErrorMsgDisp, isValidDisp);
	}

	@Override
	protected String createErrorMessage() {
		String value = getViewModel().getEnglishItem().getValue();

		return Objects.requireNonNull(getContext())
			.getString(R.string.validation_sentence_already_exists, StringUtils.trim(value));
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

	static class EnglishItemLayout {

		@BindView(R.id.detail_item_multiline_edit_text)
		EditText editText;
	}
}
