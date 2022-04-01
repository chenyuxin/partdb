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
import com.alibaba.druid.sql.parser.Token;
import com.wondersgroup.common.spring.util.container.TotalTransactionManager;
import com.wondersgroup.partdb.common.po.exepo.PartDbExeResult;
import com.wondersgroup.partdb.common.po.tablegroup.PartDataBaseTableGroup;
import com.wondersgroup.partdb.common.util.PartDBConst;
import com.wondersgroup.partdb.configservice.intf.TableGroupService;
import com.wondersgroup.partdb.partservice.intf.PartDbTransaction;
import com.wondersgroup.partdb.service.intf.ExecuteSqlService;

@Service
public class ExecuteSelect implements ExecuteSqlService {
	
	@SuppressWarnings("unused")
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ExecuteSelect.class);

	@Autowired ApplicationContext applicationContext;
	
	@Autowired TableGroupService tableGroupService;
	
	@Override
	public PartDbExeResult<?> executeSql(String sql) {
		
		boolean readOnly = false;
		PartDbTransaction partDbTransaction = null;
		try {
			//新建sql分析
			SQLStatementParser parser = new SQLStatementParser(sql);
			
			// 使用Parser解析生成AST，这里SQLStatement就是AST
			//SQLParserUtils.createSQLStatementParser(sql,);
			String partDbTransactionBeanName = PartDBConst.updatePartDataBase;
			
			Token token = parser.getExprParser().getLexer().token();
			if ( Token.SELECT.equals(token) ) {
				readOnly = true;
				partDbTransactionBeanName = PartDBConst.selectPartDataBase;
				log.debug(partDbTransactionBeanName);
				parser.parseStatement();
			} else if (Token.INSERT.equals(token)) {
				SQLInsertStatement statement = (SQLInsertStatement) parser.parseInsert();
				SQLName sqlName = statement.getTableName();
				log.debug(sqlName.getSimpleName());
				
				//TODO    通过表名获取主键，全部字段信息，计算分表一致性hash
				
				
				PartDataBaseTableGroup partDataBaseTableGroup = tableGroupService.getTableGroup(sqlName.getSimpleName());
				if (null == partDataBaseTableGroup || partDataBaseTableGroup.isMainTable()) {
					log.debug("getPrimaryKeySet");
					List<String> primaryKeys =  tableGroupService.getPrimaryKeys(sqlName.getSimpleName());
					log.debug(primaryKeys.toString());
					for (String primaryKey :primaryKeys) {
						int primaryKeyIndex = statement.getColumns().indexOf(new SQLIdentifierExpr(primaryKey));
						
						ValuesClause  values =  statement.getValues();
						SQLExpr value = values.getValues().get(primaryKeyIndex);
						
						log.debug("主键的value：" +value.toString());
						
						//TODO 主键，全部字段信息，计算分表一致性hash
					}
					
				} else {
					
				}
			} else {
				parser.parseStatement();
			}
			
			partDbTransaction = (PartDbTransaction) applicationContext.getBean(partDbTransactionBeanName);
			
			 // 使用visitor来访问AST
//			SchemaStatVisitor visitor = new SchemaStatVisitor();
//	        statement.accept(visitor);
//	        
//	        log.debug("getTables:" + visitor.getTables().keySet() );
//	        for (Name tabName : visitor.getTables().keySet()) {
//	        	log.debug("getTableStat:" + visitor.getTableStat(tabName.getName()));
//	        	if ( Mode.Select. name().equals( visitor.getTableStat(tabName.getName()).toString()) )  {
//	        		log.debug("Select equals");
//	        	}
//			}
//	        log.debug("getColumns:" + visitor.getColumns() );
//	        log.debug("OrderByColumns:" + visitor.getOrderByColumns() );
			
			
		} catch (Exception e) {
			PartDbExeResult<?> partDbExeResult = new PartDbExeResult<>();
			partDbExeResult.setComplateDate(new Date());
			partDbExeResult.setReason(e.getMessage());
			return partDbExeResult;
		}	
		
		long startExectueTime = System.currentTimeMillis();
		
		PartDbExeResult<?> partDbExeResult = partDbTransaction.execute(sql, new TotalTransactionManager(readOnly,applicationContext,PartDBConst.partdbs));
        Date complateDate = new Date();
        partDbExeResult.setComplateDate(complateDate);
        partDbExeResult.setUseTime(complateDate.getTime() - startExectueTime);
        return partDbExeResult;
		
		
	}

}
