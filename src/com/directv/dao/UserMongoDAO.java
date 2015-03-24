package com.directv.dao;

import java.util.List;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import com.directv.dto.UserDTO;
import com.mongodb.DBObject;

@Repository
@Qualifier(value="userMongoDAO")
public class UserMongoDAO implements IDAO<Object>{
	
	private final String USER_COLLECTION = "user";
	@Autowired
	private MongoTemplate mongoTemplate;
	
	public UserMongoDAO() {
	}
	
	@Override
	public Object getDataSource() {
		List<UserDTO> userList = mongoTemplate.findAll(UserDTO.class, USER_COLLECTION);
		return new JRBeanCollectionDataSource(userList);
	}

}
