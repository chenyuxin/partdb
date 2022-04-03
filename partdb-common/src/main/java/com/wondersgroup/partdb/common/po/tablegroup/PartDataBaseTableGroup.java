package com.wondersgroup.partdb.common.po.tablegroup;

import com.wondersgroup.commondao.dao.daoutil.anotation.Id;
import com.wondersgroup.commondao.dao.daoutil.anotation.Table;

@Table(name = "part_data_base_table_group")
public class PartDataBaseTableGroup {
	
	@Id
	private String tableId;//表id
	
	private String tableGroupId;//表分组id,主表id
	
	private String schemaName;//所在数据模块schema
	
	private String tableName;//表名
	
	private boolean mainTable;//是否主表

	public String getTableId() {
		return tableId;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}

	public String getTableGroupId() {
		return tableGroupId;
	}

	public void setTableGroupId(String tableGroupId) {
		this.tableGroupId = tableGroupId;
	}

	public String getSchemaName() {
		return schemaName;
	}

	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public boolean isMainTable() {
		return mainTable;
	}

	public void setMainTable(boolean mainTable) {
		this.mainTable = mainTable;
	}


}
