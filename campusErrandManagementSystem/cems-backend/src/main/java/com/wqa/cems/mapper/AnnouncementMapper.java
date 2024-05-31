package com.wqa.cems.mapper;
import java.util.List;

import com.wqa.cems.model.entity.Announcement;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author lenovo
* @description 针对表【announcement】的数据库操作Mapper
* @createDate 2024-03-20 20:46:32
* @Entity generator.domain.Announcement
*/
public interface AnnouncementMapper extends BaseMapper<Announcement> {

    List<Announcement> selectAllOrderByPublishDateDescLimit5();
}




