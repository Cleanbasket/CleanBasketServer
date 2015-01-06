package com.bridge4biz.wash.data;

public class AddressData {
	public Integer adrid = 0;
	public Integer uid = 0;
	public Integer type = 0;
	public String address = "";
	public String addr_number = "";
	public String addr_building = "";
	public String addr_remainder = "";
	public String rdate = "";

	public AddressData() {

	}

	public AddressData(Integer uid, Integer type, String address, String addr_number, String addr_building, String addr_remainder) {
		this.uid = uid;
		this.type = type;
		this.address = address;
		this.addr_number = addr_number;
		this.addr_building = addr_building;
		this.addr_remainder = addr_remainder;
	}
}
