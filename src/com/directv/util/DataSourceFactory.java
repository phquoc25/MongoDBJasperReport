package com.directv.util;

import com.directv.dao.IDAO;
import com.directv.dao.UserMongoDAO;

import net.sf.jasperreports.engine.JRDataSource;

public class DataSourceFactory {
	static IDAO daoImpl = new UserMongoDAO();
	public static JRDataSource getDataSource() {
		return (JRDataSource) daoImpl.getDataSource();
	}
}
