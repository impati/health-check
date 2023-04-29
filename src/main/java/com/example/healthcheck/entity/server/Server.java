package com.example.healthcheck.entity.server;

import com.example.healthcheck.entity.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
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

    @OneToMany(mappedBy = "server",cascade = CascadeType.ALL)
    private List<QueryParam> queryParams = new ArrayList<>();

    @Builder
    public Server(Long customerId,
                  String host,
                  String path,
                  EndPointHttpMethod method,
                  MultiValueMap<String , String> params,
                  Integer interval,
                  boolean active) {
        this.customerId = customerId;
        this.host = host;
        this.path = path;
        this.method = method;
        this.interval = interval;
        this.active = active;
        saveQueryParams(params);
    }

    public String getUrl(){
        return UriComponentsBuilder.fromHttpUrl(host)
                .path(path)
                .queryParams(toPrams())
                .build()
                .toString();
    }

    private void saveQueryParams(MultiValueMap<String,String> params){
        if(params == null) return;
        for(var key : params.keySet()){
            saveQueryParams(key,params.get(key));
        }
    }

    private void saveQueryParams(String key,List<String> values){
        for(var value :values) {
            queryParams.add(QueryParam.builder()
                    .key(key)
                    .value(value)
                    .server(this)
                    .build());
        }
    }

    private MultiValueMap<String,String> toPrams(){
        MultiValueMap<String,String> multiValueMap = new LinkedMultiValueMap<>();
        for(var element : queryParams){
            multiValueMap.add(element.getKey(),element.getValue());
        }
        return multiValueMap;
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
