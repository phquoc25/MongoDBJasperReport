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
	String chartType;
	Map<String, Object> reportParameters;
	Map<String, Map<String, Object>> classParameters;
	
	
	public abstract String getReportBody(HttpServletRequest request);
	public abstract void initCollection();
	public abstract List getCollection();
	
	public ModelAndView generateReportPage(Map<String, Object> pageParameters){
		Map<String, Object> parameters = classParameters.get(chartType);
		ModelAndView modelAndView = new ModelAndView(parameters.get("downloadPage").toString());
		pageParameters.put("pdfLink", parameters.get("pdfLink"));
		modelAndView.addAllObjects(pageParameters);
		return modelAndView;
	}
	
	public IDAO getDaoImpl() {
		return daoImpl;
	}
	
	public void setDaoImpl(IDAO daoImpl) {
		this.daoImpl = daoImpl;
	}
	
	public Map<String, Object> getReportParameters() {
		return reportParameters;
	}
	public void setReportParameters(Map<String, Object> reportParameters) {
		this.reportParameters = reportParameters;
	}
	public String getChartType() {
		return chartType;
	}
	public void setChartType(String chartType) {
		this.chartType = chartType;
	}
	public Map<String, Map<String, Object>> getClassParameters() {
		return classParameters;
	}
	public void setClassParameters(Map<String, Map<String, Object>> classParameters) {
		this.classParameters = classParameters;
	}
}
