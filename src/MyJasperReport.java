import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

import com.jaspersoft.mongodb.connection.MongoDbConnection;


public class MyJasperReport {
	public static void main(String[] args) {
		
		String mongoURI = "mongodb://localhost:27017/qph";
		MongoDbConnection conn = null;
		
		//MongoClient mongo = new MongoClient( "localhost" , 27017 );
		
		try {
			conn = new MongoDbConnection(mongoURI, null, null);

			Map<String, Object> parameters = new HashMap<String, Object>();

			//parameters.put(MongoDbDataSource.CONNECTION, conn);
			
			JasperReport jasperReport = JasperCompileManager.compileReport("D:\\TrainingCodes\\reportsTemplate\\Table_Report.jrxml");
			
			JasperPrint fillReport = JasperFillManager.fillReport(jasperReport, parameters, conn);
			
			//JasperExportManager.exportReportToHtmlFile("D:\\TrainingCodes\\reportsTemplate\\MongoDbReport.html");
			JasperExportManager.exportReportToPdfFile(fillReport,"D:\\TrainingCodes\\reportsTemplate\\Table_Report.pdf");
			
		} catch (JRException e) {
			e.printStackTrace();
		} finally {
			if(conn!=null)
				conn.close();
		}

	}
}
