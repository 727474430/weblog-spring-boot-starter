package com.raindrop.web.log.util;

import org.springframework.util.StreamUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;

/**
 * @name: com.raindrop.web.log.util.RequestWrapper.java
 * @description: 获取请求内容工具类
 * @author: Wang Liang
 * @create Time: 2018/6/4 10:34
 */
public class MyRequestWrapper extends HttpServletRequestWrapper {

	private final byte[] body;

	public MyRequestWrapper(HttpServletRequest request) throws IOException {
		super(request);
		body = StreamUtils.copyToByteArray(request.getInputStream());
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		final ByteArrayInputStream bais = new ByteArrayInputStream(body);
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

	public String getBody() throws UnsupportedEncodingException {
		return new String(body, "UTF-8");
	}
}
