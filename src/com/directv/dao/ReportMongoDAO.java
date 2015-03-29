package com.directv.dao;

import java.util.List;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import com.directv.dto.ReportDTO;

@Repository
@Qualifier(value="reportMongoDAO")
public class ReportMongoDAO implements IDAO<Object>{

	private final String REPORT_COLLECTION = "user";
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Override
	public Object getDataSource() {
		List<ReportDTO> reportList = mongoTemplate.findAll(ReportDTO.class, REPORT_COLLECTION);
		return new JRBeanCollectionDataSource(reportList);
	}

}
