package com.wqa.cems.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wqa.cems.common.ErrorCode;
import com.wqa.cems.exception.BusinessException;
import com.wqa.cems.exception.ThrowUtils;
import com.wqa.cems.model.dto.recruit.RecruitUpdateRequest;
import com.wqa.cems.model.entity.Recruit;
import com.wqa.cems.service.RecruitService;
import com.wqa.cems.mapper.RecruitMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author lenovo
 * @description 针对表【recruit】的数据库操作Service实现
 * @createDate 2024-03-22 16:56:00
 */
@Service
public class RecruitServiceImpl extends ServiceImpl<RecruitMapper, Recruit>
        implements RecruitService {

    @Resource
    private RecruitMapper recruitMapper;

    @Override
    public boolean addRecruit(String content, String title) {
        if (StringUtils.isAnyBlank(content, title)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        Recruit recruit = new Recruit();
        recruit.setContent(content);
        recruit.setTitle(title);
        boolean result = save(recruit);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return true;
    }

    @Override
    public boolean updateRecruit(RecruitUpdateRequest recruitUpdateRequest) {
        Recruit recruit = new Recruit();
        BeanUtils.copyProperties(recruitUpdateRequest, recruit);
        boolean result = updateById(recruit);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return true;
    }

    @Override
    public List<Recruit> getAll() {
        QueryWrapper<Recruit> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("publish_time");
        return recruitMapper.selectList(queryWrapper);
    }

    @Override
    public List<Recruit> getRecruitsBySearch(String searchValue) {
        return recruitMapper.searchAllByContentLikeOrTitleLikeOrderByPublishTimeDesc(searchValue);
    }
}




