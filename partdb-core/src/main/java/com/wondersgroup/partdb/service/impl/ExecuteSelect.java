package com.wondersgroup.partdb.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.alibaba.druid.sql.visitor.SchemaStatVisitor;
import com.alibaba.druid.stat.TableStat.Mode;
import com.alibaba.druid.stat.TableStat.Name;
import com.wondersgroup.common.spring.util.container.TotalTransactionManager;
import com.wondersgroup.partdb.common.po.exepo.PartDbExeResult;
import com.wondersgroup.partdb.common.util.PartDBConst;
import com.wondersgroup.partdb.partservice.intf.PartDbTransaction;
import com.wondersgroup.partdb.service.intf.ExecuteSqlService;

@Service
public class ExecuteSelect implements ExecuteSqlService {
	
	@SuppressWarnings("unused")
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ExecuteSelect.class);

	@Autowired PartDbTransaction partDbTransaction;
	
	@Autowired ApplicationContext applicationContext;
	
	@Override
	public PartDbExeResult<List<Map<String, Object>>> executeSql(String sql) {
		String[] partdbs = PartDBConst.partdbs;
		
		//新建sql分析
		SQLStatementParser parser = new SQLStatementParser(sql);
		
		// 使用Parser解析生成AST，这里SQLStatement就是AST
		SQLStatement statement = parser.parseStatement();
		
		 // 使用visitor来访问AST
		SchemaStatVisitor visitor = new SchemaStatVisitor();
        statement.accept(visitor);
        
        //System.out.println("getTables:" + visitor.getTables().keySet() );
        log.debug("getTables:" + visitor.getTables().keySet() );
        for (Name tabName : visitor.getTables().keySet()) {
        	log.debug("getTableStat:" + visitor.getTableStat(tabName.getName()));
        	if ( Mode.Select. name().equals( visitor.getTableStat(tabName.getName()).toString()) )  {
        		log.debug("Select equals");
        	}
		}
        log.debug("getColumns:" + visitor.getColumns() );
        log.debug("OrderByColumns:" + visitor.getOrderByColumns() );
		
        @SuppressWarnings("unchecked")
		PartDbExeResult<List<Map<String, Object>>> a = (PartDbExeResult<List<Map<String, Object>>>) partDbTransaction.execute(sql, new TotalTransactionManager(applicationContext, partdbs));
		return a;
	}

}
