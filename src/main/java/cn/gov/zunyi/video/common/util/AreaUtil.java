package cn.gov.zunyi.video.common.util;

import java.util.HashMap;
import java.util.Map;


/**
 * 地域通用工具类
 * @author lmh
 *
 */
public class AreaUtil {
	
	@SuppressWarnings("unchecked")
	public static  Map<String,String> areaMap(){
		Map<String, String> map=new HashMap<String, String>();
			map.put("110000", "北京市");
			map.put("120000", "天津市");
			map.put("130000", "河北省");
			map.put("140000", "山西省");
			map.put("150000", "内蒙古自治区");
			map.put("210000", "辽宁省");
			map.put("220000", "吉林省");
			map.put("230000", "黑龙江省");
			map.put("310000", "上海市");
			map.put("320000", "江苏省");
			map.put("330000", "浙江省");
			map.put("340000", "安徽省");
			map.put("350000", "福建省");
			map.put("360000", "江西省");
			map.put("370000", "山东省");
			map.put("410000", "河南省");
			map.put("420000", "湖北省");
			map.put("430000", "湖南省");
			map.put("440000", "广东省");
			map.put("450000", "广西壮族自治区");
			map.put("460000", "海南省");
			map.put("500000", "重庆市");
			map.put("510000", "四川省");
			map.put("520000", "贵州省");
			map.put("530000", "云南省");
			map.put("540000", "西藏自治区");
			map.put("610000", "陕西省");
			map.put("620000", "甘肃省");
			map.put("630000", "青海省");
			map.put("640000", "宁夏回族自治区");
			map.put("650000", "新疆维吾尔自治区");
			map.put("710000", "台湾省");
			map.put("810000", "香港特别行政区");
			map.put("820000", "澳门特别行政区");
		
		return map;
	}
	
	//取国际key
	@SuppressWarnings("unused")
	public static String getAreaByKey(String key){
		Map<String,String> areaMap=areaMap();
		if(key!=null || key!=""){
			return areaMap.get(key);
		} else {
			return null;
		}
	}
	
	
	

}
