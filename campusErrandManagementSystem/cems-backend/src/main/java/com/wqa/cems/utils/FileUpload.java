package com.wqa.cems.utils;

import cn.hutool.core.util.IdUtil;
import com.wqa.cems.common.ErrorCode;
import com.wqa.cems.exception.BusinessException;
import com.wqa.cems.model.enums.FileUploadPath;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Log4j2
public class FileUpload {

    public static String savaFile(MultipartFile file, Long userId, String type) {
        // 获取文件名
        String fileName = file.getOriginalFilename();
        assert fileName != null;
        String hzName = fileName.substring(fileName.lastIndexOf("."));
        fileName = IdUtil.simpleUUID() + hzName;
        String filePath = type + userId;
        File saveFile = new File(filePath);
        if (!saveFile.exists() || !saveFile.isDirectory()) {
            boolean mkdir = saveFile.mkdir();
            if (!mkdir) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "上传失败");
            }
        }
        String fileUrl;
        try {
            String path = saveFile.getCanonicalPath() + "\\" + fileName;
            file.transferTo(new File(path));
            fileUrl = FileUploadPath.URL.getPath() + userId + "/" + fileName;
        } catch (IOException e) {
            log.error("file upload error, filepath = " + filePath, e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "上传失败");
        }
        return fileUrl;
    }
}
