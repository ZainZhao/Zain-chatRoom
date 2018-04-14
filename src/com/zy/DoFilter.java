package com.fh;

import java.io.IOException;
import java.net.UnknownHostException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.java_websocket.WebSocketImpl;




//过滤器
public class DoFilter implements Filter{

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain arg2) throws IOException, ServletException {
		// TODO Auto-generated method stub
		
	}

	/*@Override
	public void init(FilterConfig arg0) throws ServletException {
		System.out.println("====");
		
	}*/
	
	@Override
	public void init(FilterConfig arg0) throws ServletException {
		WebSocketImpl.DEBUG = false;
		int port = 8887; //端口
		ChatServer s;
		try {
			s = new ChatServer(port);
			s.start();
			//System.out.println( "服务器的端口" + s.getPort() );
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

}
