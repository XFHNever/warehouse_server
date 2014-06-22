package com.nju.warehouse.net;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import com.nju.warehouse.util.FileUtil;
import com.nju.warehouse.util.IPUtil;

public class LaunchServer {
	public static final String IP = "127.0.0.1";            //ip��ַ
	public static final int PORT = 9999;                    //�˿ں�
	
	private static IDataRemoteService dataRemoteService = null;
	
	public static void main(String[] args) {
		LaunchServer server = new LaunchServer();
		
		IPUtil.getIp();
		
		System.out.println(FileUtil.ReadIP("data/ip.txt"));
		server.createRemote();
	}
	
	public void createRemote(){
        try {
        	
        	getDataRemoteService();

            //���������ϵ�Զ�̶���ע���Registry��ʵ������ָ���˿�Ϊ8888����һ���ز����٣�JavaĬ�϶˿���1099�����ز���ȱ��һ����ȱ��ע����������޷��󶨶���Զ��ע�����
            LocateRegistry.createRegistry(PORT);

            //��Զ�̶���ע�ᵽRMIע��������ϣ�������ΪIDataRemoteService
            //�󶨵�URL��׼��ʽΪ��rmi://host:port/name
            Naming.bind("rmi://" + IP + ":" + PORT + "/warehouse",dataRemoteService);

            System.out.println(">>>>>INFO:Զ�̶���󶨳ɹ���");
        } catch (RemoteException e) {
            System.out.println("����Զ�̶������쳣��");
            e.printStackTrace();
        } catch (AlreadyBoundException e) {
            System.out.println("�����ظ��󶨶����쳣��");
            e.printStackTrace();
        } catch (MalformedURLException e) {
            System.out.println("����URL�����쳣��");
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
