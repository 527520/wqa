package com.wqa.cems.controller;

import com.wqa.cems.annotation.AuthCheck;
import com.wqa.cems.common.BaseResponse;
import com.wqa.cems.common.DeleteRequest;
import com.wqa.cems.common.ErrorCode;
import com.wqa.cems.common.ResultUtils;
import com.wqa.cems.constant.UserConstant;
import com.wqa.cems.exception.BusinessException;
import com.wqa.cems.exception.ThrowUtils;
import com.wqa.cems.model.dto.announcement.AnnouncementAddRequest;
import com.wqa.cems.model.dto.announcement.AnnouncementUpdateRequest;
import com.wqa.cems.model.entity.Announcement;
import com.wqa.cems.service.AnnouncementService;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Log4j2
@RequestMapping("/announcement")
public class AnnouncementController {
    @Resource
    private AnnouncementService announcementService;

    /**
     * 添加公告
     *
     * @param announcementAddRequest
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> addAnnouncement(@RequestBody AnnouncementAddRequest announcementAddRequest) {
        if (announcementAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return ResultUtils.success(announcementService.insertAnnouncement(announcementAddRequest));
    }

    /**
     * 更新公告
     *
     * @param announcementUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateAnnouncement(@RequestBody AnnouncementUpdateRequest announcementUpdateRequest) {
        if (announcementUpdateRequest == null || announcementUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return ResultUtils.success(announcementService.updateAnnouncement(announcementUpdateRequest));
    }

    /**
     * 删除公告
     * @param deleteRequest
     * @return
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteAnnouncement(@RequestBody DeleteRequest deleteRequest) {
        if (deleteRequest == null || deleteRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long id = deleteRequest.getId();
        boolean result = announcementService.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 仅展示最新的五条公告
     * @return
     */
    @GetMapping("/get/latestFive")
    public BaseResponse<List<Announcement>> getLatestFive(){
        return ResultUtils.success(announcementService.getLatestFive());
    }
}
