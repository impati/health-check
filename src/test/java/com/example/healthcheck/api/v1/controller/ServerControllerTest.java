package com.example.healthcheck.api.v1.controller;

import com.example.healthcheck.api.v1.request.QueryParamRequest;
import com.example.healthcheck.api.v1.request.ServerRegistrationV1Request;
import com.example.healthcheck.entity.server.EndPointHttpMethod;
import com.example.healthcheck.security.BringCustomer;
import com.example.healthcheck.security.Customer;
import com.example.healthcheck.service.server.ServerRegister;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ServerController.class)
@AutoConfigureRestDocs(uriScheme = "https",uriHost = "api.health-check.com",uriPort = 443)
@ExtendWith(RestDocumentationExtension.class)
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
                .andExpect(handler().methodName("register"))
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andDo(document("server",
                        requestFields(
                                fieldWithPath("host").description("서버 호스트"),
                                fieldWithPath("path").description("서버 앤드포인트"),
                                fieldWithPath("method").description("HTTP 메서드"),
                                fieldWithPath("queryParams[].key").description("쿼리 파라미터 key"),
                                fieldWithPath("queryParams[].value").description("쿼리 파라미터 value"),
                                fieldWithPath("interval").description("헬스 체크 요청 간격"),
                                fieldWithPath("active").description("헬스 체크 활성화 여부")),
                        responseFields(fieldWithPath("resultCode").description("상태 코드"))));
    }

    private ServerRegistrationV1Request create(){
        return new ServerRegistrationV1Request("https://service-hub.org","/service/search",
                EndPointHttpMethod.GET,
                queryParams(),
                30,
                true);
    }

    private List<QueryParamRequest> queryParams(){
        return List.of(new QueryParamRequest("key","value"));
    }
    private String stubToken() {
        return "${AccessToken}";
    }

    private Customer stubCustomer(){
        return new Customer(1L,"test","test@test.com","USER","test");
    }
}