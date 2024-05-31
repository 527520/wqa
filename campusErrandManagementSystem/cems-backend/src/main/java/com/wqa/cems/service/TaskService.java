package com.wqa.cems.service;

import com.wqa.cems.model.dto.task.TaskAddRequest;
import com.wqa.cems.model.dto.task.TaskUpdateRequest;
import com.wqa.cems.model.entity.Task;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
* @author lenovo
* @description 针对表【task】的数据库操作Service
* @createDate 2024-03-10 19:38:17
*/
public interface TaskService extends IService<Task> {

    boolean addTask(TaskAddRequest taskAddRequest, HttpSession session);

    List<Task> getByUserId(Long userId);

    List<Task> getByUserIdAndStatus(Long userId, String status1, String status2);

    String getStatusById(Long id);

    Boolean updateTaskById(TaskUpdateRequest taskUpdateRequest, HttpSession session);

    List<Task> getAllWaitingOrder();

    Task getTaskByDeliverymanIdAndStatus(Long deliverymanId, String status1, String status2);

    List<Task> getTaskByDeliverymanId(Long id);
}
