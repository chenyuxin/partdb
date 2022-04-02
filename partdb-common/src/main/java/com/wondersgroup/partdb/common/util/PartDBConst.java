package com.wondersgroup.partdb.common.util;

/**
 * partdb常量数据
 */
public class PartDBConst {
	/**
	 * 分片名
	 */
	public static String[] partdbs = null;//启动时再初始化
	
	/**
	 * 操作PartDataBase进行变更
	 */
	public static final String updatePartDataBase = "updatePartDataBase";
	
	/**
	 * 查询PartDataBase
	 */
	public static final String selectPartDataBase = "selectPartDataBase";
	
	/**
	 * 日期类型字段做主键时的hash前格式化
	 */
	public static final String datePrimaryKey2HashFomat = "yyyyMMddHHmmss";
	
	/**
	 * 6个0表示的时分秒替换为""
	 */
	public static final String six0 = "000000";

}
