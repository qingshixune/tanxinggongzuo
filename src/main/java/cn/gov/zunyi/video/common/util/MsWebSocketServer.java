package cn.gov.zunyi.video.common.util;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.servlet.http.HttpSession;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@ServerEndpoint(value = "/msWebsocketServer/{userId}")//, configurator = GetHttpSessionConfigurator.class
@Component
public class MsWebSocketServer {

	private static int onlineCount = 0;
	// 存放每个客户端对应的MsWebSocketServer对象。
	private static CopyOnWriteArraySet<MsWebSocketServer> webSocketSet = new CopyOnWriteArraySet<MsWebSocketServer>();
	private static final Logger logger = LoggerFactory
			.getLogger(MsWebSocketServer.class);
	// 与某个客户端的连接会话，需要通过它来给客户端发送数据
	private Session session;
	private String nickname;
	private String userId;
	private HttpSession httpSession;
	private static Map<String, MsWebSocketServer> clients = new ConcurrentHashMap<String, MsWebSocketServer>();

	/**
	 * 连接建立成功调用的方法
	 */
	@OnOpen
	public void onOpen(@PathParam("userId") String userId, Session session,
			EndpointConfig config) {
		this.session = session;
		this.userId = userId;

		//把以前的移除掉  后面加上新增的
		for (MsWebSocketServer item : webSocketSet) {
			if (item.userId.equals(userId)){
				webSocketSet.remove(item);
				break;
			}
		}


		clients.put(userId, this);
		webSocketSet.add(this);
		addOnlineCount();
		logger.info("有新连接加入,当前在线人数为>>>>>>" + getOnlineCount());
		try {
			sendMessage("wsConnectSuccess");
		} catch (IOException e) {
			logger.info(">>>>>>IO异常<<<<<<");
		}
	}

	/**
	 * 连接关闭调用的方法
	 */
	@OnClose
	public void onClose() {
//		for (MsWebSocketServer item : clients.values()) {
//			if (item.userId.equals(userId))
//				webSocketSet.remove(this.userId);
//		}
		subOnlineCount();
		logger.info("有一连接关闭!当前在线人数为>>>>>>" + getOnlineCount());
	}

	/**
	 * 收到客户端消息后调用的方法
	 * 
	 * @param message
	 *            客户端发送过来的消息
	 */
	@OnMessage
	public void onMessage(String message, Session session,
			HttpSession httpSession) {
		logger.info("来自客户端的消息:>>>>>>" + message);
		// 群发消息

		for (MsWebSocketServer item : webSocketSet) {
			try {
				if (session == item.session) {
					item.sendMessage(message);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 发生错误时调用
	 */
	@OnError
	public void onError(Session session, Throwable error) {
		error.printStackTrace();
		logger.error("发生错误!!!!", error);
	}

	/**
	 * 群发自定义消息
	 * */
	public void sendMessage(String message) throws IOException {
		this.session.getBasicRemote().sendText(message);
	}

	/**
	 * 群发自定义消息
	 * */
	public void sendMessage(String message, HttpSession se) throws IOException {
		for (MsWebSocketServer item : webSocketSet) {
			if (item.httpSession == se) {
				this.session.getBasicRemote().sendText(message);
			}
		}
		this.session.getBasicRemote().sendText(message);
	}

	public static void sendInfo(String message) throws IOException {
		for (MsWebSocketServer item : webSocketSet) {
			try {
				item.sendMessage(message);
			} catch (IOException e) {
				continue;
			}
		}
	}

	public void sendMessageTo(String message, String To) throws IOException {
		for (MsWebSocketServer item : webSocketSet) {
			if (item.userId.equals(To)){
				logger.info("当前在线人数为>>>>>>" + getOnlineCount());
				logger.info("成功发送信息>>>>>>>>>"+To+"<<<<<<<message is "+message);
				item.session.getBasicRemote().sendText(message);
			}
		}
		
//		for (MsWebSocketServer item : clients.values()) {
//			if (item.userId.equals("2"))
//				logger.info("当前在线人数为>>>>>>" + getOnlineCount());
//				logger.info("成功发送信息>>>>>>>>>"+To+"<<<<<<<message is "+message);
//				item.session.getAsyncRemote().sendText(message);
//		}
		
	}

	/**
	 * 获取在线人数
	 * 
	 * @return
	 */
	public static synchronized int getOnlineCount() {
		return onlineCount;
	}

	/**
	 * 新链接建立
	 */
	public static synchronized void addOnlineCount() {
		MsWebSocketServer.onlineCount++;
	}

	/**
	 * 关闭链接
	 */
	public static synchronized void subOnlineCount() {
		MsWebSocketServer.onlineCount--;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public HttpSession getHttpSession() {
		return httpSession;
	}

	public void setHttpSession(HttpSession httpSession) {
		this.httpSession = httpSession;
	}
}
