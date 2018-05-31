package cn.gov.zunyi.video.conf;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.Resource;
import java.nio.charset.Charset;

@Configuration
@EnableCaching
public class RedisCacheConfig extends CachingConfigurerSupport {

    /**
     * 载入通过配置文件配置的连接工厂
     */
	@Resource
	private RedisConnectionFactory redisConnectionFactory;

	@Bean
	public RedisTemplate<String, String> redisTemplate() {
		RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory);
		redisTemplate.afterPropertiesSet();
		setSerializer(redisTemplate);
		return redisTemplate;
	}

    @Bean
    public CacheManager cacheManager(RedisTemplate<String, String> redisTemplate) {
        return new RedisCacheManager(redisTemplate);
    }

	private void setSerializer(RedisTemplate<String, String> template) {
		FastJson2RedisSerializer<Object> fastJson2RedisSerializer = new FastJson2RedisSerializer<>(Object.class);
		template.setKeySerializer(new StringRedisSerializer());
		template.setValueSerializer(fastJson2RedisSerializer);
		template.afterPropertiesSet();
	}

	private static class FastJson2RedisSerializer<T> implements RedisSerializer<T> {

		private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

		private Class<T> clazz;

		private FastJson2RedisSerializer(Class<T> clazz) {
			this.clazz = clazz;
		}

		@Override
		public byte[] serialize(T t) throws SerializationException {
			return JSON.toJSONString(t, SerializerFeature.WriteClassName).getBytes(DEFAULT_CHARSET);
		}

		@Override
		public T deserialize(byte[] bytes) throws SerializationException {
			if (bytes.length <= 0) {
				return null;
			}
			String str = new String(bytes, DEFAULT_CHARSET);
			return JSON.parseObject(str, clazz);
		}
	}

}