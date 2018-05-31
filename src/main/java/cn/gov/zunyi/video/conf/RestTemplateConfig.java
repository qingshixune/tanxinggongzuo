package cn.gov.zunyi.video.conf;

import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

	@Bean
	public SimpleClientHttpRequestFactory simpleClientHttpRequestFactory() {
		SimpleClientHttpRequestFactory httpRequestFactory = new SimpleClientHttpRequestFactory();
		httpRequestFactory.setReadTimeout(5000);// ms
		httpRequestFactory.setConnectTimeout(15000);// ms
		return httpRequestFactory;
	}

	@Bean
	@ConditionalOnMissingBean({ RestOperations.class, RestTemplate.class })
	public RestTemplate restTemplate(SimpleClientHttpRequestFactory httpClientFactory) {
		RestTemplate restTemplate = new RestTemplate(httpClientFactory);
		// 使用 utf-8 编码集的 conver 替换默认的 conver（默认的stringconverter的编码集为"ISO-8859-1"）
		List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
		Iterator<HttpMessageConverter<?>> iterator = messageConverters.iterator();
		while (iterator.hasNext()) {
			HttpMessageConverter<?> converter = iterator.next();
			if (converter instanceof StringHttpMessageConverter) {
				iterator.remove();
			}
		}
		messageConverters.add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
		return restTemplate;
	}

}