package com.example.healthcheck.entity.server;

import com.example.healthcheck.entity.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpMethod;

import java.util.Objects;

@Table(name = "server_table")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Server extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "server_id")
    private Long id;
    private Long customerId;

    @Column(nullable = false)
    private String host;
    private String path;
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private EndPointHttpMethod method;
    @Column(name = "interval_time" , nullable = false)
    private Integer interval;
    @Column(nullable = false)
    private boolean active;

    @Builder
    public Server(Long customerId, String host, String path, EndPointHttpMethod method, Integer interval, boolean active) {
        this.customerId = customerId;
        this.host = host;
        this.path = path;
        this.method = method;
        this.interval = interval;
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Server server)) return false;
        return this.getId() != null && Objects.equals(id, server.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
