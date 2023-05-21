package com.example.healthcheck.entity.health;

import com.example.healthcheck.entity.server.Server;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ActiveServer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "active_server_id")
    private Long id;

    @Column(nullable = false)
    private long targetTime; // 분 단위

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "server_id")
    private Server server;

    @Builder
    public ActiveServer(long targetTime, Server server) {
        this.targetTime = targetTime;
        this.server = server;
    }

    public void updateTargetTime(long time){
        targetTime += time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ActiveServer that)) return false;
        return this.getId() != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
