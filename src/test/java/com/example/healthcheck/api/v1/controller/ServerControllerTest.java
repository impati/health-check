package com.example.healthcheck.api.v1.controller;

import com.example.healthcheck.api.v1.request.ServerRegistrationV1Request;
import com.example.healthcheck.entity.server.EndPointHttpMethod;
import com.example.healthcheck.security.BringCustomer;
import com.example.healthcheck.security.Customer;
import com.example.healthcheck.service.server.ServerRegister;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ServerController.class)
class ServerControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @MockBean
    private ServerRegister serverRegister;
    @MockBean
    private BringCustomer bringCustomer;
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("[POST] [/api/v1/server] 서버 등록")
    public void given_when_then() throws Exception{

        String token = stubToken();
        Customer customer = stubCustomer();
        ServerRegistrationV1Request request = create();

        given(bringCustomer.bring(token)).willReturn(customer);
        willDoNothing().given(serverRegister).register(customer.getId(),request.convert());

        mockMvc.perform(post("/api/v1/server")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request))
                        .header("Authorization","Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.handler().methodName("register"))
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"));
    }

    private ServerRegistrationV1Request create(){
        return new ServerRegistrationV1Request("host","path", EndPointHttpMethod.GET,30,true);
    }

    private String stubToken() {
        return "token";
    }

    private Customer stubCustomer(){
        return new Customer(1L,"test","test@test.com","USER","test");
    }
}