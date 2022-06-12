package com.training.servlet;

import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;

import com.training.model.Goods;
import com.training.service.BackendService;
import com.training.vo.GoodsReplenishmentForm;

import net.sf.json.JSONObject;

import com.training.model.SalesReport;
import com.training.model.pageGoods;

public class BackendServlet extends DispatchAction {
	
	private BackendService backendService = BackendService.getInstance();
	
	public ActionForward queryGoods(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse resp) {
		
		GoodsReplenishmentForm goodsReplenishmentForm = (GoodsReplenishmentForm) form;
		
		List<Goods> goods = backendService.queryGoods();
		int pageNo = goodsReplenishmentForm.getPageNo();
		List<Goods> goodsBetween = backendService.queryGoodsBetween(pageNo);
		goodsBetween.stream().forEach(a -> System.out.println(a));
		req.setAttribute("goods", goodsBetween);
		
		pageGoods pageGoods = new pageGoods();
		pageGoods.setPagination(goods, 10);
		pageGoods.setEndPageNo(goods);
		req.setAttribute("pageGoods", pageGoods);
		
		return mapping.findForward("GoodsListView");
	}
	
	
	public ActionForward updateGoods(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse resp) throws Exception {
		HttpSession session = req.getSession();
		GoodsReplenishmentForm goodsReplenishmentForm = (GoodsReplenishmentForm) form;
		Goods goods = new Goods();
		BeanUtils.copyProperties(goods, goodsReplenishmentForm);
		
		boolean updateResult = backendService.updateGoods(goods);
		String message = updateResult ? "商品資料更新成功！" : "商品資料更新失敗！";
		System.out.println(message);
		session.setAttribute("updateMsg", message);
		session.setAttribute("updateGoods", goods);
		
		return mapping.findForward("GoodsReplenishment");
	}
	
	public ActionForward updateGoodsView(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse resp) throws Exception {
		List<Goods> goods = backendService.queryGoods();
		req.setAttribute("goods", goods);
		
		HttpSession session = req.getSession();
		Goods good = (Goods) session.getAttribute("updateGoods");
		if(good != null && goods.contains(good)){
			good = goods.get(goods.indexOf(good));
			req.setAttribute("updateGoods", good);
		}
		
		return mapping.findForward("GoodsReplenishmentView");
	}
	// AJAX
	public ActionForward getUpdateGood(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse resp) throws Exception {
		
		String id = req.getParameter("id");
		Goods good = backendService.queryGoodsById(id);
		
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json");
		PrintWriter out = resp.getWriter();
		out.println(JSONObject.fromObject(good));
		out.flush();
		out.close();
		
		return null;
	}
	
	
	
	public ActionForward addGoods(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse resp) throws Exception {
    	HttpSession session = req.getSession();
		GoodsReplenishmentForm goodsReplenishmentForm = (GoodsReplenishmentForm) form;
		FormFile  filePart = goodsReplenishmentForm.getGoodsImage();
		String fileName = filePart.getFileName();
		goodsReplenishmentForm.setGoodsImageName(fileName);
		Goods goods = new Goods();
		BeanUtils.copyProperties(goods, goodsReplenishmentForm);
		
		boolean addResult = backendService.addGoods(goods);
		goods = backendService.queryGood(goods);
		String message = addResult ? "商品資料新增成功！新增商品編號:"+goods.getGoodsID() : "商品資料新增失敗！";
		System.out.println(message);
		session.setAttribute("addMsg", message);
		if(addResult) {
			String goodsImgPath = getServlet().getInitParameter("GoodsImgPath");
			String serverGoodsImgPath = getServlet().getServletContext().getRealPath(goodsImgPath);
			
			Path serverImgPath = Paths.get(serverGoodsImgPath).resolve(fileName);
			try (InputStream fileContent = filePart.getInputStream();){
				Files.copy(fileContent, serverImgPath, StandardCopyOption.REPLACE_EXISTING);
			}			
		}
		return mapping.findForward("GoodsCreate");
	}
	
	public ActionForward addGoodsView(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse resp) throws Exception {
		return mapping.findForward("GoodsCreateView");
	}
	
	
	public ActionForward deleteGoods(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse resp) throws Exception {
		HttpSession session = req.getSession();
		String goodsId = req.getParameter("id");
		boolean deleteResult = backendService.deleteGoods(goodsId);
		String message = deleteResult ? "商品資料刪除成功！" : "商品資料刪除失敗！";
		System.out.println(message);
		if(deleteResult) {
			String goodsImageName = req.getParameter("goodsImageName");
			String goodsImgPath = getServlet().getInitParameter("GoodsImgPath");
			String serverGoodsImgPath = getServlet().getServletContext().getRealPath(goodsImgPath);
			Path serverImgPath = Paths.get(serverGoodsImgPath).resolve(goodsImageName);
		    Files.delete(serverImgPath);
		}
		session.setAttribute("deleteMsg", message);
		return mapping.findForward("DeleteGoodsRedirect");
	}
	
	public ActionForward querySalesReport(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse resp) throws Exception {
		String queryStartDate = req.getParameter("queryStartDate");
		String queryEndDate = req.getParameter("queryEndDate");
		Set<SalesReport> SalesReports = backendService.queryOrderBetweenDate(queryStartDate, queryEndDate);
		SalesReports.stream().forEach(a -> System.out.println(a));
		req.setAttribute("SalesReports", SalesReports);
		
		return mapping.findForward("GoodsSaleReport");
	}
	
	public ActionForward querySalesReportView(ActionMapping mapping, ActionForm form,
			HttpServletRequest req, HttpServletResponse resp) throws Exception {
		
		return mapping.findForward("GoodsSaleReport");
	}
	
}
