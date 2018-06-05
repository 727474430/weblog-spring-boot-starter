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

	private final String body;

	public MyRequestWrapper(HttpServletRequest request) throws IOException {
		super(request);
		StringBuilder sb = new StringBuilder();
		String line;
		try (BufferedReader reader = request.getReader()) {
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		}
		body = sb.toString();
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		final ByteArrayInputStream bais = new ByteArrayInputStream(body.getBytes());
		return new ServletInputStream() {
			@Override
			public boolean isFinished() { return false; }
			@Override
			public boolean isReady() { return false; }
			@Override
			public void setReadListener(ReadListener readListener) { }
			@Override
			public int read() throws IOException {
				return bais.read();
			}
		};
	}

	@Override
	public BufferedReader getReader() throws IOException {
		return new BufferedReader(new InputStreamReader(this.getInputStream()));
	}

	public String getBody() {
		return body;
	}
}
