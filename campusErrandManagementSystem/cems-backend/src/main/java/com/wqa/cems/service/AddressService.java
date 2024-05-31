package com.wqa.cems.service;

import com.wqa.cems.model.dto.address.AddressAddRequest;
import com.wqa.cems.model.dto.address.AddressUpdateRequest;
import com.wqa.cems.model.entity.Address;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
* @author lenovo
* @description 针对表【address】的数据库操作Service
* @createDate 2024-03-11 22:01:49
*/
public interface AddressService extends IService<Address> {

    boolean saveAddress(AddressAddRequest addressAddRequest, HttpSession session);

    List<Address> getAll(HttpSession session);

    boolean updateAddress(AddressUpdateRequest addressUpdateRequest, HttpSession session);
}
