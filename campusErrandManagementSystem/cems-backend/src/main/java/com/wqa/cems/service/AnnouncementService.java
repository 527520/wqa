package com.wqa.cems.service;

import com.wqa.cems.model.dto.announcement.AnnouncementAddRequest;
import com.wqa.cems.model.dto.announcement.AnnouncementUpdateRequest;
import com.wqa.cems.model.entity.Announcement;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author lenovo
* @description 针对表【announcement】的数据库操作Service
* @createDate 2024-03-20 20:46:32
*/
public interface AnnouncementService extends IService<Announcement> {

    boolean insertAnnouncement(AnnouncementAddRequest announcementAddRequest);

    boolean updateAnnouncement(AnnouncementUpdateRequest announcementUpdateRequest);

    List<Announcement> getLatestFive();
}
