package com.directv.dao;

import java.util.List;


public interface IDAO<T> {
	public List<T> getCollection();
	public void initCollection();
}
