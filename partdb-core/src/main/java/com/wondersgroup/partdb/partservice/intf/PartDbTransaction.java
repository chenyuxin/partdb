package com.wondersgroup.partdb.partservice.intf;

import com.wondersgroup.common.spring.util.container.TotalTransactionManager;
import com.wondersgroup.partdb.common.po.exepo.PartDbExeResult;
import com.wondersgroup.partdb.common.po.exepo.PartDbFeature;

/**
 * partDb分片数据库事务
 * @author chenyuxin
 */
public interface PartDbTransaction {
	
	/**
	 * 初步设置一个执行接口
	 * @param <?>
	 * @param sql
	 * @param totalTransactionManager 新建立一个事务管理器,管理本次事务
	 * @param partDbFeature 
	 * @return
	 */
	PartDbExeResult<?> execute(String sql,TotalTransactionManager totalTransactionManager,PartDbFeature... partDbFeature);
	
	
	
}
