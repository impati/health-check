package com.example.healthcheck.api.v1.controller;

import com.example.healthcheck.api.v1.response.Response;
import com.example.healthcheck.service.health.HealthCheckManager;
import com.example.healthcheck.service.health.HealthCheckTimeExaminer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/check")
@RequiredArgsConstructor
public class HealthCheckController {

    private final HealthCheckManager healthCheckManager;
    private final ObjectProvider<HealthCheckTimeExaminer> healthCheckTimeExaminers;

    @PostMapping("/{serverId}")
    public Response<Void> check(@PathVariable Long serverId){
        healthCheckManager.check(serverId);
        return Response.success();
    }

    @GetMapping
    public Response<Void> check(){
        HealthCheckTimeExaminer examiner = healthCheckTimeExaminers.getObject();
        examiner.examine();
        return Response.success();
    }
}
