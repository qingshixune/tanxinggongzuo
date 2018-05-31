package cn.gov.zunyi.video.conf;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@Configuration
@ConditionalOnClass({ JSON.class })
public class FastJsonHttpMessageConvertersConfig {

	@Configuration
	@ConditionalOnClass({ FastJsonHttpMessageConverter.class })
	@ConditionalOnProperty(name = {
			"spring.http.converters.preferred-json-mapper" }, havingValue = "fastjson", matchIfMissing = true)
	protected static class FastJson2HttpMessageConverterConfiguration {
		protected FastJson2HttpMessageConverterConfiguration() {
		}

		@Bean
		@ConditionalOnMissingBean({ FastJsonHttpMessageConverter.class })
		public FastJsonHttpMessageConverter fastJsonHttpMessageConverter() {
			FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();

			FastJsonConfig fastJsonConfig = new FastJsonConfig();
			fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat, SerializerFeature.WriteClassName,
					SerializerFeature.WriteMapNullValue);
			// 处理ie浏览器保存数据时出现下载json数据问题
			List<MediaType> mediaTypes = new ArrayList<>();
			// mediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
			// mediaTypes.add(MediaType.TEXT_PLAIN);
			mediaTypes.add(new MediaType("text", "plain", Charset.forName("UTF-8")));
			converter.setSupportedMediaTypes(mediaTypes);
			// o 是class,s 是key值,o1 是value值
			ValueFilter valueFilter = (o, s, o1) -> {
				if (null == o1) {
					o1 = "";
				}
				return o1;
			};
			fastJsonConfig.setSerializeFilters(valueFilter);
			converter.setFastJsonConfig(fastJsonConfig);
			return converter;
		}
	}

}
