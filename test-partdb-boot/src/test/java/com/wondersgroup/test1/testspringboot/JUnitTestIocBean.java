package com.wondersgroup.test1.testspringboot;

import java.util.concurrent.ConcurrentHashMap;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.alibaba.druid.pool.DruidDataSource;
import com.wondersgroup.common.spring.util.DynamicIocUtilComponent;
import com.wondersgroup.common.spring.util.MultipleDataSource;
import com.wondersgroup.commondao.dao.daoutil.DaoUtil;
import com.wondersgroup.commondao.dao.daoutil.springcfg.SpringConfiguration;
import com.wondersgroup.commonutil.type.database.DataBaseType;
import com.wondersgroup.partdb.test.service.intf.TestIocBeanRunTime;
import com.wondersgroup.partdb.test.service.intf.TestTransactional;


@SpringBootTest(classes = {SpringConfiguration.class})
public class JUnitTestIocBean {
	
	@Autowired ApplicationContext applicationContext;
	
	@Autowired DynamicIocUtilComponent dynamicIocUtil;
	
	@Autowired MultipleDataSource multipleDataSource;
	
	@Autowired TestIocBeanRunTime iocBean;
	
	@Autowired TestTransactional testTransactional;
	
	@Test
	public void test1() {
		
		
		DruidDataSource defaultDataSource = null;
        DruidDataSource testDataSource = null;
        try {
        	defaultDataSource = (DruidDataSource) applicationContext.getBean(DaoUtil.defaultDataSourceName);
        	//System.out.println(defaultDataSource.getUrl());
        	testDataSource = defaultDataSource.cloneDruidDataSource();
        	//testDataSource.setUrl(DataBaseType.getCurrentDataBaseType().getJdbcUrl("localhost", 5432, "partdb2"));
        	testDataSource.setUrl(DataBaseType.getCurrentDataBaseType().getJdbcUrl("localhost", 3306, "partdb2"));
            //System.out.println(testDataSource.getUrl());
            testDataSource.init();
        } catch (Exception e) {
        	e.printStackTrace();
		}
        dynamicIocUtil.registerBean("testDataSource", testDataSource);
        
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager(); //定义事务管理TransactionManager
        dataSourceTransactionManager.setDataSource(testDataSource); // 设置数据源
        dynamicIocUtil.registerBean("testDataSourceTransactionManager", dataSourceTransactionManager);
        
		
		
		testTransactional.TestDoubleTransactional3("testDataSource",DaoUtil.defaultDataSourceName);
	}
	
	
	@Test
	public void test2() {
//		multipleDataSource.registerDataSource("testDataSource", "localhost", 5432, "partdb2", DataBaseType.getCurrentDataBaseType());
		multipleDataSource.registerDataSource("testDataSource", "localhost", 3306, "partdb2", DataBaseType.getCurrentDataBaseType());
		
		//testTransactional.TestDoubleTransactional3(null);
		testTransactional.TestDoubleTransactional3("testDataSource",DaoUtil.defaultDataSourceName);
	}
	
	@Test
	public void test3() {
		multipleDataSource.registerDataSource("testDataSource", "localhost", 3306, "partdb2", DataBaseType.getCurrentDataBaseType());
		
		testTransactional.TestDoubleTransactional4(new ConcurrentHashMap<>(), "testDataSource",DaoUtil.defaultDataSourceName);
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


}
