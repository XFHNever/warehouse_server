package com.nju.warehouse.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class IPUtil {
	public static final String IPPATH = "data/ip.txt";
	
	public static String getIp() {
		String ip = null;
		try {
			
			ip = InetAddress.getLocalHost().toString();
			FileUtil.saveToFile(IPPATH, ip);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		return ip;
	}
	
}
