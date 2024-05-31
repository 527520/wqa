package com.wqa.cems.mapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.wqa.cems.model.entity.Task;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author lenovo
* @description 针对表【task】的数据库操作Mapper
* @createDate 2024-03-10 19:38:17
* @Entity generator.domain.Task
*/
public interface TaskMapper extends BaseMapper<Task> {

    List<Task> selectAllByUserId(@Param("userId") Long userId);

    List<Task> selectAllByUserIdAndStatusOrStatusOrderByPublishTimeDesc(@Param("userId") Long userId, @Param("status") String status, @Param("oldStatus") String oldStatus);

    String getStatusById(@Param("id") Long id);

    List<Task> getAllByStatus();

    Task selectOneByDeliverymanIdAndStatusOrStatus(@Param("deliverymanId") Long deliverymanId, @Param("status1") String status1, @Param("status2") String status2);

    List<Task> selectAllByDeliverymanId(@Param("deliverymanId") Long deliverymanId);
}




