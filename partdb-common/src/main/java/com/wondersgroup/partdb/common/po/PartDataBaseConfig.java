package com.wondersgroup.partdb.common.po;

import com.wondersgroup.commondao.dao.daoutil.anotation.Id;
import com.wondersgroup.commondao.dao.daoutil.anotation.Table;

@Table(name="PART_DATA_BASE_CONFIG")
public class PartDataBaseConfig {
	
	/**
	 * 分片数据库名
	 */
	@Id
	private String partName;
	
	/**
	 * 分片数据库ip
	 */
	private String ip;
	
	/**
	 * 分片数据库端口
	 */
	private int port;
	
	/**
	 * 在用
	 */
	private boolean use;

	
	public String getPartName() {
		return partName;
	}

	public void setPartName(String partName) {
		this.partName = partName;
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

	public boolean isUse() {
		return use;
	}

	public void setUse(boolean use) {
		this.use = use;
	}
	
	
	

}
