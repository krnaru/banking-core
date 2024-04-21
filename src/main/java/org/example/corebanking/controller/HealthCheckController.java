package org.example.corebanking.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/ping")
@RestController
public class HealthCheckController {

    @GetMapping
    public String ping() {
        return "pong";

    }
}
