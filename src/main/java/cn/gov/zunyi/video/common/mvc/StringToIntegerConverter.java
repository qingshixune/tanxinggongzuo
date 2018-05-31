package cn.gov.zunyi.video.common.mvc;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;

public class StringToIntegerConverter implements Converter<String, Integer> {

	/**
	 * @see Converter#convert(Object)
	 */
	@Override
	public Integer convert(String source) {
		if (StringUtils.isBlank(source)) {
			return null;
		}
		try {
			source = source.trim();
			return Integer.parseInt(source);
		} catch (Exception e) {
			throw new RuntimeException(String.format("parser %s to Date fail", source));
		}
	}

}