package com.lianxi.springboot.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lianxi.springboot.common.Constants;
import com.lianxi.springboot.common.Result;
import com.lianxi.springboot.controller.dto.UserDto;
import com.lianxi.springboot.entity.User;
import com.lianxi.springboot.exception.ServiceException;
import com.lianxi.springboot.mapper.UserMapper;
import com.lianxi.springboot.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lianxi.springboot.utils.TokenUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author laoba
 * @since 2022-08-09
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private static final Log LOG = Log.get();

    //登录校验
    @Override
    public UserDto login(UserDto userDto) {
        User one = getUserInfo(userDto);
        if (one != null) {
            BeanUtils.copyProperties(one, userDto);
            //设置token
            String token = TokenUtils.genToken(one.getId(), toString(), one.getPassword());
            userDto.setToken(token);
            return userDto;
        } else {
            throw new ServiceException(Constants.CODE_600, "用户名或密码错误");
        }
    }

    //注册校验
    @Override
    public User register(UserDto userDto) {
        User one = getUserInfo(userDto);
        if (one == null) {
            one = new User();
            BeanUtils.copyProperties(userDto, one);
            save(one);  //把copy完之后的用户对象存储到数据库
        } else {
            throw new ServiceException(Constants.CODE_600, "用户已存在!");
        }
        return null;
    }

    //封装方法
    private User getUserInfo(UserDto userDto) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userDto.getUsername());
        queryWrapper.eq("password", userDto.getPassword());
        //防止数据库中的脏数据(一模一样的数据)
        User one;
        try {
            one = getOne(queryWrapper);//从数据库查询用户信息
        } catch (Exception e) {
            LOG.error(e);
            throw new ServiceException(Constants.CODE_500, "系统错误");
        }
        return one;
    }
}
