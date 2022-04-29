package com.wondersgroup.partdb.test.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wondersgroup.common.spring.aop.CommonAop;
import com.wondersgroup.common.spring.transaction.MultipleManagerAsyncTransaction;
import com.wondersgroup.common.spring.util.container.TotalTransactionManager;
import com.wondersgroup.commondao.dao.daoutil.DaoEnumOptions;
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
	
	
	@Transactional(transactionManager = "testDataSourceTransactionManager")
//	@Transactional(transactionManager = "dataSourceTransactionManager")
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
		
		commonDao.saveOrUpdateObj(testPo,"testDataSource");//@Transactional只能管理单个事务,此处执行在抛异常后不会回滚
		throw new RuntimeException("测试事务抛异常看看回滚2");
	}
	

	@CommonAop(cuterClass = MultipleManagerAsyncTransaction.class)
	@Override
	public void TestDoubleTransactional4(TotalTransactionManager totalTransactionManager) {
		TestPo testPo = new TestPo();
		try {
			testPo.setId(CommonUtilUUID.hexToIdbase64(Cipher.MD5.encrypt("123")));
		} catch (Exception e) {
			e.printStackTrace();
		}
		testPo.setName("测试事务4");
		
		//Lamda表达式封装成这样的固定格式
		totalTransactionManager.execute(dataSourceBeanName -> {
			
			String r = commonDao.saveOrUpdateObj(testPo,dataSourceBeanName,DaoEnumOptions.RuntimeException);
//			String r = commonDao.saveObj(testPo,dataSourceBeanName,DaoEnumOptions.RuntimeException);
			
//			if ("testDataSource".equals(dataSourceBeanName)) {
//				throw new RuntimeException("测试单个数据源抛异常 testDataSource 总事务的回滚情况");
//			}
			
			return r;
			
		});
		
		try {
			Thread.sleep(13000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		testPo.setId(CommonUtilUUID.getUUID64());
		
		totalTransactionManager.execute(dataSourceBeanName -> {
			
//			String r = commonDao.saveOrUpdateObj(testPo,dataSourceBeanName,DaoEnumOptions.RuntimeException);
			String r = commonDao.saveObj(testPo,dataSourceBeanName,DaoEnumOptions.RuntimeException);
			
			try {
				Thread.sleep(15000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
//			if ("testDataSource".equals(dataSourceBeanName)) {
//				throw new RuntimeException("测试单个数据源抛异常 testDataSource 总事务的回滚情况");
//			}
			
			return r;
			
		});
		
		throw new RuntimeException("测试事务抛异常看看回滚4");
	}


}
