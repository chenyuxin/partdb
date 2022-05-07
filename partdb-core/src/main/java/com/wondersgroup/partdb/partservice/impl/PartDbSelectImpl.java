package com.wondersgroup.partdb.partservice.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wondersgroup.common.spring.aop.CommonAop;
import com.wondersgroup.common.spring.transaction.MultipleManagerAsyncTransaction;
import com.wondersgroup.common.spring.util.container.TotalTransactionManager;
import com.wondersgroup.common.spring.util.feature.CommonTransactionFeature.ReadOnly;
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
	
	@CommonAop(cuterClass = MultipleManagerAsyncTransaction.class,commonAopFeatures = {ReadOnly.class})
	@Override
	public PartDbExeResult<List<Map<String,Object>>> execute(String sql, TotalTransactionManager totalTransactionManager, PartDbFeature... partDbFeature) {
		
		List<Map<String,Object>> r = new CopyOnWriteArrayList<Map<String,Object>>(); 
		
		Object msg = totalTransactionManager.execute(dataSrouceName -> {
			List<Map<String,Object>> r1  = commonDao.selectObjMap(sql,dataSrouceName,DaoEnumOptions.RuntimeException);
			//log.debug("执行第一次：" + r1.toString());
			r.addAll(r1);
			return r1;
		});
		
		
		
		PartDbFeature feature = partDbFeature[0];
		PartDbExeResult<List<Map<String,Object>>> partDbExeResult = new PartDbExeResult<>();
		if (null != feature) {
			Stream<Map<String, Object>> rStream = r.parallelStream();
			List<Map<String,Object>> rt;
			if (feature.getGroupBy() != null) {
				rt = rStream.collect(Collectors.groupingBy(e -> e.get(feature.getGroupBy()))).values().stream().map(d -> {
					Map<String, Object> sampleData = d.get(0);
					sampleData.put(feature.getCount(), d.stream().mapToLong(e -> Long.valueOf(e.get(feature.getCount()).toString())).sum());
					return sampleData;
				}).collect(Collectors.toList());
			} else {
				rt = new ArrayList<Map<String,Object>>();
				long convergenceNum = rStream.mapToLong(e -> Long.valueOf(e.get(feature.getCount()).toString()) ).sum();
				rt.add(r.get(0));
				rt.get(0).put(feature.getCount(), convergenceNum);
				partDbExeResult.setResultDatas(rt);
			}
		} else {
			partDbExeResult.setResultDatas(r);
		}
		partDbExeResult.setReason(CommonUtilString.subString(msg, 0, 4000));
		log.debug("查询结果："+ CommonUtilString.parseString(partDbExeResult.getResultDatas()) );
		return partDbExeResult;
		
	}

}


