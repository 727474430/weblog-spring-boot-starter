package com.raindrop.web.log.filter;

import com.raindrop.web.log.util.ResponseWarrperd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * @name: com.raindrop.web.log.filter.WebLoggingFilter.java
 * @description: 请求日志Filter
 * @author: Wang Liang
 * @create Time: 2018/6/1 20:46
 */
public class WebLoggingFilter implements Filter {

	public static final Logger LOGGER = LoggerFactory.getLogger(WebLoggingFilter.class);
	/** No need to filter url */
	private String excludeMappingPath;
	/** Need to print header */
	private String printHeader;

	public void init(FilterConfig filterConfig) { }

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		String uri = request.getRequestURI().substring(request.getContextPath().length()).replaceAll("[/]+$", "");
		// Packaging Response, To get response data
		ResponseWarrperd responseWarrperd = new ResponseWarrperd(response);
		// To meet the conditions conduct log print
		if (!isContains(excludeMappingPath, uri)) {
			// Print specified header
			String headers = getRequestHeaders(request, printHeader);
			// Print json format the request data
			String parameters = getRequestParameters(request);
			LOGGER.info("Request: method={}; content type={}; url={} \r\nRequest Headers: {} \r\nRequest Body={}",
					request.getMethod(), request.getContentType(), request.getRequestURL().toString(),
					headers, parameters);

			long startTime = System.currentTimeMillis();
			chain.doFilter(request, responseWarrperd);
			long endTime = System.currentTimeMillis();

			String result = new String(responseWarrperd.getResponseData());
			handlerResponse(response, result);
			LOGGER.info("Response: status={}; \r\nResponse Body={} \r\nRequest time [{}ms]",
					response.getStatus(), result, endTime - startTime);
		} else {
			chain.doFilter(request, response);
		}
	}

	/**
	 * Handler Response Result And Encoding
	 *
	 * @param response
	 * @param result
	 * @throws IOException
	 */
	private void handlerResponse(HttpServletResponse response, String result) throws IOException {
		response.setContentLength(-1);
		response.setCharacterEncoding("UTF-8");
		PrintWriter writer = response.getWriter();
		writer.write(result);
		writer.flush();
		writer.close();
	}

	/**
	 * 是否包含排除路径
	 *
	 * @param excludeMappingPath
	 * @param uri
	 * @return
	 */
	private boolean isContains(String excludeMappingPath, String uri) {
		String[] excludeMappingPaths = excludeMappingPath != null ? excludeMappingPath.split(";") : new String[0];
		List<String> excludes = Arrays.asList(excludeMappingPaths);
		return excludes.contains(uri);
	}

	/**
	 * 获取请求参数,json格式
	 *
	 * @param request
	 * @return
	 */
	private String getRequestParameters(HttpServletRequest request) {
		StringBuilder sb = new StringBuilder();
		sb.append("{\r\n");
		Map<String, String[]> parameterMap = request.getParameterMap();
		Set<Map.Entry<String, String[]>> entries = parameterMap.entrySet();
		for (Map.Entry<String, String[]> entry : entries) {
			String key = entry.getKey();
			String value = entry.getValue()[0];
			sb.append("\t" + key + ": " + value + ",\r\n");
		}
		if (sb.length() > 1) {
			sb.substring(0, sb.length() - 1);
		}
		sb.append("}");
		return sb.toString();
	}

	/**
	 * 获取请求头,Json格式
	 *
	 * @param request
	 * @param printHeader
	 * @return
	 */
	private String getRequestHeaders(HttpServletRequest request, String printHeader) {
		printHeader = printHeader.toLowerCase();
		StringBuilder sb = new StringBuilder("{");
		Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String key = headerNames.nextElement();
			String value = request.getHeader(key);
			if (printHeader.indexOf(key) != -1) {
				sb.append(key).append("=").append(value).append(";  ");
			}
		}
		sb.append("}");
		return sb.toString();
	}

	public void destroy() { }

	public WebLoggingFilter(String excludeMappingPath, String printHeader) {
		this.excludeMappingPath = excludeMappingPath;
		this.printHeader = printHeader;
	}

	public WebLoggingFilter() { }

}
