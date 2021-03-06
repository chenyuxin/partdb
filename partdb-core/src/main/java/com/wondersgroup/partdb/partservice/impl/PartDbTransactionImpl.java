package com.wondersgroup.partdb.partservice.impl;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wondersgroup.common.spring.aop.CommonAop;
import com.wondersgroup.common.spring.transaction.MultipleManagerAsyncTransaction;
import com.wondersgroup.common.spring.util.container.TotalTransactionManager;
import com.wondersgroup.commondao.dao.daoutil.DaoEnumOptions;
import com.wondersgroup.commondao.dao.intf.CommonDao;
import com.wondersgroup.commonutil.CommonUtilString;
import com.wondersgroup.partdb.common.po.exepo.PartDbExeResult;
import com.wondersgroup.partdb.common.po.exepo.PartDbFeature;
import com.wondersgroup.partdb.common.util.PartDBConst;
import com.wondersgroup.partdb.partservice.intf.PartDbTransaction;

@Service(PartDBConst.updatePartDataBase)
public class PartDbTransactionImpl implements PartDbTransaction {
	
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(PartDbTransactionImpl.class);

	@Autowired CommonDao commonDao;
	
	@CommonAop(cuterClass = MultipleManagerAsyncTransaction.class)
	@Override
	public PartDbExeResult<Set<String>> execute(String sql, TotalTransactionManager totalTransactionManager, PartDbFeature... partDbFeature) {
		
		//List<String> r = new CopyOnWriteArrayList<String>(); 
		Map<String, String> r = new ConcurrentHashMap<String,String>();
		
		Object msg = totalTransactionManager.execute(dataSrouceName -> {
			String r1  = commonDao.useTable(sql,dataSrouceName,DaoEnumOptions.RuntimeException);
			//log.debug("执行第一次：" + r1.toString());
			r.put(r1, r1);
			return r1;
		});
	
		PartDbExeResult<Set<String>> partDbExeResult = new PartDbExeResult<>();
		partDbExeResult.setResultDatas(r.keySet());
		partDbExeResult.setReason(CommonUtilString.subString(msg, 0, 4000));
		log.debug("执行变更结果："+ partDbExeResult.getResultDatas().toString());
		return partDbExeResult;
		
	}

}
