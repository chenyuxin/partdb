package com.wondersgroup.partdb.test.service.impl;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wondersgroup.common.spring.aop.CommonAop;
import com.wondersgroup.common.spring.transaction.MultipleManagerAsyncTransaction;
import com.wondersgroup.common.spring.transaction.MultipleManagerTransaction;
import com.wondersgroup.common.spring.util.thread.AsyncTransactionThread;
import com.wondersgroup.commondao.dao.intf.CommonDao;
import com.wondersgroup.commonutil.CommonUtilUUID;
import com.wondersgroup.commonutil.cipher.Cipher;
import com.wondersgroup.partdb.test.po.TestPo;
import com.wondersgroup.partdb.test.service.intf.TestTransactional;

@Service
public class TestTransactionalImpl implements TestTransactional {

	@Autowired CommonDao commonDao;
	
	@Transactional(transactionManager = "dataSourceTransactionManager")
	@Override
	public void TestDoubleTransactional1() {
		TestPo testPo = new TestPo();
		try {
			testPo.setId(CommonUtilUUID.hexToIdbase64(Cipher.MD2.encrypt("123")));
			System.out.println(testPo.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		testPo.setName("测试事务1");
		commonDao.saveObj(testPo);
		
		throw new RuntimeException("测试事务抛异常看看回滚1");
	}
	
	
//	@Transactional(transactionManager = "testDataSourceTransactionManager")
	@Transactional(transactionManager = "dataSourceTransactionManager")
	@Override
	public void TestDoubleTransactional2() {
		TestPo testPo = new TestPo();
		try {
			testPo.setId(CommonUtilUUID.hexToIdbase64(Cipher.MD5.encrypt("123")));
			System.out.println(testPo.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		testPo.setName("测试事务2");
		commonDao.saveObj(testPo);
		
		
		
		commonDao.saveOrUpdateObj(testPo,"testDataSource");
		throw new RuntimeException("测试事务抛异常看看回滚2");
	}
	
	@CommonAop(cuterClass = MultipleManagerTransaction.class)
	@Override
	public void TestDoubleTransactional3(String... dataSrouceBeanNames) {
		TestPo testPo = new TestPo();
		try {
			testPo.setId(CommonUtilUUID.hexToIdbase64(Cipher.MD5.encrypt("123")));
			System.out.println(testPo.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		testPo.setName("测试事务3");
		
		for (String dataSrouceBeanName : dataSrouceBeanNames) {
			commonDao.saveOrUpdateObj(testPo,dataSrouceBeanName);
		}
		
		throw new RuntimeException("测试事务抛异常看看回滚3");
	}


	@CommonAop(cuterClass = MultipleManagerAsyncTransaction.class)
	@Override
	public void TestDoubleTransactional4(Map<String, AsyncTransactionThread> map,String... dataSrouceBeanNames) {
		TestPo testPo = new TestPo();
		try {
			testPo.setId(CommonUtilUUID.hexToIdbase64(Cipher.MD5.encrypt("123")));
			System.out.println(testPo.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		testPo.setName("测试事务4");
		
		//TODO 把map封装成一个多线程事务的传参对象
		Arrays.stream(dataSrouceBeanNames).parallel().forEach(dataSrouceBeanName ->{ 
			map.get(dataSrouceBeanName).offer( () -> {
				return commonDao.saveOrUpdateObj(testPo,dataSrouceBeanName);
			});
		});
		
		//throw new RuntimeException("测试事务抛异常看看回滚4");
	}


}
