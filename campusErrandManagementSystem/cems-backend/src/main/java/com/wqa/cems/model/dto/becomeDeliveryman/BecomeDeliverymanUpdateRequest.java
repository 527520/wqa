package com.wqa.cems.model.dto.becomeDeliveryman;

import lombok.Data;

import java.io.Serializable;

@Data
public class BecomeDeliverymanUpdateRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 状态
     */
    private String status;

    /**
     * 如果未通过展示给用户的消息
     */
    private String message;

    private static final long serialVersionUID = 1L;
}
