package com.wqa.cems.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wqa.cems.common.ErrorCode;
import com.wqa.cems.exception.BusinessException;
import com.wqa.cems.exception.ThrowUtils;
import com.wqa.cems.model.dto.announcement.AnnouncementAddRequest;
import com.wqa.cems.model.dto.announcement.AnnouncementUpdateRequest;
import com.wqa.cems.model.entity.Announcement;
import com.wqa.cems.service.AnnouncementService;
import com.wqa.cems.mapper.AnnouncementMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author lenovo
 * @description 针对表【announcement】的数据库操作Service实现
 * @createDate 2024-03-20 20:46:32
 */
@Service
public class AnnouncementServiceImpl extends ServiceImpl<AnnouncementMapper, Announcement>
        implements AnnouncementService {

    @Resource
    private AnnouncementMapper announcementMapper;

    @Override
    public boolean insertAnnouncement(AnnouncementAddRequest announcementAddRequest) {
        String title = announcementAddRequest.getTitle();
        String content = announcementAddRequest.getContent();
        if (StringUtils.isAnyBlank(title, content)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        Announcement announcement = new Announcement();
        announcement.setContent(content);
        announcement.setTitle(title);
        boolean result = save(announcement);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return true;
    }

    @Override
    public boolean updateAnnouncement(AnnouncementUpdateRequest announcementUpdateRequest) {
        Long id = announcementUpdateRequest.getId();
        String title = announcementUpdateRequest.getTitle();
        String content = announcementUpdateRequest.getContent();
        if (StringUtils.isAnyBlank(title, content)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        Announcement announcement = new Announcement();
        announcement.setId(id);
        announcement.setTitle(title);
        announcement.setContent(content);
        boolean result = updateById(announcement);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return true;
    }

    @Override
    public List<Announcement> getLatestFive() {
        return announcementMapper.selectAllOrderByPublishDateDescLimit5();
    }
}




