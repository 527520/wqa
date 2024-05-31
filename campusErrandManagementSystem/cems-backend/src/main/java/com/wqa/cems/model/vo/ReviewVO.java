package com.wqa.cems.model.vo;

import com.wqa.cems.model.entity.Review;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class ReviewVO implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 评价者id
     */
    private UserVO reviewer;

    /**
     * 被评价者id
     */
    private DeliverymanVOVO reviewed;

    /**
     * 评分
     */
    private BigDecimal rating;

    /**
     * 评价内容
     */
    private String comment;

    /**
     * 图片
     */
    private String image;

    /**
     * 评价时间
     */
    private Date reviewTime;

    public static ReviewVO objToVO(Review review) {
        if (review == null) {
            return null;
        }
        ReviewVO reviewVO = new ReviewVO();
        BeanUtils.copyProperties(review, reviewVO);
        return reviewVO;
    }
}
