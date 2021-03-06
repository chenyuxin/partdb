package com.wondersgroup.partdb.configservice.intf;

import java.util.List;

import com.wondersgroup.partdb.common.po.tablegroup.PartDataBaseTableGroup;

public interface TableGroupService {

	/**
	 * 获取主键字段<br>
	 * 每次查询出来的主键必须一致
	 * @param tableName 表名
	 * @return
	 */
	List<String> getPrimaryKeys(String tableName);
	
	/**
	 * 获取表分组情况
	 * @param tableName 表名
	 * @return
	 */
	PartDataBaseTableGroup getTableGroup(String tableName);

}
