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
import com.directv.services.AbstractReportService;

@Controller
@RequestMapping("/report")
public class ReportGeneratorController {
	
	@Autowired
	private AbstractReportService reportServiceImpl;
	
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
    	//reportServiceImpl.initCollection();
		JRDataSource dataSource = new JRBeanCollectionDataSource(reportServiceImpl.getCollection());
    	JRDataSource dataSource1 = new JRBeanCollectionDataSource(reportServiceImpl.getCollection());
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
    
 
    
	@RequestMapping(value="/showchart", method=RequestMethod.GET)
	public ModelAndView reportHtml(HttpServletRequest request, HttpServletResponse response){
		
		logger.debug("Received request to show report");
		reportServiceImpl.initCollection();
		/*String rootPath = request.getSession().getServletContext().getRealPath("/");
		String template =  rootPath + "WEB-INF\\PieChartReport.jasper";		*/
		
		ModelAndView modelAndView = null;
		String reportBody = reportServiceImpl.getReportBody(request);
		modelAndView = reportServiceImpl.generateReportPage(reportBody);
		return modelAndView;
	}
	
}
