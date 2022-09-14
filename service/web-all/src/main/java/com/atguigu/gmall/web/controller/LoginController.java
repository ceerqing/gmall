package com.atguigu.gmall.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Author：张世平
 * Date：2022/9/14 11:54
 */
@Controller
public class LoginController {
    @GetMapping("/login.html")
    public String loginPage(@RequestParam("originUrl") String originUrl,
                            Model model){
        model.addAttribute("originUrl",originUrl);
        return "login";
    }
}
