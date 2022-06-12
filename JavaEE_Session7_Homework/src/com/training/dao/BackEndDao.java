package com.training.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.training.model.Goods;
import com.training.model.SalesReport;

public class BackEndDao {
	
	private static BackEndDao backEndDao = new BackEndDao();
	private BackEndDao() {	}
	public static BackEndDao getInstance() {
		return backEndDao;
	}
	
	public List<Goods> queryAllGoods(){
		List<Goods> goods = new ArrayList<Goods>();
		String queryAllGoodsSQL = "SELECT * FROM BEVERAGE_GOODS ORDER BY GOODS_ID";
		try (Connection conn = DBConnectionFactory.getOracleDBConnection();
			PreparedStatement stmt = conn.prepareStatement(queryAllGoodsSQL);
			ResultSet rs = 	stmt.executeQuery()){
			while(rs.next()) {
				Goods goodTemp = new Goods();
				goodTemp.setGoodsID(rs.getBigDecimal("GOODS_ID"));
				goodTemp.setGoodsName(rs.getString("GOODS_NAME"));
				goodTemp.setGoodsPrice(rs.getInt("PRICE"));
				goodTemp.setGoodsQuantity(rs.getInt("QUANTITY"));
				goodTemp.setGoodsImageName(rs.getString("IMAGE_NAME"));
				goodTemp.setStatus(rs.getString("STATUS"));
				goods.add(goodTemp);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return goods;
	}
	
	public List<Goods> queryGoodsBetween(int startRowNo, int endRowNo){
		List<Goods> goods = new ArrayList<Goods>();
		String queryGoodsBetweenSQL = "SELECT * FROM"
				+ "( SELECT ROWNUM ROW_NUM, G.* FROM BEVERAGE_GOODS G ORDER BY GOODS_ID)"
				+ "WHERE ROW_NUM >= ? AND ROW_NUM <= ?";
		try (Connection conn = DBConnectionFactory.getOracleDBConnection();
			PreparedStatement stmt = conn.prepareStatement(queryGoodsBetweenSQL)){
			stmt.setInt(1, startRowNo);
			stmt.setInt(2, endRowNo);		
			try(ResultSet rs = 	stmt.executeQuery()){
				while(rs.next()) {
					Goods goodTemp = new Goods();
					goodTemp.setGoodsID(rs.getBigDecimal("GOODS_ID"));
					goodTemp.setGoodsName(rs.getString("GOODS_NAME"));
					goodTemp.setGoodsPrice(rs.getInt("PRICE"));
					goodTemp.setGoodsQuantity(rs.getInt("QUANTITY"));
					goodTemp.setGoodsImageName(rs.getString("IMAGE_NAME"));
					goodTemp.setStatus(rs.getString("STATUS"));
					goods.add(goodTemp);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return goods;
	}
	
	
	public Goods queryGoodsById(String id){
		Goods good = null;
		String querySQL = "SELECT GOODS_ID, GOODS_NAME, PRICE, QUANTITY, STATUS FROM BEVERAGE_GOODS WHERE GOODS_ID = ?";
		try (Connection conn = DBConnectionFactory.getOracleDBConnection();
				PreparedStatement stmt = conn.prepareStatement(querySQL);){
			stmt.setString(1, id);
			try(ResultSet rs = 	stmt.executeQuery()){
				while(rs.next()) {
					good = new Goods();
					good.setGoodsID(rs.getBigDecimal("GOODS_ID"));
					good.setGoodsName(rs.getString("GOODS_NAME"));
					good.setGoodsPrice(rs.getInt("PRICE"));
					good.setGoodsQuantity(rs.getInt("QUANTITY"));
					good.setStatus(rs.getString("STATUS"));
				}				
			}
			
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return good;
	}
	
	public Goods queryGood(Goods goods) {
		Goods good = null;
		String querySQL = "SELECT GOODS_ID, GOODS_NAME, PRICE, QUANTITY, STATUS FROM BEVERAGE_GOODS WHERE GOODS_NAME = ?";
		try (Connection conn = DBConnectionFactory.getOracleDBConnection();
				PreparedStatement stmt = conn.prepareStatement(querySQL);){
			stmt.setString(1, goods.getGoodsName());
			try(ResultSet rs = 	stmt.executeQuery()){
				while(rs.next()) {
					good = new Goods();
					good.setGoodsID(rs.getBigDecimal("GOODS_ID"));
					good.setGoodsName(rs.getString("GOODS_NAME"));
					good.setGoodsPrice(rs.getInt("PRICE"));
					good.setGoodsQuantity(rs.getInt("QUANTITY"));
					good.setStatus(rs.getString("STATUS"));
				}				
			}
			
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return good;
	}	
	
	
	public boolean updateGoods(Goods goods) {
		boolean updateSuccess = false;
		try (Connection conn = DBConnectionFactory.getOracleDBConnection()){
			conn.setAutoCommit(false);
			
			String updateSql = "UPDATE BEVERAGE_GOODS SET PRICE = ?, QUANTITY = QUANTITY + ?, STATUS = ?"
					+ "WHERE GOODS_ID = ? ";
			try (PreparedStatement stmt = conn.prepareStatement(updateSql)){
				int count = 1;
				stmt.setInt(count++, goods.getGoodsPrice());
				stmt.setInt(count++, goods.getGoodsQuantity());
				stmt.setString(count++, goods.getStatus());
				stmt.setBigDecimal(count++, goods.getGoodsID());
				int st = stmt.executeUpdate();
				// int 比數變化才更新成功 if
				updateSuccess = ( st > 0 ) ? true : false ;					
				conn.commit();
			} catch (SQLException e) {
				conn.rollback();
				throw e;
			}
		} catch (SQLException e) {
			updateSuccess = false;
			e.printStackTrace();
		}	
		return updateSuccess;
	}
	
	public boolean createGoods(Goods goods){
		boolean createSuccess = false;
		int goodsID = 0;
		String insertSQL = "INSERT INTO BEVERAGE_GOODS (GOODS_ID, GOODS_NAME, PRICE, QUANTITY, IMAGE_NAME, STATUS) "
				+ "values(BEVERAGE_GOODS_SEQ.NEXTVAL,?,?,?,?,?)";
		String cols[] = { "GOODS_ID" };
		try (Connection conn = DBConnectionFactory.getOracleDBConnection()){
			conn.setAutoCommit(false);
			int count = 1;
			try (PreparedStatement stmt = conn.prepareStatement(insertSQL, cols)){
				stmt.setString(count++, goods.getGoodsName());
				stmt.setInt(count++, goods.getGoodsPrice());
				stmt.setDouble(count++, goods.getGoodsQuantity());
				stmt.setString(count++, goods.getGoodsImageName());
				stmt.setString(count++, goods.getStatus());
				int recordCount = stmt.executeUpdate();
				createSuccess = (recordCount > 0) ? true : false;
				conn.commit();	
				
				// 取對應的自增主鍵值
				ResultSet rsKeys = stmt.getGeneratedKeys();
				if(rsKeys.next()) {
					goodsID = rsKeys.getInt(1);
				}
			} catch (SQLException e) {
				conn.rollback();
				throw e;
			}
		} catch (SQLException e) {
			createSuccess = false;
			e.printStackTrace();
		}
		return createSuccess;
	}
	
	
	public boolean deleteGoods(String goodsId) {
		boolean deleteSuccess = false;
		try (Connection conn = DBConnectionFactory.getOracleDBConnection()){
			conn.setAutoCommit(false);
			String deleteSql = "DELETE FROM BEVERAGE_GOODS WHERE GOODS_ID = ?";
			try (PreparedStatement stmt = conn.prepareStatement(deleteSql)){
				stmt.setString(1, goodsId);
				int st = stmt.executeUpdate();
				// 受異動筆數
				deleteSuccess = (st > 0) ? true : false;
				conn.commit();
			}catch (SQLException e) {
				conn.rollback();
				throw e;
			}
		} catch (SQLException e) {
			deleteSuccess = false;
			e.printStackTrace();
		}
		return deleteSuccess;
	}
	
	public Set<SalesReport> queryOrderBetweenDate(String queryStartDate, String queryEndDate) {
		Set<SalesReport> reports = new LinkedHashSet<>();
		String queryOrderGoodsSQL = 
				" SELECT O.ORDER_ID, M.CUSTOMER_NAME, O.ORDER_DATE, G.GOODS_NAME, O.GOODS_BUY_PRICE, O.BUY_QUANTITY "
				+ " FROM BEVERAGE_ORDER O "
				+ " LEFT JOIN BEVERAGE_MEMBER M ON O.CUSTOMER_ID = M.IDENTIFICATION_NO "
				+ " LEFT JOIN BEVERAGE_GOODS G ON O.GOODS_ID = G.GOODS_ID "
				+ " WHERE O.ORDER_DATE BETWEEN ? AND ? "
				+ " ORDER BY O.ORDER_ID ";
		try(Connection conn = DBConnectionFactory.getOracleDBConnection();
				PreparedStatement stmt = conn.prepareStatement(queryOrderGoodsSQL)) {
			// String 轉 Date
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date StartDate = sdf.parse(queryStartDate);
			Date EndDate = sdf.parse(queryEndDate+1);
			// java.util.date 轉 java.sql.Date
			java.sql.Date StartDateSql = new java.sql.Date(StartDate.getTime());
			java.sql.Date EndDateSql = new java.sql.Date(EndDate.getTime()-1);
			stmt.setDate(1, StartDateSql);
			stmt.setDate(2, EndDateSql);
			try(ResultSet rs = stmt.executeQuery()){
				while(rs.next()) {
					long orderID = rs.getLong("ORDER_ID");
					String customerName = rs.getString("CUSTOMER_NAME");
					String orderDate = rs.getString("ORDER_DATE");
					String goodsName = rs.getString("GOODS_NAME");
					int goodsBuyPrice = rs.getInt("GOODS_BUY_PRICE");
					int buyQuantity	= rs.getInt("BUY_QUANTITY");
					int buyAmount = goodsBuyPrice*buyQuantity;
					SalesReport salesReport = new SalesReport();
					salesReport.setOrderID(orderID);
					salesReport.setCustomerName(customerName);
					salesReport.setOrderDate(orderDate);
					salesReport.setGoodsName(goodsName);
					salesReport.setGoodsBuyPrice(goodsBuyPrice);
					salesReport.setBuyQuantity(buyQuantity);
					salesReport.setBuyAmount(buyAmount);
					reports.add(salesReport);
				}
			} catch (SQLException e) {
				throw e;
			}
		} catch (SQLException | ParseException  e) {
			e.printStackTrace();
		}
		return reports;
	}
	
}
