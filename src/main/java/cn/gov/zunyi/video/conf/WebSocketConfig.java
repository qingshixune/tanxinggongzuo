package cn.gov.zunyi.video.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.server.support.OriginHandshakeInterceptor;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

	@Override
	public void registerStompEndpoints(
			StompEndpointRegistry stompEndpointRegistry) {
		// 注册STOMP 协议的节点（endpoint） 并映射到指定的URL
		// 注册一个 STOMP 的 endpoint ，并指定使用 SockJS协议
		stompEndpointRegistry.addEndpoint("/endpointMs")
				.addInterceptors(new OriginHandshakeInterceptor())
				.setAllowedOrigins("http://localhost").withSockJS();
	}

	/**
	 * 配置消息代理
	 * 
	 * @param registry
	 */
	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		// 广播式应配置一个 /topic 的消息代理
		registry.enableSimpleBroker("/topic");
	}

}
