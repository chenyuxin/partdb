package com.wondersgroup.partdb.service.intf;


import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.wondersgroup.partdb.common.po.exepo.PartDbExeResult;

public interface ExecuteSqlService {
	
	PartDbExeResult<?> executeSql(String sql,SQLStatementParser parser);

}
