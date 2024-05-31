package com.wqa.cems.model.dto.recruit;

import lombok.Data;

import java.io.Serializable;

@Data
public class RecruitUpdateRequest implements Serializable {
    /**
     * id
     */
    private Long id;

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
