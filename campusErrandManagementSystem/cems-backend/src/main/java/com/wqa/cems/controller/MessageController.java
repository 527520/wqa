package com.wqa.cems.controller;

import com.wqa.cems.common.BaseResponse;
import com.wqa.cems.common.ResultUtils;
import com.wqa.cems.model.entity.Message;
import com.wqa.cems.model.entity.User;
import com.wqa.cems.model.vo.MessageVO;
import com.wqa.cems.service.MessageService;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.wqa.cems.constant.UserConstant.USER_LOGIN_STATE;

@RestController
@Log4j2
@RequestMapping("/message")
public class MessageController {
    @Resource
    private MessageService messageService;

    @GetMapping("/list")
    public BaseResponse<List<Message>> getMessageList(HttpServletRequest request) {
        User loginUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        return ResultUtils.success(messageService.getMessageList(loginUser.getId()));
    }

    @GetMapping("/informationInquiries")
    public BaseResponse<Map<MessageVO, Integer>> getChatList(HttpServletRequest request) {
        User loginUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        Long myId = loginUser.getId();
        List<MessageVO> chatList = messageService.getChatList(myId);
        Map<MessageVO, Integer> map = new HashMap<>();
        for (MessageVO messageVO : chatList) {
            int unReadMessageNum = messageService.getUnReadMessages(messageVO.getFormUserId(), messageVO.getToUserId());
            map.put(messageVO, unReadMessageNum);
        }
        return ResultUtils.success(map);
    }
}
