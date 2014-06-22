package com.nju.warehouse.dao;

import java.util.ArrayList;
import java.util.logging.Level;

import com.nju.warehouse.model.Commodity;
import com.nju.warehouse.result.AddResult;
import com.nju.warehouse.result.ModifyResult;
import com.nju.warehouse.util.FileUtil;
import com.nju.warehouse.util.LogUtil;

public class CommodityDao {
	public static final String COMMODITYPATH = "data/commodity.ser";
	
	private ArrayList<Commodity> commodities = null;
	
	public CommodityDao() {
	}
	
	public AddResult add(Commodity commodity) {
		LogUtil.getInstance().log(Level.INFO, "CommodityDao--add is called");
		
		
		findAll();
		
		AddResult result = AddResult.添加成功;
		
		boolean isExist = false;
	
		if(commodities != null) {
			for(Commodity c : commodities) {
				isExist = (commodity.getName().equals(c.getName())) && (commodity.getType().equals(c.getType()));
				break;
			}
		}
		
		
			
		if(isExist) {
			result = AddResult.添加失败该商品已经存在;
		} else {
			StorageDao storageDao = new StorageDao();
			storageDao.add(commodity);
			
			commodities.add(commodity);
			FileUtil.saveToFile(COMMODITYPATH, commodities);
		}
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Commodity> findAll() {
		LogUtil.getInstance().log(Level.INFO, "CommodityDao---findAll is called");
		

		commodities = (ArrayList<Commodity>)(FileUtil.readFromFile(COMMODITYPATH));

		
		return commodities;
	}
	
	public Commodity find(String name, String type) {
		LogUtil.getInstance().log(Level.INFO, "CommodityDao---find is called");
		
		Commodity result = null;
		
		findAll();
		
		if(commodities != null) {
			for(int i=0; i<commodities.size(); i++) {
				System.out.println(commodities.get(i).getName() + " ---" + name + ";" + commodities.get(i).getType() + "---" + type);
				if(commodities.get(i).getName().equals(name) && commodities.get(i).getType().equals(type)) {
					result = commodities.get(i);
					break;
				}
			}
		}
		
		return result;
	}
	
	public ArrayList<Commodity> find(String param) {
		LogUtil.getInstance().log(Level.INFO, "CommodityDao---find is called");
		
		findAll();
		
		ArrayList<Commodity> list = new ArrayList<Commodity>();

		if(param.length() == 0) {
			list = commodities;
		} else {
			if(commodities != null) {
				for(Commodity c : commodities) {
					if(c.getName().equals(param) || c.getType().equals(param)) {
						list.add(c);
					}
				}
			}
		}
		
		return list;
	}
	
	public void delete(String name, String type) {
		LogUtil.getInstance().log(Level.INFO, "CommodityDao---delete is called");
		
		findAll();
		
		if(commodities != null) {
			for(int i=0; i<commodities.size(); i++) {
				if(commodities.get(i).getName().equals(name) && commodities.get(i).getType().equals(type)) {
					StorageDao storageDao = new StorageDao();
					storageDao.delete(name,type);
					
					commodities.remove(i);
					FileUtil.saveToFile(COMMODITYPATH, commodities);
					break;
				}
			}
		}
		
		
		
	}
	
	public ModifyResult update(Commodity commodity) {
		LogUtil.getInstance().log(Level.INFO, "CommodityDao---update is called");
		
		ModifyResult result = ModifyResult.修改失败请输入数字类型;
		
		findAll();
		
		if(commodities != null) {
			for(int i=0; i<commodities.size(); i++) {
				if(commodities.get(i).getName().equals(commodity.getName()) && 
						commodities.get(i).getType().equals(commodity.getType())) {
					
					commodities.get(i).setDefaultPurchasePrice(commodity.getDefaultPurchasePrice());
					commodities.get(i).setDefaultSellingPrice(commodity.getDefaultSellingPrice());
					commodities.get(i).setQuantity(commodity.getQuantity());
					commodities.get(i).setLastPurchasePrice(commodity.getLastPurchasePrice());
					commodities.get(i).setLastSellingPrice(commodity.getLastSellingPrice());
					
					FileUtil.saveToFile(COMMODITYPATH, commodities);
					
					result = ModifyResult.修改成功;
					
					break;
				}
			}
		}
		
		return result;
	}
/*		
	public static void main(String args[]) {
		ArrayList<Commodity> list = new ArrayList<Commodity>();
		list.add(new Commodity("computer", "sony", 0, 5000, 6000, 4900, 0));
		
		FileUtil.saveToFile(COMMODITYPATH, list);
	}
*/
}
