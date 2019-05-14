package lv.latvijaff.sugoinihongo.persistence.dto;

public class WordToSecondaryProps {

	private String wordId;
	private String secondaryPropsId;

	public WordToSecondaryProps(String wordId, String secondaryPropsId) {
		this.wordId = wordId;
		this.secondaryPropsId = secondaryPropsId;
	}

	public String getWordId() {
		return wordId;
	}

	public String getSecondaryPropsId() {
		return secondaryPropsId;
	}
}
