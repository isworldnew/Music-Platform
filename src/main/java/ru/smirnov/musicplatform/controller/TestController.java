package ru.smirnov.musicplatform.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/data")
    @PreAuthorize("hasAnyRole('ADMIN', 'DISTRIBUTOR')")
    public String getData() {
        return "test data";
    }

}
