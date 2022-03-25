package com.wondersgroup.partdb.service.impl;

import java.util.List;

import com.wondersgroup.partdb.common.util.PartDBConst;
import com.wondersgroup.partdb.service.intf.ExecuteSqlService;

public class ExecuteSelect implements ExecuteSqlService {

	@Override
	public List<?> executeSql(String sql) {
		String[] partdbs = PartDBConst.partdbs;
		
		return null;
	}

}
