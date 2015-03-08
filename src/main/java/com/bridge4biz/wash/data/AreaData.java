package com.bridge4biz.wash.data;

public class AreaData {
	public Integer acid = 0;
	public String areacode = "";
	public String area = "";

	public AreaData() {
		
	}
	
	public AreaData(String areacode, String area) {
		this.areacode = areacode;
		this.area = area;
	}
	
	public AreaData(int acid, String areacode, String area) {
		this.acid = acid;
		this.areacode = areacode;
		this.area = area;
	}
}