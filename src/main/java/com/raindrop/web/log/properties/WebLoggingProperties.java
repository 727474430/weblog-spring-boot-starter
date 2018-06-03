package com.raindrop.web.log.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @name: com.raindrop.web.log.properties.WebLoggingProperties.java
 * @description: 请求日志配置
 * @author: Wang Liang
 * @create Time: 2018/6/1 20:51
 */
@ConfigurationProperties("web.log")
public class WebLoggingProperties {

	private String mappingPath = "/*";
	private String excludeMappingPath = "";
	private String enable = "false";
	private String printHeader = "";

	public String getMappingPath() {
		return mappingPath;
	}

	public void setMappingPath(String mappingPath) {
		this.mappingPath = mappingPath;
	}

	public String getExcludeMappingPath() {
		return excludeMappingPath;
	}

	public void setExcludeMappingPath(String excludeMappingPath) {
		this.excludeMappingPath = excludeMappingPath;
	}

	public String getEnable() {
		return enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
	}

	public String getPrintHeader() {
		return printHeader;
	}

	public void setPrintHeader(String printHeader) {
		this.printHeader = printHeader;
	}
}
