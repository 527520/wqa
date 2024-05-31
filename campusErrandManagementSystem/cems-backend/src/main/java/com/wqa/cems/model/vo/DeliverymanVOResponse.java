package com.wqa.cems.model.vo;

import com.wqa.cems.model.entity.Deliveryman;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class DeliverymanVOResponse implements Serializable {

    /**
     * 配送员id
     */
    private Long id;

    /**
     * 姓名
     */
    private String name;

    private static final long serialVersionUID = 1L;
}
