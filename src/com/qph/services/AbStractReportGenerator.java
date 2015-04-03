package com.qph.services;

import org.springframework.ui.ModelMap;

public abstract class AbStractReportGenerator implements IReportGenerator{
	protected String reportTemplatePath;
	protected ModelMap model;
	
	public String getReportTemplatePath() {
		return reportTemplatePath;
	}
	public void setReportTemplatePath(String reportTemplatePath) {
		this.reportTemplatePath = reportTemplatePath;
	}
	public ModelMap getModel() {
		return model;
	}
	public void setModel(ModelMap model) {
		this.model = model;
	}
	
}
