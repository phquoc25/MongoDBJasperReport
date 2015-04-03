package com.qph.util;

import com.qph.dao.IDAO;
import com.qph.dao.UserMongoDAO;

import net.sf.jasperreports.engine.JRDataSource;

public class DataSourceFactory {
	static IDAO daoImpl = new UserMongoDAO();

}
