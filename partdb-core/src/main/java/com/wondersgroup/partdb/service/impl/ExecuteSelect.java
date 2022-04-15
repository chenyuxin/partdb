package com.wondersgroup.partdb.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.wondersgroup.common.spring.util.container.TotalTransactionManager;
import com.wondersgroup.commonutil.CommonUtilString;
import com.wondersgroup.partdb.common.po.exepo.PartDbExeResult;
import com.wondersgroup.partdb.common.util.PartDBConst;
import com.wondersgroup.partdb.configservice.intf.TableGroupService;
import com.wondersgroup.partdb.partservice.intf.PartDbTransaction;
import com.wondersgroup.partdb.service.intf.ExecuteSqlService;

@Service("SELECT")
public class ExecuteSelect implements ExecuteSqlService {
	
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ExecuteSelect.class);

	@Autowired ApplicationContext applicationContext;
	
	@Autowired TableGroupService tableGroupService;
	
	@Override
	public PartDbExeResult<?> executeSql(String sql,SQLStatementParser parser) {
		long startExectueTime = System.currentTimeMillis();
		
		String[] useDbs = PartDBConst.partdbs;
		PartDbTransaction partDbTransaction = null;
		try {
			parser.parseStatement();
			List<String> paramsKeyNames = CommonUtilString.getParamsKeyNames(sql);
			for (String paramsKeyName : paramsKeyNames) {
				log.debug("注释内容paramsKeyName:" + paramsKeyName);
			}
			
			partDbTransaction = (PartDbTransaction) applicationContext.getBean(PartDBConst.selectPartDataBase);
			PartDbExeResult<?> partDbExeResult = partDbTransaction.execute(sql, new TotalTransactionManager(useDbs));
	        Date completeDate = new Date();
	        partDbExeResult.setCompleteDate(completeDate);
	        partDbExeResult.setUseTime(completeDate.getTime() - startExectueTime);
	        return partDbExeResult;
		} catch (Exception e) {
			PartDbExeResult<?> partDbExeResult = new PartDbExeResult<>();
			partDbExeResult.setCompleteDate(new Date());
			partDbExeResult.setReason(e.getMessage());
			partDbExeResult.setUseTime(System.currentTimeMillis() - startExectueTime);
			return partDbExeResult;
		}	
		
	}

}




// 使用visitor来访问AST
//SchemaStatVisitor visitor = new SchemaStatVisitor();
//statement.accept(visitor);
//
//log.debug("getTables:" + visitor.getTables().keySet() );
//for (Name tabName : visitor.getTables().keySet()) {
//	log.debug("getTableStat:" + visitor.getTableStat(tabName.getName()));
//	if ( Mode.Select. name().equals( visitor.getTableStat(tabName.getName()).toString()) )  {
//		log.debug("Select equals");
//	}
//}
//log.debug("getColumns:" + visitor.getColumns() );
//log.debug("OrderByColumns:" + visitor.getOrderByColumns() );



