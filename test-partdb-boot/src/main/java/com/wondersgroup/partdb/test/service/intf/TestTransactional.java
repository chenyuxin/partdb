package com.wondersgroup.partdb.test.service.intf;

import java.util.Map;

import com.wondersgroup.common.spring.util.thread.AsyncTransactionThread;

public interface TestTransactional {
	
	void TestDoubleTransactional1();

	void TestDoubleTransactional2();

	void TestDoubleTransactional3(String... dataSrouceBeanNames);
	
	void TestDoubleTransactional4(Map<String, AsyncTransactionThread> map,String... dataSrouceBeanNames);

}
