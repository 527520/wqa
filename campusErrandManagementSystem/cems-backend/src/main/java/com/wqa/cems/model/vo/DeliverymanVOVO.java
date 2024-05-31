package com.wqa.cems.model.vo;

import com.wqa.cems.model.entity.Deliveryman;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class DeliverymanVOVO implements Serializable {

    /**
     * 配送员id
     */
    private Long id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 手机号码
     */
    private String phoneNumber;

    /**
     * 平均评分
     */
    private BigDecimal averageRating;

    private static final long serialVersionUID = 1L;

    public static DeliverymanVOVO objToVO(Deliveryman deliveryman){
        if (deliveryman == null) {
            return null;
        }
        DeliverymanVOVO deliverymanVOVO = new DeliverymanVOVO();
        BeanUtils.copyProperties(deliveryman, deliverymanVOVO);
        return deliverymanVOVO;
    }
}
