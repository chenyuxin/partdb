package com.wondersgroup.partdb.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.alibaba.druid.sql.visitor.SchemaStatVisitor;
import com.alibaba.druid.stat.TableStat.Column;
import com.alibaba.druid.stat.TableStat.Condition;
import com.alibaba.druid.stat.TableStat.Name;
import com.wondersgroup.common.spring.util.container.TotalTransactionManager;
import com.wondersgroup.commonutil.CommonUtilUUID;
import com.wondersgroup.commonutil.constant.StringPool;
import com.wondersgroup.partdb.common.po.exepo.PartDbExeResult;
import com.wondersgroup.partdb.common.po.exepo.PartDbFeature;
import com.wondersgroup.partdb.common.po.tablegroup.PartDataBaseTableGroup;
import com.wondersgroup.partdb.common.util.PartDBConst;
import com.wondersgroup.partdb.common.util.PartDbHashUtil;
import com.wondersgroup.partdb.common.util.PartDbUtil;
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
		
		PartDbTransaction partDbTransaction = null;
		try {
			PartDbFeature partDbFeature = PartDbUtil.getFeatureInSql("SELECT", sql);
			
			SQLStatement statement = parser.parseSelect();
			SchemaStatVisitor visitor = new SchemaStatVisitor();
			statement.accept(visitor);
			Set<Name> tables = visitor.getTables().keySet();
			log.debug("表名\t\t\t" + visitor.getTables().keySet());
			String table = tables.iterator().next().toString();
			
			//通过表名获取主键，全部字段信息，计算分表一致性hash
			StringBuffer primaryKeyStrings = new StringBuffer();
			PartDataBaseTableGroup partDataBaseTableGroup = tableGroupService.getTableGroup(table);
			if (null == partDataBaseTableGroup || partDataBaseTableGroup.isMainTable()) {
				
				log.debug("分组字段\t\t\t" + visitor.getGroupByColumns());
				for (Column a :visitor.getGroupByColumns()) {
					partDbFeature.setGroupBy(a.getName());
				}
				
				List<Condition> conditions = visitor.getConditions();
				log.debug("条件\t\t\t" + visitor.getConditions());
				
				List<String> primaryKeys =  tableGroupService.getPrimaryKeys(table);
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
				//TODO 通过外键关联查询出 分组表的主表的分片字段
			}
			partDbTransaction = (PartDbTransaction) applicationContext.getBean(PartDBConst.selectPartDataBase);
			PartDbExeResult<?> partDbExeResult;
			if (primaryKeyStrings.length() > 0) {//有分表字段作为筛选条件
				String primaryKeyHash = CommonUtilUUID.getUUIDC64(primaryKeyStrings.toString());
				int partDbIndex = PartDbHashUtil.hashDb(primaryKeyHash,PartDBConst.partdbs);
				partDbExeResult = partDbTransaction.execute(sql, new TotalTransactionManager(PartDBConst.partdbs[partDbIndex]), partDbFeature);
			} else {
				partDbExeResult = partDbTransaction.execute(sql, new TotalTransactionManager(PartDBConst.partdbs), partDbFeature);
			}
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



