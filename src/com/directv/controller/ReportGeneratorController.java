package com.directv.controller;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

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
	public ModelAndView reportHtml(HttpServletRequest request, HttpServletResponse response, 
						@RequestParam String chartType, @RequestParam String reportType){
		
		logger.debug("Received request to show report");
		reportServiceImpl.initCollection();
		reportServiceImpl.setChartType(chartType);
		
		JRDataSource dataSource = new JRBeanCollectionDataSource(reportServiceImpl.getCollection());
		JRDataSource dataSource1 = new JRBeanCollectionDataSource(reportServiceImpl.getCollection());
		Map<String, Object> reportParameters = new HashMap<String, Object>();
		reportParameters.put("SubDataSource", dataSource);
		reportParameters.put("SubDataSource1", dataSource1);
		reportParameters.put("ChartTitle", "Utterance Report");
		reportParameters.put("dateFormater", new SimpleDateFormat("yyyy-MM-dd"));
		
		reportServiceImpl.setReportParameters(reportParameters);
		ModelAndView modelAndView = null;
		String reportBody = reportServiceImpl.getReportBody(request);
		Map<String, Object> pageParameters = new HashMap<String, Object>();
		pageParameters.put("reportBody", reportBody);
		modelAndView = reportServiceImpl.generateReportPage(pageParameters);
		return modelAndView;
	}
	
}
