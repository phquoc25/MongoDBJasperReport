package com.directv.services;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import com.directv.dao.IDAO;

public abstract class AbstractReportService {
	
	IDAO daoImpl;
	public IDAO getDaoImpl() {
		return daoImpl;
	}
	
	protected String reportTemplate;
	protected String reportPage;
	protected Map<String, Object> parameters;
	
	public abstract String getReportBody(HttpServletRequest request);
	public abstract void initCollection();
	public abstract List getCollection();
	
	public ModelAndView generateReportPage(String reportBody){
		ModelAndView modelAndView = new ModelAndView(reportPage);
		parameters.put("reportBody", reportBody);
		modelAndView.addAllObjects(parameters);
		return modelAndView;
	}
	
	public void setDaoImpl(IDAO daoImpl) {
		this.daoImpl = daoImpl;
	}
	public String getReportTemplate() {
		return reportTemplate;
	}
	public void setReportTemplate(String reportTemplate) {
		this.reportTemplate = reportTemplate;
	}
	public String getReportPage() {
		return reportPage;
	}
	public void setReportPage(String reportPage) {
		this.reportPage = reportPage;
	}
	public Map<String, Object> getParameters() {
		return parameters;
	}
	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}


	
}
