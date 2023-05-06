package com.example.healthcheck.steps;

import com.example.healthcheck.entity.server.EndPointHttpMethod;
import com.example.healthcheck.entity.server.Server;
import com.example.healthcheck.repository.server.ServerRepository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class ServerSteps {

    private final ServerRepository serverRepository;

    public ServerSteps(ServerRepository serverRepository) {
        this.serverRepository = serverRepository;
    }

    public static Server createStubServerWithDefaults(){
        return createDefaultBuilder()
                .build();
    }

    public static Server createStubServerWithHost(String host){
        return createDefaultBuilder()
                .host(host)
                .build();
    }

    public static Server createStubServer(String host,
                                          String path,
                                          MultiValueMap<String,String> params){
        return createDefaultBuilder()
                .host(host)
                .path(path)
                .params(params)
                .build();
    }

    private static Server.ServerBuilder createDefaultBuilder(){
        return Server.builder()
                .serverName("디폴트 서버 네임")
                .email("test@test.com")
                .method(EndPointHttpMethod.GET)
                .interval(30)
                .host("https://service-hub.org")
                .path("/service/search")
                .params(new LinkedMultiValueMap<>())
                .active(true);
    }

    public Server createDefault(){
        return serverRepository.save(createStubServerWithDefaults());
    }

    public Server createNonexistentServer(){
        return save(createDefaultBuilder()
                .host("http://XXXX:8080")
                .serverName("존재하지 않는 서버")
                .build());
    }

    public Server createExistServer(){
        return save(createDefaultBuilder()
                .serverName("존재하는 서버")
                .host("https://naver.com")
                .build());
    }

    public Server createWithEmail(String email){
        return save(createDefaultBuilder()
                .email(email)
                .build());
    }

    public Server create(String serverName,boolean isActive){
        return save(createDefaultBuilder()
                .serverName(serverName)
                .active(isActive)
                .build());
    }

    private Server save(Server server){
        return serverRepository.save(server);
    }

    public Server create(String serverName,boolean isActive,int interval){
        return save(createDefaultBuilder()
                .serverName(serverName)
                .active(isActive)
                .interval(interval)
                .build());
    }


}
