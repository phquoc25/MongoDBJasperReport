package com.qph.dao;

import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import com.qph.dto.DummyAggByDateComparator;
import com.qph.dto.DummyAggregateDTO;

@Repository(value="dummyAggregateMongoDAO")
public class DummyAggregateMongoDAO implements IDAO<DummyAggregateDTO>{
	
	private Logger log = Logger.getLogger("dummyAggregateMongoDAO");
	
	private final String DATA_COLLECTION = "DummyAggregate";
	@Autowired
	private MongoTemplate mongoTemplate;
	
	private List<DummyAggregateDTO> collection;
	
	@Override
	public List<DummyAggregateDTO> getCollection() {
		if(collection == null){
			initCollection();
		}
		return  collection;
	}
	
	public void initCollection() {
		log.info("dummyAggregateMongoDAO: Hit DB");
		collection = mongoTemplate.findAll(DummyAggregateDTO.class, DATA_COLLECTION);
		Collections.sort(collection, new DummyAggByDateComparator());
	}
}
