package com.wqa.cems.controller;

import com.wqa.cems.annotation.AuthCheck;
import com.wqa.cems.common.BaseResponse;
import com.wqa.cems.common.DeleteRequest;
import com.wqa.cems.common.ErrorCode;
import com.wqa.cems.common.ResultUtils;
import com.wqa.cems.constant.UserConstant;
import com.wqa.cems.exception.BusinessException;
import com.wqa.cems.model.dto.order.AddOrderNoteRequest;
import com.wqa.cems.model.dto.task.TaskUpdateStatusRequest;
import com.wqa.cems.model.entity.Order;
import com.wqa.cems.model.entity.Task;
import com.wqa.cems.model.entity.User;
import com.wqa.cems.model.vo.OrderVO;
import com.wqa.cems.service.DeliverymanService;
import com.wqa.cems.service.OrderService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.List;

import static com.wqa.cems.constant.UserConstant.USER_LOGIN_STATE;

@RestController
@Log4j2
@RequestMapping("/order")
public class OrderController {

    @Resource
    private OrderService orderService;

    @PostMapping("/delete/my")
    public BaseResponse<Boolean> deleteMyOrder(@RequestBody DeleteRequest deleteRequest) {
        if (deleteRequest == null || deleteRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return ResultUtils.success(orderService.deleteMyOrder(deleteRequest.getId()));
    }

    /**
     * 根据任务id获取订单信息
     */
    @GetMapping("/get/vo")
    public BaseResponse<OrderVO> getOrderVO(long taskId) { // 任务id
        if (taskId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return ResultUtils.success(orderService.getOrderVo(taskId));
    }

    /**
     * 配送员正在进行的订单
     */
    @GetMapping("/deliveryman/get/vo")
    @AuthCheck(mustRole = UserConstant.DELIVERYMAN_Role)
    public BaseResponse<OrderVO> deliverymanGetOrderVO(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj; // 配送员
        if (user == null || user.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        OrderVO orderVO = orderService.deliverymanGetOrderVO(user.getId());
        return ResultUtils.success(orderVO);
    }

    /**
     * 完成订单(任务完成的时候自动完成)
     */

    /**
     * 更新状态
     */
    @PostMapping("/update/status")
    public BaseResponse<Boolean> updateStatus(@RequestBody TaskUpdateStatusRequest taskUpdateStatusRequest,
                                              HttpSession session) {
        if (taskUpdateStatusRequest == null ||
                taskUpdateStatusRequest.getId() == null ||
                taskUpdateStatusRequest.getOrderId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long id = taskUpdateStatusRequest.getId();
        Long orderId = taskUpdateStatusRequest.getOrderId();
        String status = taskUpdateStatusRequest.getStatus();
        if (StringUtils.isAnyBlank(status)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return ResultUtils.success(orderService.updateStatus(id, orderId, status, session));
    }

    /**
     * 添加订单备注
     */
    @PostMapping("/add/orderNote")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public BaseResponse<Boolean> addOrderNote(@RequestBody AddOrderNoteRequest addOrderNoteRequest) {
        if (addOrderNoteRequest == null || addOrderNoteRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String orderNote = addOrderNoteRequest.getOrderNote();
        if (StringUtils.isAnyBlank(orderNote)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        return ResultUtils.success(orderService.addOrderNote(addOrderNoteRequest));
    }
}
