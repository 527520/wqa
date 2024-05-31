package com.wqa.cems.model.dto.announcement;

import lombok.Data;

import java.io.Serializable;

@Data
public class AnnouncementAddRequest implements Serializable {

    /**
     * 公告标题
     */
    private String title;

    /**
     * 公告内容
     */
    private String content;

    private static final long serialVersionUID = 1L;
}
