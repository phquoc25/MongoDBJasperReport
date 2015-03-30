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
	
}
