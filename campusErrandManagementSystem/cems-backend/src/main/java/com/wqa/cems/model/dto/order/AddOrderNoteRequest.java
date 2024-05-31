package com.wqa.cems.model.dto.order;

import lombok.Data;

import java.io.Serializable;

@Data
public class AddOrderNoteRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 订单备注
     */
    private String orderNote;

    private static final long serialVersionUID = 1L;
}
