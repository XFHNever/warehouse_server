package com.nju.warehouse.dao;

import java.util.ArrayList;
import java.util.logging.Level;

import com.nju.warehouse.model.Commodity;
import com.nju.warehouse.model.CommodityBill;
import com.nju.warehouse.model.SalesBill;
import com.nju.warehouse.model.Storage;
import com.nju.warehouse.result.CommodityBillType;
import com.nju.warehouse.result.SalesBillType;
import com.nju.warehouse.util.FileUtil;
import com.nju.warehouse.util.LogUtil;

public class StorageDao {
	public static final String STORAGEPATH = "data/storage.ser";
	
	private ArrayList<Storage> storages = null;
	
	public StorageDao() {
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Storage> findAll() {
		LogUtil.getInstance().log(Level.INFO, "StorageDao---findAll is called");
		

		storages = (ArrayList<Storage>)(FileUtil.readFromFile(STORAGEPATH));

		
		return storages;
	}
	
	public void add(Commodity commodity) {
		LogUtil.getInstance().log(Level.INFO, "StorageDao---add is called");
		
		findAll();
		
		Storage storage = new Storage(commodity.getName(), commodity.getType(), 
				commodity.getQuantity(), commodity.getDefaultPurchasePrice(),
				commodity.getQuantity()*commodity.getDefaultPurchasePrice(), 
				0, commodity.getDefaultSellingPrice(), 0.0, commodity.getQuantity(), commodity.getDefaultPurchasePrice(), 
				commodity.getQuantity()*commodity.getDefaultPurchasePrice());
		storages.add(storage);
		
		FileUtil.saveToFile(STORAGEPATH, storages);
	}
	
	public void delete(String name, String type) {
		LogUtil.getInstance().log(Level.INFO, "StorageDao---delete is called");
		
		findAll();
		
		int size = storages.size();
		
		for(int i=0; i<size; i++) {
			if(storages.get(i).getName().equals(name)
					&& storages.get(i).getType().equals(type)) {
				storages.remove(i);
				FileUtil.saveToFile(STORAGEPATH, storages);
				break;
			}
		}
		
	}
	
	public void updateByCommodityBill(CommodityBill commodityBill) {
		LogUtil.getInstance().log(Level.INFO, "StorageDao---updateByBill is called");
		
		findAll();
		
		if(storages != null) {
			for(Storage s : storages) {
				if(s.getName().equals(commodityBill.getCommodityName()) && 
						s.getType().equals(commodityBill.getCommodityType())) {
					
					int purchasesQuantity = s.getPurchasesQuantity();
					double averagePurchasesPrice = s.getAveragePurchasesPrice();
					double totalPurchasesPrice = s.getTotalPurchasesPrice();
					
					int storageQuantity = s.getStorageQuanttiy();
					double averageStoragePrice = s.getAverageStoragePrice();
					double totalStoragePrice = s.getTotalStoragePrice();
					
					if(commodityBill.getType() == CommodityBillType.ADD) {
						purchasesQuantity += commodityBill.getAmount();
						totalPurchasesPrice += commodityBill.getTotal();
						averagePurchasesPrice = totalPurchasesPrice/purchasesQuantity;
						
						totalStoragePrice += commodityBill.getTotal();
						storageQuantity += commodityBill.getAmount();
						averageStoragePrice = totalStoragePrice/storageQuantity;
					} else {
						purchasesQuantity -= commodityBill.getAmount();
						totalPurchasesPrice -= commodityBill.getTotal();
						averagePurchasesPrice = totalPurchasesPrice/purchasesQuantity;
						
						totalStoragePrice -= commodityBill.getTotal();
						storageQuantity -= commodityBill.getAmount();
						averageStoragePrice = totalStoragePrice/storageQuantity;
					}
					s.setPurchasesQuantity(purchasesQuantity);
					s.setAveragePurchasesPrice(averagePurchasesPrice);
					s.setTotalPurchasesPrice(totalPurchasesPrice);
					s.setStorageQuanttiy(storageQuantity);
					s.setAverageStoragePrice(averageStoragePrice);
					s.setTotalStoragePrice(totalStoragePrice);
				}
			}
			
			FileUtil.saveToFile(STORAGEPATH, storages);
		}
	}
	
	public void updateBySalesBill(SalesBill salesBill) {
		LogUtil.getInstance().log(Level.INFO, "StorageDao---updateByBill is called");
		
		findAll();
		
		if(storages != null) {
			for(Storage s : storages) {
				if(s.getName().equals(salesBill.getCommodityName()) && 
						s.getType().equals(salesBill.getCommodityType())) {
					
					int salesQuantity = s.getSaleQuantity();
					double averagesalePrice = s.getAverageSaleQuantity();
					double totalsalesPrice = s.getTatalSaleQuantity();
					
					int storageQuantity = s.getStorageQuanttiy();
					double averageStoragePrice = s.getAverageStoragePrice();
					double totalStoragePrice = s.getTotalStoragePrice();
					
					if(salesBill.getType() == SalesBillType.ADD) {
						salesQuantity += salesBill.getAmount();
						totalsalesPrice += salesBill.getTotal();
						averagesalePrice = totalsalesPrice/salesQuantity;
						
						totalStoragePrice -= salesBill.getTotal();
						storageQuantity -= salesBill.getAmount();
						averageStoragePrice = totalStoragePrice/storageQuantity;
					} else {
						salesQuantity -= salesBill.getAmount();
						totalsalesPrice -= salesBill.getTotal();
						averagesalePrice = totalsalesPrice/salesQuantity;
						
						totalStoragePrice += salesBill.getTotal();
						storageQuantity += salesBill.getAmount();
						averageStoragePrice = totalStoragePrice/storageQuantity;
					}
					s.setPurchasesQuantity(salesQuantity);
					s.setAveragePurchasesPrice(averagesalePrice);
					s.setTotalPurchasesPrice(totalsalesPrice);
					s.setStorageQuanttiy(storageQuantity);
					s.setAverageStoragePrice(averageStoragePrice);
					s.setTotalStoragePrice(totalStoragePrice);
				}
			}
			
			FileUtil.saveToFile(STORAGEPATH, storages);
		}
	}
	/*		
	public static void main(String args[]) {
		ArrayList<Storage> list = new ArrayList<Storage>();
		list.add(new Storage("computer", "acer", 100, 0.0, 0.0, 100, 0.0, 0.0, 0, 0.0, 0.0));
		list.add(new Storage("computer", "sony", 100, 0.0, 0.0, 100, 0.0, 0.0, 0, 0.0, 0.0));
		FileUtil.saveToFile(STORAGEPATH, list);
	}
	
	*/

}
