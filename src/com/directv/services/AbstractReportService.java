package com.directv.services;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

public abstract class AbstractReportService {
	private String reportTemplate;
	private String reportPage;
	private Map<String, Object> parameters;
	
	public abstract String getReportBody(HttpServletRequest request);
	public ModelAndView generateReportPage(String reportBody){
		ModelAndView modelAndView = new ModelAndView(reportPage);
		parameters.put("reportBody", reportBody);
		modelAndView.addAllObjects(parameters);
		return modelAndView;
	}
	
}
