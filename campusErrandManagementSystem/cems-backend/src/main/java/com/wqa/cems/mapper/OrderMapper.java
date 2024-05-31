package com.wqa.cems.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.wqa.cems.model.entity.Order;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author lenovo
 * @description 针对表【order】的数据库操作Mapper
 * @createDate 2024-03-11 22:51:50
 * @Entity generator.domain.Order
 */
public interface OrderMapper extends BaseMapper<Order> {

    String selectStatusById(@Param("id") Long id);

    int updateStatusById(@Param("status") String status, @Param("id") Long id);

    Order selectOneByTaskId(@Param("taskId") Long taskId);
}




