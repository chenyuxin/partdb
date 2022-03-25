package com.wondersgroup.partdb.viewcontroller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AdminController {
	
	
	@RequestMapping("/showparts")
	public String showParts() {
		System.out.println(1);
		return "";
	}

}
