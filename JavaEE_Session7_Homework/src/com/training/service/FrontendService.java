package com.training.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.training.dao.FrontEndDao;
import com.training.model.Account;
import com.training.model.BuyGoodsRtn;
import com.training.model.Goods;
import com.training.model.pageGoods;

public class FrontendService {

	private static FrontendService frontendService = new FrontendService();
	
	private FrontendService() { }
	
	private FrontEndDao frontEndDao = FrontEndDao.getInstance();
	
	public static FrontendService getInstance() {
		return frontendService;
	}
	
	
	public Account queryMemberByIdentificationNo(String identificationNo) {
		return frontEndDao.queryMemberByIdentificationNo(identificationNo);
	}
	
	public Set<Goods> searchGoods(String searchKeyword, int pageNo){
		if(searchKeyword==null) {
			searchKeyword = "";
		}
		int startRowNo = (pageNo-1)*6+1;
		int endRowNo = pageNo*6;
		
		return frontEndDao.searchGoods(searchKeyword, startRowNo, endRowNo);
	}
	
	public pageGoods searchGoodsPage(String searchKeyword) {
		if(searchKeyword==null) {
			searchKeyword = "";
		}
		
		Set<Goods> searchAllGoods = frontEndDao.searchAllGoods(searchKeyword);
		pageGoods pageGoods = new pageGoods();
		pageGoods.setEndPageNo(searchAllGoods);
		pageGoods.setPagination(searchAllGoods,6);
		
		return pageGoods;
	}
	
	
	public Map<BigDecimal, Goods> queryBuyGoods(Set<BigDecimal> goodsIDs){
		Map<BigDecimal, Goods> buyGoods = frontEndDao.queryBuyGoods(goodsIDs);
//		Map<BigDecimal, Goods> buyGoodsFinal = new HashMap<>();
//		Iterator<Entry<BigDecimal, Goods>> iterator = buyGoods.entrySet().iterator();
//		while(iterator.hasNext()) {
//			Entry<BigDecimal, Goods> entry= iterator.next();
//			int quantity = entry.getValue().getGoodsQuantity();
//			if(quantity > 0) {
//				buyGoodsFinal.put(entry.getKey(), entry.getValue());
//			}
//		}
		
		return buyGoods;
	}
	
	
	public boolean checkGoodsQuantity(int money, Map<Goods,Integer> goodsOrders){
		boolean tradeSuccess = false;
		int orderPrice = 0;
		
		for(Goods goodsOrder: goodsOrders.keySet()) {
			if(goodsOrder.getGoodsQuantity() == 0) {
				tradeSuccess = false;
				System.out.println("庫存不足");
			}else if(goodsOrder.getGoodsQuantity()>=goodsOrders.get(goodsOrder)) {
				tradeSuccess = true;
				orderPrice += goodsOrder.getGoodsPrice()*goodsOrders.get(goodsOrder);
			}else {
				tradeSuccess = false;
			}
		}
		
		if(orderPrice > money) {
			tradeSuccess = false;
		}
		return tradeSuccess;
	}
	
	public boolean batchUpdateGoodsQuantity(Set<Goods> goods) {
		return frontEndDao.batchUpdateGoodsQuantity(goods);
	}
	
	public boolean batchCreateGoodsOrder(String customerID, Map<Goods,Integer> goodsOrders) {
		return frontEndDao.batchCreateGoodsOrder(customerID, goodsOrders);
	}
	
	public BuyGoodsRtn orderbuyGoods(Map<Goods,Integer> goodsOrders, int customerMoney) {
		BuyGoodsRtn buyGoodsRtn = new BuyGoodsRtn();
		int orderPrice = 0;
		for(Goods goods: goodsOrders.keySet()) {
			int Quantity = goodsOrders.get(goods);
			orderPrice += goods.getGoodsPrice()*goodsOrders.get(goods);
		}
		int cash = customerMoney-orderPrice;
		
		System.out.println("buyGoods...");
		System.out.println("投入金額： "+ customerMoney);
		System.out.println("購買金額： "+ orderPrice);
		System.out.println("找零金額： "+ cash);
		for(Goods goods: goodsOrders.keySet()) {
			System.out.println("商品名稱: "+goods.getGoodsName() +" 商品金額: " + goods.getGoodsPrice() + " 購買數量: "+ goodsOrders.get(goods));				
		}		
		System.out.println("-------------------------");
		
		buyGoodsRtn.setCustomerMoney(customerMoney);
		buyGoodsRtn.setOrderPrice(orderPrice);
		buyGoodsRtn.setCash(cash);
		buyGoodsRtn.setGoodsOrders(goodsOrders);
			
		return buyGoodsRtn;
	}

	
}
