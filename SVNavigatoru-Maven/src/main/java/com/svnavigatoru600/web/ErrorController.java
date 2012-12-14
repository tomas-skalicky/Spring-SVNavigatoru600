package com.svnavigatoru600.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorController {

    @RequestMapping(value = "/chyby/403/")
    public String show403() {
        return "error403";
    }

    @RequestMapping(value = "/chyby/404/")
    public String show404() {
        return "error404";
    }
}
