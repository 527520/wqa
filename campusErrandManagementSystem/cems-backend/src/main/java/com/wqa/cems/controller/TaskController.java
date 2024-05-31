package com.wqa.cems.controller;

import com.wqa.cems.annotation.AuthCheck;
import com.wqa.cems.common.BaseResponse;
import com.wqa.cems.common.DeleteRequest;
import com.wqa.cems.common.ErrorCode;
import com.wqa.cems.common.ResultUtils;
import com.wqa.cems.constant.UserConstant;
import com.wqa.cems.exception.BusinessException;
import com.wqa.cems.exception.ThrowUtils;
import com.wqa.cems.model.dto.task.*;
import com.wqa.cems.model.entity.Deliveryman;
import com.wqa.cems.model.entity.Task;
import com.wqa.cems.model.entity.User;
import com.wqa.cems.service.DeliverymanService;
import com.wqa.cems.service.OrderService;
import com.wqa.cems.service.TaskService;
import lombok.extern.log4j.Log4j2;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.List;

import static com.wqa.cems.constant.UserConstant.USER_LOGIN_STATE;

@RestController
@RequestMapping("/task")
@Log4j2
public class TaskController {

    @Resource
    private TaskService taskService;
    @Resource
    private DeliverymanService deliverymanService;
    @Resource
    private OrderService orderService;

    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public BaseResponse<Boolean> addTask(@RequestBody TaskAddRequest taskAddRequest, HttpSession session) {
        if (taskAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = taskService.addTask(taskAddRequest, session);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    @PostMapping("/get/byStatus")
    @AuthCheck(mustRole = UserConstant.DEFAULT_ROLE)
    public BaseResponse<List<Task>> getTask(@RequestBody String status, HttpSession session) {
        User user = (User) session.getAttribute(USER_LOGIN_STATE);
        Long userId = user.getId();
        /*
        all: "全部订单",
        waitingOrder: "待接单",
        toBeDelivered: "待送达",
        toBeReceived: "待收货",
        toBeComment: "待评论",
        待接单，已接单，配送中，已送达，已完成，已评论，已取消
         */
        List<Task> taskList;
        switch (status) {
            case "全部订单":
                taskList = taskService.getByUserId(userId);
                break;
            case "待接单"://待接单
                taskList = taskService.getByUserIdAndStatus(userId, "待接单", null);
                break;
            case "待送达"://已接单，配送中
                taskList = taskService.getByUserIdAndStatus(userId, "已接单", "配送中");
                break;
            case "待收货"://已送达(配送员确认)
                taskList = taskService.getByUserIdAndStatus(userId, "已送达", null);
                break;
            case "待评论"://已完成(下单用户确认)
                taskList = taskService.getByUserIdAndStatus(userId, "已完成", null);
                break;
            case "已取消"://已取消
                taskList = taskService.getByUserIdAndStatus(userId, "已取消", null);
                break;
            default:
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数错误");
        }
        return ResultUtils.success(taskList);
    }

    @GetMapping("/get/byDeliveryman")
    public BaseResponse<List<Task>> getTaskByDeliveryman(HttpSession session) {
        User user = (User) session.getAttribute(USER_LOGIN_STATE);
        if (user.getRole().equals(UserConstant.DEFAULT_ROLE) || user.getRole().equals(UserConstant.BAN_ROLE)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        return ResultUtils.success(taskService.getAllWaitingOrder());
    }

    /**
     * 任务接取
     * 接取成功自动生成订单表
     * TaskReceiveRequest
     */
    @Transactional
    @PostMapping("/receive")
    @AuthCheck(mustRole = UserConstant.DELIVERYMAN_Role)
    public BaseResponse<Boolean> receiveTask(@RequestBody TaskReceiveRequest taskReceiveRequest, HttpSession session) {
        if (taskReceiveRequest == null || taskReceiveRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long taskId = taskReceiveRequest.getId();// 任务id
        User user = (User) session.getAttribute(USER_LOGIN_STATE);//这个user是配送员
        Long userId = user.getId();
        // 先查询有没有被接单
        String status = taskService.getStatusById(taskId);
        if (!"待接单".equals(status)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "该订单已被接单或以取消");
        }
        Task task = new Task();
        task.setId(taskId);
        // 查询自己的配送员id
        Long deliverymanId = deliverymanService.getIdByUserId(userId);
        // 暂停接单和离职也不能接单
        Deliveryman deliveryman = deliverymanService.getById(deliverymanId);
        String status1 = deliveryman.getStatus();
        ThrowUtils.throwIf(!"正常".equals(status1), ErrorCode.OPERATION_ERROR, "账号状态异常无法接单");
        // 已接单不能再接单
        String acceptingOrders = deliveryman.getAcceptingOrders();// deliverymanService.getAcceptingOrdersById(deliverymanId);
        ThrowUtils.throwIf(!"未接单".equals(acceptingOrders), ErrorCode.OPERATION_ERROR, "请先完成你的订单");
        task.setDeliverymanId(deliverymanId);// 配送员id
        task.setStatus("已接单");
        synchronized (this) { // 加锁，保证同时只有一个能接单
            boolean result = taskService.updateById(task);
            ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
            //  生成订单表(事务)
            Task taskInfo = taskService.getById(taskId);
            // 更新自己的接单状态
            result = deliverymanService.updateAcceptingOrdersById("已接单", deliverymanId);
            ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
            // 调用order自己的service保存数据
            boolean save = orderService.insertInto(taskInfo, deliverymanId);
            ThrowUtils.throwIf(!save, ErrorCode.OPERATION_ERROR);
            return ResultUtils.success(true);
        }
    }

    /**
     * 更新信息
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> update(@RequestBody TaskUpdateRequest taskUpdateRequest, HttpSession session) {
        if (taskUpdateRequest == null || taskUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return ResultUtils.success(taskService.updateTaskById(taskUpdateRequest, session));
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> delete(@RequestBody DeleteRequest deleteRequest, HttpSession session) {
        if (deleteRequest == null || deleteRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String role = ((User) session.getAttribute(USER_LOGIN_STATE)).getRole();
        // 只能用户和管理员能删除
        if (role.equals(UserConstant.DELIVERYMAN_Role)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = taskService.removeById(deleteRequest.getId());
        ThrowUtils.throwIf(!b, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 管理员分配任务
     */
    @PostMapping("/assign")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> assignTask(@RequestBody AssignTaskRequest assignTaskRequest) {
        if (assignTaskRequest == null || assignTaskRequest.getTaskId() == null
                || assignTaskRequest.getDeliverymanId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        Long deliverymanId = assignTaskRequest.getDeliverymanId();
        Long taskId = assignTaskRequest.getTaskId();
        Task task = new Task();
        task.setId(taskId);
        task.setDeliverymanId(deliverymanId);// 配送员id
        task.setStatus("已接单");
        boolean result = taskService.updateById(task);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        Task taskInfo = taskService.getById(taskId);
        // 根据id更新跑腿员的接单状态
        result = deliverymanService.updateAcceptingOrdersById("已接单", deliverymanId);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        // 调用order自己的service保存数据
        boolean save = orderService.insertInto(taskInfo, deliverymanId);
        ThrowUtils.throwIf(!save, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 查询配送员所有接过的单(历史订单)
     */
    @GetMapping("/deliveryman/getAllOrder")
    @AuthCheck(mustRole = UserConstant.DELIVERYMAN_Role)
    public BaseResponse<List<Task>> getAllOrderForDeliveryman(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj; // 配送员
        if (user == null || user.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return ResultUtils.success(taskService.getTaskByDeliverymanId(user.getId()));
    }
}
