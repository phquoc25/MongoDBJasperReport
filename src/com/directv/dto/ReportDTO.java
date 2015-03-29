package com.directv.dto;

import java.util.Date;

public class ReportDTO {
	
	private String _class;
	private Date curDate;
	private Long timeStamp;
	private Long aggVal;

	public String get_class() {
		return _class;
	}
	public void set_class(String _class) {
		this._class = _class;
	}
	public Date getCurDate() {
		return curDate;
	}
	public void setCurDate(Date curDate) {
		this.curDate = curDate;
	}
	public Long getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(Long timeStamp) {
		this.timeStamp = timeStamp;
	}
	public Long getAggVal() {
		return aggVal;
	}
	public void setAggVal(Long aggVal) {
		this.aggVal = aggVal;
	}
	@Override
	public String toString() {
		StringBuffer builder = new StringBuffer();
		builder.append("ReportDTO [_class=");
		builder.append(_class);
		builder.append(", curDate=");
		builder.append(curDate);
		builder.append(", timeStamp=");
		builder.append(timeStamp);
		builder.append(", aggVal=");
		builder.append(aggVal);
		builder.append("]");
		return builder.toString();
	}

}
