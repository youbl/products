package com.chaoip.ipproxy.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * HomeController
 *
 * @author youbl
 * @version 1.0
 * @date 2020/11/17 19:35
 */
@RestController
@RequestMapping("home")
public class HomeController {
    @GetMapping("index")
    public String index() {
        return "";
    }


    @GetMapping("index2")
    public String index2(@RequestParam(required = false) Integer aa) {
        return aa.toString();
    }
}
