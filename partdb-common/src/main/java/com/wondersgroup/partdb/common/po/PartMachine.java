package com.wondersgroup.partdb.common.po;

import com.wondersgroup.commondao.dao.daoutil.anotation.Id;
import com.wondersgroup.commondao.dao.daoutil.anotation.Table;

@Table(name="PART_MACHINE")
public class PartMachine {
	
	/**
	 * 机器编号
	 */
	@Id
	private String id;
	
	/**
	 * 物理机ip
	 */
	private String ip;
	
	/**
	 * 端口
	 */
	private int port;
	
	/**
	 * 在用
	 */
	private boolean useConfig;
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public boolean isUseConfig() {
		return useConfig;
	}

	public void setUseConfig(boolean useConfig) {
		this.useConfig = useConfig;
	}

	

}
