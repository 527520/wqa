package com.wqa.cems.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wqa.cems.common.ErrorCode;
import com.wqa.cems.exception.BusinessException;
import com.wqa.cems.exception.ThrowUtils;
import com.wqa.cems.model.dto.address.AddressAddRequest;
import com.wqa.cems.model.dto.address.AddressUpdateRequest;
import com.wqa.cems.model.entity.Address;
import com.wqa.cems.model.entity.User;
import com.wqa.cems.service.AddressService;
import com.wqa.cems.mapper.AddressMapper;
import com.wqa.cems.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author lenovo
 * @description 针对表【address】的数据库操作Service实现
 * @createDate 2024-03-11 22:01:49
 */
@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address>
        implements AddressService {

    @Resource
    private AddressMapper addressMapper;

    @Resource
    private UserService userService;


    @Override
    public boolean saveAddress(AddressAddRequest addressAddRequest, HttpSession session) {

        String address = addressAddRequest.getAddress();
        String consignee = addressAddRequest.getConsignee();
        String phoneNumber = addressAddRequest.getPhoneNumber();
        Integer isDefault = addressAddRequest.getIsDefault();
        User loginUser = userService.getLoginUser(session);
        if (StringUtils.isAnyBlank(address, consignee, phoneNumber)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        String regex = "^1[3456789]\\d{9}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNumber);
        // 手机号不合法
        if (!matcher.matches()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "手机号不合法");
        }
        // 如果设置为默认地址
        if (isDefault == 1) {
            // 去数据库里查有没有默认地址
            Address address1 = addressMapper.selectOneByUserIdAndIsDefault(loginUser.getId());
            if (address1 != null) {
                // 就将数据库里那个默认地址改为0
                address1.setIsDefault(0);
                boolean b = updateById(address1);
                ThrowUtils.throwIf(!b, ErrorCode.OPERATION_ERROR);
            }
        }
        Address address1 = new Address();
        BeanUtils.copyProperties(addressAddRequest, address1);
        address1.setUserId(loginUser.getId());
        boolean save = save(address1);
        ThrowUtils.throwIf(!save, ErrorCode.OPERATION_ERROR);
        return true;
    }

    @Override
    public List<Address> getAll(HttpSession session) {
        User loginUser = userService.getLoginUser(session);
        return addressMapper.selectAllByUserIdOrderByIsDefaultDesc(loginUser.getId());
    }

    @Override
    public boolean updateAddress(AddressUpdateRequest addressUpdateRequest, HttpSession session) {
        User loginUser = userService.getLoginUser(session);
        // 如果设置为默认地址
        Integer isDefault = addressUpdateRequest.getIsDefault();
        if (isDefault == 1) {
            // 清除别的地址的默认状态
            Address address = addressMapper.selectOneByUserIdAndIsDefault(loginUser.getId());
            if (address != null) {
                int i = addressMapper.updateIsDefaultByUserIdAndIsDefault(loginUser.getId());
                ThrowUtils.throwIf(i <= 0, ErrorCode.OPERATION_ERROR);
            }
        }
        Address address = new Address();
        BeanUtils.copyProperties(addressUpdateRequest, address);
        boolean result = updateById(address);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return true;
    }
}




