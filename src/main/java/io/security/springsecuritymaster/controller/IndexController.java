package io.security.springsecuritymaster.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/loginPage")
    @ResponseBody
    public String loginPage() {
        return "loginPage";
    }

    @GetMapping("/home")
    @ResponseBody
    public String homePage() {
        return "home";
    }


}
