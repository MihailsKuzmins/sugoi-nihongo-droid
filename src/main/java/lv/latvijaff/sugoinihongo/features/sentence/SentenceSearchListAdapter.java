package lv.latvijaff.sugoinihongo.features.sentence;

import android.widget.Filter;
import android.widget.Filterable;

import com.moji4j.MojiConverter;

import java.util.List;

import lv.latvijaff.sugoinihongo.persistence.models.SentenceModel;
import lv.latvijaff.sugoinihongo.utils.JapaneseTextUtils;
import lv.latvijaff.sugoinihongo.utils.StringUtils;


public class SentenceSearchListAdapter extends SentenceListAdapter implements Filterable {

	private final Filter filter;
	private final MojiConverter mMojiConverter = new MojiConverter();

	SentenceSearchListAdapter(List<SentenceModel> list) {
		super(list);

		filter = createFilter((model, searchText) -> {
			for (String text : getSearchFields(model)) {
				if (text.toLowerCase().contains(searchText))
					return true;
			}

			return false;
		});
	}

	@Override
	public Filter getFilter() {
		return filter;
	}

	private String[] getSearchFields(SentenceModel model) {
		return new String[] {
			model.getEnglish(),
			JapaneseTextUtils.getRomanisedText(mMojiConverter, model.getTranscription(), model.getTranslation()),
			model.getTranslation(),
			StringUtils.emptyIfNull(model.getTranscription())
		};
	}
}
