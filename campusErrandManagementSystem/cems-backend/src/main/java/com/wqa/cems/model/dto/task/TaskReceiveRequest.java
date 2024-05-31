package com.wqa.cems.model.dto.task;

import lombok.Data;

import java.io.Serializable;

/**
 *
 */
@Data
public class TaskReceiveRequest implements Serializable {

    /**
     * 任务id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}
