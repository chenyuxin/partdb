package com.wondersgroup.partdb.common.util;

import java.util.Date;
import java.util.Map;

import com.alibaba.druid.sql.ast.SQLDataType;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.wondersgroup.commonutil.CommonUtilString;
import com.wondersgroup.commonutil.constant.StringPool;
import com.wondersgroup.commonutil.type.database.DataBaseType;
import com.wondersgroup.commonutil.type.format.DateType;

/**
 * 通用工具
 */
public class PartDbUtil {
	
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(PartDbUtil.class);
	
	/**
	 * 读取sql中的分片字段对应的value，转为String格式的value
	 * @param value 分片字段对应的value(sql)
	 * @return
	 */
	public static String parseSqlStringValue(SQLExpr value) {
		//String a = DateType.getInstance().getFomatValue(value, "yyyy-MM-dd HH:mm:ss");
		//DateType.getInstance().getParseValue(value, "yyyy-MM-dd HH:mm:ss");
		SQLDataType sqlDataType = value.computeDataType();
		String primaryKeysValue = value.toString();
		if (sqlDataType.getName().equals("date")) {
			if(DataBaseType.getCurrentDataBaseType() == DataBaseType.POSTGREPSQL) {
				throw new RuntimeException("POSTGREPSQL内核数据库的日期主键插入请直接使用字符串格式");
			}
			int si = primaryKeysValue.indexOf(StringPool.APOSTROPHE)+1;
			int ei = primaryKeysValue.indexOf(StringPool.APOSTROPHE,si);
			primaryKeysValue = primaryKeysValue.substring(si, ei);
		} else if (primaryKeysValue.contains(StringPool.APOSTROPHE)) {
			primaryKeysValue = primaryKeysValue.substring(1,primaryKeysValue.length()-1);
			log.debug("char:" + primaryKeysValue);
		}
		Date date = DateType.getInstance().getParseValue(primaryKeysValue);
		if (null != date) {
			primaryKeysValue = DateType.getInstance().getFomatValue(date, PartDBConst.datePrimaryKey2HashFomat);
			primaryKeysValue = primaryKeysValue.replaceAll(PartDBConst.six0, StringPool.BLANK);
		}
		return primaryKeysValue;
	}
	
	/**
	 * 读取sql中的分片字段对应的value，转为String格式的value
	 * @param value 分片字段对应的value(sql)
	 * @return
	 */
	public static String parseSqlStringValue(Object value) {
		String valueString = CommonUtilString.parseString(value);
		String primaryKeysValue = valueString;
		if (valueString.toLowerCase().contains("to_date")) {
			if(DataBaseType.getCurrentDataBaseType() == DataBaseType.POSTGREPSQL) {
				throw new RuntimeException("POSTGREPSQL内核数据库的日期主键插入请直接使用字符串格式");
			}
			int si = primaryKeysValue.indexOf(StringPool.APOSTROPHE)+1;
			int ei = primaryKeysValue.indexOf(StringPool.APOSTROPHE,si);
			primaryKeysValue = primaryKeysValue.substring(si, ei);
		} else if (primaryKeysValue.contains(StringPool.APOSTROPHE)) {
			primaryKeysValue = primaryKeysValue.substring(1,primaryKeysValue.length()-1);
			log.debug("char:" + primaryKeysValue);
		}
		Date date = DateType.getInstance().getParseValue(primaryKeysValue);
		if (null != date) {
			primaryKeysValue = DateType.getInstance().getFomatValue(date, PartDBConst.datePrimaryKey2HashFomat);
			primaryKeysValue = primaryKeysValue.replaceAll(PartDBConst.six0, StringPool.BLANK);
		}
		return primaryKeysValue;
	}
	
	/**
	 * 获取分片配置属性，从sql语句中<br>
	 * 分片配置传入方法： Token后面紧紧跟上斜杠（除号）星注释<br>
	 * <p>"SELECT/* 键值内容 *斜杠（除号）"<p>
	 * @param sql
	 * @return
	 */
	public static Map<String, Object> getFeatureInSql(String tokenName,String sql) {
		String beginMatching = tokenName.concat("/*");
		String endMatching = "*/";
		int begin = sql.toUpperCase().indexOf(beginMatching);
		if (-1 == begin) return null;
		begin += beginMatching.length();
		int end = sql.indexOf(endMatching);
		if (-1 == end) return null;
		StringBuilder sbBuilder = new StringBuilder(StringPool.OPEN_CURLY_BRACE);
		sbBuilder.append(sql.substring(begin, end));
		sbBuilder.append(StringPool.CLOSE_CURLY_BRACE);
		log.debug(sbBuilder.toString());
		return JSON.parseObject(sbBuilder.toString(),new TypeReference<Map<String, Object>>(){} );
	}
	
}
