package com.qph.services;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

public class PDFReportGenerator extends AbStractReportGenerator{

	@Override
	public void generateReport(String outputFileName) throws JRException {

		JasperPrint jasperPrint = JasperFillManager.fillReport(
				getReportTemplatePath(), getModel(), new JREmptyDataSource());

		JRPdfExporter pdfExporter = new JRPdfExporter();
		SimpleExporterInput exporterInput = new SimpleExporterInput(jasperPrint);
		pdfExporter.setExporterInput(exporterInput);
		
		SimpleOutputStreamExporterOutput exporterOutput;
		exporterOutput = new SimpleOutputStreamExporterOutput(outputFileName);
		pdfExporter.setExporterOutput(exporterOutput);
		pdfExporter.exportReport();
	}

	@Override
	public void generateReport(HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}

}
