package com.wondersgroup.test.testspringboot.rest;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wondersgroup.commondao.dao.intf.CommonDao;
import com.wondersgroup.commonutil.baseutil.BASE64Util;
import com.wondersgroup.commonutil.cipher.Cipher;

@RestController
public class TestRestController {
	
	@Autowired CommonDao commonDao;
	
	@RequestMapping(value = "info/{action}",
			method= {RequestMethod.POST,RequestMethod.GET},
			produces="application/json;charset=UTF-8")
	public String info(@RequestBody String body,@RequestHeader Map<String,String> header,
			@RequestParam Map<String, String> getParam,@PathVariable String action ){
		//List<Map<String, Object>> aString = commonDao.selectObjMap("select xm from brmp_grxx","dataSource");
		Date date = commonDao.selectBaseObj("select sysdate from dual", Date.class,"dataSource");
		return date.toString();

	}
	
	@RequestMapping("/testR3")
	public String test3() {
		String sqlString = "select a.kz_renwuzhaopian as \"a\" from md_9316af152beb4483bbcd a where a.certificate_holder='孙丽婷'";
		List<Map<String,Object>> kz_renwuzhaopian = commonDao.selectObjMap(sqlString);
		byte[] b =  (byte[]) kz_renwuzhaopian.get(0).get("a");
		String zpString = Base64Utils.encodeToString(b);
		String zpString2 = BASE64Util.encryptBASE64(b);
		if (zpString.equals(zpString2)) {
			System.out.println("=");
			//System.out.println(zpString);
		}
		try {
			System.out.println(Cipher.MD5.encrypt(zpString));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		byte[] b2 = commonDao.selectBaseObj(sqlString, byte[].class);
		String zp2String = Base64Utils.encodeToString(b2);
		String zp2String2 = BASE64Util.encryptBASE64(b2);
		if (zp2String.equals(zpString2)) {
			System.out.println("=2");
			//System.out.println(zp2String);
		}
		if (zp2String2.equals(zpString2)) {
			System.out.println("=3");
			//System.out.println(zp2String2);
		}
		try {
			System.out.println(Cipher.MD5.encrypt(zp2String2));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return zpString2 + "<br>" + zp2String2;
	}


}
