package com.nju.warehouse.dao;

import java.util.ArrayList;
import java.util.logging.Level;

import com.nju.warehouse.model.AccountBill;
import com.nju.warehouse.model.Customer;
import com.nju.warehouse.result.AccountType;
import com.nju.warehouse.result.AddCustomerResult;
import com.nju.warehouse.result.ModifyResult;
import com.nju.warehouse.util.FileUtil;
import com.nju.warehouse.util.LogUtil;

public class CustomerDao {
	public static final String CUSTOMERPATH = "data/customer.ser";
	
	private ArrayList<Customer> customers = null;
	
	public CustomerDao() {
	}
	
	public AddCustomerResult add(Customer customer) {
		LogUtil.getInstance().log(Level.INFO, "CustomerDao--add is called");
		
		
		findAll();
		
		AddCustomerResult result = AddCustomerResult.添加成功;
		
		boolean isExist = false;
		if(customers != null) {
			for(Customer c : customers) {
				isExist = (c.getName().contains(customer.getName()));
				break;
			}
		}
		
		
		if(isExist) {
			result = AddCustomerResult.添加失败该用户已经存在;
		} else {
			customers.add(customer);
			FileUtil.saveToFile(CUSTOMERPATH, customers);
		}
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Customer> findAll() {
		LogUtil.getInstance().log(Level.INFO, "CustomerDao---findAll is called");
		

		customers = (ArrayList<Customer>)(FileUtil.readFromFile(CUSTOMERPATH));

		
		return customers;
	}
	
	public ArrayList<Customer> find(String param) {
		LogUtil.getInstance().log(Level.INFO, "CustomerDao---find is called");
		
		findAll();
		
		ArrayList<Customer> list = new ArrayList<Customer>();

		if(param.length() == 0) {
			list = customers;
		} else {
			if(customers != null) {
				for(Customer c : customers) {
					if(c.getName().equals(param)) {
						list.add(c);
					}
				}
			}
		}
		
		return list;
	}
	
	public void delete(String name) {
		LogUtil.getInstance().log(Level.INFO, "CustomerDao---delete is called");
		
		findAll();
		
		if(customers != null) {
			for(int i=0; i<customers.size(); i++) {
				if(customers.get(i).getName().equals(name)) {
					customers.remove(i);
					FileUtil.saveToFile(CUSTOMERPATH, customers);
					break;
				}
			}
		}
		
		
	}
	
	public ModifyResult update(Customer customer) {
		LogUtil.getInstance().log(Level.INFO, "CustomerDao---update is called");
		
		ModifyResult result = ModifyResult.修改失败请输入数字类型;
		
		findAll();
		
		if(customers != null) {
			for(int i=0; i<customers.size(); i++) {
				if(customers.get(i).getName().equals(customer.getName())) {
					customers.get(i).setPhone(customer.getPhone());
					customers.get(i).setPayAccount(customer.getPayAccount());
					customers.get(i).setReceiveAccount(customer.getReceiveAccount());
					customers.get(i).setTotalAccount(customer.getTotalAccount());
					
					FileUtil.saveToFile(CUSTOMERPATH, customers);
					
					result = ModifyResult.修改成功;
					
					break;
				}
			}
		}
		
		return result;
	}
	
	public void updateByAccountBill(AccountBill accountBill) {
		LogUtil.getInstance().log(Level.INFO, "CustomerDao---updateByAccountBill is called");
	
		findAll();
		
		if(customers != null) {
			for(Customer c : customers) {
				if(c.getName().equals(accountBill.getName())) {
					if(accountBill.getType() == AccountType.IN) {
						c.setPayAccount(c.getPayAccount() - accountBill.getMoney());
					} else {
						c.setReceiveAccount(c.getReceiveAccount() - accountBill.getMoney());
					}
					
					FileUtil.saveToFile(CUSTOMERPATH, customers);
					break;
				}
			}
		}
	}
/*
	public static void main(String args[]) {
		ArrayList<Customer> list = new ArrayList<Customer>();
		list.add(new Customer("never", "15805197544", 0,0,0));
		
		FileUtil.saveToFile(CUSTOMERPATH, list);
	}
	*/	
}
