package com.wondersgroup.partdb.viewcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wondersgroup.partdb.service.intf.ExecuteSqlService;

@Controller
public class AdminController {
	
	@Autowired ExecuteSqlService executeSql;
	
	@RequestMapping("/showparts")
	public String showParts() {
		System.out.println(1);
		return "";
	}
	
	@RequestMapping("/select")
	public String select(String sql) {
		executeSql.executeSql(sql);
		return "";
	}

}
