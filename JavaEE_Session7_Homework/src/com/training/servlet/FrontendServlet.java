package com.training.servlet;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.training.formbean.GoodsOrderForm;
import com.training.model.Account;
import com.training.model.BuyGoodsRtn;
import com.training.model.Goods;
import com.training.model.pageGoods;
import com.training.service.FrontendService;

public class FrontendServlet extends DispatchAction {
	
	private FrontendService frontendService = FrontendService.getInstance();
	
	public ActionForward buyGoods(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse resp) throws IOException {
		GoodsOrderForm goodsOrderForm = (GoodsOrderForm) form;
		// 登入身分
		HttpSession session = req.getSession();	
		Account account = (Account) session.getAttribute("account");
		String customerID = account.getIdentificationNo();

		
		Set<BigDecimal> goodsIDs = new HashSet<>();
		BigDecimal[] GoodsIDsA = goodsOrderForm.getGoodsID();
		int[] buyQuantitys = goodsOrderForm.getBuyQuantity();
		// 購買商品及數量
		Map<String,Integer> goodsOrder = new HashMap<>();
		for(int i=0;i<buyQuantitys.length;i++) {
			if(buyQuantitys[i]>0) {
				goodsIDs.add(GoodsIDsA[i]);
				goodsOrder.put(GoodsIDsA[i].toString(), buyQuantitys[i]);
			}
		}
		// 查詢庫存
		Map<BigDecimal,Goods> buyGoods = frontendService.queryBuyGoods(goodsIDs);
		
		// 確認購買庫存(不足，部份給貨)
		Map<Goods,Integer> goodsOrders = new HashMap<>();
		goodsIDs.stream().forEach(goodsID -> {
			Goods g = buyGoods.get(goodsID);
			if(g.getGoodsQuantity()>goodsOrder.get(goodsID.toString())) {
				goodsOrders.put(g, goodsOrder.get(goodsID.toString()));
			}else {
				goodsOrders.put(g, g.getGoodsQuantity());
			}
		});
		
		
		// 確認金額並扣款
		int customerMoney = goodsOrderForm.getInputMoney();
		boolean tradeSuccess = frontendService.checkGoodsQuantity(customerMoney, goodsOrders);
		
		if(tradeSuccess) {
			// 更新庫存
			buyGoods.values().stream().forEach(g -> g.setGoodsQuantity(g.getGoodsQuantity() - goodsOrders.get(g)));
			boolean updateSuccess = frontendService.batchUpdateGoodsQuantity(buyGoods.values().stream().collect(Collectors.toSet()));
			String messageGoodsUpdateResult = updateSuccess ? "商品庫存更新成功!":"商品庫存更新失敗!";
			System.out.println(messageGoodsUpdateResult);
			
			// 訂單新增
			boolean insertSuccess = frontendService.batchCreateGoodsOrder(customerID, goodsOrders);
			String messageOrderResult = insertSuccess ? "建立訂單成功!" : "建立訂單失敗！";
			System.out.println(messageOrderResult);
			
			BuyGoodsRtn buyGoodsRtn =frontendService.orderbuyGoods(goodsOrders, customerMoney);
			req.setAttribute("buyGoodsRtn", buyGoodsRtn);
			
		} else {
			System.out.println("投入金額不足");
		}
		
		String searchKeyword = goodsOrderForm.getSearchKeyword();
		int pageNo = goodsOrderForm.getPageNo();
		
		Set<Goods> searchGoods = frontendService.searchGoods(searchKeyword, pageNo);
		searchGoods.stream().forEach(a -> System.out.println(a));
		req.setAttribute("searchGoods", searchGoods);
		
		pageGoods pageGoods = frontendService.searchGoodsPage(searchKeyword);
		req.setAttribute("pageGoods", pageGoods);
		

		return mapping.findForward("VendingMachineView");
	}
	
	public ActionForward searchGoods(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse resp) throws IOException {
		GoodsOrderForm goodsOrderForm = (GoodsOrderForm) form;
		String searchKeyword = goodsOrderForm.getSearchKeyword();
		int pageNo = goodsOrderForm.getPageNo();
		
		Set<Goods> searchGoods = frontendService.searchGoods(searchKeyword, pageNo);
		searchGoods.stream().forEach(a -> System.out.println(a));
		req.setAttribute("searchGoods", searchGoods);
		
		pageGoods pageGoods = frontendService.searchGoodsPage(searchKeyword);
		req.setAttribute("pageGoods", pageGoods);
		
		return mapping.findForward("VendingMachineView");
	}
	
	
}
