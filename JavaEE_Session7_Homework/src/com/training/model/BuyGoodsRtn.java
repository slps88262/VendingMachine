package com.training.model;

import java.util.Map;

public class BuyGoodsRtn {
	
	private int customerMoney;
	private int orderPrice;
	private int cash;
	private Map<Goods,Integer> goodsOrders;
	
	public int getCustomerMoney() {
		return customerMoney;
	}
	public void setCustomerMoney(int customerMoney) {
		this.customerMoney = customerMoney;
	}
	public int getOrderPrice() {
		return orderPrice;
	}
	public void setOrderPrice(int orderPrice) {
		this.orderPrice = orderPrice;
	}
	public int getCash() {
		return cash;
	}
	public void setCash(int cash) {
		this.cash = cash;
	}
	public Map<Goods, Integer> getGoodsOrders() {
		return goodsOrders;
	}
	public void setGoodsOrders(Map<Goods, Integer> goodsOrders) {
		this.goodsOrders = goodsOrders;
	}
	
	
}
