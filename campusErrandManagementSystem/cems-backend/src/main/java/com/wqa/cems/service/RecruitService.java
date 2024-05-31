package com.wqa.cems.service;

import com.wqa.cems.model.dto.recruit.RecruitUpdateRequest;
import com.wqa.cems.model.entity.Recruit;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author lenovo
* @description 针对表【recruit】的数据库操作Service
* @createDate 2024-03-22 16:56:00
*/
public interface RecruitService extends IService<Recruit> {

    boolean addRecruit(String content, String title);

    boolean updateRecruit(RecruitUpdateRequest recruitUpdateRequest);

    List<Recruit> getAll();

    List<Recruit> getRecruitsBySearch(String searchValue);
}
