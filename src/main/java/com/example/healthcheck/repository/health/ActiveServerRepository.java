package com.example.healthcheck.repository.health;

import com.example.healthcheck.entity.health.ActiveServer;
import com.example.healthcheck.entity.server.Server;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ActiveServerRepository extends JpaRepository<ActiveServer,Long> {

    @Query("SELECT A FROM ActiveServer A " +
            "JOIN FETCH A.server S " +
            "WHERE A.targetTime <= :time")
    List<ActiveServer> findActiveServerByTargetTime(@Param("time") long currentTime);

    @EntityGraph(attributePaths = {"server"})
    Optional<ActiveServer> findActiveServerByServer(Server server);
}
