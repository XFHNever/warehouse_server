package com.nju.warehouse.dao;

import java.util.ArrayList;
import java.util.logging.Level;

import com.nju.warehouse.model.User;
import com.nju.warehouse.result.UserType;
import com.nju.warehouse.util.FileUtil;
import com.nju.warehouse.util.LogUtil;

public class UserDao {
	public static final String USERPATH = "data/user.ser";
	
	private ArrayList<User> users = new ArrayList<User>();

	
	@SuppressWarnings("unchecked")
	public User login(String name,String password,String type) {
		User user = null;
		
		users = (ArrayList<User>) FileUtil.readFromFile(USERPATH);
		
		LogUtil.getInstance().log(Level.INFO, "size" + users.size());
		
		if(users != null) {
			
			for(User u : users) {
				if(u.getName().equals(name) && u.getPassword().equals(password) && u.getType().toString().equals(type)) {
					user = new User(name, password, UserType.valueOf(type));
					break;
				}
			}
		}
		
		return user;
	}
/*
	public static void main(String arg[]) {
		ArrayList<User> users = new ArrayList<User>();
		users.add(new User("leo","leo",UserType.valueOf("库存管理员")));
		users.add(new User("1","1",UserType.valueOf("销售人员")));
		users.add(new User("2","2",UserType.valueOf("财务人员")));
		FileUtil.saveToFile(USERPATH, users);
		
		System.out.println("result:" + FileUtil.readFromFile(USERPATH));
	}
	*/	
}
