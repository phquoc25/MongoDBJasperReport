package com.qph.dto;

public class UserDTO {
	private String userName;
	private String fullName;
	private Integer bugFixing;
	private Integer feature;
	private Integer research;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public Integer getBugFixing() {
		return bugFixing;
	}
	public void setBugFixing(Integer bugFixing) {
		this.bugFixing = bugFixing;
	}
	public Integer getFeature() {
		return feature;
	}
	public void setFeature(Integer feature) {
		this.feature = feature;
	}
	public Integer getResearch() {
		return research;
	}
	public void setResearch(Integer research) {
		this.research = research;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UserDTO [userName=");
		builder.append(userName);
		builder.append(", fullName=");
		builder.append(fullName);
		builder.append(", bugFixing=");
		builder.append(bugFixing);
		builder.append(", feature=");
		builder.append(feature);
		builder.append(", research=");
		builder.append(research);
		builder.append("]");
		return builder.toString();
	}
	
	
}
