package com.example.healthcheck.api.v1.controller;

import com.example.healthcheck.security.BringCustomer;
import com.example.healthcheck.service.health.HealthRecordSaver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HealthCheckController.class)
@AutoConfigureRestDocs(uriScheme = "https",uriHost = "api.health-check.com",uriPort = 443)
@ExtendWith(RestDocumentationExtension.class)
class HealthCheckControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BringCustomer bringCustomer;

    @MockBean
    private HealthRecordSaver healthRecordSaver;

    @Test
    @DisplayName("[POST] [/api/v1/check/{serviceId}] 서버에 헬스 체크 테스트")
    public void checkTest() throws Exception{

        willDoNothing().given(healthRecordSaver).saveRecord(1L);

        mockMvc.perform(post("/api/v1/check/{serverId}",1))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("check"))
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andDo(document("check",
                        pathParameters(parameterWithName("serverId").description("서버 아이디")),
                        responseFields(fieldWithPath("resultCode").description("상태 코드"))));

    }


}