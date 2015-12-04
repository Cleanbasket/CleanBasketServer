package com.bridge4biz.wash.service;

public class Item {
	public Integer itid = 0;
	public Integer oid = 0;
	public Integer item_code = 0;
	public int category = 0;
	public String name = "";
	public String descr = "";
	public Integer price = 0;
	public Integer scope = 0;
	public Integer count = 0;
	public String img = "";
	public String rdate = "";
	public Integer info = 0;
	public double discount_rate = 0;
	
	public Item() {
		
	}
	
	public Item(ItemCode itemCode) {
		this.item_code = itemCode.item_code;
		this.category = itemCode.category;
		this.name = itemCode.name;
		this.descr = itemCode.descr;
		this.price = itemCode.price;
		this.scope = itemCode.scope;
		this.img = itemCode.img;
	}
}
