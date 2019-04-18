
package com.security.security;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {
    @RequestMapping("/login.do")
    public String login() {
        return "login";
    }

    @RequestMapping("/loginError.do")
    public String loginError() {
        return "loginError";
    }
}

