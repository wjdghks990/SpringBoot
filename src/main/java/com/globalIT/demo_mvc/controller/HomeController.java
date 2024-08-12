package com.globalIT.demo_mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.globalIT.demo_mvc.service.TestService;


@Controller
public class HomeController {

    @Autowired
    TestService test_service;

    public HomeController() {
        System.out.println("--HomeController()--");
    }

    @RequestMapping("/hello.do")
    public String hello(Model model) {

        String msg = test_service.hello();

        // 결과적으로 request binding
        model.addAttribute("msg", msg);

        return "hello";
    }

}
