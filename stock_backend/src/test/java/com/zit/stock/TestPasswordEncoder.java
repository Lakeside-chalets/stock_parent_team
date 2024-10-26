package com.zit.stock;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @ClassName: TestPasswordEncoder
 * @Description: TODO
 * @Author: mianbaoren
 * @Date: 2024/8/29 14:34
 */

@SpringBootTest
public class TestPasswordEncoder {

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 测试密码加密
     */
    @Test
    public void test01(){
        String pwd= "123456";
        String encodePwd = passwordEncoder.encode(pwd);
        System.out.println(encodePwd);
    }
    /**
     * 测试密码匹配
     */
    @Test
    public void test02(){
        String pwd= "123456";
        String enpwd = "$2a$10$jMXeV2Bo1PVE6aADU4UYw.mEej6VM3hupUBbgGGxehXayjxhwWq";
        boolean isSucess = passwordEncoder.matches(pwd, enpwd);
        System.out.println(isSucess?"匹配成功":"匹配失败");
    }
}
