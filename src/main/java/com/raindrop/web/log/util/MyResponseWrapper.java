package com.raindrop.web.log.util;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.*;

/**
 * @name: com.raindrop.web.log.util.ResponseWrapper.java
 * @description: 获取响应内容工具
 * @author: Wang Liang
 * @create Time: 2018/6/2 20:12
 */
public class MyResponseWrapper extends HttpServletResponseWrapper {

	private ByteArrayOutputStream buffer;
	private ServletOutputStream out;
	private PrintWriter printWriter;

	public MyResponseWrapper(HttpServletResponse response) throws UnsupportedEncodingException {
		super(response);
		this.buffer = new ByteArrayOutputStream();
		this.out = new WrapperOutputStream(buffer);
		this.printWriter = new PrintWriter(new OutputStreamWriter(buffer, this.getCharacterEncoding()));
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		return out;
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		return printWriter;
	}

	@Override
	public void flushBuffer() throws IOException {
		if (out != null) {
			out.flush();
		}
		if (printWriter != null) {
			printWriter.flush();
		}
	}

	@Override
	public void reset() {
		buffer.reset();
	}

	/**
	 * 将out、writer中的数据强制输出到WapperedResponse的buffer中,否则取不到数据
	 *
	 * @return
	 * @throws IOException
	 */
	public byte[] getResponseData() throws IOException {
		flushBuffer();
		return buffer.toByteArray();
	}

	/** 内部类,重写ServletOutputStream.write方法,使用ByteArrayOutputStream替换OutputStream */
	private class WrapperOutputStream extends ServletOutputStream {
		private ByteArrayOutputStream bos;

		public WrapperOutputStream(ByteArrayOutputStream bos) {
			this.bos = bos;
		}

		@Override
		public void write(int b) throws IOException {
			bos.write(b);
		}

		@Override
		public void write(byte[] b) throws IOException {
			bos.write(b, 0, b.length);
		}

		@Override
		public boolean isReady() {
			return false;
		}

		@Override
		public void setWriteListener(WriteListener writeListener) { }
	}
}
