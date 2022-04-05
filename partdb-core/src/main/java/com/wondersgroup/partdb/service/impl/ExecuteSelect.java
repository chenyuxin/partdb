package com.wondersgroup.partdb.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.alibaba.druid.sql.ast.SQLDataType;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement.ValuesClause;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.alibaba.druid.sql.parser.Token;
import com.wondersgroup.common.spring.util.container.TotalTransactionManager;
import com.wondersgroup.commonutil.CommonUtilUUID;
import com.wondersgroup.commonutil.constant.StringPool;
import com.wondersgroup.commonutil.type.database.DataBaseType;
import com.wondersgroup.commonutil.type.format.DateType;
import com.wondersgroup.partdb.common.po.exepo.PartDbExeResult;
import com.wondersgroup.partdb.common.po.tablegroup.PartDataBaseTableGroup;
import com.wondersgroup.partdb.common.util.PartDBConst;
import com.wondersgroup.partdb.common.util.PartDbHashUtil;
import com.wondersgroup.partdb.configservice.intf.TableGroupService;
import com.wondersgroup.partdb.partservice.intf.PartDbTransaction;
import com.wondersgroup.partdb.service.intf.ExecuteSqlService;

@Service
public class ExecuteSelect implements ExecuteSqlService {
	
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ExecuteSelect.class);

	@Autowired ApplicationContext applicationContext;
	
	@Autowired TableGroupService tableGroupService;
	
	@Override
	public PartDbExeResult<?> executeSql(String sql) {
		long startExectueTime = System.currentTimeMillis();
		
		String[] useDbs = PartDBConst.partdbs;
		PartDbTransaction partDbTransaction = null;
		try {
			//新建sql分析
			SQLStatementParser parser = new SQLStatementParser(sql);
			
			// 使用Parser解析生成AST，这里SQLStatement就是AST
			//SQLParserUtils.createSQLStatementParser(sql,);
			String partDbTransactionBeanName = PartDBConst.updatePartDataBase;//执行sql的服务
			
			Token token = parser.getExprParser().getLexer().token();
			if ( Token.SELECT.equals(token) ) {
				partDbTransactionBeanName = PartDBConst.selectPartDataBase;//查询sql的服务
				log.debug(partDbTransactionBeanName);
				parser.parseStatement();
			} else if (Token.INSERT.equals(token)) {
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
						//String a = DateType.getInstance().getFomatValue(value, "yyyy-MM-dd HH:mm:ss");
						//DateType.getInstance().getParseValue(value, "yyyy-MM-dd HH:mm:ss");
						SQLDataType sqlDataType = value.computeDataType();
						log.debug("2主键的sqlDataType：" + sqlDataType.getName());
						log.debug("主键的value：" + value.toString());
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
						log.debug("lastPrimaryKeysValue:" + primaryKeysValue);
						
						//主键，计算分表一致性hash
						primaryKeyStrings.append(primaryKeysValue);
					}
					
					
					
				} else {
					//TODO 通过外键关联查询出 分组表的主表的主键字段
				}
				
				String primaryKeyHash = CommonUtilUUID.getUUIDC64(primaryKeyStrings.toString());
				
				int partDbIndex = PartDbHashUtil.hashDb(primaryKeyHash,PartDBConst.partdbs);
				useDbs = new String[]{ PartDBConst.partdbs[partDbIndex] };
				
			} else {
				parser.parseStatement();
			}
			
			partDbTransaction = (PartDbTransaction) applicationContext.getBean(partDbTransactionBeanName);
			
		} catch (Exception e) {
			PartDbExeResult<?> partDbExeResult = new PartDbExeResult<>();
			partDbExeResult.setCompleteDate(new Date());
			partDbExeResult.setReason(e.getMessage());
			partDbExeResult.setUseTime(System.currentTimeMillis() - startExectueTime);
			return partDbExeResult;
		}	
		
		PartDbExeResult<?> partDbExeResult = partDbTransaction.execute(sql, new TotalTransactionManager(useDbs));
        Date completeDate = new Date();
        partDbExeResult.setCompleteDate(completeDate);
        partDbExeResult.setUseTime(completeDate.getTime() - startExectueTime);
        return partDbExeResult;
		
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



