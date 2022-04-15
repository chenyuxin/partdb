package com.wondersgroup.partdb.configservice.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wondersgroup.commondao.dao.daoutil.sqlreader.SelectDataBaseConfigSql;
import com.wondersgroup.commondao.dao.intf.CommonDao;
import com.wondersgroup.commonutil.type.database.DataBaseType;
import com.wondersgroup.partdb.common.po.tablegroup.PartDataBaseTableGroup;
import com.wondersgroup.partdb.configservice.intf.TableGroupService;

@Service
public class TableGroupServiceImpl implements TableGroupService {

	@Autowired CommonDao commonDao;
	
	@Override
	public PartDataBaseTableGroup getTableGroup(String simpleName) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("tableName", simpleName);
		PartDataBaseTableGroup partDataBaseTableGroup = commonDao.selectObjSingle(PartDataBaseTableGroup.class,paramMap);
		
		return partDataBaseTableGroup;
	}

	@Override
	public List<String> getPrimaryKeys(String tableName) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("tableName", tableName);
		List<String> primaryKeys = commonDao.selectBaseObjs(
				SelectDataBaseConfigSql.getPrimaryKeySql(tableName,DataBaseType.getCurrentDataBaseType()), String.class, paramMap);
		
		return primaryKeys;
	}

}
