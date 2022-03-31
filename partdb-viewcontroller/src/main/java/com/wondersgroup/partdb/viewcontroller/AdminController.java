package com.wondersgroup.partdb.viewcontroller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wondersgroup.commonutil.CommonUtilMap;
import com.wondersgroup.partdb.common.po.exepo.PartDbExeResult;
import com.wondersgroup.partdb.service.intf.ExecuteSqlService;

@Controller
public class AdminController {
	
	@SuppressWarnings("unused")
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AdminController.class);
	
	@Autowired ExecuteSqlService executeSql;
	
	@RequestMapping("/showparts")
	public String showParts() {
		return "";
	}
	
	@RequestMapping("/sql")
	public String sql() {
		return "index";
	}
	
	
	@RequestMapping("/executesql")
	@ResponseBody
	public PartDbExeResult<?>  executesql(@RequestBody Map<String, Object> map) {
		String sql = CommonUtilMap.getValueOfMap(String.class, "sql", map);
		
		return executeSql.executeSql(sql);
	}

}
