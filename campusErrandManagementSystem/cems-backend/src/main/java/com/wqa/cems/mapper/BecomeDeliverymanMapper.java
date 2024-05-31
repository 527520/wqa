package com.wqa.cems.mapper;
import java.util.List;

import com.wqa.cems.model.vo.BecomeDeliverymanVO;
import org.apache.ibatis.annotations.Param;

import com.wqa.cems.model.entity.BecomeDeliveryman;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author lenovo
* @description 针对表【become_deliveryman】的数据库操作Mapper
* @createDate 2024-03-07 23:08:30
* @Entity generator.domain.BecomeDeliveryman
*/
public interface BecomeDeliverymanMapper extends BaseMapper<BecomeDeliveryman> {


    BecomeDeliverymanVO selectOneVOByUserId(@Param("userId") Long userId);

    BecomeDeliveryman selectOneByUserId(@Param("userId") Long userId);

    List<BecomeDeliveryman> getAllByStatus();
}




