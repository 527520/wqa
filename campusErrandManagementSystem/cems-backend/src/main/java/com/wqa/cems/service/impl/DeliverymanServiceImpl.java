package com.wqa.cems.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wqa.cems.model.entity.Deliveryman;
import com.wqa.cems.model.vo.DeliverymanVO;
import com.wqa.cems.model.vo.DeliverymanVOResponse;
import com.wqa.cems.service.DeliverymanService;
import com.wqa.cems.mapper.DeliverymanMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author lenovo
 * @description 针对表【deliveryman】的数据库操作Service实现
 * @createDate 2024-03-07 21:55:39
 */
@Service
public class DeliverymanServiceImpl extends ServiceImpl<DeliverymanMapper, Deliveryman>
        implements DeliverymanService {

    @Resource
    private DeliverymanMapper deliverymanMapper;

    @Override
    public List<Deliveryman> getAll() {
        return deliverymanMapper.getAll();
    }

    @Override
    public Long getIdByUserId(Long userId) {
        return deliverymanMapper.selectIdByUserId(userId);
    }

    @Override
    public boolean updateAccountStatus(String isOnline, String acceptingOrders, Long userId) {
        return deliverymanMapper.updateIsOnlineAndAcceptingOrdersByUserId(isOnline, acceptingOrders, userId) > 0;
    }

    @Override
    public boolean updateAcceptingOrdersById(String acceptingOrder, Long deliverymanId) {
        return deliverymanMapper.updateAcceptingOrdersById(acceptingOrder, deliverymanId) > 0;
    }

    @Override
    public List<DeliverymanVOResponse> getAllInline() {
        return deliverymanMapper.selectAllByIsOnlineAndAcceptingOrdersAndStatus();
    }

    @Override
    public BigDecimal getRatingById(Long deliverymanId) {
        return deliverymanMapper.selectCompletedOrdersAndAverageRatingById(deliverymanId);
    }

    @Override
    public boolean updateAverageRatingById(BigDecimal newAverageRating, Long reviewedId) {
        return deliverymanMapper.updateAverageRatingById(newAverageRating, reviewedId) > 0;
    }

    @Override
    public DeliverymanVO getDeliveryVOById(Long id) {
        QueryWrapper<Deliveryman> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        Deliveryman deliveryman = deliverymanMapper.selectOne(queryWrapper);
        DeliverymanVO deliverymanVO = new DeliverymanVO();
        BeanUtils.copyProperties(deliveryman, deliverymanVO);
        return deliverymanVO;
    }
}




