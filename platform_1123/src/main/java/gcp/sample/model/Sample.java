package gcp.sample.model;

import intune.gsf.model.BaseEntity;

public class Sample extends BaseEntity {
	private String	sampleId;
	private String	sampleName;

	public String getSampleId() {
		return sampleId;
	}

	public void setSampleId(String sampleId) {
		this.sampleId = sampleId;
	}

	public String getSampleName() {
		return sampleName;
	}

	public void setSampleName(String sampleName) {
		this.sampleName = sampleName;
	}

}
