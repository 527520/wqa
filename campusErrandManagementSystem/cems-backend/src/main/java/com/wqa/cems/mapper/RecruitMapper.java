package com.wqa.cems.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.wqa.cems.model.entity.Recruit;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author lenovo
 * @description 针对表【recruit】的数据库操作Mapper
 * @createDate 2024-03-22 16:56:00
 * @Entity generator.domain.Recruit
 */
public interface RecruitMapper extends BaseMapper<Recruit> {
    List<Recruit> searchAllByContentLikeOrTitleLikeOrderByPublishTimeDesc(@Param("searchValue") String searchValue);
}




