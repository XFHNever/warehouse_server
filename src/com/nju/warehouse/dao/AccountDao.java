package com.nju.warehouse.dao;


import com.nju.warehouse.model.Account;
import com.nju.warehouse.util.FileUtil;

public class AccountDao {
	public static final String ACCOUNTPATH = "data/account.ser";
	
	private Account account = null;
	
	public AccountDao() {
	}
	
	public Account find() {
		account = (Account) FileUtil.readFromFile(ACCOUNTPATH);
		return account;
	}
	
	public void update(Account account) {
		FileUtil.saveToFile(ACCOUNTPATH, account);
	}
	
	public void initAccount() {
		account.setTotal(10000.0);
		update(account);
	}
	
/*	
	public static void main(String args[]) {
		Account account = new Account(50000.0,2000.0,3000.0);
		
		FileUtil.saveToFile(ACCOUNTPATH, account);
	}
*/	
}

