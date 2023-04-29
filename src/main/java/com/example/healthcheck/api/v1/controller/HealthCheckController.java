package com.example.healthcheck.api.v1.controller;

import com.example.healthcheck.api.v1.response.Response;
import com.example.healthcheck.service.health.HealthRecordSaver;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/check")
@RequiredArgsConstructor
public class HealthCheckController {

    private final HealthRecordSaver healthRecordSaver;

    @PostMapping("/{serverId}")
    public Response<Void> check(@PathVariable Long serverId){
        healthRecordSaver.saveRecord(serverId);
        return Response.success();
    }

}
