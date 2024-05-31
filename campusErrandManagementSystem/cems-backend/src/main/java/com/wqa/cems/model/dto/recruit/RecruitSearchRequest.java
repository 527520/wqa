package com.wqa.cems.model.dto.recruit;

import lombok.Data;

import java.io.Serializable;

@Data
public class RecruitSearchRequest implements Serializable {

    /**
     * 搜索内容
     */
    private String searchValue;

    private static final long serialVersionUID = 1L;
}
