package cn.gov.zunyi.video.common.util;

import java.util.HashMap;
import java.util.Map;


/**
 * 国籍通用工具类
 * @author lmh
 *
 */
public class NationUtil {
	
	@SuppressWarnings("unchecked")
	public static  Map<String,String> nationMap(){
		Map<String, String> map=new HashMap<String, String>();
			map.put("0", "中国");
			map.put("1", "中国香港");
			map.put("2", "中国澳门");
			map.put("3", "中华台北");
			map.put("4", "美国");
			map.put("5", "澳大利亚");
			map.put("6", "日本");
			map.put("7", "阿根廷");
			map.put("8", "俄罗斯");
			map.put("9", "奥地利");
			map.put("10", "巴西");
			map.put("11", "比利时");
			map.put("12", "保加利亚");
			map.put("13", "加拿大");
			map.put("14", "法国");
			map.put("15", "德国");
			map.put("16", "英国");
			map.put("17", "印度");
			map.put("18", "以色列");
			map.put("19", "意大利");
			map.put("20", "蒙古");
			map.put("21", "白俄罗斯");
			map.put("22", "墨西哥");
			map.put("23", "荷兰");
			map.put("24", "新西兰");
			map.put("25", "朝鲜");
			map.put("26", "其他");
		
		return map;
	}
	
	//取国际key
	@SuppressWarnings("unused")
	public static String getNationByKey(String key){
		Map<String,String> nationMap=nationMap();
		if(key!=null || key!=""){
			return nationMap.get(key);
		} else {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public static  Map<String,String> nationValue(){
		Map<String, String> map=new HashMap<String, String>();
			map.put("中国","0");
			map.put("中国香港","1");
			map.put("中国澳门","2");
			map.put("中华台北","3");
			map.put("美国","4");
			map.put("澳大利亚","5");
			map.put("日本","6");
			map.put("阿根廷","7");
			map.put("俄罗斯","8");
			map.put("奥地利","9");
			map.put("巴西","10");
			map.put("比利时","11");
			map.put("保加利亚","12");
			map.put("加拿大","13");
			map.put("法国","14");
			map.put("德国","15");
			map.put("英国","16");
			map.put("印度","17");
			map.put("以色列","18");
			map.put("意大利","19");
			map.put("蒙古","20");
			map.put("白俄罗斯","21");
			map.put("墨西哥","22");
			map.put("荷兰","23");
			map.put("新西兰","24");
			map.put("朝鲜","25");
			map.put("其他","26");
		
		return map;
	}
	
	//取国际value
		@SuppressWarnings("unused")
		public static String getNationByValue(String key){
			Map<String,String> nationValue=nationValue();
			if(key!=null || key!=""){
				return nationValue.get(key);
			} else {
				return null;
			}
		}
	
	

}
