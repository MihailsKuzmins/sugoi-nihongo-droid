package lv.latvijaff.sugoinihongo.persistence.models;

public class ThirdPartyLibraryModel implements Model {

	private String id;
	private String name;
	private String copyright;
	private String projectUrl;
	private LicenceType licenceType;

	public ThirdPartyLibraryModel(String id) {
		this.id = id;
	}

	@Override
	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCopyright() {
		return copyright;
	}

	@SuppressWarnings("unused")
	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}

	public String getProjectUrl() {
		return projectUrl;
	}

	@SuppressWarnings("unused")
	public void setProjectUrl(String projectUrl) {
		this.projectUrl = projectUrl;
	}

	public LicenceType getLicenceType() {
		return licenceType;
	}

	@SuppressWarnings("unused")
	public void setLicenceType(LicenceType licenceType) {
		this.licenceType = licenceType;
	}
}
