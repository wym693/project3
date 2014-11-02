package demo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Data implements Serializable{
	
	private boolean isLogin;
	private String  operation;
	private User user;
	private String msg;
	
	private boolean success;
	
	
	
	
	
	public boolean isSuccess() {
		return success;
	}


	public void setSuccess(boolean success) {
		this.success = success;
	}
	private List list=new ArrayList();
	
	
	public List getList() {
		return list;
	}


	public void setList(List list) {
		this.list = list;
	}


	public Data() {
	}
	
	
	public Data(boolean isLogin, String operation, User user, String msg) {
		this.isLogin = isLogin;
		this.operation = operation;
		this.user = user;
		this.msg = msg;
	}
	
	public Data(boolean isLogin, String operation, User user) {
		this.isLogin = isLogin;
		this.operation = operation;
		this.user = user;
	}
	
	public Data(boolean isLogin, String operation) {
		this.isLogin = isLogin;
		this.operation = operation;
	}




	public boolean isLogin() {
		return isLogin;
	}
	public void setLogin(boolean isLogin) {
		this.isLogin = isLogin;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	
	

}
