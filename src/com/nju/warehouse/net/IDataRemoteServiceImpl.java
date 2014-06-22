package com.nju.warehouse.net;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;

import com.nju.warehouse.dao.AccountBillDao;
import com.nju.warehouse.dao.AccountDao;
import com.nju.warehouse.dao.CommodityBillDao;
import com.nju.warehouse.dao.CommodityDao;
import com.nju.warehouse.dao.CustomerDao;
import com.nju.warehouse.dao.SalesBillDao;
import com.nju.warehouse.dao.StorageDao;
import com.nju.warehouse.dao.UserDao;
import com.nju.warehouse.model.Account;
import com.nju.warehouse.model.AccountBill;
import com.nju.warehouse.model.Commodity;
import com.nju.warehouse.model.CommodityBill;
import com.nju.warehouse.model.Customer;
import com.nju.warehouse.model.SalesBill;
import com.nju.warehouse.model.Storage;
import com.nju.warehouse.model.User;
import com.nju.warehouse.result.AddCustomerResult;
import com.nju.warehouse.result.AddResult;
import com.nju.warehouse.result.DeleteResult;
import com.nju.warehouse.result.ModifyResult;
import com.nju.warehouse.util.LogUtil;

public class IDataRemoteServiceImpl extends UnicastRemoteObject implements IDataRemoteService{

	private static final long serialVersionUID = 1L;
	
	private UserDao userDao = null;
	private CommodityDao commodityDao = null;
	private CustomerDao customerDao = null;
	private StorageDao storageDao = null;
	private CommodityBillDao commodityBillDao = null;
	private SalesBillDao salesBillDao = null;
	private AccountBillDao accountBillDao = null;
	private AccountDao accountDao = null;

	protected IDataRemoteServiceImpl() throws RemoteException {
		super();
		userDao = new UserDao();
		commodityDao = new CommodityDao();
		customerDao = new CustomerDao();
		storageDao = new StorageDao();
		commodityBillDao = new CommodityBillDao();
		salesBillDao = new SalesBillDao();
		accountBillDao = new AccountBillDao();
		accountDao = new AccountDao();
	}

	@Override
	public String hello(String name) throws RemoteException {
		return "Hello " + name + ",RMI success";
	}

	@Override
	public User login(String name, String password, String type)
			throws RemoteException {
		LogUtil.getInstance().log(Level.INFO, "name:" + name + ", password:" + password + ", type:" + type);
		return userDao.login(name, password, type);
	}

	@Override
	public ArrayList<Commodity> getAllCommodity() throws RemoteException {
		LogUtil.getInstance().log(Level.INFO, "IDataRemoteServiceImpl--getAllCommodity is called");
		
		return commodityDao.findAll();
	}
	
	@Override
	public ArrayList<Commodity> getCommodityByParam(String param)
			throws RemoteException {
		return commodityDao.find(param);
	}
	

	@Override
	public AddResult addNewCommodity(Commodity commodity)
			throws RemoteException {
		LogUtil.getInstance().log(Level.INFO, "IDataRemoteServiceImpl--addNewCommodity is called");
		
		return commodityDao.add(commodity);
	}

	@Override
	public void deleteCommodity(String name, String type)
			throws RemoteException {
		commodityDao.delete(name, type);
	}

	@Override
	public ModifyResult modifyCommodity(Commodity commodity)
			throws RemoteException {
		return commodityDao.update(commodity);
	}

	@Override
	public ArrayList<Customer> getAllCustomer() throws RemoteException {
		return customerDao.findAll();
	}

	@Override
	public ArrayList<Customer> getCustomerByParam(String param)
			throws RemoteException {
		return customerDao.find(param);
	}

	@Override
	public AddCustomerResult addNewCustomer(Customer customer)
			throws RemoteException {
		return customerDao.add(customer);
	}

	@Override
	public void deleteCustomer(String name) throws RemoteException {
		customerDao.delete(name);
	}

	@Override
	public ModifyResult modifyCustomer(Customer customer)
			throws RemoteException {
		return customerDao.update(customer);
	}

	@Override
	public ArrayList<Storage> getAllStorage() throws RemoteException {
		return storageDao.findAll();
	}

	@Override
	public ArrayList<CommodityBill> getAllCommofityBill()
			throws RemoteException {
		return commodityBillDao.findAll();
	}

	@Override
	public ArrayList<CommodityBill> getCommodityBillsByDate(Date date)
			throws RemoteException {
		return commodityBillDao.find(date);
	}

	@Override
	public AddResult addNewCommodityBill(CommodityBill commodityBill)
			throws RemoteException {
		return commodityBillDao.add(commodityBill);
	}

	@Override
	public DeleteResult deleteCommodityBill(CommodityBill commodityBill)
			throws RemoteException {
		return commodityBillDao.delete(commodityBill);
	}

	@Override
	public ArrayList<SalesBill> getAllSalesBill() throws RemoteException {
		return salesBillDao.findAll();
	}

	@Override
	public ArrayList<SalesBill> getSalesBillsByDate(Date date)
			throws RemoteException {
		return salesBillDao.find(date);
	}

	@Override
	public AddResult addNewSalesBill(SalesBill salesBill)
			throws RemoteException {
		return salesBillDao.add(salesBill);
	}

	@Override
	public DeleteResult deleteSalesBill(SalesBill salesBill)
			throws RemoteException {
		return salesBillDao.delete(salesBill);
	}

	@Override
	public ArrayList<AccountBill> getAllAccountBill() throws RemoteException {
		return accountBillDao.findAll();
	}

	@Override
	public AddResult addNewAccountBill(AccountBill accountBill)
			throws RemoteException {
		return accountBillDao.add(accountBill);
	}

	@Override
	public Account getAccount() throws RemoteException {
		return accountDao.find();
	}

	@Override
	public void initAccount() throws RemoteException {
		accountDao.initAccount();
	}




}
