package com.wondersgroup.partdb.test.service.intf;

public interface TestTransactional {
	
	void TestDoubleTransactional1();

	void TestDoubleTransactional2();

	void TestDoubleTransactional3(String... dataSrouceBeanNames);

}
