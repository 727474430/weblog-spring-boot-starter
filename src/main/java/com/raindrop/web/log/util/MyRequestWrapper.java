package com.raindrop.web.log.util;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @name: com.raindrop.web.log.util.RequestWrapper.java
 * @description: 获取请求内容工具类
 * @author: Wang Liang
 * @create Time: 2018/6/4 10:34
 */
public class MyRequestWrapper extends HttpServletRequestWrapper {

	private HttpServletRequest request;
	private String body;

	public MyRequestWrapper(HttpServletRequest request) throws IOException {
		super(request);
		this.request = request;
		body = "";
		try (BufferedReader reader = request.getReader()) {
			String line;
			while ((line = reader.readLine()) != null) {
				body += line;
			}
		}
	}

	@Override
	public ServletInputStream getInputStream() {
		final ByteArrayInputStream bis = new ByteArrayInputStream(body.getBytes());
		return new WrapperInputStream(bis);
	}

	@Override
	public BufferedReader getReader() throws IOException {
		return new BufferedReader(new InputStreamReader(this.getInputStream()));
	}

	private class WrapperInputStream extends ServletInputStream {

		private ByteArrayInputStream bis;

		public WrapperInputStream(ByteArrayInputStream bis) {
			this.bis = bis;
		}

		@Override
		public boolean isFinished() {
			return false;
		}

		@Override
		public boolean isReady() {
			return false;
		}

		@Override
		public void setReadListener(ReadListener readListener) {
		}

		@Override
		public int read() throws IOException {
			return bis.read();
		}
	}

}
