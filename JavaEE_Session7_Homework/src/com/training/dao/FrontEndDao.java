package com.training.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import com.training.model.Account;
import com.training.model.Goods;

public class FrontEndDao {
	
	private static FrontEndDao frontEndDao = new FrontEndDao();
	private FrontEndDao() {	}
	public static FrontEndDao getInstance() {
		return frontEndDao;
	}

	public Account queryMemberByIdentificationNo(String identificationNo){
		Account account = new Account();
		String queryMemberBIN = "SELECT * FROM BEVERAGE_MEMBER WHERE IDENTIFICATION_NO = ?";
		try (Connection conn = DBConnectionFactory.getOracleDBConnection();
				PreparedStatement stmt = conn.prepareStatement(queryMemberBIN)){
			stmt.setString(1, identificationNo);
			try (ResultSet rs = stmt.executeQuery()){
				while(rs.next()) {
					String idNo = rs.getString("IDENTIFICATION_NO");
					String password = rs.getString("PASSWORD");
					String customerName = rs.getString("CUSTOMER_NAME");
					account.setIdentificationNo(idNo);
					account.setPassword(password);
					account.setCustomerName(customerName);
				}
			} catch (SQLException e) {
				throw e;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return account;
	}
	
	public Set<Goods> searchGoods(String searchKeyword, int startRowNo, int endRowNo) {
		Set<Goods> goods = new LinkedHashSet<>();
		String searchSql = "SELECT * FROM "
				+ "( SELECT ROWNUM ROW_NUM, G.* FROM BEVERAGE_GOODS G WHERE Upper(G.GOODS_NAME) LIKE ? AND G.STATUS='1' ORDER BY GOODS_ID )"
				+ "WHERE ROW_NUM >= ? AND ROW_NUM <= ?";
		try (Connection conn = DBConnectionFactory.getOracleDBConnection();
				PreparedStatement stmt = conn.prepareStatement(searchSql)){
			stmt.setString(1, "%"+searchKeyword.toUpperCase()+"%");
			stmt.setInt(2, startRowNo);
			stmt.setInt(3, endRowNo);
			try (ResultSet rs = stmt.executeQuery()){
				while(rs.next()) {
					BigDecimal goodsID = rs.getBigDecimal("GOODS_ID");
					String goodsName = rs.getString("GOODS_NAME");
					int price = rs.getInt("PRICE");
					int quantity = rs.getInt("QUANTITY");
					String imageName = rs.getString("IMAGE_NAME");
					String status = rs.getString("STATUS");
					Goods goodsTemp = new Goods();
					goodsTemp.setGoodsID(goodsID);
					goodsTemp.setGoodsName(goodsName);
					goodsTemp.setGoodsPrice(price);
					goodsTemp.setGoodsQuantity(quantity);
					goodsTemp.setGoodsImageName(imageName);
					goodsTemp.setStatus(status);
					goods.add(goodsTemp);
				}
			} catch (SQLException e) {
				throw e;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		return goods;
	}
	
	public Set<Goods> searchAllGoods(String searchKeyword) {
		Set<Goods> goods = new LinkedHashSet<>();
		String searchSql = "SELECT * FROM "
				+ "( SELECT ROWNUM ROW_NUM, G.* FROM BEVERAGE_GOODS G WHERE Upper(G.GOODS_NAME) LIKE ? AND G.STATUS='1' )";
		try (Connection conn = DBConnectionFactory.getOracleDBConnection();
				PreparedStatement stmt = conn.prepareStatement(searchSql)){
			stmt.setString(1, "%"+searchKeyword.toUpperCase()+"%");
			try (ResultSet rs = stmt.executeQuery()){
				while(rs.next()) {
					BigDecimal goodsID = rs.getBigDecimal("GOODS_ID");
					String goodsName = rs.getString("GOODS_NAME");
					int price = rs.getInt("PRICE");
					int quantity = rs.getInt("QUANTITY");
					String imageName = rs.getString("IMAGE_NAME");
					String status = rs.getString("STATUS");
					Goods goodsTemp = new Goods();
					goodsTemp.setGoodsID(goodsID);
					goodsTemp.setGoodsName(goodsName);
					goodsTemp.setGoodsPrice(price);
					goodsTemp.setGoodsQuantity(quantity);
					goodsTemp.setGoodsImageName(imageName);
					goodsTemp.setStatus(status);
					goods.add(goodsTemp);
				}
			} catch (SQLException e) {
				throw e;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		return goods;
	}
	
	
	
	public Map<BigDecimal, Goods> queryBuyGoods(Set<BigDecimal> goodsIDs){
		// key:商品編號、value:商品
		Map<BigDecimal, Goods> goods = new LinkedHashMap<>();
		String queryGoods = "SELECT * FROM BEVERAGE_GOODS WHERE GOODS_ID = ?";
		try(Connection conn = DBConnectionFactory.getOracleDBConnection();
				PreparedStatement stmt = conn.prepareStatement(queryGoods)) {
			for(int i=0;i<goodsIDs.size();i++) {
				stmt.setBigDecimal(1, (BigDecimal) goodsIDs.toArray()[i]);
				try (ResultSet rs = stmt.executeQuery()){
					while(rs.next()) {
						BigDecimal goodsID = rs.getBigDecimal("GOODS_ID");
						String goodsName = rs.getString("GOODS_NAME");
						int price = rs.getInt("PRICE");
						int quantity = rs.getInt("QUANTITY");
						Goods goodsTemp = new Goods();
						goodsTemp.setGoodsID(goodsID);
						goodsTemp.setGoodsName(goodsName);
						goodsTemp.setGoodsPrice(price);
						goodsTemp.setGoodsQuantity(quantity);
						goods.put(goodsTemp.getGoodsID(), goodsTemp);
					}
				} catch (SQLException e) {
					throw e;
				}
			}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		return goods;
	}	
	
	
	
	public boolean batchUpdateGoodsQuantity(Set<Goods> goods){
		boolean updateSuccess = false;
		String GoodsOrderUpdateSql = "UPDATE BEVERAGE_GOODS SET QUANTITY = ? WHERE GOODS_ID = ?";
		try(Connection conn = DBConnectionFactory.getOracleDBConnection();
				PreparedStatement stmt = conn.prepareStatement(GoodsOrderUpdateSql)) {	
			Iterator<Goods> it = goods.iterator();
			while(it.hasNext()) {
				Goods itT = it.next();
				stmt.setInt(1, itT.getGoodsQuantity());
				stmt.setBigDecimal(2, itT.getGoodsID());
				stmt.addBatch();
			}
			int[] stA = stmt.executeBatch();
			// 異動判定 oracle成功回傳-2不會回傳更新比數，錯誤回傳-3 
			// 參考資料
			// https://docs.oracle.com/database/121/JJDBC/oraperf.htm#JJDBC28773
			if(Arrays.stream(stA).allMatch(s -> s == -2)) {
				updateSuccess = true;				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return updateSuccess;
	}
	
	public boolean batchCreateGoodsOrder(String customerID, Map<Goods,Integer> goodsOrders){
		boolean insertSuccess = false;
		String GoodsOrderSql = "INSERT INTO BEVERAGE_ORDER (ORDER_ID, ORDER_DATE, CUSTOMER_ID, GOODS_ID, GOODS_BUY_PRICE, BUY_QUANTITY) "
				+ "values(BEVERAGE_ORDER_SEQ.NEXTVAL, ?, ?, ?, ?, ?)";		
		try(Connection conn = DBConnectionFactory.getOracleDBConnection();
				PreparedStatement stmt = conn.prepareStatement(GoodsOrderSql)) {		
			for(Goods goodsOrder : goodsOrders.keySet()) {
				stmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
				stmt.setString(2, customerID);
				stmt.setBigDecimal(3, goodsOrder.getGoodsID());
				stmt.setInt(4, goodsOrder.getGoodsPrice());
				stmt.setInt(5, goodsOrders.get(goodsOrder));
				stmt.addBatch();
			}
			int[] stA =	stmt.executeBatch();
			// 異動判定 oracle成功回傳-2不會回傳更新比數，錯誤回傳-3 
			if(Arrays.stream(stA).allMatch(s -> s == -2)) {
				insertSuccess = true;				
			}
			
		} catch (SQLException e) {
			insertSuccess = false;
			e.printStackTrace();
		}
		return insertSuccess;
	}	

	
	
}
