package lv.latvijaff.sugoinihongo.persistence.models;

public class GrammarRuleModel extends BaseModel {

	private String header;
	private String body;

	public GrammarRuleModel(String id) {
		super(id);
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
}
