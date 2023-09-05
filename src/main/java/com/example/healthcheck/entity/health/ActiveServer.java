package com.example.healthcheck.entity.health;

import java.util.Objects;

import com.example.healthcheck.entity.server.Server;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ActiveServer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "active_server_id")
    private Long id;

    @Column(name = "target_time", nullable = false)
    private long targetTime; // 분 단위

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "server_id")
    private Server server;

    @Builder
    public ActiveServer(final long targetTime, final Server server) {
        this.targetTime = targetTime;
        this.server = server;
    }

    public void updateTargetTime(final long time) {
        targetTime += time;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ActiveServer that)) {
            return false;
        }

        return this.getId() != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
