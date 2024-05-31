package com.wqa.cems.model.vo;

import com.wqa.cems.model.entity.Task;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class TaskVO implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 服务类型
     */
    private String serviceType;

    /**
     * 物品名称
     */
    private String itemName;

    /**
     * 物品重量
     */
    private String itemWeight;

    /**
     * 描述信息
     */
    private String description;

    /**
     * 任务状态(待接单，已接单，配送中，已完成，已评论，已取消)
     */
    private String status;

    /**
     * 订单金额
     */
    private BigDecimal price;

    private static final long serialVersionUID = 1L;

    public static TaskVO objToVo(Task task) {
        if (task == null) {
            return null;
        }
        TaskVO taskVO = new TaskVO();
        BeanUtils.copyProperties(task, taskVO);
        return taskVO;
    }
}