package com.wqa.cems.controller;

import com.wqa.cems.common.BaseResponse;
import com.wqa.cems.common.DeleteRequest;
import com.wqa.cems.common.ErrorCode;
import com.wqa.cems.common.ResultUtils;
import com.wqa.cems.exception.BusinessException;
import com.wqa.cems.exception.ThrowUtils;
import com.wqa.cems.model.dto.address.AddressAddRequest;
import com.wqa.cems.model.dto.address.AddressUpdateRequest;
import com.wqa.cems.model.entity.Address;
import com.wqa.cems.service.AddressService;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@Log4j2
@RequestMapping("/address")
public class AddressController {

    @Resource
    private AddressService addressService;

    @PostMapping("/add")
    public BaseResponse<Boolean> addAddress(@RequestBody AddressAddRequest addressAddRequest, HttpSession session) {
        if (addressAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        return ResultUtils.success(addressService.saveAddress(addressAddRequest, session));
    }

    @GetMapping("/get")
    public BaseResponse<List<Address>> getAllAddress(HttpSession session) {
        List<Address> addressList = addressService.getAll(session);
        return ResultUtils.success(addressList);
    }

    @PostMapping("/update")
    public BaseResponse<Boolean> updateAddress(@RequestBody AddressUpdateRequest addressUpdateRequest, HttpSession session) {
        if (addressUpdateRequest == null || addressUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return ResultUtils.success(addressService.updateAddress(addressUpdateRequest, session));
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteAddress(@RequestBody DeleteRequest deleteRequest) {
        if (deleteRequest == null || deleteRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = addressService.removeById(deleteRequest.getId());
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }
}
