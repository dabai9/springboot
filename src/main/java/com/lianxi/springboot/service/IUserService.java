package com.lianxi.springboot.service;

import com.lianxi.springboot.controller.dto.UserDto;
import com.lianxi.springboot.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author laoba
 * @since 2022-08-09
 */
public interface IUserService extends IService<User> {

    UserDto login(UserDto userDto);

    User register(UserDto userDto);
}
