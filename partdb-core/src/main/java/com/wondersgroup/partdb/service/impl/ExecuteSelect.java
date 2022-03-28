package com.wondersgroup.partdb.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.alibaba.druid.sql.visitor.SchemaStatVisitor;
import com.alibaba.druid.stat.TableStat.Mode;
import com.alibaba.druid.stat.TableStat.Name;
import com.wondersgroup.partdb.common.util.PartDBConst;
import com.wondersgroup.partdb.service.intf.ExecuteSqlService;

@Service
public class ExecuteSelect implements ExecuteSqlService {

	@Override
	public List<?> executeSql(String sql) {
		String[] partdbs = PartDBConst.partdbs;
		
		//新建sql分析
		SQLStatementParser parser = new SQLStatementParser(sql);
		
		// 使用Parser解析生成AST，这里SQLStatement就是AST
		SQLStatement statement = parser.parseStatement();
		
		 // 使用visitor来访问AST
		SchemaStatVisitor visitor = new SchemaStatVisitor();
        statement.accept(visitor);
        
        System.out.println("getTables:" + visitor.getTables().keySet() );
        for (Name tabName : visitor.getTables().keySet()) {
        	System.out.println("getTableStat:" + visitor.getTableStat(tabName.getName()));
        	if ( Mode.Select. name().equals( visitor.getTableStat(tabName.getName()).toString()) )  {
        		System.out.println("Select equals");
        	}
		}
        
        System.out.println("getColumns:" + visitor.getColumns() );
        
        System.out.println(visitor.getColumns());
        System.out.println(visitor.getOrderByColumns());
		
		return null;
	}

}
