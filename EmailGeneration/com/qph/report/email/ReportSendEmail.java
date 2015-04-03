package com.qph.report.email;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.ui.ModelMap;

import com.qph.dao.IDAO;
import com.qph.report.email.supporter.AbstractMailSupporter;
import com.qph.services.AbStractReportGenerator;
import com.qph.services.PDFReportGenerator;
import com.qph.util.ApplicationContextProvider;

public class ReportSendEmail {
	public static void main(String[] args) {

		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"spring/applicationContext.xml");
		//ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
		String reportTemplatePath = "D:/PieChartReport.jasper";
		String outputFileName = "D:/UtterranceReport.pdf";

		// Generating report PDF file
		ModelMap model = new ModelMap();
		IDAO reportDaoImpl = (IDAO) applicationContext
				.getBean("dummyAggregateMongoDAO");
		JRDataSource dataSource = new JRBeanCollectionDataSource(
				reportDaoImpl.getCollection());
		JRDataSource dataSource1 = new JRBeanCollectionDataSource(
				reportDaoImpl.getCollection());
		model.put("datasource", new JREmptyDataSource());
		model.put("ChartTitle", "Utt per day");
		model.put("SubDataSource", dataSource);
		model.put("SubDataSource1", dataSource1);
		model.put("dateFormater", new SimpleDateFormat("yyyy-MM-dd"));

		AbStractReportGenerator pdfReportGenerator = new PDFReportGenerator();
		pdfReportGenerator.setReportTemplatePath(reportTemplatePath);
		pdfReportGenerator.setModel(model);
		try {
			pdfReportGenerator.generateReport(outputFileName);
		} catch (JRException e) {
			e.printStackTrace();
		}
		AbstractMailSupporter mailSupporter = (AbstractMailSupporter) applicationContext
				.getBean("outlookMailSupporter");
		List<String> attachments = new ArrayList<>();
		attachments.add(outputFileName);
		String body = "Dear, \n\nThis email is generated automatically for sending utterance report."
				+ "\n\nThanks & Best Regards \nVoice analytic team";
		mailSupporter.setAttachments(attachments);
		mailSupporter.setBody(body);
		
		mailSupporter.sendMail();
		// defining destination and sender emails

		// preparing the email attachment

		// sending email
	}
}
