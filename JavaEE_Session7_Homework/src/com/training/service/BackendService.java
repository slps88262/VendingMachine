package com.training.service;

import java.util.List;
import java.util.Set;

import com.training.dao.BackEndDao;
import com.training.model.Goods;
import com.training.model.SalesReport;

public class BackendService {

	private static BackendService backendService = new BackendService();
	
	private BackendService() { }
	
	private BackEndDao backEndDao = BackEndDao.getInstance();
	
	public static BackendService getInstance() {
		return backendService;
	}
	
	public List<Goods> queryGoods(){
		return backEndDao.queryAllGoods();
	}
	
	public List<Goods> queryGoodsBetween(int pageNo){
		int startRowNo = (pageNo-1)*10+1;
		int endRowNo = pageNo*10;
		return backEndDao.queryGoodsBetween(startRowNo, endRowNo);
	}
	
	
	public Goods queryGoodsById(String id){
		return backEndDao.queryGoodsById(id);
	}
	
	public Goods queryGood(Goods goods){
		return backEndDao.queryGood(goods);
	}
	
	public boolean updateGoods(Goods goods) {	
		return backEndDao.updateGoods(goods);
	}
	
	public boolean addGoods(Goods goods) {	
		return backEndDao.createGoods(goods);
	}
	
	public boolean deleteGoods(String goodsID) {
		
		return backEndDao.deleteGoods(goodsID);
	}
	
	public Set<SalesReport> queryOrderBetweenDate(String queryStartDate, String queryEndDate){
		return backEndDao.queryOrderBetweenDate(queryStartDate, queryEndDate);
	}
	
}
