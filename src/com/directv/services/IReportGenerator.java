package com.directv.services;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;

public interface IReportGenerator {
	public void generateReport(String outputFileName) throws JRException;
	public void generateReport(HttpServletRequest request, HttpServletResponse response);
}
