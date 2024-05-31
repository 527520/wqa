package com.wqa.cems.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wqa.cems.common.ErrorCode;
import com.wqa.cems.exception.BusinessException;
import com.wqa.cems.exception.ThrowUtils;
import com.wqa.cems.model.dto.becomeDeliveryman.BecomeDeliverymanUpdateRequest;
import com.wqa.cems.model.entity.BecomeDeliveryman;
import com.wqa.cems.model.entity.Deliveryman;
import com.wqa.cems.model.enums.FileUploadPath;
import com.wqa.cems.model.vo.BecomeDeliverymanVO;
import com.wqa.cems.service.BecomeDeliverymanService;
import com.wqa.cems.mapper.BecomeDeliverymanMapper;
import com.wqa.cems.service.DeliverymanService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author lenovo
 * @description 针对表【become_deliveryman】的数据库操作Service实现
 * @createDate 2024-03-07 23:08:30
 */
@Service
public class BecomeDeliverymanServiceImpl extends ServiceImpl<BecomeDeliverymanMapper, BecomeDeliveryman>
        implements BecomeDeliverymanService {

    @Resource
    private BecomeDeliverymanMapper becomeDeliverymanMapper;
    @Resource
    private DeliverymanService deliverymanService;

    @Override
    public BecomeDeliverymanVO getStatusAndMessage(Long id) {
        return becomeDeliverymanMapper.selectOneVOByUserId(id);
    }

    @Override
    public BecomeDeliveryman getOneByUserId(Long userId) {
        return becomeDeliverymanMapper.selectOneByUserId(userId);
    }

    @Transactional
    @Override
    public boolean updateStatus(BecomeDeliverymanUpdateRequest becomeDeliverymanUpdateRequest) {
        if ("审核通过".equals(becomeDeliverymanUpdateRequest.getStatus())) {
            // 审核通过要将数据拷贝到跑腿员表中
            BecomeDeliveryman becomeDeliveryman = getById(becomeDeliverymanUpdateRequest.getId());
            Deliveryman deliveryman = new Deliveryman();
            BeanUtils.copyProperties(becomeDeliveryman, deliveryman);
            // 先查询有没有该用户的信息，如有则修改
            Long deliverymanId = deliverymanService.getIdByUserId(becomeDeliveryman.getUserId());
            boolean save;
            deliveryman.setCreateTime(null);
            if (deliverymanId != null) {
                // 如果存在，重置参数
                deliveryman.setId(deliverymanId);
                deliveryman.setStatus("正常");
                deliveryman.setCompletedOrders(0);
                deliveryman.setAverageRating(BigDecimal.valueOf(100));
                save = deliverymanService.updateById(deliveryman);
            } else {
                deliveryman.setStatus(null);
                deliveryman.setUpdateTime(null);
                save = deliverymanService.save(deliveryman);
            }
            if (!save) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "系统错误，请稍后再试");
            }
        } else if ("审核失败".equals(becomeDeliverymanUpdateRequest.getStatus())) {
            if (StringUtils.isAnyBlank(becomeDeliverymanUpdateRequest.getMessage())) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "请填写未通过理由");
            }
        }
        BecomeDeliveryman becomeDeliveryman = new BecomeDeliveryman();
        BeanUtils.copyProperties(becomeDeliverymanUpdateRequest, becomeDeliveryman);
        becomeDeliveryman.setName(null);
        boolean result = updateById(becomeDeliveryman);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return true;
    }

    @Override
    public List<BecomeDeliveryman> getAll() {
        return becomeDeliverymanMapper.getAllByStatus();
    }
}




