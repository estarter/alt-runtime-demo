package com.google.alt_runtime_demo;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {

    @RequestMapping(value = "/test")
    @ResponseBody
    public String home(@RequestBody(required = false) String json) {
        return "asdf";
    }
}
