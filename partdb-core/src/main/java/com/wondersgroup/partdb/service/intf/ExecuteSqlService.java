package com.wondersgroup.partdb.service.intf;

import java.util.List;
import java.util.Map;

import com.wondersgroup.partdb.common.po.exepo.PartDbExeResult;

public interface ExecuteSqlService {
	
	PartDbExeResult<List<Map<String, Object>>> executeSql(String sql);

}
