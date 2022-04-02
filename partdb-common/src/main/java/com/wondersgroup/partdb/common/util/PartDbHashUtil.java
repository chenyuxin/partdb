package com.wondersgroup.partdb.common.util;

import com.wondersgroup.commonutil.constant.IdCodeConst;

/**
 * hash分表分库计算
 * @author chenyuxin
 */
public class PartDbHashUtil {

	/**
	 * 通过主键的自定base64值确定所在分片
	 * @param primaryKeyHash 主键的自定base64值
	 * @param partdbs 当前的全部分片 [partdb数量必须是2^x次方， 也就是每次扩容需要扩大一倍]
	 * @return 分片索引数值
	 */
	public static int hashDb(String primaryKeyHash, String[] partdbs) {
		if( partdbs.length < 64 ) {
			char c = primaryKeyHash.charAt(0);
			int a = IdCodeConst.idBase64.indexOf(c);
			
		}
		
		
		
		return 0;
	}

}
