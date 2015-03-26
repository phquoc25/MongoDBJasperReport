package com.directv.dao;

import java.util.List;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import com.directv.dto.DummyAggregateDTO;

@Repository
@Qualifier("dummyAggregateMongoDAO")
public class DummyAggregateMongoDAO implements IDAO<Object>{

	private final String DATA_COLLECTION = "DummyAggregate";
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Override
	public Object getDataSource() {
		List<DummyAggregateDTO> dataList = mongoTemplate.findAll(DummyAggregateDTO.class, DATA_COLLECTION);
		return new JRBeanCollectionDataSource(dataList);
	}
}
