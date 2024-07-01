package com.wqa.cems.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wqa.cems.model.entity.Message;
import com.wqa.cems.model.vo.MessageVO;

import java.util.List;

/**
* @author 吴奇安
* @description 针对表【message】的数据库操作Service
* @createDate 2024-06-07 11:06:35
*/
public interface MessageService extends IService<Message> {

    List<Message> getMessageList(Long id, Long counterpartId);

    List<MessageVO> getChatList(Long myId);

    int getUnReadMessages(Long formUserId, Long toUserId);
}
