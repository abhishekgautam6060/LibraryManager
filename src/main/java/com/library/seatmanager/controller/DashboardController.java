package com.library.seatmanager.controller;

import com.library.seatmanager.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class DashboardController {

    @Autowired
    private DashboardService service;

    @GetMapping("/dashboard")
    public Map<String, Object> dashboard() {
        return service.getDashboardStats();
    }
}

