package com.wqa.cems.model.dto.task;

import lombok.Data;

import java.io.Serializable;

@Data
public class AssignTaskRequest implements Serializable {

    private Long taskId;
    private Long deliverymanId;

    private static final long serialVersionUID = 1L;
}
