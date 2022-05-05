package com.wondersgroup.partdb.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.statement.SQLUpdateStatement;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.alibaba.druid.sql.visitor.SchemaStatVisitor;
import com.alibaba.druid.stat.TableStat.Condition;
import com.wondersgroup.common.spring.util.container.TotalTransactionManager;
import com.wondersgroup.commonutil.CommonUtilUUID;
import com.wondersgroup.commonutil.constant.StringPool;
import com.wondersgroup.partdb.common.po.exepo.PartDbExeResult;
import com.wondersgroup.partdb.common.po.tablegroup.PartDataBaseTableGroup;
import com.wondersgroup.partdb.common.util.PartDBConst;
import com.wondersgroup.partdb.common.util.PartDbHashUtil;
import com.wondersgroup.partdb.common.util.PartDbUtil;
import com.wondersgroup.partdb.configservice.intf.TableGroupService;
import com.wondersgroup.partdb.partservice.intf.PartDbTransaction;
import com.wondersgroup.partdb.service.intf.ExecuteSqlService;

@Service("UPDATE")
public class ExecuteUpdate implements ExecuteSqlService {

	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ExecuteUpdate.class);
	
	@Autowired ApplicationContext applicationContext;
	
	@Autowired TableGroupService tableGroupService;
	
	@Override
	public PartDbExeResult<?> executeSql(String sql, SQLStatementParser parser) {
		long startExectueTime = System.currentTimeMillis();
		try {
			SQLUpdateStatement statement  = parser.parseUpdateStatement();
			SQLName sqlName = statement.getTableName();
			log.debug(sqlName.getSimpleName());
			
			//通过表名获取主键，全部字段信息，计算分表一致性hash
			StringBuffer primaryKeyStrings = new StringBuffer();
			
			PartDataBaseTableGroup partDataBaseTableGroup = tableGroupService.getTableGroup(sqlName.getSimpleName());
			if (null == partDataBaseTableGroup || partDataBaseTableGroup.isMainTable()) {
				SchemaStatVisitor visitor = new SchemaStatVisitor();
				statement.accept(visitor);
				List<Condition> conditions = visitor.getConditions();
				
				List<String> primaryKeys =  tableGroupService.getPrimaryKeys(sqlName.getSimpleName());
				log.debug(primaryKeys.toString());
				for (String primaryKey :primaryKeys) {
					for (Condition condition : conditions) {
						String columnName = condition.getColumn().getName();
						String operator = condition.getOperator();
						if (primaryKey.equalsIgnoreCase(columnName) && StringPool.EQUAL.equals(operator)) {
							if (condition.getValues().size() == 1) {
								Object value = condition.getValues().get(0);
								log.debug(value.toString());
								//主键，计算分表一致性hash
								primaryKeyStrings.append(PartDbUtil.parseSqlStringValue(value));
							}
						}
					}
				}
				
			} else {
				//TODO 通过外键关联查询出 分组表的主表的分表字段
			}
			
			PartDbTransaction partDbTransaction = (PartDbTransaction) applicationContext.getBean(PartDBConst.updatePartDataBase);
			PartDbExeResult<?> partDbExeResult;
			if (primaryKeyStrings.length() > 0) {//有分表字段作为筛选条件
				String primaryKeyHash = CommonUtilUUID.getUUIDC64(primaryKeyStrings.toString());
				int partDbIndex = PartDbHashUtil.hashDb(primaryKeyHash,PartDBConst.partdbs);
				partDbExeResult = partDbTransaction.execute(sql, new TotalTransactionManager(PartDBConst.partdbs[partDbIndex]));
			} else {
				partDbExeResult = partDbTransaction.execute(sql, new TotalTransactionManager(PartDBConst.partdbs));
			}
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
