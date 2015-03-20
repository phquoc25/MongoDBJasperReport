package com.directv.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;

import net.sf.jasperreports.engine.JRException;

import org.springframework.web.servlet.ModelAndView;

public interface IReportService {
	public void generateReport(String inputTemplate, String output, Connection connection) throws JRException;
	public ModelAndView generateReportToJsp(String inputTemplate, String output, Connection connection) throws JRException, FileNotFoundException, IOException;
}
