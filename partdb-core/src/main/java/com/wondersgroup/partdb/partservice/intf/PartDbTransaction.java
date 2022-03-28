package com.wondersgroup.partdb.partservice.intf;

import com.wondersgroup.partdb.common.po.exepo.PartDbExeResult;
import com.wondersgroup.partdb.common.po.exepo.PartDbFeature;

/**
 * partDb分片数据库事务
 * @author chenyuxin
 */
public interface PartDbTransaction {
	
	/**
	 * 初步设置一个执行接口
	 * @param <T>
	 * @param sql
	 * @param partDbFeature
	 * @return
	 */
	<T> PartDbExeResult<T> execute(String sql,PartDbFeature... partDbFeature);
	
}
