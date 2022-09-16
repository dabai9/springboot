package com.lianxi.springboot.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lianxi.springboot.common.Constants;
import com.lianxi.springboot.common.Result;
import com.lianxi.springboot.controller.dto.UserDto;
import com.lianxi.springboot.utils.TokenUtils;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

import com.lianxi.springboot.service.IUserService;
import com.lianxi.springboot.entity.User;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author laoba
 * @since 2022-08-09
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private IUserService userService;


    //登录
    @PostMapping("/login")
    public Result login(@RequestBody UserDto userDto) {
        String username = userDto.getUsername();
        String password = userDto.getPassword();
        //校验字符串是否为空
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            return Result.error(Constants.CODE_400, "参数错误");
        }
        UserDto dto = userService.login(userDto);
        return Result.success(dto);
    }

    //注册
    @PostMapping("/register")
    public Result register(@RequestBody UserDto userDto) {
        String username = userDto.getUsername();
        String password = userDto.getPassword();
        //校验字符串是否为空
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            return Result.error(Constants.CODE_400, "参数错误");
        }
        return Result.success(userService.register(userDto));
    }

    //新增或者更新
    @PostMapping
    public Result save(@RequestBody User user) {
        return Result.success(userService.saveOrUpdate(user));
    }

    //删除
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.success(userService.removeById(id));
    }

    @PostMapping("/del/batch")
    //批量删除
    public Result deleteBeach(@RequestBody List<Integer> ids) {
        return Result.success(userService.removeBatchByIds(ids));
    }

    //查询所有
    @GetMapping
    public Result findAll() {
        return Result.success(userService.list());
    }

    //根据id查询
    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(userService.getById(id));
    }


    @GetMapping("/username/{username}")
    public Result findOne(@PathVariable String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return Result.success(userService.getOne(queryWrapper));
    }

    //分页查询
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                               @RequestParam Integer pageSize,
                               @RequestParam(defaultValue = "") String username,
                               @RequestParam(defaultValue = "") String email,
                               @RequestParam(defaultValue = "") String address) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        //模糊查询
        if (!"".equals(username)) {
            queryWrapper.like("username", username);
        }
        if (!"".equals(email)) {
            queryWrapper.like("email", email);
        }
        if (!"".equals(address)) {
            queryWrapper.like("address", address);
        }
        queryWrapper.orderByDesc("id");//倒序
        return Result.success(userService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }


    //导出接口
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        //从数据库查询所有数据
        List<User> list = userService.list();
        //在内存操作,写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题名
//        writer.addHeaderAlias("username", "用户名");
//        writer.addHeaderAlias("password", "密码");
//        writer.addHeaderAlias("nickname", "昵称");
//        writer.addHeaderAlias("email", "邮箱");
//        writer.addHeaderAlias("phone", "电话");
//        writer.addHeaderAlias("address", "地址");
//        writer.addHeaderAlias("createTime", "创建时间");
//        writer.addHeaderAlias("avatarUrl", "头像");

        //一次性写出list内对象到excel,使用默认样式,强制输出标题
        writer.write(list, true);

        //设置浏览器响应格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("用户信息", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();
    }

    //导入接口
    @PostMapping("/import")
    public boolean imp(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
//        List<User> list = reader.readAll(User.class);
        List<User> list = reader.read(0, 1, User.class);
        userService.saveBatch(list);
        return true;
    }
}

