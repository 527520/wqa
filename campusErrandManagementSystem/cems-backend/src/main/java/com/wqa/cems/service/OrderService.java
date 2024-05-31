package com.wqa.cems.service;

import com.wqa.cems.model.dto.order.AddOrderNoteRequest;
import com.wqa.cems.model.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wqa.cems.model.entity.Task;
import com.wqa.cems.model.vo.OrderVO;

import javax.servlet.http.HttpSession;

/**
* @author lenovo
* @description 针对表【order】的数据库操作Service
* @createDate 2024-03-11 22:51:50
*/
public interface OrderService extends IService<Order> {
    boolean insertInto(Task taskInfo, Long deliverymanId);

    boolean deleteMyOrder(Long id);

    OrderVO getOrderVo(long taskId);

    boolean updateStatus(Long id, Long orderId, String status, HttpSession session);

    OrderVO deliverymanGetOrderVO(Long userId);

    boolean addOrderNote(AddOrderNoteRequest addOrderNoteRequest);
}
