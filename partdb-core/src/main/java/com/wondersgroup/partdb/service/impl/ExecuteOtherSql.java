package com.wondersgroup.partdb.service.impl;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.wondersgroup.common.spring.util.container.TotalTransactionManager;
import com.wondersgroup.commonutil.constant.StringPool;
import com.wondersgroup.partdb.common.po.exepo.PartDbExeResult;
import com.wondersgroup.partdb.common.util.PartDBConst;
import com.wondersgroup.partdb.common.util.PartDbUtil;
import com.wondersgroup.partdb.configservice.intf.TableGroupService;
import com.wondersgroup.partdb.partservice.intf.PartDbTransaction;
import com.wondersgroup.partdb.service.intf.ExecuteSqlService;

@Service("OTHER")
public class ExecuteOtherSql implements ExecuteSqlService {
	
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ExecuteCreate.class);
	
	@Autowired ApplicationContext applicationContext;
	
	@Autowired TableGroupService tableGroupService;

	@Override
	public PartDbExeResult<?> executeSql(String sql, SQLStatementParser parser) {
		long startExectueTime = System.currentTimeMillis();
		try {
			log.debug("OTHER SQL:" + sql );
			Map<String, Object> featureMap = PartDbUtil.getFeatureInSql(StringPool.BLANK, sql);
			featureMap.forEach((key,value) -> { 
				log.debug(key + "->" + value);
			});
			PartDbTransaction partDbTransaction = (PartDbTransaction) applicationContext.getBean(PartDBConst.updatePartDataBase);
			PartDbExeResult<?> partDbExeResult = partDbTransaction.execute(sql, new TotalTransactionManager(PartDBConst.partdbs));
			Date completeDate = new Date();
			partDbExeResult.setCompleteDate(completeDate);
			partDbExeResult.setUseTime(completeDate.getTime() - startExectueTime);
			return partDbExeResult;
		} catch (Exception e) {
			PartDbExeResult<?> partDbExeResult = new PartDbExeResult<>();
			Date completeDate = new Date();
			partDbExeResult.setCompleteDate(completeDate);
			partDbExeResult.setReason(e.getMessage());
			partDbExeResult.setUseTime(completeDate.getTime() - startExectueTime);
			return partDbExeResult;
		}
	}

}
