package com.wondersgroup.partdb.partservice.impl;

import org.springframework.stereotype.Service;

import com.wondersgroup.common.spring.aop.CommonAop;
import com.wondersgroup.common.spring.transaction.MultipleManagerAsyncTransaction;
import com.wondersgroup.partdb.common.po.exepo.PartDbExeResult;
import com.wondersgroup.partdb.common.po.exepo.PartDbFeature;
import com.wondersgroup.partdb.partservice.intf.PartDbTransaction;

@Service
public class PartDbTransactionImpl implements PartDbTransaction {

	
	@CommonAop(cuterClass = MultipleManagerAsyncTransaction.class)
	@Override
	public <T> PartDbExeResult<T> execute(String sql, PartDbFeature... partDbFeature) {
		
		
		return null;
	}

}
