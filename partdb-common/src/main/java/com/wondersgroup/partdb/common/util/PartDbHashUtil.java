package com.wondersgroup.partdb.common.util;

import java.util.HashMap;
import java.util.Map;

/**
 * hash分表分库计算<br>
 * 此算法可以满足分片扩容<br>
 * 扩容规则 分片1分一半数据 到 新的第一个分片 一直到最后一个分片。<br>
 * 一半数据的分法：当前分片包含的base64哈希值，使用到第几位开始计算，保留上半区，转移下半区。
 * @author chenyuxin
 */
public class PartDbHashUtil {
	
	/**
	 * 通过主键的自定base64值确定所在分片
	 * @param primaryKeyHash 主键的自定base64值
	 * @param partdbs 当前的全部分片 [partdb数量必须是2的n次方，也就是每次扩容需要扩大一倍]
	 * @return 分片索引数值,确定数据库分片
	 */
	public static int hashDb(String primaryKeyHash, String[] partdbs) {
		int parts = partdbs.length;
		int p = parts >> 6;//除以64
		if( p > 1 ) {//分库超过64个的情况
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
				for(int j=0;j<i;j++) {
					p1 = p1 << 6; // 乘以64，进位
				}
				pr += p1;
			}
			String hashString = HASH_STRINGS.get(part2);
			char c2 = primaryKeyHash.charAt(carrybit);
			int i2 = hashString.indexOf(c2);
			int p2 = i2 & (part2-1);
			int pn = 0;
			for(int j=0;j<carrybit;j++) {
				pn += p2 << 6; // 乘以64，进位
			}
			return pr + pn;//前面的位数 + 最后一位
		} else {//分库不足或等于64个的情况
			String hashString = HASH_STRINGS.get(parts);//part数
			char c = primaryKeyHash.charAt(0);
			int i = hashString.indexOf(c);
			return i & (parts-1);
		}
		
	}
	
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

}


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
