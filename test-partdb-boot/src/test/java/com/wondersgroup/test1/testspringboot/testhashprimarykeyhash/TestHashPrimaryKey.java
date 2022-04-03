package com.wondersgroup.test1.testspringboot.testhashprimarykeyhash;


import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.wondersgroup.commonutil.constant.IdCodeConst;


public class TestHashPrimaryKey {
	
	public static final String[] partdbs = {"partdb1","partdb2"};
	
	@Test
	public void test1() {
		String[] testPartdbs = new String[8192];//测试分库数量
		//String primaryKeyHash = BaseUtil.getUUIDC64("主键字段1");
		String primaryKeyHash = "Rvdcba";
		System.out.println(primaryKeyHash);
		int i = hashDb(primaryKeyHash, testPartdbs);
		System.out.println("分片号:" + i);
	}
	
	
	
	
	
	public static int hashDb(String primaryKeyHash, String[] partdbs) {
		int parts = partdbs.length;
		
		int p = parts >> 6;//除以64
		if( p==0 || p==1 ) {//分库不足或等于64个的情况
			String hashString = HASH_STRINGS.get(parts);//part数
			System.out.println("分片数："+ parts + ", 取用hash字符串：" + hashString);
			char c = primaryKeyHash.charAt(0);
			int i = hashString.indexOf(c);
			return i & (parts-1);
		} else {
			int carrybit = 0;//分片数大于64,进位判断分片
			int part2;
			do {
				carrybit++;//首次+1次,因为开始已经被64除过了
				part2 = p;//记录最后一次能够除64的余数
				p = p >> 6;//除以64
			} while (p > 1); //64进制，还能进位的情况继续计数
			
			int pr = 0;
			for(int i=0;i<carrybit;i++) {
				char c = primaryKeyHash.charAt(i);
				int ir = n1.indexOf(c);
				int p1 = ir & 63;
				System.out.println("索引"+ i +":" + p1 + ", 取用hash字符串：" + n1);
				System.out.print("分片号"+ i + ":" + p1);
				System.out.print("，序号" + ir + ": ");
				System.out.println(n1.charAt(ir)); 
				for(int j=0;j<i;j++) {
					p1 = p1 << 6; // 乘以64，进位
				}
				pr += p1;
			}
			
			String hashString = HASH_STRINGS.get(part2);
			char c2 = primaryKeyHash.charAt(carrybit);
			int i2 = hashString.indexOf(c2);
			
			System.out.println("分片数："+ parts + ", 取用hash字符串：" + hashString);
			int p2 = i2 & (part2-1);
			System.out.print("分片号e：" + p2);
			System.out.print("，序号" + i2 + ": ");
			System.out.println(hashString.charAt(i2)); 
			
			int pn = 0;
			for(int j=0;j<carrybit;j++) {
				pn += (i2 & (part2-1)) << 6; // 乘以64，进位
			}
			
			return pr + pn;//前面的位数 + 最后一位
			
		}
		
	}
	
	
	
	
	/**
	 * 
	 * @param primaryKeyHash
	 * @param partdbs
	 * @return
	public static int oldHashDb(String primaryKeyHash, String[] partdbs) {
		int parts = partdbs.length;
		
		if( parts < 65 ) {
			String hashString = HASH_STRINGS.get(parts);//part数
			System.out.println("分片数："+ parts + ", 取用hash字符串：" + hashString);
			char c = primaryKeyHash.charAt(0);
			int i = hashString.indexOf(c);
			return i & (parts-1);
		} else if (parts < 4097) {
			int part2 = parts >> 6;//除以64
			
			char c1 = primaryKeyHash.charAt(0);
			int i1 = n1.indexOf(c1);
			
			int p1 = i1 & 63;
			System.out.println("索引1："+ p1 + ", 取用hash字符串：" + n1);
			System.out.print("分片号1：" + p1);
			System.out.print("，序号" + i1 + ": ");
			System.out.println(n1.charAt(i1)); 
			
			String hashString = HASH_STRINGS.get(part2);
			
			
			char c2 = primaryKeyHash.charAt(1);
			int i2 = hashString.indexOf(c2);
			
			System.out.println("分片数："+ parts + ", 取用hash字符串：" + hashString);
			int p2 = i2 & (part2-1);
			System.out.print("分片号2：" + p2);
			System.out.print("，序号" + i2 + ": ");
			System.out.println(hashString.charAt(i2)); 
			
			return p1 + (i2 & (part2-1))*64;
			
		}
		
		return -1;
	}
	 */
	
	
	
	
	
