package com.wqa.cems.service.impl;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wqa.cems.common.ErrorCode;
import com.wqa.cems.constant.UserConstant;
import com.wqa.cems.exception.BusinessException;
import com.wqa.cems.exception.ThrowUtils;
import com.wqa.cems.model.dto.order.AddOrderNoteRequest;
import com.wqa.cems.model.entity.Deliveryman;
import com.wqa.cems.model.entity.Order;
import com.wqa.cems.model.entity.Task;
import com.wqa.cems.model.entity.User;
import com.wqa.cems.model.vo.DeliverymanVOVO;
import com.wqa.cems.model.vo.OrderVO;
import com.wqa.cems.model.vo.TaskVO;
import com.wqa.cems.service.DeliverymanService;
import com.wqa.cems.service.OrderService;
import com.wqa.cems.mapper.OrderMapper;
import com.wqa.cems.service.TaskService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.wqa.cems.constant.UserConstant.USER_LOGIN_STATE;

/**
 * @author lenovo
 * @description 针对表【order】的数据库操作Service实现
 * @createDate 2024-03-11 22:51:50
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order>
        implements OrderService {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private TaskService taskService;

    @Resource
    private DeliverymanService deliverymanService;

    @Override
    public boolean insertInto(Task taskInfo, Long deliverymanId) {
        Order order = new Order();
        order.setUserId(taskInfo.getUserId());
        order.setDeliverymanId(deliverymanId);
        order.setTaskId(taskInfo.getId());
        order.setOrderTime(taskInfo.getPublishTime());
        order.setFetchAddress(taskInfo.getFetchAddress());
        String shippingAddress = taskInfo.getShippingAddress();
        order.setShippingAddress(shippingAddress);
        String consignee = extractValue(shippingAddress, "consignee='(.*?)'");
        String phoneNumber = extractValue(shippingAddress, "phoneNumber='(.*?)'");
        if (StringUtils.isAnyBlank(consignee, phoneNumber)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        order.setReceiverName(consignee);
        order.setReceiverPhoneNumber(phoneNumber);
        order.setOrderAmount(taskInfo.getPrice());
        order.setEstimatedCompletionTime(taskInfo.getEstimatedCompletionTime());
        Order order1 = orderMapper.selectOneByTaskId(taskInfo.getId());
        // 确保一个任务对应一个订单，有则更新，无则新建
        if (order1 != null) {
            order.setId(order1.getId());
            return updateById(order);
        }
        return save(order);
    }

    @Override
    public boolean deleteMyOrder(Long id) {
        // 查询当前订单状态，只有已完成和已取消的订单可以删除
        String status = orderMapper.selectStatusById(id);
        if (!(status.equals("已完成") || status.equals("已取消"))) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "未完成的订单不能删除");
        }
        boolean result = removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return true;
    }

    @Override
    public OrderVO getOrderVo(long taskId) {
        Task task = taskService.getById(taskId);
        Long deliverymanId = task.getDeliverymanId();
        TaskVO taskVO = TaskVO.objToVo(task);
        if (deliverymanId == null) { // 如果配送员id为null表示没有被接单（待接单、已取消）
            OrderVO orderVO = new OrderVO();

            orderVO.setTask(taskVO);
            return orderVO;
        }
        Order order = orderMapper.selectOneByTaskId(taskId);
        OrderVO orderVO = OrderVO.objToVo(order);
        Deliveryman deliveryman = deliverymanService.getById(deliverymanId);
        DeliverymanVOVO deliverymanVOVO = DeliverymanVOVO.objToVO(deliveryman);
        orderVO.setDeliveryman(deliverymanVOVO);
        orderVO.setTask(taskVO);
        return orderVO;
    }

    private static String extractValue(String input, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return null;
        }
    }

    @Override
    public boolean updateStatus(Long id, Long orderId, String status, HttpSession session) {
        User user = (User) session.getAttribute(USER_LOGIN_STATE);
        Task task = new Task();
        String oldStatus = taskService.getStatusById(id);
        //配送中，已送达，已完成，已取消
        // 管理员都可以
        switch (status) {
            case "配送中":// 配送员
            case "已送达"://已送达(配送员确认)
                // 走到这里，这个use就是配送员
                if (user.getRole().equals(UserConstant.DEFAULT_ROLE)) {
                    throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
                }
                // 更新配送员的状态
                Long deliverymanId = deliverymanService.getIdByUserId(user.getId());
                // 完成订单数加一（已改）
                boolean b = deliverymanService.updateAcceptingOrdersById("未接单", deliverymanId);
                // 任务表添加实际完成时间
                task.setCompletionTime(DateTime.now());
                ThrowUtils.throwIf(!b, ErrorCode.OPERATION_ERROR);
                break;
            case "已完成"://已完成(下单用户确认) 还要更新订单表和任务表
                if (user.getRole().equals(UserConstant.DELIVERYMAN_Role)) {
                    throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
                }
                if (!"已送达".equals(oldStatus)) {
                    if (!user.getRole().equals(UserConstant.ADMIN_ROLE)) {
                        throw new BusinessException(ErrorCode.OPERATION_ERROR,
                                "配送员没有送达或配送员没有点击送达，如需要请联系配送员");
                    }
                }
                Order order = new Order();
                order.setId(orderId);
                order.setCompletionTime(task.getCompletionTime());
                order.setStatus("已完成");
                boolean result = updateById(order);
                ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
                break;
            case "已取消":// 用户取消
                if (!(user.getRole().equals(UserConstant.DEFAULT_ROLE))) {
                    throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
                }
                if (!oldStatus.equals("待接单")) {
                    throw new BusinessException(ErrorCode.OPERATION_ERROR, "进行中的订单不能取消哦，要取消请联系管理员");
                }
                break;
            default:
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数错误");
        }
        task.setStatus(status);
        task.setId(id);
        boolean result = taskService.updateById(task);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return true;
    }

    /**
     * 查询配送员正在进行中的订单
     *
     * @param userId
     * @return
     */
    @Override
    public OrderVO deliverymanGetOrderVO(Long userId) {
        Long deliverymanId = deliverymanService.getIdByUserId(userId);
        Task task = taskService.getTaskByDeliverymanIdAndStatus(deliverymanId, "已接单", "配送中");
        if (task == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "未查询到进行中的订单，请前往接单");
        }
        TaskVO taskVO = TaskVO.objToVo(task);
        Order order = orderMapper.selectOneByTaskId(taskVO.getId());
        OrderVO orderVO = OrderVO.objToVo(order);
        orderVO.setTask(taskVO);
        return orderVO;
    }

    @Override
    public boolean addOrderNote(AddOrderNoteRequest addOrderNoteRequest) {
        Order order = new Order();
        BeanUtils.copyProperties(addOrderNoteRequest, order);
        return updateById(order);
    }
}




