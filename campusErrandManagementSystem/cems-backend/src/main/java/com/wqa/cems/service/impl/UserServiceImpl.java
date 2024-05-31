package com.wqa.cems.service.impl;

import static com.wqa.cems.constant.UserConstant.USER_LOGIN_STATE;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wqa.cems.common.ErrorCode;
import com.wqa.cems.constant.CommonConstant;
import com.wqa.cems.constant.UserConstant;
import com.wqa.cems.exception.BusinessException;
import com.wqa.cems.exception.ThrowUtils;
import com.wqa.cems.mapper.UserMapper;
import com.wqa.cems.model.dto.user.UserQueryRequest;
import com.wqa.cems.model.entity.Task;
import com.wqa.cems.model.entity.User;
import com.wqa.cems.model.enums.UserRoleEnum;
import com.wqa.cems.model.vo.LoginUserVO;
import com.wqa.cems.model.vo.UserInfo;
import com.wqa.cems.model.vo.UserVO;
import com.wqa.cems.service.DeliverymanService;
import com.wqa.cems.service.TaskService;
import com.wqa.cems.service.UserService;
import com.wqa.cems.utils.SqlUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

/**
 * 用户服务实现
 *
 * @author lenovo
 * @description 针对表【user】的数据库操作Service实现
 * @createDate 2024-03-05 19:55:21
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private DeliverymanService deliverymanService;

    @Resource
    private TaskService taskService;

    /**
     * 盐值，混淆密码
     */
    private static final String SALT = "wqa";

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword, String username, String phoneNumber) {
        // 1. 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过短");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }
        // 密码和校验密码相同
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入的密码不一致");
        }
        if ("未登录".equals(username)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "改名字为关键词");
        }
        String regex = "^1[3456789]\\d{9}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNumber);
        // 手机号不合法
        if (!matcher.matches()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "手机号不合法");
        }
        synchronized (userAccount.intern()) {
            // 账户和手机号都不能重复
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_account", userAccount).or()
                    .eq("phone_number", phoneNumber);
            long count = this.baseMapper.selectCount(queryWrapper);
            if (count > 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号或手机号重复");
            }
            // 2. 加密
            String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
            // 3. 插入数据
            User user = new User();
            user.setUserAccount(userAccount);
            user.setPassword(encryptPassword);
            user.setPhoneNumber(phoneNumber);
            user.setUsername(username);
            boolean saveResult = this.save(user);
            if (!saveResult) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败，数据库错误");
            }
            return user.getId();
        }
    }

    @Override
    public LoginUserVO userLogin(String userAccount, String userPassword, HttpSession session) {
        // 1. 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号错误");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码错误");
        }
        // 2. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // 查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account", userAccount);
        queryWrapper.eq("password", encryptPassword);
        User user = this.baseMapper.selectOne(queryWrapper);
        // 用户不存在
        if (user == null) {
            log.info("user login failed, userAccount cannot match userPassword");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在或密码错误");
        }
        // 登陆成功，如果是配送员，更改配送员的接单状态等(先查询有没有未完成的订单)
        if (user.getRole().equals(UserConstant.DELIVERYMAN_Role)) {
            // 是配送员
            Long deliverymanId = deliverymanService.getIdByUserId(user.getId());
            Task task = taskService.getTaskByDeliverymanIdAndStatus(deliverymanId, "已接单", "配送中");
            boolean result;
            if (task != null) {
                // 还有进行中的订单
                result = deliverymanService.updateAccountStatus("是", "已接单", user.getId());
            } else {
                result = deliverymanService.updateAccountStatus("是", "未接单", user.getId());
            }
            ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        }
        // 3. 记录用户的登录态
        session.setAttribute(USER_LOGIN_STATE, user);
        return this.getLoginUserVO(user);
    }

    @Override
    public LoginUserVO userLoginByMpOpen(WxOAuth2UserInfo wxOAuth2UserInfo, HttpServletRequest request) {
        return null;
    }

    /**
     * 获取当前登录用户
     *
     * @param session
     * @return
     */
    @Override
    public User getLoginUser(HttpSession session) {
        // 先判断是否已登录
        Object userObj = session.getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        log.info(String.valueOf(currentUser));
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        // 从数据库查询（追求性能的话可以注释，直接走缓存）
        long userId = currentUser.getId();
        currentUser = this.getById(userId);
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return currentUser;
    }

    /**
     * 获取当前登录用户（允许未登录）
     *
     * @param request
     * @return
     */
    @Override
    public User getLoginUserPermitNull(HttpServletRequest request) {
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            return null;
        }
        // 从数据库查询（追求性能的话可以注释，直接走缓存）
        long userId = currentUser.getId();
        return this.getById(userId);
    }

    /**
     * 是否为管理员
     *
     * @param request
     * @return
     */
    @Override
    public boolean isAdmin(HttpServletRequest request) {
        // 仅管理员可查询
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        return isAdmin(user);
    }

    @Override
    public boolean isAdmin(User user) {
        return user != null && UserRoleEnum.ADMIN.getValue().equals(user.getRole());
    }

    /**
     * 用户注销
     *
     * @param session
     */
    @Override
    public boolean userLogout(HttpSession session) {
        if (session.getAttribute(USER_LOGIN_STATE) == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "未登录");
        }
        // 如果是配送员，重置账号状态
        User loginUser = (User) session.getAttribute(USER_LOGIN_STATE);
        if (loginUser.getRole().equals(UserConstant.DELIVERYMAN_Role)) {
            boolean result = deliverymanService.updateAccountStatus("否", "离线", loginUser.getId());
            ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        }
        // 移除登录态
        session.removeAttribute(USER_LOGIN_STATE);
        return true;
    }

    @Override
    public LoginUserVO getLoginUserVO(User user) {
        if (user == null) {
            return null;
        }
        LoginUserVO loginUserVO = new LoginUserVO();
        BeanUtils.copyProperties(user, loginUserVO);
        return loginUserVO;
    }

    @Override
    public UserVO getUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    @Override
    public List<UserVO> getUserVO(List<User> userList) {
        if (CollectionUtils.isEmpty(userList)) {
            return new ArrayList<>();
        }
        return userList.stream().map(this::getUserVO).collect(Collectors.toList());
    }

    @Override
    public QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest) {
        if (userQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Long id = userQueryRequest.getId();
        String phoneNumber = userQueryRequest.getPhoneNumber();
        String userName = userQueryRequest.getUsername();
        String userRole = userQueryRequest.getRole();
        String userAccount = userQueryRequest.getUserAccount();
        String sortField = userQueryRequest.getSortField();
        String sortOrder = userQueryRequest.getSortOrder();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(id != null, "id", id);
        queryWrapper.eq(StringUtils.isNotBlank(phoneNumber), "phone_umber", phoneNumber);
        queryWrapper.eq(StringUtils.isNotBlank(userAccount), "user_account", userAccount);
        queryWrapper.eq(StringUtils.isNotBlank(userRole), "role", userRole);
        queryWrapper.like(StringUtils.isNotBlank(userName), "user_name", userName);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    @Override
    public PageInfo<UserInfo> getListUserInfoByRole(long current, long size, String defaultRole) {
        // 开启MyBatis分页
        PageHelper.startPage((int) current, (int) size);
        log.info("current:" + current + ", size:" + size);
        List<UserInfo> users = userMapper.selectAllByRole(defaultRole);
        return new PageInfo<>(users, 5);
    }

    @Override
    public PageInfo<UserInfo> search(long current, long size, String field, String fieldValue) {
        switch (field) {
            case "用户名":
                field = "user_account";
                break;
            case "姓名":
                field = "username";
                break;
            case "手机号":
                field = "phone_number";
                break;
            case "id":
                break;
            default:
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数错误");
        }
        // 开启MyBatis分页
        PageHelper.startPage((int) current, (int) size);
        List<UserInfo> userInfos = userMapper.searchUsers(field, fieldValue);
        return new PageInfo<>(userInfos, 5);
    }
}




