package com.raindrop.web.log.filter;

import com.raindrop.web.log.util.MyRequestWrapper;
import com.raindrop.web.log.util.MyResponseWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

/**
 * @name: com.raindrop.web.log.filter.WebLoggingFilter.java
 * @description: 请求日志Filter
 * @author: Wang Liang
 * @create Time: 2018/6/1 20:46
 */
public class WebLoggingFilter implements Filter {

	public static final Logger LOGGER = LoggerFactory.getLogger(WebLoggingFilter.class);
	public static final String POST = "POST";
	/** No need to filter url */
	private String excludeMappingPath;
	/** Need to print header */
	private String printHeader;

	@Override
	public void init(FilterConfig filterConfig) { }

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;

		String uri = request.getRequestURI();
		String method = request.getMethod();
		String contentType = request.getContentType();
		String url = request.getRequestURL().toString();

		MyResponseWrapper responseWrapper = new MyResponseWrapper(response);
		MyRequestWrapper requestWrapper = null;

		// To meet the conditions conduct log print
		if (!isContains(excludeMappingPath, uri)) {
			// Print specified header
			String headers = getRequestHeaders(request, printHeader);

			// Print json or form format the request data
			String parameters;
			if (POST.equalsIgnoreCase(request.getMethod())) {
				requestWrapper = new MyRequestWrapper(request);
				parameters = requestWrapper.getBody();
			} else {
				parameters = getFormRequestParameters(request);
			}
			LOGGER.info("Request: method={}; content type={}; url={} \r\nRequest Headers: {} \r\nRequest Body={}",
					method, contentType, url, headers, parameters);

			long startTime = System.currentTimeMillis();
			chain.doFilter(requestWrapper == null ? request : requestWrapper, responseWrapper);
			long endTime = System.currentTimeMillis();

			String result = "";
			if (response.getContentType() != null && response.getContentType().indexOf("text/html") == -1) {
				result = new String(responseWrapper.getResponseData());
			}
			LOGGER.info("Response: status={}; \r\nResponse Body={} \r\nRequest time [{}ms]",
					response.getStatus(), result, endTime - startTime);
			if ("".equals(result)) {
				result = new String();
			}
			// write data
			handlerResponse(response, "".equals(result) ? new String(responseWrapper.getResponseData()) : result);
		} else {
			chain.doFilter(request, response);
		}
	}

	/**
	 * Get Form Format Request Data
	 *
	 * @param request
	 * @return
	 */
	private String getFormRequestParameters(HttpServletRequest request) {
		StringBuilder sb = new StringBuilder();
		Enumeration<String> parameterNames = request.getParameterNames();
		while (parameterNames.hasMoreElements()) {
			String key = parameterNames.nextElement();
			String value = request.getParameter(key);
			sb.append("\t" + key + ": " + value + "\t");
		}
		return sb.toString();
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
		for (int i = 0; i < excludeMappingPaths.length; i++) {
			if (uri.indexOf(excludeMappingPaths[i]) != -1) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取请求参数,json格式
	 *
	 * @param request
	 * @return
	 */
	private String getJsonRequestParameters(HttpServletRequest request) {
		StringBuilder sb = new StringBuilder();
		sb.append("{\r\n");
		String line;
		try {
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null) {
				sb.append("\t" + line + ",");
			}
		} catch (IOException e) {
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

	@Override
	public void destroy() { }

	public WebLoggingFilter(String excludeMappingPath, String printHeader) {
		this.excludeMappingPath = excludeMappingPath;
		this.printHeader = printHeader;
	}

	public WebLoggingFilter() { }

}
