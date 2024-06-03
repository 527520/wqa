package com.wqa.cems.component;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wqa.cems.model.entity.Message;
import com.wqa.cems.model.entity.User;
import com.wqa.cems.model.vo.UserVO;
import com.wqa.cems.service.MessageService;
import com.wqa.cems.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/websocket")
@Component
@Log4j2
public class WebSocketServer {

    /**
     * 记录当前在线连接数
     */
    public static final Map<String, Session> sessionMap = new ConcurrentHashMap<>();

    private final ObjectMapper objectMapper = new ObjectMapper();

    // 从Spring上下文中获取MessageService实例
    private final MessageService messageService;
    private final UserService userService;

    public WebSocketServer() {
        this.messageService = SpringContextUtil.getBean(MessageService.class);
        this.userService = SpringContextUtil.getBean(UserService.class);
    }

    /**
     * 连接成功调用的方法
     *
     * @param session
     */
    @OnOpen
    public void onOpen(Session session) {
        String userId = getUserIdFromQueryString(session.getQueryString());
        if (userId != null) {
            sessionMap.put(userId, session);
        }
    }

    private String getUserIdFromQueryString(String queryString) {
        // 从连接参数中提取用户的 ID
        // 这里需要根据实际情况实现解析逻辑
        // 示例中假设连接参数格式为 "?userId=xxx"
        String[] params = queryString.split("=");
        if (params.length == 2 && "userId".equals(params[0])) {
            return params[1];
        } else {
            return null;
        }
    }

    @OnClose
    public void onClose(Session session, @PathParam("id") String id) {
        sessionMap.remove(id);
        log.info(id + "用户连接关闭");
    }

    /**
     * 服务端收到客户端发送过来的信息
     *
     * @param message
     * @param session
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        JSONObject object = JSONUtil.parseObj(message);
        String toId = object.getStr("to");
        String formId = object.getStr("form");
        String messageText = object.getStr("messageText");
        String counterpartAvatar = object.getStr("counterpartAvatar");
        String toAvatar = object.getStr("toAvatar");
        log.info("服务端收到用户id={}的消息：{}", formId, message);
        Session toSession = sessionMap.get(toId);
        Message messageEntity;
        if (toSession != null) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.set("form", formId);
            jsonObject.set("messageText", messageText);
            jsonObject.set("counterpartAvatar", counterpartAvatar);
            this.sendMessage(jsonObject.toString(), toSession);
            messageEntity = new Message(null, Long.parseLong(formId), Long.parseLong(toId),
                    messageText, counterpartAvatar, null, null, 1);
            log.info("发送消息{}给用户{}", jsonObject.toString(), toId);
        } else {
            messageEntity = new Message(null, Long.parseLong(formId), Long.parseLong(toId),
                    messageText, counterpartAvatar, null, null, 0);
            log.info("发送失败，未找到{}的session", toId);
        }
        if (toAvatar == null || toAvatar.isEmpty()) {
            User user = userService.getById(Long.parseLong(toId));
            messageEntity.setToAvatar(user.getUserAvatar());
        } else {
            messageEntity.setToAvatar(toAvatar);
        }
        messageService.save(messageEntity);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        log.error("发生错误");
        throwable.printStackTrace();
    }

    /**
     * 服务器端给特定客户端发送消息
     *
     * @param message
     * @param toSession
     */
    private void sendMessage(String message, Session toSession) {
        try {
            log.info("服务端给客户端[{}]发送消息{}", toSession.getId(), message);
            toSession.getBasicRemote().sendText(message);
        } catch (Exception e) {
            log.error("服务端发送消息给客户端失败", e);
        }
    }

    /**
     * 服务器端给所有客户端发送消息
     *
     * @param message
     */
    private void sendAllMessage(String message) {
        try {
            for (Session session : sessionMap.values()) {
                log.info("服务端给客户端[{}]发送消息{}", session.getId(), message);
                session.getBasicRemote().sendText(message);
            }
        } catch (Exception e) {
            log.error("服务端发送消息给客户端失败", e);
        }
    }
}
