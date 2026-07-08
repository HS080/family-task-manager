package com.example.demo;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class TestController {

    // 1. 测试接口
    @GetMapping("/test")
    public String test() {
        return "成功！！！";
    }

    // 2. 登录接口（毕设核心）
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> user) {
        // 获取前端传过来的账号和密码
        String username = user.get("username");
        String password = user.get("password");

        Map<String, Object> result = new HashMap<>();

        // 简单验证：毕设阶段，直接写死 "admin" / "123456" 即可通过
        if ("admin".equals(username) && "123456".equals(password)) {
            result.put("code", 200);
            result.put("msg", "登录成功！");
            result.put("token", "your_token_123456"); // 模拟token
        } else {
            result.put("code", 401);
            result.put("msg", "账号或密码错误！");
        }
        return result;
    }
}