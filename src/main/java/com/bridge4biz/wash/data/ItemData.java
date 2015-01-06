package com.bridge4biz.wash.data;

public class ItemData {
	public Integer itid = 0;
	public Integer oid = 0;
	public Integer item_code = 0;
	public Integer price = 0;
	public Integer count = 0;
	public String rdate = "";

	public ItemData() {

	}

	public ItemData(Integer oid, Integer item_code, Integer price, Integer count) {
		this.oid = oid;
		this.item_code = item_code;
		this.price = price;
		this.count = count;
	}
}