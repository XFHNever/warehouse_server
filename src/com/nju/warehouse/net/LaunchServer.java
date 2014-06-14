package com.nju.warehouse.net;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class LaunchServer {
	public static final String IP = "127.0.0.1";            //ip地址
	public static final int PORT = 9999;                    //端口号
	
	private static IDataRemoteService dataRemoteService = null;
	
	public static void main(String[] args) {
		LaunchServer server = new LaunchServer();
		server.createRemote();
	}
	
	public void createRemote(){
        try {
        	
        	getDataRemoteService();

            //本地主机上的远程对象注册表Registry的实例，并指定端口为8888，这一步必不可少（Java默认端口是1099），必不可缺的一步，缺少注册表创建，则无法绑定对象到远程注册表上
            LocateRegistry.createRegistry(PORT);

            //把远程对象注册到RMI注册服务器上，并命名为IDataRemoteService
            //绑定的URL标准格式为：rmi://host:port/name
            Naming.bind("rmi://" + IP + ":" + PORT + "/warehouse",dataRemoteService);

            System.out.println(">>>>>INFO:远程对象绑定成功！");
        } catch (RemoteException e) {
            System.out.println("创建远程对象发生异常！");
            e.printStackTrace();
        } catch (AlreadyBoundException e) {
            System.out.println("发生重复绑定对象异常！");
            e.printStackTrace();
        } catch (MalformedURLException e) {
            System.out.println("发生URL畸形异常！");
            e.printStackTrace();
        }
	}
	
	public IDataRemoteService getDataRemoteService() throws RemoteException {
		if(dataRemoteService == null) {
			dataRemoteService = new IDataRemoteServiceImpl();
		}
		
		return dataRemoteService;
	}
	
	public void close()  {

		try {
			Naming.unbind("rmi://" + IP + ":" + PORT + "/warehouse");
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
