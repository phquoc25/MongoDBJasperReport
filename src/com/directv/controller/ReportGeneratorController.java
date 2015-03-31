package com.directv.controller;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleHtmlReportConfiguration;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.j2ee.servlets.ImageServlet;
import net.sf.jasperreports.web.util.WebHtmlResourceHandler;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.directv.dao.IDAO;

@Controller
@RequestMapping("/report")
public class ReportGeneratorController {
	
	@Autowired
	@Qualifier(value="dummyAggregateMongoDAO")
	private IDAO daoImpl;
	
	private Logger logger = Logger.getLogger("ReportGeneratorController");
	
	public ReportGeneratorController() {
		logger.debug("Initialization of ReportGeneratorController");
	}
	/**
	 * Handles and retrieves the download page
	 * 
	 * @return the name of the JSP page
	 */
    @RequestMapping(method = RequestMethod.GET)
    public String getDownloadPage() {
    	logger.debug("Received request to show download page");
    
    	// Do your work here. Whatever you like
    	// i.e call a custom service to do your business
    	// Prepare a model to be used by the JSP page
    	
    	// This will resolve to /WEB-INF/jsp/downloadpage.jsp
    	return "report";
	}
    
	   /**
     * Handles multi-format report requests
     * 
     * @param type the format of the report, i.e pdf
     */
    @RequestMapping(value = "/downloadchart/{chartType}", method = RequestMethod.GET)
    public ModelAndView doSalesMultiReport(@RequestParam("type") String type, @PathVariable String chartType,
    		ModelAndView modelAndView, ModelMap model, HttpServletRequest request, HttpServletResponse response) 
		 {
		logger.debug("Received request to download report type " + type);
    	//daoImpl.initCollection();
		JRDataSource dataSource = new JRBeanCollectionDataSource(daoImpl.getCollection());
    	JRDataSource dataSource1 = new JRBeanCollectionDataSource(daoImpl.getCollection());
		model.put("datasource", new JREmptyDataSource());
		model.put("format", type);
		model.put("requestObject", request);
		model.put("ChartTitle", "Utt per day");
		model.put("SubDataSource", dataSource);
		model.put("SubDataSource1", dataSource1);
		model.put("dateFormater", new SimpleDateFormat("yyyy-MM-dd"));
		
		// multiReport is the View of our application
		// This is declared inside the /WEB-INF/jasper-views.xml
		switch(chartType){
		case "pie":
			modelAndView = new ModelAndView("pieChartMultiReport", model);
			break;
		case "bar":
			modelAndView = new ModelAndView("barChartMultiReport", model);
			break;
		case "line":
			modelAndView = new ModelAndView("lineChartMultiReport", model);
			break;
		}
		
		// Return the View and the Model combined
		return modelAndView;
	}
    
 
    
	@RequestMapping(value="/showpiechart", method=RequestMethod.GET)
	public ModelAndView reportHtml(HttpServletRequest request, HttpServletResponse response){
		
		logger.debug("Received request to show report");
		daoImpl.initCollection();
		String rootPath = request.getSession().getServletContext().getRealPath("/");
		String template =  rootPath + "WEB-INF\\PieChartReport.jasper";		
		
		ModelAndView modelAndView = null;
		String reportBody = getReportBody(request, rootPath, template);
		modelAndView = generateReportPage(reportBody);
		modelAndView.addObject("pdfLink", "/MongoDBWsAndReport/report/downloadchart/pie?type=pdf");
		return modelAndView;
	}
	
	@RequestMapping(value="/showbarchart", method=RequestMethod.GET)
	public ModelAndView showBarChart(HttpServletRequest request, HttpServletResponse response){
		
		logger.debug("Received request to show report");
		daoImpl.initCollection();
		String rootPath = request.getSession().getServletContext().getRealPath("/");
		String template =  rootPath + "WEB-INF\\BarChart.jasper";		
		
		ModelAndView modelAndView = null;
		String reportBody = getReportBody(request, rootPath, template);
		modelAndView = generateReportPage(reportBody);
		modelAndView.addObject("pdfLink", "/MongoDBWsAndReport/report/downloadchart/bar?type=pdf");
		return modelAndView;
	}
	
	@RequestMapping(value="/showlinechart", method=RequestMethod.GET)
	public ModelAndView showLineChart(HttpServletRequest request, HttpServletResponse response){
		
		logger.debug("Received request to show report");
		daoImpl.initCollection();
		String rootPath = request.getSession().getServletContext().getRealPath("/");
		String template =  rootPath + "WEB-INF\\LineChart.jasper";		
		
		ModelAndView modelAndView = null;
		String reportBody = getReportBody(request, rootPath, template);
		modelAndView = generateReportPage(reportBody);
		modelAndView.addObject("pdfLink", "/MongoDBWsAndReport/report/downloadchart/line?type=pdf");
		return modelAndView;
	}
	
	public ModelAndView generateReportPage(String reportBody) {
		ModelAndView modelAndView;
		ModelMap model = new ModelMap();
		model.put("reportBody", reportBody);
		//model.put("pdfLink", "/MongoDBWsAndReport/main/downloadpiechart?type=pdf");
		modelAndView = new ModelAndView("downloadpage");
		modelAndView.addAllObjects(model);
		return modelAndView;
	}
	
	public String getReportBody(HttpServletRequest request, String rootPath,
			String template) {
		JRDataSource dataSource = new JRBeanCollectionDataSource(daoImpl.getCollection());
		JRDataSource dataSource1 = new JRBeanCollectionDataSource(daoImpl.getCollection());
		BufferedReader reader = null;
		StringBuffer reportBody = null;
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("SubDataSource", dataSource);
			parameters.put("SubDataSource1", dataSource1);
			parameters.put("ChartTitle", "Voice Usage: 'Utterances per Day'");
			parameters.put("dateFormater", new SimpleDateFormat("yyyy-MM-dd"));
			//JasperReport jasperReport = JasperCompileManager.compileReport(template);
			
			JasperPrint jasperPrint = JasperFillManager.fillReport(template, parameters, new JREmptyDataSource());
			
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(reader != null){
				try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return reportBody.toString();
	}
	
	@RequestMapping(value="/showResult", method=RequestMethod.GET)
	public String showResult(HttpServletRequest request, HttpServletResponse response){
		return "MyReportOutput"; 
	}
}
