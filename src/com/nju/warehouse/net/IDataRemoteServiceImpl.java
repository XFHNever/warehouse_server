package com.nju.warehouse.net;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class IDataRemoteServiceImpl extends UnicastRemoteObject implements IDataRemoteService{

	private static final long serialVersionUID = 1L;

	protected IDataRemoteServiceImpl() throws RemoteException {
		super();
	}

	@Override
	public String hello(String name) throws RemoteException {
		return "Hello " + name + ",RMI success";
	}
	
}
