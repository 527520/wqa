package com.wqa.cems.model.dto.review;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ReviewSearchRequest implements Serializable {

    /**
     * 搜索内容
     */
    private String searchValue;

    /**
     * 评分
     */
    private BigDecimal rating;

    private static final long serialVersionUID = 1L;
}
