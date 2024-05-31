package com.wqa.cems.model.dto.recruit;

import lombok.Data;

import java.io.Serializable;

@Data
public class RecruitAddRequest implements Serializable {
    /**
     * 标题
     */
    private String title;

    /**
     * 招聘内容
     */
    private String content;

    private static final long serialVersionUID = 1L;
}
