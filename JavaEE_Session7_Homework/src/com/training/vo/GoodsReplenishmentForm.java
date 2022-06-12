package com.training.vo;

import java.math.BigDecimal;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

public class GoodsReplenishmentForm extends ActionForm{
	
		private long goodsID;
		private int goodsPrice;
		private int goodsQuantity;	
		private String status;
		private String goodsName;
		private FormFile goodsImage;
		private String goodsImageName;
		private int pageNo;
		
		public int getPageNo() {
			return pageNo;
		}

		public void setPageNo(int pageNo) {
			this.pageNo = pageNo;
		}

		public String getGoodsImageName() {
			return goodsImageName;
		}

		public void setGoodsImageName(String goodsImageName) {
			this.goodsImageName = goodsImageName;
		}

		public String getGoodsName() {
			return goodsName;
		}
		public void setGoodsName(String goodsName) {
			this.goodsName = goodsName;
		}
		public FormFile getGoodsImage() {
			return goodsImage;
		}
		public void setGoodsImage(FormFile goodsImage) {
			this.goodsImage = goodsImage;
		}
	
		public long getGoodsID() {
			return goodsID;
		}

		public void setGoodsID(long goodsID) {
			this.goodsID = goodsID;
		}

		public int getGoodsPrice() {
			return goodsPrice;
		}
		public void setGoodsPrice(int goodsPrice) {
			this.goodsPrice = goodsPrice;
		}
		public int getGoodsQuantity() {
			return goodsQuantity;
		}
		public void setGoodsQuantity(int goodsQuantity) {
			this.goodsQuantity = goodsQuantity;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		
		
}
