package com.wqa.cems.service;

import com.wqa.cems.model.dto.review.ReviewAddRequest;
import com.wqa.cems.model.entity.Review;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wqa.cems.model.entity.User;
import com.wqa.cems.model.vo.ReviewVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author lenovo
* @description 针对表【review】的数据库操作Service
* @createDate 2024-03-21 00:23:40
*/
public interface ReviewService extends IService<Review> {

    boolean insertReview(ReviewAddRequest reviewAddRequest, HttpServletRequest request);

    List<ReviewVO> getMyAllComments(User loginUser);

    List<ReviewVO> getDeliverymanComments(Long deliverymanId);

    List<ReviewVO> getAll();

    List<ReviewVO> searchComments(String searchValue);
}