	/**
		@Test
		public void test2() {//生成规则
			//char[] cs = new char[64];
			int parts = 64;//分片数 【变量： 2,4,8,16,32,64】
			int nodes = 64/parts;//每片节点数
			Map<Integer,Character> map = new HashMap<>();
			for (int i=0;i<64;) {
				for (int db=0;db<parts;db++) {
					//char[] row = new char[64/partdbs.length];
					for (int j=0;j<nodes;j++,i++) {
						char c = IdCodeConst.idBase64.charAt(i);
						System.out.println( "i:" +i/nodes + ", j:" +j +",char:" + c);
						System.out.println("index:" + (j*100 + i/nodes) );
						int sort = Integer.valueOf(j*100 + i/nodes);
						map.put(sort, c);
					}
				}
			}
			map.keySet().stream().sorted((key1,key2) -> {return key1-key2;}).forEachOrdered(sort -> {
				//System.out.println(sort);
				System.out.print(map.get(sort));
			});
			
		}
	 */
	public static final String n32	=	"0w1x2y3z4A5B6C7D8E9FaGbHcIdJeKfLgMhNiOjPkQlRmSnToUpVqWrXsYtZu-v_";//part2
	public static final String n16	=	"0gwM1hxN2iyO3jzP4kAQ5lBR6mCS7nDT8oEU9pFVaqGWbrHXcsIYdtJZeuK-fvL_";//part4
	public static final String n8	=	"08gowEMU19hpxFNV2aiqyGOW3bjrzHPX4cksAIQY5dltBJRZ6emuCKS-7fnvDLT_";//part8
	public static final String n4	= 	"048cgkoswAEIMQUY159dhlptxBFJNRVZ26aeimquyCGKOSW-37bfjnrvzDHLPTX_";//part16
	public static final String n2	= 	"02468acegikmoqsuwyACEGIKMOQSUWY-13579bdfhjlnprtvxzBDFHJLNPRTVXZ_";//part32
	public static final String n1   = 	"0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-_";//part64
	public static final Map<Integer,String> HASH_STRINGS = new HashMap<Integer,String>();
	static {
		HASH_STRINGS.put(2, n32);
		HASH_STRINGS.put(4, n16);
		HASH_STRINGS.put(8, n8);
		HASH_STRINGS.put(16, n4);
		HASH_STRINGS.put(32, n2);
		HASH_STRINGS.put(64, n1);
	}
	
	@Test
	public void test2() {
		//char[] cs = new char[64];
		int parts = 64;//分片数
		int nodes = 64/parts;//每片节点数
		Map<Integer,Character> map = new HashMap<>();
		for (int i=0;i<64;) {
			for (int db=0;db<parts;db++) {
				//char[] row = new char[64/partdbs.length];
				for (int j=0;j<nodes;j++,i++) {
					char c = IdCodeConst.idBase64.charAt(i);
					System.out.println( "i:" +i/nodes + ", j:" +j +",char:" + c);
					System.out.println("index:" + (j*100 + i/nodes) );
					int sort = Integer.valueOf(j*100 + i/nodes);
					map.put(sort, c);
				}
			}
		}
		map.keySet().stream().sorted((key1,key2) -> {return key1-key2;}).forEachOrdered(sort -> {
			//System.out.println(sort);
			System.out.print(map.get(sort));
		});
		
	}
	
	public static final String[] partdbs4 = {"partdb1","partdb2","partdb3","partdb4"};
	
	@Test
	public void test3() {
		for (int i=0;i<64;i++) {
			System.out.print("序号" + i + "，分片: ");
			System.out.print( i & (partdbs4.length-1) );
			System.out.println(" char:" + n16.charAt(i));
		}
	}
	
	@Test
	public void test4() {
		for (int i=0;i<64;i++) {
			System.out.print("序号" + i + "，分片: ");
			System.out.print( i & (partdbs.length-1) );
			System.out.println(" char:" +  n32.charAt(i));
		}
	}

}
