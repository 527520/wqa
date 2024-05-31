package com.wqa.cems.controller;

import com.wqa.cems.annotation.AuthCheck;
import com.wqa.cems.common.BaseResponse;
import com.wqa.cems.common.ErrorCode;
import com.wqa.cems.common.ResultUtils;
import com.wqa.cems.constant.UserConstant;
import com.wqa.cems.exception.BusinessException;
import com.wqa.cems.exception.ThrowUtils;
import com.wqa.cems.model.dto.becomeDeliveryman.BecomeDeliverymanUpdateRequest;
import com.wqa.cems.model.dto.user.UserBecomeDeliverymanRequest;
import com.wqa.cems.model.entity.BecomeDeliveryman;
import com.wqa.cems.model.entity.User;
import com.wqa.cems.model.vo.BecomeDeliverymanVO;
import com.wqa.cems.service.BecomeDeliverymanService;
import com.wqa.cems.service.UserService;
import com.wqa.cems.utils.FileUpload;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.wqa.cems.constant.UserConstant.USER_LOGIN_STATE;
import static com.wqa.cems.model.enums.FileUploadPath.ID_CARD_IMAGE;

@RestController
@RequestMapping("/becomeDeliveryman")
@Log4j2
public class BecomeDeliverymanController {

    @Resource
    private BecomeDeliverymanService becomeDeliverymanService;
    @Resource
    private UserService userService;

    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public BaseResponse<Boolean> becomeDeliveryman(@RequestBody UserBecomeDeliverymanRequest userBecomeDeliverymanRequest,
                                                   HttpServletRequest request) {
        if (userBecomeDeliverymanRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long userId = ((User) request.getSession().getAttribute(USER_LOGIN_STATE)).getId();
        String name = userBecomeDeliverymanRequest.getName();
        String sex = userBecomeDeliverymanRequest.getSex();
        Date birthday = userBecomeDeliverymanRequest.getBirthday();
        String idNumber = userBecomeDeliverymanRequest.getIdNumber();
        String phoneNumber = userBecomeDeliverymanRequest.getPhoneNumber();
        String idCardFront = userBecomeDeliverymanRequest.getIdCardFront();
        String idCardBack = userBecomeDeliverymanRequest.getIdCardBack();
        if (StringUtils.isAnyBlank(name, sex, idNumber, phoneNumber, idCardBack, idCardFront) && birthday == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (idNumber.length() != 18) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "身份证号码不合法");
        }
        char[] idArray = idNumber.toCharArray();
        int[] coefficients = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
        char[] checksums = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};
        int sum = 0;
        for (int i = 0; i < 17; i++) {
            if (!Character.isDigit(idArray[i])) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "身份证号码不合法");
            }
            sum += (idArray[i] - '0') * coefficients[i];
        }
        char expectedChecksum = checksums[sum % 11];
        if (!(idArray[17] == expectedChecksum)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "身份证号码不合法");
        }
        String regex = "^1[3456789]\\d{9}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNumber);
        // 手机号不合法
        if (!matcher.matches()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "手机号不合法");
        }
        BecomeDeliveryman becomeDeliveryman = new BecomeDeliveryman();
        BecomeDeliveryman becomeDeliveryman1 = becomeDeliverymanService.getOneByUserId(userId);
        if (becomeDeliveryman1 == null) {
            becomeDeliveryman.setUserId(userId);
            becomeDeliveryman.setName(name);
            becomeDeliveryman.setSex(sex);
            becomeDeliveryman.setBirthday(birthday);
            becomeDeliveryman.setIdNumber(idNumber);
            becomeDeliveryman.setPhoneNumber(phoneNumber);
            becomeDeliveryman.setIdCardFrontImage(idCardFront);
            becomeDeliveryman.setIdCardBackImage(idCardBack);
            log.info(userId);
            boolean save = becomeDeliverymanService.save(becomeDeliveryman);
            ThrowUtils.throwIf(!save, ErrorCode.OPERATION_ERROR);
        } else {
            BeanUtils.copyProperties(userBecomeDeliverymanRequest, becomeDeliveryman);
            becomeDeliveryman.setStatus("已提交");
            boolean result = becomeDeliverymanService.updateById(becomeDeliveryman);
            ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        }
        return ResultUtils.success(true);
    }

    @PostMapping("/upload/idCard")
    public BaseResponse<String> uploadIdCard(@RequestPart("file") MultipartFile file, HttpSession session) {
        if (file.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件为空");
        }
        User loginUser = userService.getLoginUser(session);
        String filePath = FileUpload.savaFile(file, loginUser.getId(), ID_CARD_IMAGE.getPath());
        log.info("filePath:" + filePath);
        return ResultUtils.success(filePath);
    }

    @GetMapping("/get/progress")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public BaseResponse<BecomeDeliverymanVO> getProgress(HttpSession session) {
        User loginUser = (User) session.getAttribute(USER_LOGIN_STATE);
        BecomeDeliverymanVO becomeDeliverymanVO = becomeDeliverymanService.getStatusAndMessage(loginUser.getId());
        if (becomeDeliverymanVO == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "没有提交申请");
        }
        return ResultUtils.success(becomeDeliverymanVO);
    }

    @PostMapping("/update/status")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<String> updateStatus(@RequestBody BecomeDeliverymanUpdateRequest becomeDeliverymanUpdateRequest) {
        if (becomeDeliverymanUpdateRequest == null || becomeDeliverymanUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        switch (becomeDeliverymanUpdateRequest.getStatus()) {
            case "正在审核":
            case "审核失败":
            case "审核通过":
                // 如果审核通过，更改他的权限
                break;
            default:
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数错误");
        }
        boolean result = becomeDeliverymanService.updateStatus(becomeDeliverymanUpdateRequest);
        return ResultUtils.success("修改成功");
    }

    @GetMapping("/get/all")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<List<BecomeDeliveryman>> getAll(){
        List<BecomeDeliveryman> becomeDeliverymanList = becomeDeliverymanService.getAll();
        if (becomeDeliverymanList.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "系统错误，稍后再试");
        }
        return ResultUtils.success(becomeDeliverymanList);
    }
}
