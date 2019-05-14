package lv.latvijaff.sugoinihongo.persistence.models;

import java.util.List;

import lv.latvijaff.sugoinihongo.constants.AppConstants;
import lv.latvijaff.sugoinihongo.persistence.dto.QuizItemDto;

public class QuizModel extends BaseModel {

	private String secondaryPropsId;
	private QuizItemDto question;
	private List<QuizItemDto> options;

	public QuizModel(String secondaryPropsId, QuizItemDto question) {
		super(AppConstants.Models.EMPTY_ID);

		this.secondaryPropsId = secondaryPropsId;
		this.question = question;
	}

	public String getSecondaryPropsId() {
		return secondaryPropsId;
	}

	public QuizItemDto getQuestion() {
		return question;
	}

	public List<QuizItemDto> getOptions() {
		return options;
	}

	public void setOptions(List<QuizItemDto> options) {
		this.options = options;
	}
}
