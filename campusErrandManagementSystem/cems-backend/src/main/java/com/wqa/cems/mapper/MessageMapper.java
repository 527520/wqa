package com.wqa.cems.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wqa.cems.model.entity.Message;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 吴奇安
* @description 针对表【message】的数据库操作Mapper
* @createDate 2024-06-07 11:06:35
* @Entity generator.domain.Message
*/
public interface MessageMapper extends BaseMapper<Message> {

    int updateIsReadById(@Param("id") Long id);

    List<Message> getChatList(@Param("userId") Long userId);

    int getAllUnreadMessages(@Param("userId") Long userId);

    int getUnreadMessages(@Param("formUserId") Long formUserId, @Param("toUserId") Long toUserId);
}




