package lv.latvijaff.sugoinihongo.ui.listitems;

import androidx.annotation.NonNull;
import lv.latvijaff.sugoinihongo.constants.AppConstants;
import lv.latvijaff.sugoinihongo.persistence.models.GrammarRuleModel;
import lv.latvijaff.sugoinihongo.utils.StringUtils;

public class GrammarRuleListItem extends BaseListItem {

	public GrammarRuleListItem(@NonNull GrammarRuleModel model) {
		super(model.getId());

		String text = createText(model.getHeader(), model.getBody());
		setText(text);
	}

	private String createText(String header, String body) {
		return header
			+ System.lineSeparator()
			+ createBodyText(body);
	}

	private String createBodyText(String body) {
		int bodyLength = body.length();
		int length = bodyLength > AppConstants.Models.GRAMMAR_RULE_LIST_ITEM_BODY_MAX_LENGTH
			? AppConstants.Models.GRAMMAR_RULE_LIST_ITEM_BODY_MAX_LENGTH
			: bodyLength;

		String trailingString = length == AppConstants.Models.GRAMMAR_RULE_LIST_ITEM_BODY_MAX_LENGTH
			? "..."
			: StringUtils.EMPTY;

		String result = body.substring(0, length) + trailingString;
		return StringUtils.removeWhiteSpaces(result);
	}
}
