package com.wqa.cems.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wqa.cems.annotation.AuthCheck;
import com.wqa.cems.common.BaseResponse;
import com.wqa.cems.common.DeleteRequest;
import com.wqa.cems.common.ErrorCode;
import com.wqa.cems.common.ResultUtils;
import com.wqa.cems.constant.UserConstant;
import com.wqa.cems.exception.BusinessException;
import com.wqa.cems.exception.ThrowUtils;
import com.wqa.cems.model.dto.review.ReviewAddRequest;
import com.wqa.cems.model.dto.review.ReviewSearchRequest;
import com.wqa.cems.model.entity.Review;
import com.wqa.cems.model.entity.User;
import com.wqa.cems.model.vo.DeliverymanVOVO;
import com.wqa.cems.model.vo.ReviewVO;
import com.wqa.cems.service.ReviewService;
import com.wqa.cems.service.UserService;
import com.wqa.cems.utils.FileUpload;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.List;

import static com.wqa.cems.constant.UserConstant.USER_LOGIN_STATE;
import static com.wqa.cems.model.enums.FileUploadPath.REVIEW_IMAGE;

@RestController
@Log4j2
@RequestMapping("/review")
public class ReviewController {
    @Resource
    private ReviewService reviewService;
    @Resource
    private UserService userService;

    /**
     * 评价
     *
     * @param reviewAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public BaseResponse<Boolean> addReview(@RequestBody ReviewAddRequest reviewAddRequest,
                                           HttpServletRequest request) {
        if (reviewAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return ResultUtils.success(reviewService.insertReview(reviewAddRequest, request));
    }

    /**
     * 上传图片
     *
     * @param file
     * @param session
     * @return
     */
    @PostMapping("/upload/image")
    public BaseResponse<String> uploadImage(@RequestPart("file") MultipartFile file, HttpSession session) {
        if (file.isEmpty()) {
            return ResultUtils.success("");
        }
        User loginUser = userService.getLoginUser(session);
        String filePath = FileUpload.savaFile(file, loginUser.getId(), REVIEW_IMAGE.getPath());
        return ResultUtils.success(filePath);
    }

    /**
     * (用户或配送员)查看自己所有评论
     */
    @GetMapping("/get/my/all")
    public BaseResponse<List<ReviewVO>> viewMyAllComments(HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request.getSession());
        return ResultUtils.success(reviewService.getMyAllComments(loginUser));
    }

    /**
     * 查看某位配送员的所有评论信息
     */
    @PostMapping("/get/deliveryman/all")
    public BaseResponse<List<ReviewVO>> getDeliveryComments(@RequestBody DeliverymanVOVO deliveryman, HttpSession session) {
        if (deliveryman == null || deliveryman.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Object userObj = session.getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        log.info(String.valueOf(currentUser));
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return ResultUtils.success(reviewService.getDeliverymanComments(deliveryman.getId()));
    }

    /**
     * 管理员获取所有评价信息
     *
     * @return
     */
    @GetMapping("/get/all")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<List<ReviewVO>> getAllComments() {
        return ResultUtils.success(reviewService.getAll());
    }

    /**
     * 管理员搜索评价信息 (未联调)
     */
    @PostMapping("/get/search")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<List<ReviewVO>> getBySearchComments(@RequestBody ReviewSearchRequest reviewSearchRequest) {
        if (reviewSearchRequest == null || reviewSearchRequest.getSearchValue().isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        // 暂时只做按姓名和内容搜
        String searchValue = reviewSearchRequest.getSearchValue();
        return ResultUtils.success(reviewService.searchComments(searchValue));
    }

    /**
     * 删除评价（只有管理员和本人能删除）
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteComments(@RequestBody DeleteRequest deleteRequest, HttpSession session) {
        if (deleteRequest == null || deleteRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(session);
        if (loginUser.getRole().equals(UserConstant.DELIVERYMAN_Role)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        Long reviewId = deleteRequest.getId();
        Review review = reviewService.getById(reviewId);
        if (!loginUser.getRole().equals(UserConstant.ADMIN_ROLE)
                && !review.getReviewerId().equals(loginUser.getId())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = reviewService.removeById(reviewId);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }
}
