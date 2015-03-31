package com.directv.services;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleHtmlReportConfiguration;
import net.sf.jasperreports.j2ee.servlets.ImageServlet;
import net.sf.jasperreports.web.util.WebHtmlResourceHandler;

public class ChartReportService extends AbstractReportService{

	
	@Override
	public String getReportBody(HttpServletRequest request) {
		String rootPath = request.getSession().getServletContext().getRealPath("/");
		JRDataSource dataSource = new JRBeanCollectionDataSource(daoImpl.getCollection());
		JRDataSource dataSource1 = new JRBeanCollectionDataSource(daoImpl.getCollection());
		BufferedReader reader = null;
		StringBuffer reportBody = null;
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("SubDataSource", dataSource);
			parameters.put("SubDataSource1", dataSource1);
			parameters.put("ChartTitle", "Utterance Report");
			parameters.put("dateFormater", new SimpleDateFormat("yyyy-MM-dd"));
			//JasperReport jasperReport = JasperCompileManager.compileReport(template);
			
			JasperPrint jasperPrint = JasperFillManager.fillReport(rootPath + reportTemplate, parameters, new JREmptyDataSource());
			
			request.getSession().setAttribute(ImageServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE, jasperPrint);
			HtmlExporter exporterHTML = new HtmlExporter();
			SimpleExporterInput exporterInput = new SimpleExporterInput(jasperPrint);
			exporterHTML.setExporterInput(exporterInput);
			SimpleHtmlExporterOutput exporterOutput; 
			
			//exporterOutput = new SimpleHtmlExporterOutput(response.getOutputStream());
			String outputFile = rootPath + "WEB-INF\\pages\\MyReportOutput" + request.getSession().getId() + ".jsp";
			exporterOutput = new SimpleHtmlExporterOutput(outputFile);
			exporterOutput.setImageHandler(new WebHtmlResourceHandler("image?image={0}"));
			exporterHTML.setExporterOutput(exporterOutput);
			
			SimpleHtmlReportConfiguration reportExportConfiguration = new SimpleHtmlReportConfiguration();
			reportExportConfiguration.setWhitePageBackground(false);
			reportExportConfiguration.setRemoveEmptySpaceBetweenRows(true);
			exporterHTML.setConfiguration(reportExportConfiguration);
			exporterHTML.exportReport();
			
			//Read the output file in order to obtain the report contain
			reader = new BufferedReader(new FileReader(outputFile));
			reportBody = new StringBuffer();
			String currentLine;
			
			boolean isStarted = false;
			boolean isTerminated = false;
			while((currentLine = reader.readLine()) != null){
				if(currentLine.startsWith("<body")){
					isStarted = true;
					continue;
				}
				
				if(currentLine.startsWith("</body")){
					isTerminated = true;
				}
				
				if(isStarted && !isTerminated){
					reportBody.append(currentLine);
				}
			}
			
		} catch (JRException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(reader != null){
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return reportBody.toString();
	}

	@Override
	public void initCollection() {
		daoImpl.initCollection();
	}

	@Override
	public List getCollection() {
		return daoImpl.getCollection();
	}

}
