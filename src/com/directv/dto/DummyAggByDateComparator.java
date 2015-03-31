package com.directv.dto;

import java.util.Comparator;

public class DummyAggByDateComparator implements Comparator<DummyAggregateDTO>{

	@Override
	public int compare(DummyAggregateDTO o1, DummyAggregateDTO o2) {
		return o1.getCurDate().compareTo(o2.getCurDate());
	}

}
