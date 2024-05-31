package com.wqa.cems.mapper;
import java.util.List;

import com.wqa.cems.model.vo.UserInfo;
import org.apache.ibatis.annotations.Param;

import com.wqa.cems.model.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author lenovo
* @description 针对表【user】的数据库操作Mapper
* @createDate 2024-03-05 19:55:21
* @Entity generator.domain.User
*/
public interface UserMapper extends BaseMapper<User> {
    List<UserInfo> selectAllByRole(@Param("role") String role);

    List<UserInfo> searchUsers(@Param("field") String field, @Param("fieldValue") String fieldValue);
}




