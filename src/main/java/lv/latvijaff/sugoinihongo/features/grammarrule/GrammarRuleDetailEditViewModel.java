package lv.latvijaff.sugoinihongo.features.grammarrule;

import android.text.InputType;

import androidx.annotation.NonNull;

import java.time.LocalDate;
import java.util.function.Consumer;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import lv.latvijaff.sugoinihongo.App;
import lv.latvijaff.sugoinihongo.R;
import lv.latvijaff.sugoinihongo.base.BaseDetailEditViewModel;
import lv.latvijaff.sugoinihongo.constants.AppConstants;
import lv.latvijaff.sugoinihongo.persistence.models.GrammarRuleModel;
import lv.latvijaff.sugoinihongo.persistence.repos.GrammarRuleRepo;
import lv.latvijaff.sugoinihongo.ui.detailitems.DetailItemEditText;
import lv.latvijaff.sugoinihongo.ui.detailitems.DetailItemEditTextMultiline;
import lv.latvijaff.sugoinihongo.ui.detailitems.rules.LengthLessThanOrEqualRule;
import lv.latvijaff.sugoinihongo.ui.detailitems.rules.NotNullOrWhiteSpaceRule;
import lv.latvijaff.sugoinihongo.utils.ObjectUtils;

public class GrammarRuleDetailEditViewModel extends BaseDetailEditViewModel<GrammarRuleModel> {

	private final Subject<Boolean> mIsSavedSubject = PublishSubject.create();

	private final DetailItemEditText mHeaderItem;
	private final DetailItemEditTextMultiline mBodyItem;

	private Consumer<GrammarRuleModel> mConsumer;

	@Inject
	GrammarRuleRepo repo;

	public GrammarRuleDetailEditViewModel(@NonNull App application) {
		super(application);

		getRepoComponent().inject(this);

		mHeaderItem = new DetailItemEditText(application, R.string.models_grammar_rule_header, InputType.TYPE_TEXT_FLAG_CAP_SENTENCES)
			.addRuleEx(NotNullOrWhiteSpaceRule.getInstance())
			.addRuleEx(new LengthLessThanOrEqualRule(64));

		mBodyItem = new DetailItemEditTextMultiline(application, R.string.models_grammar_rule_body)
			.addRuleMultilineEx(NotNullOrWhiteSpaceRule.getInstance());
	}

	@NonNull
	@Override
	public Observable<Boolean> observeIsSaved() {
		return mIsSavedSubject;
	}

	public DetailItemEditText getHeaderItem() {
		return mHeaderItem;
	}

	public DetailItemEditTextMultiline getBodyItem() {
		return mBodyItem;
	}

	@Override
	public void setupSubscriptions(CompositeDisposable cleanUp) {
		Disposable headerValueDisp = mHeaderItem.observeValue()
			.subscribe(value -> ObjectUtils.invokeIfNotNull(getModel(), x -> x.setHeader(value)));

		Disposable bodyValueDisp = mBodyItem.observeValue()
			.subscribe(value -> ObjectUtils.invokeIfNotNull(getModel(), x -> x.setBody(value)));

		cleanUp.addAll(headerValueDisp, bodyValueDisp);
	}

	@NonNull
	@Override
	protected GrammarRuleModel createEmptyModel() {
		GrammarRuleModel model = new GrammarRuleModel(AppConstants.Models.EMPTY_ID);
		model.setDateCreated(LocalDate.now());

		return model;
	}

	@NonNull
	@Override
	protected Consumer<GrammarRuleModel> getModelConsumer() {
		if (mConsumer == null) {
			mConsumer = x -> {
				mHeaderItem.setValue(x.getHeader());
				mBodyItem.setValue(x.getBody());
			};
		}

		return mConsumer;
	}

	@Override
	protected void fetchDocument(final String id, final Consumer<GrammarRuleModel> consumer) {
		repo.getDocument(id, consumer);
	}

	void saveDetail() {
		GrammarRuleModel model = getModel();
		repo.saveDocument(model, mIsSavedSubject);
	}
}
