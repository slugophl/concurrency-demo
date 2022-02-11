package com.eic.concurrency.demo.controller;

import com.eic.concurrency.demo.service.CounterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("counter")
public class CounterController {
    Logger logger = LoggerFactory.getLogger(CounterController.class);

    private CounterService counterService;
}
