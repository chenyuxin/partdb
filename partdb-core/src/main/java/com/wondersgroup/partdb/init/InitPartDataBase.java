package com.wondersgroup.partdb.init;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wondersgroup.common.spring.util.MultipleDataSource;
import com.wondersgroup.commondao.dao.intf.CommonDao;
import com.wondersgroup.commonutil.type.database.DataBaseType;
import com.wondersgroup.partdb.common.po.PartDataBaseConfig;
import com.wondersgroup.partdb.common.util.PartDBConst;

@Component
public class InitPartDataBase implements InitializingBean  {
	
	@Autowired CommonDao commonDao;
	
	@Autowired MultipleDataSource multipleDataSource;
	
	

	@Override
	public void afterPropertiesSet() throws Exception {
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("use_config", true);
		List<PartDataBaseConfig> partDataBaseConfigs = commonDao.selectObj(PartDataBaseConfig.class,paramMap);
		
		PartDBConst.partdbs = new String[partDataBaseConfigs.size()];
		
		for (int i = 0;i<partDataBaseConfigs.size();i++) {
			PartDataBaseConfig partDataBaseConfig = partDataBaseConfigs.get(i);
			multipleDataSource.registerDataSource(partDataBaseConfig.getPartName(), partDataBaseConfig.getIp(), partDataBaseConfig.getPort(), partDataBaseConfig.getPartName(), DataBaseType.getCurrentDataBaseType());
			PartDBConst.partdbs[i] = partDataBaseConfig.getPartName();
			System.out.println(partDataBaseConfig.getPartName());
		}
		
	}

}
