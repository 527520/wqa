package com.wqa.cems.controller;

import com.wqa.cems.annotation.AuthCheck;
import com.wqa.cems.common.BaseResponse;
import com.wqa.cems.common.DeleteRequest;
import com.wqa.cems.common.ErrorCode;
import com.wqa.cems.common.ResultUtils;
import com.wqa.cems.constant.UserConstant;
import com.wqa.cems.exception.BusinessException;
import com.wqa.cems.exception.ThrowUtils;
import com.wqa.cems.model.dto.deliveryman.DeliverymanUpdateRequest;
import com.wqa.cems.model.entity.Deliveryman;
import com.wqa.cems.model.vo.DeliverymanVO;
import com.wqa.cems.model.vo.DeliverymanVOResponse;
import com.wqa.cems.model.vo.DeliverymanVOVO;
import com.wqa.cems.service.DeliverymanService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/deliveryman")
@Log4j2
public class DeliverymanController {

    @Resource
    private DeliverymanService deliverymanService;

    @PostMapping("/get/all")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<List<Deliveryman>> getAll() {
        List<Deliveryman> deliverymanList = deliverymanService.getAll();
        if (deliverymanList.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "系统错误，稍后再试");
        }
        return ResultUtils.success(deliverymanList);
    }

    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateById(@RequestBody DeliverymanUpdateRequest deliverymanUpdateRequest) {
        if (deliverymanUpdateRequest == null || deliverymanUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Deliveryman deliveryman = new Deliveryman();
        BeanUtils.copyProperties(deliverymanUpdateRequest, deliveryman);
        boolean result = deliverymanService.updateById(deliveryman);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteById(@RequestBody DeleteRequest deleteRequest) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 先拿id去查，只有离职的才能删除
        Deliveryman deliveryman = deliverymanService.getById(deleteRequest.getId());
        if (!"离职".equals(deliveryman.getStatus())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "不能删除在职人员");
        }
        boolean b = deliverymanService.removeById(deleteRequest.getId());
        return ResultUtils.success(b);
    }

    /**
     * 管理员查询出所有在线未接单人员
     */
    @GetMapping("/get/allInline")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<List<DeliverymanVOResponse>> getAllInline() {
        return ResultUtils.success(deliverymanService.getAllInline());
    }

    /**
     * 得到跑腿员VO
     */
    @PostMapping("/image")
    public BaseResponse<DeliverymanVO> getDeliverymanImage(@RequestBody DeliverymanVOVO deliveryman) {
        if (deliveryman == null || deliveryman.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return ResultUtils.success(deliverymanService.getDeliveryVOById(deliveryman.getId()));
    }
}
