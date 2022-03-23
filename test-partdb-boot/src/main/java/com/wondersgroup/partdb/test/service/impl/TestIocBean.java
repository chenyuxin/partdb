package com.wondersgroup.partdb.test.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.alibaba.druid.pool.DruidDataSource;
import com.wondersgroup.commondao.dao.daoutil.DaoUtil;
import com.wondersgroup.commondao.dao.intf.CommonDao;
import com.wondersgroup.commonutil.type.database.DataBaseType;
import com.wondersgroup.partdb.test.service.intf.TestIocBeanRunTime;

@Service
public class TestIocBean implements TestIocBeanRunTime{
	
	@Autowired ApplicationContext applicationContext;
	
	@Autowired CommonDao commonDao;

	@Override
	public String registerBean() {
//		try {
//            //这里还没有加载到IOC容器中，会抛异常
//			DataSource test1 = (DataSource) applicationContext.getBean("testDataSource");
//            System.out.println(test1);
//        }catch (Exception e) {
//            System.out.println("testDataSource___________未找到！！");
//        }
		
		//将applicationContext转换为ConfigurableApplicationContext
        ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) applicationContext;
        // 获取bean工厂并转换为DefaultListableBeanFactory
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) configurableApplicationContext.getBeanFactory();
        
        DruidDataSource defaultDataSource = null;
        DruidDataSource testDataSource = null;
        try {
        	defaultDataSource = (DruidDataSource) applicationContext.getBean(DaoUtil.defaultDataSourceName);
        	//System.out.println(defaultDataSource.getUrl());
        	testDataSource = defaultDataSource.cloneDruidDataSource();
        	testDataSource.setUrl(DataBaseType.getCurrentDataBaseType().getJdbcUrl("localhost", 5432, "partdb2"));
            //System.out.println(testDataSource.getUrl());
            testDataSource.init();
        } catch (Exception e) {
        	e.printStackTrace();
		}
        defaultListableBeanFactory.registerSingleton("testDataSource", testDataSource);
        
        
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager(); //定义一个某个框架平台的TransactionManager，如JDBC、Hibernate
        dataSourceTransactionManager.setDataSource(testDataSource); // 设置数据源
        
        
        DefaultTransactionDefinition transDef = new DefaultTransactionDefinition(); // 定义事务属性
        transDef.setPropagationBehavior(DefaultTransactionDefinition.PROPAGATION_REQUIRED); // 设置传播行为属性
        TransactionStatus status = dataSourceTransactionManager.getTransaction(transDef); // 获得事务状态
        try {
            // 数据库操作
            dataSourceTransactionManager.commit(status);// 提交
        } catch (Exception e) {
            dataSourceTransactionManager.rollback(status);// 回滚
        }
        
//		DruidDataSource test2 = (DruidDataSource) applicationContext.getBean("testDataSource");
//        System.out.println("test2:" + test2);
//        System.out.println(test2.getUrl());
//        
//        String aString1 = commonDao.selectBaseObj("select id from test1", String.class);
//        System.out.println("[1]select id:"+aString1);
//        String aString2 = commonDao.selectBaseObj("select id from test1", String.class, "testDataSource");
//        System.out.println("[2]select id:"+aString2);
//        
//        
//        List<String> name = new ArrayList<String>();
//        name.add("name");
//        PageBean<Map<String, Object>> pageBeans = commonDao.selectObjMap(1, 1,name, TableType.getTableType("test1"));
//        String name1 = (String) pageBeans.getRows().get(0).get("name");
//        System.out.println("[1]select name:"+name1);
//        PageBean<Map<String, Object>> pageBeans2 = commonDao.selectObjMap(1, 1,name, TableType.getTableType("test1"),"testDataSource");
//        String name2 = (String) pageBeans2.getRows().get(0).get("name");
//        System.out.println("[1]select name:"+name2);
        
        return "registerBean___________testDataSource";
        
	}
	
	

}
