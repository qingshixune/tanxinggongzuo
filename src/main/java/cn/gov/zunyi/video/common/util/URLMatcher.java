package cn.gov.zunyi.video.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class URLMatcher {

	public static String matchHref(String data) {
		Pattern pattern = Pattern.compile("href=\"([^\"]*)\"");
		// 空格结束
		Matcher matcher = pattern.matcher(data);
		while (matcher.find()) {
			return matcher.group(0).substring(6, matcher.group(0).length() - 1);
		}
		return null;
	}

	public static String matchRtmp(String data) {
		Pattern pattern = Pattern.compile("\"Url\":\"([^\"]*)\"");
		// 空格结束
		Matcher matcher = pattern.matcher(data);
		int i = 0;
		String result = null;
		while (matcher.find()) {
			i++;
			String group = matcher.group(1);
			if (i == 2) {
				result = group;
			}
		}
		return result;
	}

}