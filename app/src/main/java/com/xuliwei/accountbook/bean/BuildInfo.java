package com.xuliwei.accountbook.bean;

import java.io.Serializable;

public class BuildInfo implements Serializable {
	private int id;
	private String description;
	private double price;
	private double size;	
	private String workTime;
	private String writeTime;
	private int workTimeInt;

	public BuildInfo(String description, double price, double size,
			String workTime, String writeTime,int workTimeInt) {
		super();
		this.description = description;
		this.price = price;
		this.size = size;
		this.workTime = workTime;
		this.writeTime = writeTime;
		this.workTimeInt=workTimeInt;
	}

	public void setWorkTimeInt(int workTimeInt) {
		this.workTimeInt = workTimeInt;
	}

	public int getWorkTimeInt() {
		return workTimeInt;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getWriteTime() {
		return writeTime;
	}
	public void setWriteTime(String writeTime) {
		this.writeTime = writeTime;
	}
	public String getWorkTime() {
		return workTime;
	}
	public void setWorkTime(String workTime) {
		this.workTime = workTime;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public double getSize() {
		return size;
	}
	public void setSize(double size) {
		this.size = size;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	

}
