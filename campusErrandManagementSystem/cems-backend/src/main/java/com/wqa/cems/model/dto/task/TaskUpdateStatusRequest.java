package com.wqa.cems.model.dto.task;

import lombok.Data;

import java.io.Serializable;

@Data
public class TaskUpdateStatusRequest implements Serializable {

    /**
     * 任务id
     */
    private Long id;

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 状态
     */
    private String status;

    private static final long serialVersionUID = 1L;
}
