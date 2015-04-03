package com.qph.dao;

import java.util.List;

import javax.annotation.PostConstruct;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import com.mongodb.DBObject;
import com.qph.dto.ReportDTO;
import com.qph.dto.UserDTO;

@Repository
@Qualifier(value="userMongoDAO")
public class UserMongoDAO implements IDAO<UserDTO>{
	
	private final String USER_COLLECTION = "user";
	@Autowired
	private MongoTemplate mongoTemplate;
	
	private List<UserDTO> collection;
	
	public UserMongoDAO() {
	}
	
	@Override
	public List<UserDTO> getCollection() {
		return collection;
	}
	
	@PostConstruct
	public void initCollection() {
		collection = mongoTemplate.findAll(UserDTO.class, USER_COLLECTION);
	}

}
