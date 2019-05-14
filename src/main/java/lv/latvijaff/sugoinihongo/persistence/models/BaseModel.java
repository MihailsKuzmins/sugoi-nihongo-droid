package lv.latvijaff.sugoinihongo.persistence.models;

import java.time.LocalDate;

public abstract class BaseModel implements Model {

	private String id;
	private LocalDate dateCreated;

	BaseModel(String id) {
		this.id = id;
	}

	@Override
	public String getId() {
		return id;
	}

	public LocalDate getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(LocalDate dateCreated) {
		this.dateCreated = dateCreated;
	}
}
