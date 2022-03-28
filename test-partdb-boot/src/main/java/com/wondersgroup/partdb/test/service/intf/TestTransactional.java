package com.wondersgroup.partdb.test.service.intf;


import com.wondersgroup.common.spring.util.container.TotalTransactionManager;

public interface TestTransactional {
	
	void TestDoubleTransactional1();

	void TestDoubleTransactional2();

	//void TestDoubleTransactional3(String... dataSrouceBeanNames);
	
	void TestDoubleTransactional4(TotalTransactionManager totalTransactionManager);

}
