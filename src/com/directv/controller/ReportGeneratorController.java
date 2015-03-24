package com.directv.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
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
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.directv.dao.IDAO;
import com.directv.services.IReportService;
import com.jaspersoft.mongodb.connection.MongoDbConnection;

@Controller
@RequestMapping("/main")
public class ReportGeneratorController {
	
	@Autowired
	@Qualifier(value="userMongoDAO")
	private IDAO daoImpl;
	
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
    
	   /**
     * Handles multi-format report requests
     * 
     * @param type the format of the report, i.e pdf
     */
    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public ModelAndView doSalesMultiReport(@RequestParam("type") String type, 
    		ModelAndView modelAndView, ModelMap model) 
		 {
		//logger.debug("Received request to download multi report");
		
		// Retrieve our data from a custom data provider
		// Our data comes from a DAO layer
		
		// Assign the datasource to an instance of JRDataSource
		// JRDataSource is the datasource that Jasper understands
		// This is basically a wrapper to Java's collection classes
    	JRDataSource datasource = (JRDataSource) daoImpl.getDataSource();
		
		// In order to use Spring's built-in Jasper support, 
		// We are required to pass our datasource as a map parameter
		
		// Add our datasource parameter
		model.addAttribute("datasource", datasource);
		// Add the report format
		model.addAttribute("format", type);
		
		// multiReport is the View of our application
		// This is declared inside the /WEB-INF/jasper-views.xml
		modelAndView = new ModelAndView("multiReport", model);
		
		// Return the View and the Model combined
		return modelAndView;
	}
    
	@RequestMapping(value="/userReport", method=RequestMethod.GET)
	public void reportHtml(HttpServletRequest request, HttpServletResponse response){
		String rootPath = request.getSession().getServletContext().getRealPath("/");
		String template =  rootPath + "WEB-INF\\MyReport.jrxml";		
		String output = rootPath + "WEB-INF\\tempReport.jsp";
		String mongoURI = "mongodb://localhost:27017/qph";
		MongoDbConnection connection = null;
		ModelAndView model = null;
		try {
			connection = new MongoDbConnection(mongoURI, null, null);
			model = new ModelAndView();
			
			String html = "<select><option value=5>HTML Code</option></select>";
			model.addObject("html", html);
			
			Map<String, Object> parameters = new HashMap<String, Object>();
			JasperReport jasperReport = JasperCompileManager.compileReport(template);
			
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, (JRDataSource)daoImpl.getDataSource());
			
			request.getSession().setAttribute(ImageServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE, jasperPrint);
			HtmlExporter exporterHTML = new HtmlExporter();
			SimpleExporterInput exporterInput = new SimpleExporterInput(jasperPrint);
			exporterHTML.setExporterInput(exporterInput);
			SimpleHtmlExporterOutput exporterOutput;
			
			exporterOutput = new SimpleHtmlExporterOutput(response.getOutputStream());
			exporterOutput.setImageHandler(new WebHtmlResourceHandler("image?image={0}"));
			exporterHTML.setExporterOutput(exporterOutput);
			
			SimpleHtmlReportConfiguration reportExportConfiguration = new SimpleHtmlReportConfiguration();
			reportExportConfiguration.setWhitePageBackground(false);
			reportExportConfiguration.setRemoveEmptySpaceBetweenRows(true);
			exporterHTML.setConfiguration(reportExportConfiguration);
			exporterHTML.exportReport();
					
		} catch (JRException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value="/showPDFReport", method=RequestMethod.GET)
	public void showPdfReport(HttpServletRequest request, HttpServletResponse response){
		String rootPath = request.getSession().getServletContext().getRealPath("/");
		String template =  rootPath + "WEB-INF\\MyReport.jrxml";		
		String mongoURI = "mongodb://localhost:27017/qph";
		MongoDbConnection connection = null;
		
		JRDataSource dataSource = (JRDataSource)daoImpl.getDataSource();
		
		try {
			connection = new MongoDbConnection(mongoURI, null, null);

			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("SubDataSourceP", dataSource);
			parameters.put("MyName", "Quoc PHAN");
			JasperReport jasperReport = JasperCompileManager.compileReport(template);

			JasperPrint jasperPrint = JasperFillManager.fillReport(
					jasperReport, parameters, new JREmptyDataSource());

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
}
