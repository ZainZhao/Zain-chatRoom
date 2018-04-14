package com.fh;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.java_websocket.WebSocket;


public class ChatServerPool {

	// 连接map,建立连接方法
	private static final Map<WebSocket,String> socketsMap = new HashMap<WebSocket,String>();
	
	// 获取用户socket
	public static String getUserByKey(WebSocket conn){
		return socketsMap.get(conn);
	}
	
	// 获取特定用户连接
	public static WebSocket getWebSocketByUser(String user){
		Set<WebSocket> keySet = socketsMap.keySet();// 获取键组
		synchronized (keySet) { // 设置为同步，因为HashMap不是同步的
			for (WebSocket conn : keySet) {
				String cuser = socketsMap.get(conn);// 获取值
				if(cuser.equals(user)){
					return conn;
				}
			}
		}
		return null;
	}
	
	// 增加用户
	public static void addUser(String user, WebSocket conn){
		socketsMap.put(conn,user);	//添加连接
	}
	
	// 获取在线用户名组
	public static Collection<String> getOnlineUser(){
		List<String> nameList = new ArrayList<String>();
		Collection<String> setUser = socketsMap.values();// 获取hashMap值组
		for(String name:setUser){
			nameList.add("<a onclick=\"toUserMsg('"+name+"');\">"+name+"</a>");
		}
		return nameList;
	}
	
	// 移除HashMap用户
	public static boolean removeUser(WebSocket conn){
		if(socketsMap.containsKey(conn)){
			socketsMap.remove(conn);	//移除连接
			return true;
		}else{
			return false;
		}
	}
	
	// 向特定用户发送消息
	public static void sendMessageToUser(WebSocket conn,String message){
		if(null != conn && null != socketsMap.get(conn)){
			conn.send(message);// 向改套接字发送文本数据
		}
	}
	
	// 广播信息
	public static void sendMessage(String message){
		Set<WebSocket> keySet = socketsMap.keySet();
		synchronized (keySet) {// 必须使用同步
			for (WebSocket conn : keySet) {
				String user = socketsMap.get(conn);
				if(user != null){
					conn.send(message);
				}
			}
		}
	}
}
