package com.directv.services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

@Service
@Qualifier("PDFService")
public class PDFReportGenerator implements IReportService{

	public void generateReport(String inputTemplate, String output,
			Connection connection) throws JRException {
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		JasperReport jasperReport = JasperCompileManager.compileReport(inputTemplate);
		
		JasperPrint fillReport = JasperFillManager.fillReport(jasperReport, parameters, connection);
		
		JasperExportManager.exportReportToPdfFile(fillReport, output);
		
	}

	public ModelAndView generateReportToJsp(String inputTemplate, String output,
			Connection connection) throws JRException, IOException {
		
		ModelAndView model = new ModelAndView();
		
		String html = "<select><option value=5>HTML Code</option></select>";
		model.addObject("html", html);
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		JasperReport jasperReport = JasperCompileManager.compileReport(inputTemplate);
		
		JasperPrint fillReport = JasperFillManager.fillReport(jasperReport, parameters, connection);

		JasperExportManager.exportReportToHtmlFile(fillReport, output);
		
		FileReader fileReader = new FileReader(output);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		String str = "";
		int i = 0;
		List<String> lst = new ArrayList<String>();

		while ((str = bufferedReader.readLine()) != null) {
			if (i >= 9) {
				lst.add(str);
			}
			i++;
		}
		String result = "";
		for (i = 0; i < lst.size() - 1; i++) {
			result += lst.get(i);
		}
		model.addObject("result", result);
		fileReader.close();
		
		return new ModelAndView("tempReportN");
	}
}
