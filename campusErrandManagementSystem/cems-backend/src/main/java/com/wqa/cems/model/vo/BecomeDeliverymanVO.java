package com.wqa.cems.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @TableName become_deliveryman
 */
@Data
public class BecomeDeliverymanVO implements Serializable {

    /**
     * 状态
     */
    private String status;

    /**
     * 如果未通过展示给用户的消息
     */
    private String message;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}