package com.example.healthcheck.api.v1.controller;

import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

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

import com.example.healthcheck.api.v1.request.QueryParamRequest;
import com.example.healthcheck.api.v1.request.ServerChangeStatusV1Request;
import com.example.healthcheck.api.v1.request.ServerRegistrationV1Request;
import com.example.healthcheck.entity.server.EndPointHttpMethod;
import com.example.healthcheck.security.BringCustomer;
import com.example.healthcheck.security.Customer;
import com.example.healthcheck.service.server.ServerRegister;
import com.example.healthcheck.service.server.ServerStatusManager;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ServerController.class)
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.health-check.com", uriPort = 443)
@ExtendWith(RestDocumentationExtension.class)
class ServerControllerTest {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@MockBean
	private ServerRegister serverRegister;

	@MockBean
	private BringCustomer bringCustomer;

	@MockBean
	private ServerStatusManager serverStatusManager;

	@Autowired
	private MockMvc mockMvc;

	@Test
	@DisplayName("[POST] [/api/v1/server] 서버 등록")
	void registerTest() throws Exception {
		final String token = stubToken();
		final Customer customer = stubCustomer();
		final ServerRegistrationV1Request request = createServerRegistrationV1Request();
		given(bringCustomer.bring(token)).willReturn(customer);
		willDoNothing().given(serverRegister).register(customer.getEmail(), request.convert());

		mockMvc.perform(post("/api/v1/server")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(request))
				.header("Authorization", "Bearer " + token))
			.andExpect(status().isOk())
			.andExpect(handler().methodName("register"))
			.andExpect(jsonPath("$.resultCode").value("SUCCESS"))
			.andDo(document("server",
				requestFields(
					fieldWithPath("serverName").description("서버 이름"),
					fieldWithPath("host").description("서버 호스트"),
					fieldWithPath("path").description("서버 앤드포인트"),
					fieldWithPath("method").description("HTTP 메서드"),
					fieldWithPath("queryParams[].key").description("쿼리 파라미터 key"),
					fieldWithPath("queryParams[].value").description("쿼리 파라미터 value"),
					fieldWithPath("interval").description("헬스 체크 요청 간격"),
					fieldWithPath("active").description("헬스 체크 활성화 여부")),
				responseFields(fieldWithPath("resultCode").description("상태 코드"))));
	}

	@Test
	@DisplayName("[POST] [/api/v1/server/disable] 서버 비활성화")
	void disableTest() throws Exception {
		final String token = stubToken();
		final Customer customer = stubCustomer();
		final ServerChangeStatusV1Request request = createServerDisableV1Request();
		given(bringCustomer.bring(token)).willReturn(customer);
		willDoNothing().given(serverStatusManager).deactivate(request.convert(), customer.getEmail());

		mockMvc.perform(post("/api/v1/server/disable")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(request))
				.header("Authorization", "Bearer " + token))
			.andExpect(status().isOk())
			.andExpect(handler().methodName("disable"))
			.andExpect(jsonPath("$.resultCode").value("SUCCESS"))
			.andDo(document("disable",
				requestFields(
					fieldWithPath("serverName").description("서버 이름"),
					fieldWithPath("serverId").description("서버 ID")),
				responseFields(fieldWithPath("resultCode").description("상태 코드"))));
	}

	@Test
	@DisplayName("[POST] [/api/v1/server/enable] 서버 활성화")
	void enableTest() throws Exception {
		final String token = stubToken();
		final Customer customer = stubCustomer();
		final ServerChangeStatusV1Request request = createServerDisableV1Request();
		given(bringCustomer.bring(token)).willReturn(customer);
		willDoNothing().given(serverStatusManager).deactivate(request.convert(), customer.getEmail());

		mockMvc.perform(post("/api/v1/server/enable")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(request))
				.header("Authorization", "Bearer " + token))
			.andExpect(status().isOk())
			.andExpect(handler().methodName("enable"))
			.andExpect(jsonPath("$.resultCode").value("SUCCESS"))
			.andDo(document("enable",
				requestFields(
					fieldWithPath("serverName").description("서버 이름"),
					fieldWithPath("serverId").description("서버 ID")),
				responseFields(fieldWithPath("resultCode").description("상태 코드"))));
	}

	private ServerChangeStatusV1Request createServerDisableV1Request() {
		return new ServerChangeStatusV1Request("서비스 허브", 1L);
	}

	private ServerRegistrationV1Request createServerRegistrationV1Request() {
		return new ServerRegistrationV1Request("서비스 허브", "https://service-hub.org", "/service/search",
			EndPointHttpMethod.GET,
			queryParams(),
			30,
			true);
	}

	private List<QueryParamRequest> queryParams() {
		return List.of(new QueryParamRequest("key", "value"));
	}

	private String stubToken() {
		return "${AccessToken}";
	}

	private Customer stubCustomer() {
		return new Customer(1L, "test", "test@test.com", "USER", "test");
	}
}
