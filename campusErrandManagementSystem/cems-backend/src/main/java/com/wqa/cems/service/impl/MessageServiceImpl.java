package com.wqa.cems.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wqa.cems.mapper.MessageMapper;
import com.wqa.cems.model.entity.Message;
import com.wqa.cems.model.vo.MessageVO;
import com.wqa.cems.service.MessageService;
import com.wqa.cems.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author 吴奇安
 * @description 针对表【message】的数据库操作Service实现
 * @createDate 2024-06-07 11:06:35
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message>
        implements MessageService {

    @Resource
    private MessageMapper messageMapper;
    @Resource
    private UserService userService;

    @Override
    public List<Message> getMessageList(Long id, Long counterpartId) {
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("to_user_id", id)
                .eq("form_user_id", counterpartId)
                .or()
                .eq("to_user_id", counterpartId)
                .eq("form_user_id", id)
                .orderByAsc("time");
        List<Message> messageList = messageMapper.selectList(queryWrapper);
        messageList.stream()
                .filter(message -> message.getIsRead() == 0)
                .filter(message -> message.getToUserId().equals(id))
                .forEach(message -> messageMapper.updateIsReadById(message.getId()));
        return messageMapper.selectList(queryWrapper);
    }

    @Override
    public List<MessageVO> getChatList(Long myId) {
        List<Message> messageList = messageMapper.getChatList(myId);
        List<MessageVO> messageVOList = new ArrayList<>();
        if (!messageList.isEmpty()) {
            for (Message message : messageList) {
                String counterpartName = userService.getUserNameById(Objects.equals(message.getFormUserId(), myId)
                        ? message.getToUserId() : message.getFormUserId());
                MessageVO messageVO = new MessageVO();
                BeanUtils.copyProperties(message, messageVO);
                messageVO.setCounterpartName(counterpartName);
                messageVOList.add(messageVO);
            }
        }
        return messageVOList;
    }

    @Override
    public int getUnReadMessages(Long formUserId, Long toUserId) {
        return messageMapper.getUnreadMessages(formUserId, toUserId);
    }
}




