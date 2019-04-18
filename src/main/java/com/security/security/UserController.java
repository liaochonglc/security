package com.security.security;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    //有权限
    @PreAuthorize("hasAuthority('UserIndex')")
    @GetMapping("/index")
    public String index() {
        return "user/hi";
    }

    @RequestMapping("/hi")
    public String hi() {
        System.out.println("3333");
        return "你好啊李四";
    }
}
