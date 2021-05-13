package com.masterteknoloji.trafficanalyzer.web.rest.vm;

import java.math.BigInteger;

public class VideoRecordSummaryVM {

	Object date;
	String type;
	BigInteger count;
	
	public VideoRecordSummaryVM() {
		super();
		// TODO Auto-generated constructor stub
	}

	public VideoRecordSummaryVM(Object date, String type, BigInteger count) {
		super();
		this.date = date;
		this.type = type;
		this.count = count;
	}




	public Object getDate() {
		return date;
	}

	public void setDate(Object date) {
		this.date = date;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public BigInteger getCount() {
		return count;
	}

	public void setCount(BigInteger count) {
		this.count = count;
	}
	
		
}
