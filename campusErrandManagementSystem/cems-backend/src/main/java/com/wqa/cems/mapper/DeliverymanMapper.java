package com.wqa.cems.mapper;
import com.wqa.cems.model.vo.DeliverymanVOResponse;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

import com.wqa.cems.model.entity.Deliveryman;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author lenovo
* @description 针对表【deliveryman】的数据库操作Mapper
* @createDate 2024-03-07 21:55:39
* @Entity generator.domain.Deliveryman
*/
public interface DeliverymanMapper extends BaseMapper<Deliveryman> {

    List<Deliveryman> getAll();

    Long selectIdByUserId(@Param("userId") Long userId);

    int updateIsOnlineAndAcceptingOrdersByUserId(@Param("isOnline") String isOnline, @Param("acceptingOrders") String acceptingOrders, @Param("userId") Long userId);

    int updateAcceptingOrdersById(@Param("acceptingOrders") String acceptingOrders, @Param("id") Long id);

    List<DeliverymanVOResponse> selectAllByIsOnlineAndAcceptingOrdersAndStatus();

    BigDecimal selectCompletedOrdersAndAverageRatingById(@Param("id") Long id);

    int updateAverageRatingById(@Param("averageRating") BigDecimal averageRating, @Param("id") Long id);
}




