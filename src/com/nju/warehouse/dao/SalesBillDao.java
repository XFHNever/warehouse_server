package com.nju.warehouse.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;

import com.nju.warehouse.model.Commodity;
import com.nju.warehouse.model.CommodityBill;
import com.nju.warehouse.model.Customer;
import com.nju.warehouse.model.SalesBill;
import com.nju.warehouse.result.AddResult;
import com.nju.warehouse.result.DeleteResult;
import com.nju.warehouse.result.SalesBillType;
import com.nju.warehouse.util.FileUtil;
import com.nju.warehouse.util.LogUtil;

public class SalesBillDao {
	public static final String SALESBILLPATH = "data/salesbill.ser";
	
	private ArrayList<SalesBill> salesBills = null;
	
	public SalesBillDao() {
	}
	
	public AddResult add(SalesBill salesBill) {
		LogUtil.getInstance().log(Level.INFO, "SalesBillDao--add is called");
		
		
		findAll();
		
		AddResult result = AddResult.添加成功;
		
		CommodityDao commodityDao = new CommodityDao();
		Commodity commodity = commodityDao.find(salesBill.getCommodityName(), salesBill.getCommodityType());
		
		if(commodity != null) {
				commodity.setQuantity(commodity.getQuantity() - salesBill.getAmount());
				commodity.setLastSellingPrice(salesBill.getPrice());
				commodityDao.update(commodity);

				StorageDao storageDao = new StorageDao();
				storageDao.updateBySalesBill(salesBill);
				
				CustomerDao customerDao = new CustomerDao();
				Customer customer = customerDao.find(salesBill.getCustomer()).get(0);
				
				customer.setPayAccount(customer.getPayAccount() + salesBill.getTotal());
				customer.setTotalAccount(customer.getReceiveAccount() - customer.getPayAccount());
				
				customerDao.update(customer);
				
				salesBills.add(salesBill);
				FileUtil.saveToFile(SALESBILLPATH, salesBills);
			
		}
		
	
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<SalesBill> findAll() {
		LogUtil.getInstance().log(Level.INFO, "SalesBillDao---findAll is called");
		

		salesBills = (ArrayList<SalesBill>)(FileUtil.readFromFile(SALESBILLPATH));

		
		return salesBills;
	}
	
	public ArrayList<SalesBill> find(Date date) {
		LogUtil.getInstance().log(Level.INFO, "SalesBillDao---find is called");
		
		findAll();
		
		ArrayList<SalesBill> list = new ArrayList<SalesBill>();

		if(date == null) {
			list = salesBills;
		} else {
			if(salesBills != null) {
				for(SalesBill s : salesBills) {
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					if(format.format(s.getDate()).equals(format.format(date))) {
						list.add(s);
					}
				}
			}
		}
		
		return list;
	}
	
	public DeleteResult delete(SalesBill salesBill) {
		LogUtil.getInstance().log(Level.INFO, "SalesBillDao---delete is called");
		
		DeleteResult result = DeleteResult.退货成功;
		
		CommodityDao commodityDao = new CommodityDao();
		Commodity commodity = commodityDao.find(salesBill.getCommodityName(), salesBill.getCommodityType());
		
		if(commodity != null) {
			if(commodity.getLastSellingPrice() != salesBill.getPrice()){
				result = DeleteResult.已经再次销售过不能退货;
			} else {
				commodity.setQuantity(commodity.getQuantity() - salesBill.getAmount());
				commodity.setLastPurchasePrice(salesBill.getPrice());
				commodityDao.update(commodity);

				StorageDao storageDao = new StorageDao();
				storageDao.updateBySalesBill(salesBill);
				
				CustomerDao customerDao = new CustomerDao();
				
				Customer customer = null;
				ArrayList<Customer> customers = customerDao.find(salesBill.getCustomer());
				if(customers != null && customers.size() > 0) {
					customer = customers.get(0);
					customer.setPayAccount(customer.getPayAccount() - salesBill.getTotal());
					customer.setTotalAccount(customer.getReceiveAccount() - customer.getPayAccount());
					
					customerDao.update(customer);
				}
				
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				for(SalesBill c : salesBills) {
				
					if(c.getCustomer().equals(salesBill.getCustomer()) && c.getAmount() == salesBill.getAmount()
							&& c.getCommodityName().equals(salesBill.getCommodityName())
							&& c.getCommodityType().equals(salesBill.getCommodityType())
							&& format.format(c.getDate()).equals(format.format(salesBill.getDate()))
							&& c.getPrice() == salesBill.getPrice()) {
						c.setDelete(true);
					}
				}
				
				
				salesBills.add(salesBill);
				FileUtil.saveToFile(SALESBILLPATH, salesBills);
			}
		}

		return result;
	}
/*
	public static void main(String args[]) throws ParseException {
		ArrayList<SalesBill> list = new ArrayList<SalesBill>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date1 = sdf.parse("2014-06-06");
		Date date2 = sdf.parse("2014-06-18");
		list.add(new SalesBill(date1,SalesBillType.ADD,"never","computer","sony",1,3000.0,3000.0,false));
		list.add(new SalesBill(date2,SalesBillType.DEL,"never","computer","acer",1,3000.0,3000.0,false));
		
		FileUtil.saveToFile(SALESBILLPATH, list);
	}

*/		

}
