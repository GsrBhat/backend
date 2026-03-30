package com.hospital.controller;

import com.hospital.service.QueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/queue")
public class QueueController {

    @Autowired
    private QueueService queueService;

    @GetMapping("/{doctorId}")
    public Map<String, Object> getQueueStatus(@PathVariable Long doctorId) {
        return queueService.getQueueStatus(doctorId);
    }
}
