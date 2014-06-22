package com.nju.warehouse.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;

import com.nju.warehouse.model.Account;
import com.nju.warehouse.model.AccountBill;
import com.nju.warehouse.result.AccountType;
import com.nju.warehouse.result.AddResult;
import com.nju.warehouse.util.FileUtil;
import com.nju.warehouse.util.LogUtil;

public class AccountBillDao {
	public static final String ACCOUNTBILLPATH = "data/accountbill.ser";
	
	private ArrayList<AccountBill> accountBills = null;
	
	public AccountBillDao() {
	}
	
	public AddResult add(AccountBill accountBill) {
		LogUtil.getInstance().log(Level.INFO, "AccountBillDao--add is called");
		
		
		findAll();
		
		AddResult result = AddResult.添加成功;
		
		AccountDao accountDao = new AccountDao();
		Account account = accountDao.find();
		
		//更新总额
		if(account != null) {
			if(accountBill.getType() == AccountType.IN) {
				account.setTotal(account.getTotal() + accountBill.getMoney());
				account.setReceiveMoney(account.getReceiveMoney() - accountBill.getMoney());			
			} else {
				account.setTotal(account.getTotal() - accountBill.getMoney());
				account.setPayMoney(account.getReceiveMoney() - accountBill.getMoney());	
			}
			
			accountDao.update(account);
		}
		
		CustomerDao customerDao = new CustomerDao();
		customerDao.updateByAccountBill(accountBill);
		
		accountBills.add(accountBill);
		FileUtil.saveToFile(ACCOUNTBILLPATH, accountBills);
		
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<AccountBill> findAll() {
		LogUtil.getInstance().log(Level.INFO, "AccountBillDao---findAll is called");
		

		accountBills = (ArrayList<AccountBill>)(FileUtil.readFromFile(ACCOUNTBILLPATH));

		
		return accountBills;
	}
	

/*	
	public static void main(String args[]) throws ParseException {
		ArrayList<AccountBill> list = new ArrayList<AccountBill>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = sdf.parse("2014-06-06");
		list.add(new AccountBill(date, AccountType.IN,"never",100.0));
		
		FileUtil.saveToFile(ACCOUNTBILLPATH, list);
	}
*/	
}
