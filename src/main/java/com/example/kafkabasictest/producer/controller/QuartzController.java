package com.example.kafkabasictest.producer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class QuartzController {
    public QuartzController() {
    }

    @RequestMapping({"/"})
    public String main() {
        return "index";
    }
}
