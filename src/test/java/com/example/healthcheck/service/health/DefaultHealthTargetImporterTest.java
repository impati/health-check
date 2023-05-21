package com.example.healthcheck.service.health;

import com.example.healthcheck.config.JpaConfig;
import com.example.healthcheck.entity.health.HealthRecord;
import com.example.healthcheck.entity.server.Server;
import com.example.healthcheck.repository.health.HealthRecordRepository;
import com.example.healthcheck.repository.server.ServerRepository;
import com.example.healthcheck.service.health.dto.HealthCheckServer;
import com.example.healthcheck.steps.HealthRecordSteps;
import com.example.healthcheck.steps.ServerSteps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.List;

import static java.lang.System.currentTimeMillis;
import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@Import({JpaConfig.class,DefaultHealthTargetImporter.class})
class DefaultHealthTargetImporterTest {

    @Autowired
    private HealthTargetImporter healthTargetImporter;

    @Autowired
    private HealthRecordRepository healthRecordRepository;

    @Autowired
    private ServerRepository serverRepository;

    private HealthRecordSteps healthRecordSteps;

    private ServerSteps serverSteps;

    @BeforeEach
    void setup(){
        healthRecordSteps = new HealthRecordSteps(healthRecordRepository);
        serverSteps = new ServerSteps(serverRepository);
    }


    @Test
    @DisplayName("헬스 체크 대상 서버를 HealthCheckServer 객체로 가져오기 테스트")
    public void importTargetTest() throws Exception{
        // given
        Server firstTestServer = serverSteps.create("[테스트 서버 1]", true,3);
        Server secondTestServer = serverSteps.create("[테스트 서버 2]", true,10);
        Server thirdTestServer = serverSteps.create("[테스트 서버 3]", true,30);

        HealthRecord firstRecordOfFirstTestServer = healthRecordSteps.create(firstTestServer);
        HealthRecord lastRecordOfFirstTestServer = healthRecordSteps.create(firstTestServer);

        HealthRecord firstRecordOfSecondTestServer = healthRecordSteps.create(secondTestServer);
        HealthRecord lastRecordOfSecondTestServer = healthRecordSteps.create(secondTestServer);

        // when
        List<HealthCheckServer> healthCheckServers = healthTargetImporter.importTarget(10);

        // then
        assertThat(healthCheckServers.size()).isEqualTo(3);

    }

    /**
     * 서버 당 Record 가 많을 수록 불리
     * (100 , 100_000) -> 38 초
     * (1000, 100_000) -> 5초 ,
     */
    @Test
    @DisplayName("헬스 체크 대상 서버를 HealthCheckServer 객체로 가져오기 성능 테스트 - 서브쿼리를 활용한 한번 쿼리 - 38 초")
    public void DefaultHealthTargetImporter() throws Exception{
        // given
        int n = 1000;
        List<Server> servers = new ArrayList<>();
        for(int i = 0 ; i < n ;i++)servers.add(serverSteps.create("테스트서버",true));
        for(int i =0 ; i < n * 100 ; i++) healthRecordSteps.create(servers.get(i % n));

        // when
        long beforeTime = currentTimeMillis();

        healthTargetImporter.importTarget(10);

        long diffTime = currentTimeMillis() - beforeTime;

        // then

        System.out.println(diffTime + "ms");

    }

    /**
     * 서버가 많아질 수록 분리
     * (100 , 100_000) 6초
     * (1000 , 100_000) 60초
     */
    @Test
    @DisplayName("헬스 체크 대상 서버를 HealthCheckServer 객체로 가져오기 성능 테스트 - 서브쿼리 X , 여러번 쿼리 - 60초")
    public void given_when_then() throws Exception{
        // given

        HealthTargetImporter advanced = new HealthTargetImporter() {
            @Override
            public List<HealthCheckServer> importTarget(long time) {
                List<HealthCheckServer> result = new ArrayList<>();
                List<Server> activeServer = serverRepository.findActiveServer();
                for(var server : activeServer){
                    result.add(HealthCheckServer.from(healthRecordRepository.findLatestRecordOfActiveServer(server.getId())));
                }
                result.addAll(activeServer
                        .stream()
                        .map(server -> HealthCheckServer.of(server, now()))
                        .filter(healthCheckServer -> !result.contains(healthCheckServer))
                        .toList());
                return result;
            }
        };

        int n = 1000;
        List<Server> servers = new ArrayList<>();
        for(int i = 0 ; i < n ;i++)servers.add(serverSteps.create("테스트서버",true));
        for(int i =0 ; i < n * 100 ; i++) healthRecordSteps.create(servers.get(i % n));

        // when
        long beforeTime = currentTimeMillis();

        advanced.importTarget(10);

        long diffTime = currentTimeMillis() - beforeTime;

        // then

        System.out.println(diffTime + "ms");

    }

}