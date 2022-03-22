package com.wondersgroup.partdb.test.po;

import com.wondersgroup.commondao.dao.daoutil.anotation.Id;
import com.wondersgroup.commondao.dao.daoutil.anotation.Table;

@Table(name = "test1")
public class TestPo {
	@Id
	String id;
	String name;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	

}
