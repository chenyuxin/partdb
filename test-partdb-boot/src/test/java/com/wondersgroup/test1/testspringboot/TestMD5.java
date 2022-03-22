package com.wondersgroup.test1.testspringboot;

import org.junit.jupiter.api.Test;

import com.wondersgroup.commonutil.CommonUtilUUID;
import com.wondersgroup.commonutil.cipher.Cipher;
import com.wondersgroup.commonutil.constant.StringPool;

public class TestMD5 {
	
	private static final String md5TestString = "中国@Ab123";
	
	@Test
	public void test1() {
		String md5 = StringPool.BLANK;
		try {
			md5 = Cipher.MD5.encrypt(md5TestString);
			System.out.println("md5 : " + md5);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String uuid = CommonUtilUUID.getUUIDC(md5TestString);
		System.out.println("uuid: " + uuid);
		
		
		String md5BaseId64  = CommonUtilUUID.hexToIdbase64(md5);
		System.out.println("md5 : " + md5BaseId64);
		String uuidBaseId64  = CommonUtilUUID.hexToIdbase64(uuid);
		System.out.println("uuid: " + uuidBaseId64);
		
		
		
		String md5Convert = CommonUtilUUID.idbase64ToHex(md5BaseId64);
		System.out.println(md5Convert);
	}

}
