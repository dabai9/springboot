package com.lianxi.springboot.utils;


import cn.hutool.core.date.DateUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Date;

/**
 * 生成Token
 */

public class TokenUtils {
    public static String genToken(Integer id, String userId, String sign) {
        return JWT.create().withAudience(userId) //将 userId 保存到 token 中,做为载荷
                .withExpiresAt(DateUtil.offsetHour(new Date(), 2)) //2小时后 token 失效
                .sign(Algorithm.HMAC256(sign)); //以 password 作为 token 的密钥
    }
}
