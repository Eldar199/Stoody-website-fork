package com.loam.stoody.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Test {
    @GetMapping("/")
    public String home(){
        return "pages/landings/home-academy";
    }
}