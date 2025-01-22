package com.mss.pre.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class ViewController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/detail/{id}")
    public String getResult(@PathVariable("id") Long id) {
        return "detail";
    }

    @GetMapping("/brand")
    public String brand() {
        return "form/brand";
    }

    @GetMapping("/product")
    public String product() {
        return "form/product";
    }
}
