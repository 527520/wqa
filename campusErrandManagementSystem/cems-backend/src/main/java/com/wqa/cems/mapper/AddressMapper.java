package com.wqa.cems.mapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.wqa.cems.model.entity.Address;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author lenovo
* @description 针对表【address】的数据库操作Mapper
* @createDate 2024-03-11 22:01:49
* @Entity generator.domain.Address
*/
public interface AddressMapper extends BaseMapper<Address> {

    Address selectOneByUserIdAndIsDefault(@Param("userId") Long userId);

    List<Address> selectAllByUserIdOrderByIsDefaultDesc(@Param("userId") Long userId);

    int updateIsDefaultByUserIdAndIsDefault(@Param("userId") Long userId);
}




