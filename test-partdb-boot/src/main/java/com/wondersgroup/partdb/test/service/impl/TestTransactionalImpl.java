package com.wondersgroup.partdb.test.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wondersgroup.commondao.dao.intf.CommonDao;
import com.wondersgroup.commonutil.CommonUtilUUID;
import com.wondersgroup.commonutil.cipher.Cipher;
import com.wondersgroup.partdb.test.po.TestPo;
import com.wondersgroup.partdb.test.service.intf.TestTransactional;

@Service
public class TestTransactionalImpl implements TestTransactional {

	@Autowired CommonDao commonDao;
	
	@Transactional
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
	@Transactional(transactionManager = "TransactionManager")
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


}
