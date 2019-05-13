package com.server.draw.controller;

import com.server.draw.model.UserEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @RequestMapping("/hello")
    public int hello()
    {
        return 2;
    }
    @RequestMapping("/index")
    public String index()
    {
        return "This is index html.";
    }
}
