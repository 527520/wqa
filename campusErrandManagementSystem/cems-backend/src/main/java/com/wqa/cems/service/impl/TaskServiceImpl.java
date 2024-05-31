package com.wqa.cems.service.impl;

import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wqa.cems.common.ErrorCode;
import com.wqa.cems.constant.UserConstant;
import com.wqa.cems.exception.BusinessException;
import com.wqa.cems.exception.ThrowUtils;
import com.wqa.cems.model.dto.task.TaskAddRequest;
import com.wqa.cems.model.dto.task.TaskUpdateRequest;
import com.wqa.cems.model.entity.Task;
import com.wqa.cems.model.entity.User;
import com.wqa.cems.service.DeliverymanService;
import com.wqa.cems.service.OrderService;
import com.wqa.cems.service.TaskService;
import com.wqa.cems.mapper.TaskMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static com.wqa.cems.constant.UserConstant.USER_LOGIN_STATE;

/**
 * @author lenovo
 * @description 针对表【task】的数据库操作Service实现
 * @createDate 2024-03-10 19:38:17
 */
@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task>
        implements TaskService {

    @Resource
    private TaskMapper taskMapper;
    @Resource
    private DeliverymanService deliverymanService;

    @Override
    public boolean addTask(TaskAddRequest taskAddRequest, HttpSession session) {
        User user = (User) session.getAttribute(USER_LOGIN_STATE);
        String serviceType = taskAddRequest.getServiceType();
        String itemName = taskAddRequest.getItemName();
        String fetchAddress = taskAddRequest.getFetchAddress();
        String shippingAddress = taskAddRequest.getShippingAddress();
        String description = taskAddRequest.getDescription();
        String itemWeight = taskAddRequest.getItemWeight();
        Date estimatedCompletionTime = taskAddRequest.getEstimatedCompletionTime();
        BigDecimal price = taskAddRequest.getPrice();
        if (StringUtils.isAnyBlank(itemName, serviceType, fetchAddress, shippingAddress, description, itemWeight) ||
                estimatedCompletionTime == null || price == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        // 比较时间
        Date currentTime = new Date(); // 获取当前时间
        // 比较 estimatedCompletionTime 是否小于或等于当前时间
        if (estimatedCompletionTime.before(currentTime) || estimatedCompletionTime.equals(currentTime)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数错误，期望完成时间已过");
        }
        if (price.compareTo(BigDecimal.ZERO) < 0 || price.scale() > 2) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数错误，请输入正确金额");
        }
        if (!NumberUtil.isInteger(itemWeight)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数错误，重量请输入整数(四舍五入)");
        }
        int itemWeightInt = NumberUtil.parseInt(itemWeight);
        if (itemWeightInt <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数错误，重量不能小于等于0");
        } else if (itemWeightInt < 5) {
            taskAddRequest.setItemWeight("小于五公斤");
        } else if (itemWeightInt <= 20) {
            taskAddRequest.setItemWeight(itemWeightInt + "公斤");
        } else {
            taskAddRequest.setItemWeight("大于20公斤");
        }
        Task task = new Task();
        BeanUtils.copyProperties(taskAddRequest, task);
        task.setUserId(user.getId());
        return save(task);
    }

    @Override
    public List<Task> getByUserId(Long userId) {
        return taskMapper.selectAllByUserId(userId);
    }

    @Override
    public List<Task> getByUserIdAndStatus(Long userId, String status1, String status2) {
        return taskMapper.selectAllByUserIdAndStatusOrStatusOrderByPublishTimeDesc(userId, status1, status2);
    }

    @Override
    public String getStatusById(Long id) {
        return taskMapper.getStatusById(id);
    }

    @Override
    public Boolean updateTaskById(TaskUpdateRequest taskUpdateRequest, HttpSession session) {
        String role = ((User) session.getAttribute(USER_LOGIN_STATE)).getRole();
        // 只能用户和管理员能修改信息
        if (role.equals(UserConstant.DELIVERYMAN_Role)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        Date estimatedCompletionTime = taskUpdateRequest.getEstimatedCompletionTime();
        if (estimatedCompletionTime != null) {
            // 比较时间
            Date currentTime = new Date(); // 获取当前时间
            // 比较 estimatedCompletionTime 是否小于或等于当前时间
            if (estimatedCompletionTime.before(currentTime) || estimatedCompletionTime.equals(currentTime)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数错误，期望完成时间已过");
            }
        }
        BigDecimal price = taskUpdateRequest.getPrice();
        if (price != null) {
            if (price.compareTo(BigDecimal.ZERO) < 0 || price.scale() > 2) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数错误，请输入正确金额");
            }
        }
        String itemWeight = taskUpdateRequest.getItemWeight();
        if (itemWeight != null) {
            if (!NumberUtil.isInteger(itemWeight)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数错误，重量请输入整数(四舍五入)");
            }
            int itemWeightInt = NumberUtil.parseInt(itemWeight);
            if (itemWeightInt <= 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数错误，重量不能小于等于0");
            } else if (itemWeightInt < 5) {
                taskUpdateRequest.setItemWeight("小于五公斤");
            } else if (itemWeightInt <= 20) {
                taskUpdateRequest.setItemWeight(itemWeightInt + "公斤");
            } else {
                taskUpdateRequest.setItemWeight("大于20公斤");
            }
        }
        Task task = new Task();
        BeanUtils.copyProperties(taskUpdateRequest, task);
        boolean result = updateById(task);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return true;
    }

    @Override
    public List<Task> getAllWaitingOrder() {
        return taskMapper.getAllByStatus();
    }

    @Override
    public Task getTaskByDeliverymanIdAndStatus(Long deliverymanId, String status1, String status2) {
        return taskMapper.selectOneByDeliverymanIdAndStatusOrStatus(deliverymanId, status1, status2);
    }

    @Override
    public List<Task> getTaskByDeliverymanId(Long id) { // 用户id
        Long deliverymanServiceId = deliverymanService.getIdByUserId(id);
        return taskMapper.selectAllByDeliverymanId(deliverymanServiceId);
    }
}




