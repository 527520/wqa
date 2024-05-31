package com.wqa.cems.model.enums;

import lombok.Getter;

@Getter
public enum FileUploadPath {

    URL("http://localhost:8101/api/"),
    ID_CARD_IMAGE("D:/wqa/uploadFile/idCardImage/"),
    AVATAR_IMAGE("D:/wqa/uploadFile/avatar/"),
    REVIEW_IMAGE("D:/wqa/uploadFile/reviewImage/");

    private final String path;

    FileUploadPath(String path) {
        this.path = path;
    }
}
