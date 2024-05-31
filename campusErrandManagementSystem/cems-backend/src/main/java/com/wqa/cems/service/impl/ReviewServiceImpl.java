package com.wqa.cems.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wqa.cems.common.ErrorCode;
import com.wqa.cems.constant.UserConstant;
import com.wqa.cems.exception.BusinessException;
import com.wqa.cems.exception.ThrowUtils;
import com.wqa.cems.model.dto.review.ReviewAddRequest;
import com.wqa.cems.model.entity.Deliveryman;
import com.wqa.cems.model.entity.Review;
import com.wqa.cems.model.entity.Task;
import com.wqa.cems.model.entity.User;
import com.wqa.cems.model.vo.DeliverymanVOVO;
import com.wqa.cems.model.vo.ReviewVO;
import com.wqa.cems.model.vo.UserVO;
import com.wqa.cems.service.DeliverymanService;
import com.wqa.cems.service.ReviewService;
import com.wqa.cems.mapper.ReviewMapper;
import com.wqa.cems.service.TaskService;
import com.wqa.cems.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lenovo
 * @description 针对表【review】的数据库操作Service实现
 * @createDate 2024-03-21 00:23:40
 */
@Service
public class ReviewServiceImpl extends ServiceImpl<ReviewMapper, Review>
        implements ReviewService {

    @Resource
    private DeliverymanService deliverymanService;
    @Resource
    private ReviewMapper reviewMapper;
    @Resource
    private UserService userService;
    @Resource
    private TaskService taskService;

    @Override
    @Transactional
    public boolean insertReview(ReviewAddRequest reviewAddRequest, HttpServletRequest request) {
        Long orderId = reviewAddRequest.getOrderId();
        Object object = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        User loginUser = (User) object;
        Long reviewerId = loginUser.getId();
        reviewAddRequest.setReviewerId(reviewerId);
        Long reviewedId = reviewAddRequest.getReviewedId();
        BigDecimal rating = reviewAddRequest.getRating();
        Long taskId = reviewAddRequest.getTaskId();
        // 图片或评论可以为空
        if (orderId == null || reviewedId == null || rating == null || taskId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        rating = rating.multiply(new BigDecimal(2));
        String status = taskService.getStatusById(taskId);
        if (!"已完成".equals(status)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "该订单状态为不可评论状态");
        }
        Review review = new Review();
        BeanUtils.copyProperties(reviewAddRequest, review);
        boolean result = save(review);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        // 还要更新配送员的平均评分
        BigDecimal averageRating = deliverymanService.getRatingById(reviewedId);
        QueryWrapper<Review> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("reviewed_id", reviewedId);
        // 去评价表查有多少条关于这个配送员的评价, 上面已经保存所以查出来多少条就是多少条
        BigDecimal ratingNum = new BigDecimal(reviewMapper.selectCount(queryWrapper));
        // 算平均评分
        BigDecimal newAverageRating = averageRating
                .multiply(ratingNum.subtract(new BigDecimal(1)))
                .add(rating)
                .divide(ratingNum, 2, RoundingMode.HALF_UP);
        result = deliverymanService.updateAverageRatingById(newAverageRating, reviewedId);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        // 还要更新订单表的状态为已评论
        Task task = new Task();
        task.setId(taskId);
        task.setStatus("已评论");
        result = taskService.updateById(task);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return true;
    }

    @Override
    public List<ReviewVO> getMyAllComments(User loginUser) {
        QueryWrapper<Review> queryWrapper = new QueryWrapper<>();
        List<ReviewVO> reviewVOList = new ArrayList<>();
        if (loginUser.getRole().equals(UserConstant.DEFAULT_ROLE)) {
            // 用户，查看自己的所有评论
            queryWrapper.eq("reviewer_id", loginUser.getId());
            for (Review review : reviewMapper.selectList(queryWrapper)) {
                ReviewVO reviewVO = ReviewVO.objToVO(review);
                Long reviewedId = review.getReviewedId();
                Deliveryman deliveryman = deliverymanService.getById(reviewedId);
                DeliverymanVOVO deliverymanVOVO = DeliverymanVOVO.objToVO(deliveryman);
                reviewVO.setReviewed(deliverymanVOVO);
                reviewVO.setReviewer(UserVO.objToVO(loginUser));
                reviewVOList.add(reviewVO);
            }

        } else if (loginUser.getRole().equals(UserConstant.DELIVERYMAN_Role)) {
            // 配送员，查看关于自己的所有评论
            Long deliverymanId = deliverymanService.getIdByUserId(loginUser.getId());
            queryWrapper.eq("reviewed_id", deliverymanId);
            for (Review review : reviewMapper.selectList(queryWrapper)) {
                ReviewVO reviewVO = ReviewVO.objToVO(review);
                Long reviewerId = review.getReviewerId();
                User user = userService.getById(reviewerId);
                UserVO userVO = UserVO.objToVO(user);
                reviewVO.setReviewer(userVO);
                Deliveryman deliveryman = deliverymanService.getById(deliverymanId);
                DeliverymanVOVO deliverymanVOVO = DeliverymanVOVO.objToVO(deliveryman);
                reviewVO.setReviewed(deliverymanVOVO);
                reviewVOList.add(reviewVO);
            }

        }

        return reviewVOList;
    }

    @Override
    public List<ReviewVO> getDeliverymanComments(Long deliverymanId) {
        QueryWrapper<Review> queryWrapper = new QueryWrapper<>();
        List<ReviewVO> reviewVOList = new ArrayList<>();
        queryWrapper.eq("reviewed_id", deliverymanId);
        for (Review review : reviewMapper.selectList(queryWrapper)) {
            ReviewVO reviewVO = ReviewVO.objToVO(review);
            Long reviewerId = review.getReviewerId();
            User user = userService.getById(reviewerId);
            UserVO userVO = UserVO.objToVO(user);
            reviewVO.setReviewer(userVO);
            Deliveryman deliveryman = deliverymanService.getById(deliverymanId);
            DeliverymanVOVO deliverymanVOVO = DeliverymanVOVO.objToVO(deliveryman);
            reviewVO.setReviewed(deliverymanVOVO);
            reviewVOList.add(reviewVO);
        }
        return reviewVOList;
    }

    @Override
    public List<ReviewVO> getAll() {
        return getReviewVO(reviewMapper.selectAll());
    }

    @Override
    public List<ReviewVO> searchComments(String searchValue) {
        return getReviewVO(reviewMapper.searchReviews(searchValue));
    }

    public List<ReviewVO> getReviewVO(List<Review> reviewList) {
        List<ReviewVO> reviewVOList = new ArrayList<>();
        for (Review review : reviewList) {
            ReviewVO reviewVO = ReviewVO.objToVO(review);
            Long reviewerId = review.getReviewerId();
            Long reviewedId = review.getReviewedId();
            User user = userService.getById(reviewerId);
            UserVO userVO = UserVO.objToVO(user);
            Deliveryman deliveryman = deliverymanService.getById(reviewedId);
            DeliverymanVOVO deliverymanVOVO = DeliverymanVOVO.objToVO(deliveryman);
            reviewVO.setReviewed(deliverymanVOVO);
            reviewVO.setReviewer(userVO);
            reviewVOList.add(reviewVO);
        }
        return reviewVOList;
    }
}




