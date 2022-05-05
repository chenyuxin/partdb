package com.wondersgroup.partdb.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement.ValuesClause;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.wondersgroup.common.spring.util.container.TotalTransactionManager;
import com.wondersgroup.commonutil.CommonUtilUUID;
import com.wondersgroup.partdb.common.po.exepo.PartDbExeResult;
import com.wondersgroup.partdb.common.po.tablegroup.PartDataBaseTableGroup;
import com.wondersgroup.partdb.common.util.PartDBConst;
import com.wondersgroup.partdb.common.util.PartDbHashUtil;
import com.wondersgroup.partdb.common.util.PartDbUtil;
import com.wondersgroup.partdb.configservice.intf.TableGroupService;
import com.wondersgroup.partdb.partservice.intf.PartDbTransaction;
import com.wondersgroup.partdb.service.intf.ExecuteSqlService;

@Service("INSERT")
public class ExecuteInsert implements ExecuteSqlService {
	
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ExecuteInsert.class);
	
	@Autowired ApplicationContext applicationContext;
	
	@Autowired TableGroupService tableGroupService;

	@Override
	public PartDbExeResult<?> executeSql(String sql, SQLStatementParser parser) {
		long startExectueTime = System.currentTimeMillis();
		try {
			SQLInsertStatement statement = (SQLInsertStatement) parser.parseInsert();
			SQLName sqlName = statement.getTableName();
			log.debug(sqlName.getSimpleName());
			
			//通过表名获取主键，全部字段信息，计算分表一致性hash
			StringBuffer primaryKeyStrings = new StringBuffer();
			PartDataBaseTableGroup partDataBaseTableGroup = tableGroupService.getTableGroup(sqlName.getSimpleName());
			if (null == partDataBaseTableGroup || partDataBaseTableGroup.isMainTable()) {
				List<String> primaryKeys =  tableGroupService.getPrimaryKeys(sqlName.getSimpleName());
				log.debug(primaryKeys.toString());
				for (String primaryKey :primaryKeys) {
					int primaryKeyIndex = statement.getColumns().indexOf(new SQLIdentifierExpr(primaryKey));
					
					ValuesClause  values =  statement.getValues();
					SQLExpr value = values.getValues().get(primaryKeyIndex);
					String primaryKeysValue = PartDbUtil.parseSqlStringValue(value);
					log.debug("lastPrimaryKeysValue:" + primaryKeysValue);
					
					//主键，计算分表一致性hash
					primaryKeyStrings.append(primaryKeysValue);
				}
				
			} else {
				//TODO 通过外键关联查询出 分组表的主表的分片字段
			}
			//TODO 没有主键，也没有配置分片的表报错，不给予插入数据。
			
			String primaryKeyHash = CommonUtilUUID.getUUIDC64(primaryKeyStrings.toString());
			int partDbIndex = PartDbHashUtil.hashDb(primaryKeyHash,PartDBConst.partdbs);
			PartDbTransaction partDbTransaction = (PartDbTransaction) applicationContext.getBean(PartDBConst.updatePartDataBase);
			
			PartDbExeResult<?> partDbExeResult = partDbTransaction.execute(sql, new TotalTransactionManager(PartDBConst.partdbs[partDbIndex]));
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
