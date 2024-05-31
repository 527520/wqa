package com.wqa.cems.controller;

import com.wqa.cems.annotation.AuthCheck;
import com.wqa.cems.common.BaseResponse;
import com.wqa.cems.common.DeleteRequest;
import com.wqa.cems.common.ErrorCode;
import com.wqa.cems.common.ResultUtils;
import com.wqa.cems.constant.UserConstant;
import com.wqa.cems.exception.BusinessException;
import com.wqa.cems.exception.ThrowUtils;
import com.wqa.cems.model.dto.recruit.RecruitAddRequest;
import com.wqa.cems.model.dto.recruit.RecruitSearchRequest;
import com.wqa.cems.model.dto.recruit.RecruitUpdateRequest;
import com.wqa.cems.model.entity.Recruit;
import com.wqa.cems.service.RecruitService;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Log4j2
@RequestMapping("/recruit")
public class RecruitController {

    @Resource
    private RecruitService recruitService;

    /**
     * 添加招聘信息
     *
     * @param recruitAddRequest
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> addRecruit(@RequestBody RecruitAddRequest recruitAddRequest) {
        if (recruitAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String content = recruitAddRequest.getContent();
        String title = recruitAddRequest.getTitle();

        return ResultUtils.success(recruitService.addRecruit(content, title));
    }

    /**
     * 修改招聘信息
     *
     * @param recruitUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateRecruit(@RequestBody RecruitUpdateRequest recruitUpdateRequest) {
        if (recruitUpdateRequest == null || recruitUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        return ResultUtils.success(recruitService.updateRecruit(recruitUpdateRequest));
    }

    /**
     * 修改招聘信息状态
     *
     * @param deleteRequest
     * @return
     */
    @PostMapping("/update/status")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateStatus(@RequestBody DeleteRequest deleteRequest) {
        if (deleteRequest == null || deleteRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = recruitService.removeById(deleteRequest.getId());
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 获取所有招聘信息
     *
     * @return
     */
    @GetMapping("/get/all")
    public BaseResponse<List<Recruit>> getAllRecruits() {
        return ResultUtils.success(recruitService.getAll());
    }

    /**
     * 搜索招聘信息
     *
     * @param recruitSearchRequest
     * @return
     */
    @PostMapping("/search")
    public BaseResponse<List<Recruit>> getRecruitsBySearch(@RequestBody RecruitSearchRequest recruitSearchRequest) {
        if (recruitSearchRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return ResultUtils.success(recruitService.getRecruitsBySearch(recruitSearchRequest.getSearchValue()));
    }
}
