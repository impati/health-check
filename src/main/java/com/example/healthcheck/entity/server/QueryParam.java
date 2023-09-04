package com.example.healthcheck.entity.server;

import java.util.Objects;

import com.example.healthcheck.entity.common.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QueryParam extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "query_param_id")
    private Long id;

    @Column(nullable = false, name = "key_column")
    private String key;

    @Column(nullable = false, name = "value_column")
    private String value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "server_id")
    private Server server;

    @Builder
    public QueryParam(
        final String key,
        final String value,
        final Server server
    ) {
        this.key = key;
        this.value = value;
        this.server = server;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QueryParam that)) {
            return false;
        }

        return this.getId() != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "QueryParam{" +
            "id=" + id +
            ", key='" + key + '\'' +
            ", value='" + value + '\'' +
            ", server=" + server +
            '}';
    }
}
