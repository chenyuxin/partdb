package com.wondersgroup.partdb.viewcontroller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.alibaba.druid.sql.parser.Token;
import com.wondersgroup.commonutil.CommonUtilMap;
import com.wondersgroup.partdb.common.po.exepo.PartDbExeResult;
import com.wondersgroup.partdb.service.intf.ExecuteSqlService;

@Controller
public class AdminController {
	
	@SuppressWarnings("unused")
	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AdminController.class);
	
	@Autowired ApplicationContext applicationContext;
	
	
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
		//新建sql分析
		SQLStatementParser parser = new SQLStatementParser(sql);
		// 使用Parser解析生成AST，这里SQLStatement就是AST
		Token token = parser.getExprParser().getLexer().token();
		
		ExecuteSqlService executeSql = (ExecuteSqlService) applicationContext.getBean(token.name);
		
		return executeSql.executeSql(sql,parser);
	}

}
