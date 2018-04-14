package com.fh;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Date;
import org.java_websocket.WebSocket;
import org.java_websocket.WebSocketImpl;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import net.sf.json.JSONObject;



public class ChatServer extends WebSocketServer{

	public ChatServer(int port) throws UnknownHostException {
		super(new InetSocketAddress(port));
	}

	public ChatServer(InetSocketAddress address) {
		super(address);
	}

	
	public void onOpen( WebSocket socket, ClientHandshake handshake ) {
		
	}

	// 连接关闭
	@Override
	public void onClose( WebSocket conn, int code, String reason, boolean remote ) {
		leaveChatRoom(conn);
	}

	//发送消息
	public void onMessage(WebSocket socket, String message){
		message = message.toString();
		
		// 客户打开聊天窗口，系统自动推送
		if(null != message && message.startsWith("start")){
			this.joinChatRoom(message.replaceFirst("start", ""),socket);
		}
		
		// 客户离开聊天窗口，系统自动推送
		if(null != message && message.startsWith("leave")){
			this.leaveChatRoom(socket);
		}
			
		// 聊天信息推送
		if(null != message && message.contains("that")){
			String toUser = message.substring(message.indexOf("that")+4, message.indexOf("to"));
			// 构建推送信息结构
			message = message.substring(0, message.indexOf("that")) +"[私信]  "+ message.substring(message.indexOf("that")+4, message.length());
			ChatServerPool.sendMessageToUser(ChatServerPool.getWebSocketByUser(toUser),message);//向所某用户发送消息
			ChatServerPool.sendMessageToUser(socket, message);//同时向本人发送消息
		}else{// 多人聊天，广播
			ChatServerPool.sendMessage(message.toString());//向所有在线用户发送消息
		}
	}

	public void onFragment( WebSocket conn, Framedata fragment ) {
	}

	//异常事件
	public void onError( WebSocket conn, Exception ex ) {
		ex.printStackTrace();
		if( conn != null ) {
		}
	}

	
	// 用户上线处理
	public void joinChatRoom(String user, WebSocket socket){
		JSONObject result = new JSONObject();
		result.element("type", "user_join");
		result.element("user", "<a onclick=\"toUserMsg('"+user+"');\">"+user+"</a>");
		ChatServerPool.sendMessage(result.toString());				//把当前用户加入到所有在线用户列表中
		String joinMsg = "{\"from\":\"[系统]\",\"content\":\""+user+"上线了\",\"timestamp\":"+new Date().getTime()+",\"type\":\"message\"}";
		ChatServerPool.sendMessage(joinMsg);						//向所有在线用户推送当前用户上线的消息
		result = new JSONObject();
		result.element("type", "get_online_user");
		ChatServerPool.addUser(user,socket);							//向连接池添加当前的连接对象
		result.element("list", ChatServerPool.getOnlineUser());
		ChatServerPool.sendMessageToUser(socket, result.toString());	//向当前连接发送当前在线用户的列表
	}
	
	
	// 用户下线处理
	public void leaveChatRoom(WebSocket socket){
		String user = ChatServerPool.getUserByKey(socket); // 获取用户名
		ChatServerPool.removeUser(socket);// 在连接池中移除
		JSONObject result = new JSONObject();
		result.element("type", "user_leave");
		result.element("user", "<a onclick=\"toUserMsg('" + user + "');\">" + user + "</a>");//更换title
		ChatServerPool.sendMessage(result.toString()); // 把当前用户从所有在线用户列表中删除
		String leaveMsg = "{\"from\":\"[系统]\",\"content\":\"" + user + "下线了\",\"timestamp\":" + new Date().getTime()
				+ ",\"type\":\"message\"}";
		
		ChatServerPool.sendMessage(leaveMsg); // 向在线用户发送当前用户退出的消息

	}
	public static void main( String[] args ) throws InterruptedException , IOException {
		WebSocketImpl.DEBUG = false;
		int port = 8887; //端口
		ChatServer s = new ChatServer(port);
		s.start();
	
	}

}
