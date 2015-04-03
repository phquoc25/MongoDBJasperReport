package com.qph.dao;

import java.util.List;

import javax.annotation.PostConstruct;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import com.qph.dto.DummyAggregateDTO;
import com.qph.dto.ReportDTO;

@Repository
@Qualifier(value="reportMongoDAO")
public class ReportMongoDAO implements IDAO<ReportDTO>{

	private final String REPORT_COLLECTION = "user";
	@Autowired
	private MongoTemplate mongoTemplate;
	
	private List<ReportDTO> collection;

	@Override
	public List<ReportDTO> getCollection() {
		return collection;
	}

	@PostConstruct
	public void initCollection() {
		collection = mongoTemplate.findAll(ReportDTO.class, REPORT_COLLECTION);
	}
}
