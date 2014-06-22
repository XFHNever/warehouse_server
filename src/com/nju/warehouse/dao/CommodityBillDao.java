package com.nju.warehouse.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;

import com.nju.warehouse.model.Commodity;
import com.nju.warehouse.model.CommodityBill;
import com.nju.warehouse.model.Customer;
import com.nju.warehouse.result.AddResult;
import com.nju.warehouse.result.CommodityBillType;
import com.nju.warehouse.result.DeleteResult;
import com.nju.warehouse.util.FileUtil;
import com.nju.warehouse.util.LogUtil;

public class CommodityBillDao {
	public static final String COMMODITYBILLPATH = "data/commoditybill.ser";
	
	private ArrayList<CommodityBill> commodityBills = null;
	
	public CommodityBillDao() {
	}
	
	public AddResult add(CommodityBill commodityBill) {
		LogUtil.getInstance().log(Level.INFO, "CommodityBillDao--add is called");
		
		
		findAll();
		
		AddResult result = AddResult.添加成功;
		
		CommodityDao commodityDao = new CommodityDao();
		Commodity commodity = commodityDao.find(commodityBill.getCommodityName(), commodityBill.getCommodityType());
		
		if(commodity != null) {
				commodity.setQuantity(commodity.getQuantity() + commodityBill.getAmount());
				commodity.setLastPurchasePrice(commodityBill.getPrice());
				commodityDao.update(commodity);

				StorageDao storageDao = new StorageDao();
				storageDao.updateByCommodityBill(commodityBill);
				
				CustomerDao customerDao = new CustomerDao();
				Customer customer = customerDao.find(commodityBill.getCustomer()).get(0);
				
				customer.setReceiveAccount(customer.getReceiveAccount() + commodityBill.getTotal());
				customer.setTotalAccount(customer.getReceiveAccount() - customer.getPayAccount());
				
				customerDao.update(customer);
				
				commodityBills.add(commodityBill);
				FileUtil.saveToFile(COMMODITYBILLPATH, commodityBills);
			
		}
		
	
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<CommodityBill> findAll() {
		LogUtil.getInstance().log(Level.INFO, "CommodityBillDao---findAll is called");
		

		commodityBills = (ArrayList<CommodityBill>)(FileUtil.readFromFile(COMMODITYBILLPATH));

		
		return commodityBills;
	}
	
	public ArrayList<CommodityBill> find(Date date) {
		LogUtil.getInstance().log(Level.INFO, "CommodityBillDao---find is called");
		
		findAll();
		
		ArrayList<CommodityBill> list = new ArrayList<CommodityBill>();

		if(date == null) {
			list = commodityBills;
		} else {
			if(commodityBills != null) {
				for(CommodityBill c : commodityBills) {
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					if(format.format(c.getDate()).equals(format.format(date))) {
						list.add(c);
					}
				}
			}
		}
		
		return list;
	}
	
	public DeleteResult delete(CommodityBill commodityBill) {
		DeleteResult result = DeleteResult.退货成功;
		
		CommodityDao commodityDao = new CommodityDao();
		Commodity commodity = commodityDao.find(commodityBill.getCommodityName(), commodityBill.getCommodityType());
		
		if(commodity != null) {
			if(commodity.getLastSellingPrice() != 0) {
				result = DeleteResult.已经销售过不能退货;
			} else if(commodity.getLastPurchasePrice() != commodityBill.getPrice()){
				result = DeleteResult.已经再次进过货不能退货;
			} else {
				commodity.setQuantity(commodity.getQuantity() - commodityBill.getAmount());
				commodity.setLastPurchasePrice(commodityBill.getPrice());
				commodityDao.update(commodity);

				StorageDao storageDao = new StorageDao();
				storageDao.updateByCommodityBill(commodityBill);
				
				CustomerDao customerDao = new CustomerDao();
				
				Customer customer = null;
				ArrayList<Customer> customers = customerDao.find(commodityBill.getCustomer());
				if(customers != null && customers.size() > 0) {
					customer = customers.get(0);
					customer.setReceiveAccount(customer.getReceiveAccount() - commodityBill.getTotal());
					customer.setTotalAccount(customer.getReceiveAccount() - customer.getPayAccount());
					
					customerDao.update(customer);
				}
				
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				for(CommodityBill c : commodityBills) {
				
					if(c.getCustomer().equals(commodityBill.getCustomer()) && c.getAmount() == commodityBill.getAmount()
							&& c.getCommodityName().equals(commodityBill.getCommodityName())
							&& c.getCommodityType().equals(commodityBill.getCommodityType())
							&& format.format(c.getDate()).equals(format.format(commodityBill.getDate()))
							&& c.getPrice() == commodityBill.getPrice()) {
						c.setDelete(true);
					}
				}
				
				
				commodityBills.add(commodityBill);
				FileUtil.saveToFile(COMMODITYBILLPATH, commodityBills);
			}
		}

		return result;
	}
/*
	public static void main(String args[]) throws ParseException {
		ArrayList<CommodityBill> list = new ArrayList<CommodityBill>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date1 = sdf.parse("2014-06-06");
		Date date2 = sdf.parse("2014-06-18");
		list.add(new CommodityBill(date1,CommodityBillType.ADD,"never","computer","acer",1,3000.0,3000.0,false));
		list.add(new CommodityBill(date2,CommodityBillType.DEL,"never","computer","acer",1,3000.0,3000.0,false));
		
		FileUtil.saveToFile(COMMODITYBILLPATH, list);
	}
*/
		

}
