package com.wqa.cems.mapper;

import java.util.List;

import com.wqa.cems.model.entity.Review;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author lenovo
 * @description 针对表【review】的数据库操作Mapper
 * @createDate 2024-03-21 00:23:40
 * @Entity generator.domain.Review
 */
public interface ReviewMapper extends BaseMapper<Review> {

    List<Review> selectAll();

    List<Review> searchReviews(@Param("searchKeyword") String searchKeyword);
}




