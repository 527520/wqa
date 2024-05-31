package com.wqa.cems.service;

import com.wqa.cems.model.dto.becomeDeliveryman.BecomeDeliverymanUpdateRequest;
import com.wqa.cems.model.entity.BecomeDeliveryman;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wqa.cems.model.vo.BecomeDeliverymanVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
* @author lenovo
* @description 针对表【become_deliveryman】的数据库操作Service
* @createDate 2024-03-07 23:08:30
*/
public interface BecomeDeliverymanService extends IService<BecomeDeliveryman> {

    BecomeDeliverymanVO getStatusAndMessage(Long id);

    BecomeDeliveryman getOneByUserId(Long userId);

    boolean updateStatus(BecomeDeliverymanUpdateRequest becomeDeliverymanUpdateRequest);

    List<BecomeDeliveryman> getAll();
}
