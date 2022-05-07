package com.wondersgroup.partdb.common.po.exepo;

/**
 * 分片数据库执行配置
 */
public class PartDbFeature {
	
	/**
	 * count字段名
	 */
	private String count;
	
	/**
	 * max字段名
	 */
	private String max;
	
	/**
	 * 分组字段
	 */
	private String groupBy;

	
	
	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getMax() {
		return max;
	}

	public void setMax(String max) {
		this.max = max;
	}

	public String getGroupBy() {
		return groupBy;
	}

	public void setGroupBy(String groupBy) {
		this.groupBy = groupBy;
	}
	
	
	

}
