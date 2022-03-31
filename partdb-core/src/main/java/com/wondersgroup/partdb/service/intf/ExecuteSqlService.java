package com.wondersgroup.partdb.service.intf;


import com.wondersgroup.partdb.common.po.exepo.PartDbExeResult;

public interface ExecuteSqlService {
	
	PartDbExeResult<?> executeSql(String sql);

}
