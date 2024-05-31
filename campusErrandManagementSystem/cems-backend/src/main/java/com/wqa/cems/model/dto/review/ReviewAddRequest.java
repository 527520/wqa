package com.wqa.cems.model.dto.review;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ReviewAddRequest implements Serializable {

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 评价者id
     */
    private Long reviewerId;

    /**
     * 任务id
     */
    private Long taskId;

    /**
     * 被评价者id
     */
    private Long reviewedId;

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

    private static final long serialVersionUID = 1L;
}
