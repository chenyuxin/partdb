package com.wondersgroup.test1.testspringboot;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.wondersgroup.commondao.dao.daoutil.springcfg.SpringConfiguration;
import com.wondersgroup.partdb.test.service.intf.TestIocBeanRunTime;
import com.wondersgroup.partdb.test.service.intf.TestTransactional;


@SpringBootTest(classes = {SpringConfiguration.class})
public class JUnitTestIocBean {
	
	@Autowired TestIocBeanRunTime iocBean;
	
	@Autowired TestTransactional testTransactional;
	
	@Test
	public void test1() {
		String a = iocBean.registerBean();
		System.out.println(a);
		
		testTransactional.TestDoubleTransactional2();
	}

}
