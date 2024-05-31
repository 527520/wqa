package com.wqa.cems.service;

import com.wqa.cems.model.entity.Deliveryman;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wqa.cems.model.vo.DeliverymanVO;
import com.wqa.cems.model.vo.DeliverymanVOResponse;

import java.math.BigDecimal;
import java.util.List;

/**
* @author lenovo
* @description 针对表【deliveryman】的数据库操作Service
* @createDate 2024-03-07 21:55:39
*/
public interface DeliverymanService extends IService<Deliveryman> {

    List<Deliveryman> getAll();

    Long getIdByUserId(Long userId);

    boolean updateAccountStatus(String isOnline, String acceptingOrders, Long userId);

    boolean updateAcceptingOrdersById(String acceptingOrder, Long deliverymanId);

    List<DeliverymanVOResponse> getAllInline();

    BigDecimal getRatingById(Long deliverymanId);

    boolean updateAverageRatingById(BigDecimal newAverageRating, Long reviewedId);

    DeliverymanVO getDeliveryVOById(Long id);
}
