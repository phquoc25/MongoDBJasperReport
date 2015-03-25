package com.directv.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleHtmlReportConfiguration;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.j2ee.servlets.ImageServlet;
import net.sf.jasperreports.web.util.WebHtmlResourceHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.directv.services.IReportService;
import com.jaspersoft.mongodb.connection.MongoDbConnection;

@Controller
@RequestMapping("/main")
public class ReportGeneratorController {
	
	@Autowired
	@Qualifier(value="PDFService")
	private IReportService reportService;
	
	
	/**
	 * Handles and retrieves the download page
	 * 
	 * @return the name of the JSP page
	 */
    @RequestMapping(method = RequestMethod.GET)
    public String getDownloadPage() {
    	//logger.debug("Received request to show download page");
    
    	// Do your work here. Whatever you like
    	// i.e call a custom service to do your business
    	// Prepare a model to be used by the JSP page
    	
    	// This will resolve to /WEB-INF/jsp/downloadpage.jsp
    	return "downloadpage";
	}
    
	@RequestMapping(value="/userHtmlReport", method=RequestMethod.GET)
	public String reportHtml(HttpServletRequest request, HttpServletResponse response){
		//String template = "D:\\TrainingCodes\\MongoDB\\MongoDBWsAndReport\\src\\Table_Report.jrxml";
		//ClassLoader classLoader = getClass().getClassLoader();
		//String template = classLoader.getResource("\\WEB-INF\\Table_Report.jrxml").getPath();
		String rootPath = request.getSession().getServletContext().getRealPath("/");
		String template =  rootPath + "WEB-INF\\Table_Report.jrxml";		
		//.getSession().getServletContext().getRealPath("/");
		String output = rootPath + "WEB-INF\\pages\\tempReport.jsp";
		String mongoURI = "mongodb://localhost:27017/qph";
		MongoDbConnection connection = null;
		ModelAndView model = null;
		try {
			connection = new MongoDbConnection(mongoURI, null, null);
			//model = reportService.generateReportToJsp(template, output, connection);
			model = new ModelAndView();
			
			String html = "<select><option value=5>HTML Code</option></select>";
			model.addObject("html", html);
			
			Map<String, Object> parameters = new HashMap<String, Object>();
			JasperReport jasperReport = JasperCompileManager.compileReport(template);
			
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);
			
			request.getSession().setAttribute(ImageServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE, jasperPrint);
			HtmlExporter exporterHTML = new HtmlExporter();
			SimpleExporterInput exporterInput = new SimpleExporterInput(jasperPrint);
			exporterHTML.setExporterInput(exporterInput);
			SimpleHtmlExporterOutput exporterOutput;
			
			exporterOutput = new SimpleHtmlExporterOutput(output);
			exporterOutput.setImageHandler(new WebHtmlResourceHandler("image?image={0}"));
			exporterHTML.setExporterOutput(exporterOutput);
			
			SimpleHtmlReportConfiguration reportExportConfiguration = new SimpleHtmlReportConfiguration();
			reportExportConfiguration.setWhitePageBackground(false);
			reportExportConfiguration.setRemoveEmptySpaceBetweenRows(true);
			exporterHTML.setConfiguration(reportExportConfiguration);
			exporterHTML.exportReport();
					
			//JasperExportManager.exportReportToHtmlFile(fillReport, output);
			
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} /*catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		return "tempReport";
	}
	
	@RequestMapping(value="/userPDFReport", method=RequestMethod.GET)
	public void showPdfReport(HttpServletRequest request, HttpServletResponse response){
		String rootPath = request.getSession().getServletContext().getRealPath("/");
		String template =  rootPath + "WEB-INF\\Table_Report.jrxml";		
		String mongoURI = "mongodb://localhost:27017/qph";
		MongoDbConnection connection = null;
		
		try {
			connection = new MongoDbConnection(mongoURI, null, null);

			Map<String, Object> parameters = new HashMap<String, Object>();
			JasperReport jasperReport = JasperCompileManager.compileReport(template);

			JasperPrint jasperPrint = JasperFillManager.fillReport(
					jasperReport, parameters, connection);

			JRPdfExporter pdfExporter = new JRPdfExporter();
			SimpleExporterInput exporterInput = new SimpleExporterInput(jasperPrint);
			pdfExporter.setExporterInput(exporterInput);
			
			SimpleOutputStreamExporterOutput exporterOutput;
			exporterOutput = new SimpleOutputStreamExporterOutput(response.getOutputStream());
			pdfExporter.setExporterOutput(exporterOutput);
			pdfExporter.exportReport();
			
		} catch (JRException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value="/showReport", method=RequestMethod.GET)
	public String showJsp(){
		return "tempReport";
	}
}
