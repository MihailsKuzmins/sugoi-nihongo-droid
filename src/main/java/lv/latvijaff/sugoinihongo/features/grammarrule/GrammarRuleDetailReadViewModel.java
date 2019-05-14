package lv.latvijaff.sugoinihongo.features.grammarrule;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.ListenerRegistration;

import java.util.function.Consumer;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import lv.latvijaff.sugoinihongo.App;
import lv.latvijaff.sugoinihongo.R;
import lv.latvijaff.sugoinihongo.base.BaseDetailReadViewModel;
import lv.latvijaff.sugoinihongo.constants.AppConstants.SharedPrefs.Keys;
import lv.latvijaff.sugoinihongo.di.services.SharedPreferencesService;
import lv.latvijaff.sugoinihongo.persistence.models.GrammarRuleModel;
import lv.latvijaff.sugoinihongo.persistence.repos.GrammarRuleRepo;
import lv.latvijaff.sugoinihongo.ui.detailitems.DetailItemTextView;

public class GrammarRuleDetailReadViewModel extends BaseDetailReadViewModel<GrammarRuleModel> {

	private final DetailItemTextView mIdItem;
	private final DetailItemTextView mHeaderItem;
	private final DetailItemTextView mBodyItem;

	private Consumer<GrammarRuleModel> mConsumer;

	@Inject
	SharedPreferencesService preferencesService;

	@Inject
	GrammarRuleRepo repo;

	public GrammarRuleDetailReadViewModel(@NonNull App application) {
		super(application);

		getAppRepoComponent().inject(this);

		mIdItem = new DetailItemTextView(application, R.string.general_identification);
		mHeaderItem = new DetailItemTextView(application, R.string.models_grammar_rule_header);
		mBodyItem = new DetailItemTextView(application, R.string.models_grammar_rule_body);
	}

	public DetailItemTextView getIdItem() {
		return mIdItem;
	}

	public DetailItemTextView getHeaderItem() {
		return mHeaderItem;
	}

	public DetailItemTextView getBodyItem() {
		return mBodyItem;
	}

	@Override
	public void setupSubscriptions(CompositeDisposable cleanUp) {
		Disposable isEntryIdShownDisp = preferencesService.observeBoolean(Keys.IS_ENTRY_ID_SHOWN)
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(mIdItem::setVisible);

		cleanUp.add(isEntryIdShownDisp);
	}

	@NonNull
	@Override
	protected Consumer<GrammarRuleModel> getModelConsumer() {
		if (mConsumer == null) {
			mConsumer = x -> {
				mIdItem.setValue(x.getId());
				mHeaderItem.setValue(x.getHeader());
				mBodyItem.setValue(x.getBody());
			};
		}

		return mConsumer;
	}

	@NonNull
	@Override
	protected ListenerRegistration fetchDocument(String id, Consumer<GrammarRuleModel> consumer) {
		return repo.processDocument(id, consumer);
	}
}
