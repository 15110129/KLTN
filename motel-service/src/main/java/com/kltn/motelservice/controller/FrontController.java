package com.kltn.motelservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FrontController {

    @GetMapping("/admin")
    public String getAdminPage(){
        return "/admin/index.html";
    }

    @GetMapping("/")
    public String getUserApp(){
        return "/app/index.html";
    }
}
