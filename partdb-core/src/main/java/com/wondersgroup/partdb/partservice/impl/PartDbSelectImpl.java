package com.wondersgroup.partdb.partservice.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wondersgroup.common.spring.aop.CommonAop;
import com.wondersgroup.common.spring.transaction.MultipleManagerAsyncTransaction;
import com.wondersgroup.common.spring.util.container.TotalTransactionManager;
import com.wondersgroup.common.spring.util.feature.CommonTransactionFeature;
import com.wondersgroup.commondao.dao.daoutil.DaoEnumOptions;
import com.wondersgroup.commondao.dao.intf.CommonDao;
import com.wondersgroup.commonutil.CommonUtilString;
import com.wondersgroup.partdb.common.po.exepo.PartDbExeResult;
import com.wondersgroup.partdb.common.po.exepo.PartDbFeature;
import com.wondersgroup.partdb.common.util.PartDBConst;
import com.wondersgroup.partdb.partservice.intf.PartDbTransaction;

@Service(PartDBConst.selectPartDataBase)
public class PartDbSelectImpl implements PartDbTransaction {

	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(PartDbSelectImpl.class);

	@Autowired CommonDao commonDao;
	
	@CommonAop(cuterClass = MultipleManagerAsyncTransaction.class,commonAopFeatures = {CommonTransactionFeature.ReadOnly.class})
	@Override
	public PartDbExeResult<List<Map<String,Object>>> execute(String sql, TotalTransactionManager totalTransactionManager, PartDbFeature... partDbFeature) {
		
		List<Map<String,Object>> r = new CopyOnWriteArrayList<Map<String,Object>>(); 
		
		Object msg = totalTransactionManager.execute(dataSrouceName -> {
			List<Map<String,Object>> r1  = commonDao.selectObjMap(sql,dataSrouceName,DaoEnumOptions.RuntimeException);
			//log.debug("执行第一次：" + r1.toString());
			r.addAll(r1);
			return r1;
		});
	
		PartDbExeResult<List<Map<String,Object>>> partDbExeResult = new PartDbExeResult<>();
		partDbExeResult.setResultDatas(r);
		partDbExeResult.setReason(CommonUtilString.subString(msg, 0, 4000));
		log.debug("查询结果："+ partDbExeResult.getResultDatas().toString());
		return partDbExeResult;
		
	}

}


