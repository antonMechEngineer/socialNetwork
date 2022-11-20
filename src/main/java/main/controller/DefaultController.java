package main.controller;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DefaultController {

    @RequestMapping("/")
    public String index() throws ExpiredJwtException {
        return "forward:/";
    }
}
