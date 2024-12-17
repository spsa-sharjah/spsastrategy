package com.spsa.strategy.builder.response;

import java.util.List;

import org.springframework.http.HttpEntity;

public class DatatableResponse<T> extends HttpEntity<T>{

	private int draw; // Draw counter from the request
	private long recordsTotal; // Total number of records in the database
	private long recordsFiltered;  // Total records after applying search filter
	private List<T> data; // Array of records
	
	public DatatableResponse() {
		super();
	}
	public DatatableResponse(int draw, long recordsTotal, long recordsFiltered, List<T> data) {
		super();
		this.draw = draw;
		this.recordsTotal = recordsTotal;
		this.recordsFiltered = recordsFiltered;
		this.data = data;
	}
	public int getDraw() {
		return draw;
	}
	public void setDraw(int draw) {
		this.draw = draw;
	}
	public long getRecordsTotal() {
		return recordsTotal;
	}
	public void setRecordsTotal(long recordsTotal) {
		this.recordsTotal = recordsTotal;
	}
	public long getRecordsFiltered() {
		return recordsFiltered;
	}
	public void setRecordsFiltered(long recordsFiltered) {
		this.recordsFiltered = recordsFiltered;
	}
	public List<T> getData() {
		return data;
	}
	public void setData(List<T> data) {
		this.data = data;
	}
}
