package com.bridge4biz.wash.service;

public class Address {
	public Integer adrid = 0;
	public Integer uid = 0;
	public Integer type = 0;
	public String address = "";
	public String addr_number = "";
	public String addr_building = "";
	public String addr_remainder = "";
	public String rdate = "";
	
	public Address() {
		
	}
	
	public Address(int uid, String address, String addr_building) {
		this.uid = uid;
		this.address = address;
		this.addr_building = addr_building;
	}

	public Address(Integer uid, String address,  String addr_number, String addr_building, String addr_remainder) {
		this.uid = uid;
		this.address = address;
		this.addr_number = addr_number;
		this.addr_building = addr_building;
		this.addr_remainder = addr_remainder;
	}

	public Address(Integer uid, Integer type, String address,  String addr_number, String addr_building, String addr_remainder) {
		this.uid = uid;
		this.type = type;
		this.address = address;
		this.addr_number = addr_number;
		this.addr_building = addr_building;
		this.addr_remainder = addr_remainder;
	}

}