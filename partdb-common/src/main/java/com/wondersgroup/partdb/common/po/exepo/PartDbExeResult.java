package com.wondersgroup.partdb.common.po.exepo;

import java.sql.Date;

/**
 * partDb执行结果返回<br>
 * TODO 执行计划的保存,添加执行计划需要的字段
 * @author chenyuxin
 */
public class PartDbExeResult<T> {
	
	/**
	 * 执行id
	 */
	private String exeId;
	
	/**
	 * 执行结果返回
	 */
	private T resultDatas;
	
	/**
	 * 执行状态 true成功 false失败
	 */
	private boolean state;
	
	/**
	 * 原因描述
	 */
	private String reason;
	
	/**
	 * 用时
	 */
	private long useTime;
	
	/**
	 * 完成日期
	 */
	private Date complateDate;

	public String getExeId() {
		return exeId;
	}

	public void setExeId(String exeId) {
		this.exeId = exeId;
	}

	public T getResultDatas() {
		return resultDatas;
	}

	public void setResultDatas(T resultDatas) {
		this.resultDatas = resultDatas;
	}

	public boolean isState() {
		return state;
	}

	public void setState(boolean state) {
		this.state = state;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public long getUseTime() {
		return useTime;
	}

	public void setUseTime(long useTime) {
		this.useTime = useTime;
	}

	public Date getComplateDate() {
		return complateDate;
	}

	public void setComplateDate(Date complateDate) {
		this.complateDate = complateDate;
	}
	

}
