package com.training.model;

import java.util.List;
import java.util.Set;

public class pageGoods {
	
	private int[] pagination;
	private int endPageNo;
	
	public int[] getPagination() {
		return pagination;
	}
	public void setPagination(Set<Goods> searchAllGoods,int num) {
		int page = searchAllGoods.size()/num;
		if(searchAllGoods.size()%num !=0) {
			page++;
		}
	
		pagination = new int[page];
		for(int i=0;i<pagination.length;i++) {
			pagination[i]=i+1;
		}
	}
	
	public void setPagination(List<Goods> searchAllGoods,int num) {
		int page = searchAllGoods.size()/num;
		if(searchAllGoods.size()%num !=0) {
			page++;
		}
	
		pagination = new int[page];
		for(int i=0;i<pagination.length;i++) {
			pagination[i]=i+1;
		}
	}
	
	
	public int getEndPageNo() {
		return endPageNo;
	}
	public void setEndPageNo(Set<Goods> searchAllGoods) {
		int page = searchAllGoods.size()/6;
		if(searchAllGoods.size()%6 !=0) {
			page++;
		}
		this.endPageNo = page;
	}
	
	public void setEndPageNo(List<Goods> searchAllGoods) {
		int page = searchAllGoods.size()/10;
		if(searchAllGoods.size()%10 !=0) {
			page++;
		}
		this.endPageNo = page;
	}
	
}
