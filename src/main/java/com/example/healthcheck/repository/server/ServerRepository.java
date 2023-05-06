package com.example.healthcheck.repository.server;

import com.example.healthcheck.entity.server.Server;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ServerRepository extends JpaRepository<Server,Long> {

    @Query("select s from Server s where s.active = true")
    List<Server> findActiveServer();
}


