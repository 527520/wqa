package com.wqa.cems.model.dto.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class TaskAddRequest implements Serializable {

    /**
     * 服务类型
     */
    private String serviceType;

    /**
     * 物品名称
     */
    private String itemName;

    /**
     * 取件地址
     */
    private String fetchAddress;

    /**
     * 收货地址
     */
    private String shippingAddress;

    /**
     * 物品重量
     */
    private String itemWeight;

    /**
     * 描述信息
     */
    private String description;

    /**
     * 用户期望完成时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "UTC")
    private Date estimatedCompletionTime;

    /**
     * 任务价格
     */
    private BigDecimal price;

    private static final long serialVersionUID = 1L;
}
