package com.training.formbean;

import java.math.BigDecimal;

import org.apache.struts.action.ActionForm;

public class GoodsOrderForm extends ActionForm{
	
	private String customerID;
	private int inputMoney;
	private int pageNo;
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	private String searchKeyword;
	public String getSearchKeyword() {
		return searchKeyword;
	}
	public void setSearchKeyword(String searchKeyword) {
		this.searchKeyword = searchKeyword;
	}
	private BigDecimal[] goodsID;
	private int[] buyQuantity;
	public String getCustomerID() {
		return customerID;
	}
	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}
	public int getInputMoney() {
		return inputMoney;
	}
	public void setInputMoney(int inputMoney) {
		this.inputMoney = inputMoney;
	}
	public BigDecimal[] getGoodsID() {
		return goodsID;
	}
	public void setGoodsID(BigDecimal[] goodsID) {
		this.goodsID = goodsID;
	}
	public int[] getBuyQuantity() {
		return buyQuantity;
	}
	public void setBuyQuantity(int[] buyQuantity) {
		this.buyQuantity = buyQuantity;
	}
	
	
	
	
}
