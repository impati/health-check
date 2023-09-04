package com.example.healthcheck.service.alarm;

import static com.example.healthcheck.steps.ServerSteps.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import com.example.healthcheck.entity.server.Server;

import jakarta.mail.internet.MimeMessage;

@ExtendWith(MockitoExtension.class)
class MailAlarmSenderTest {

	@InjectMocks
	private MailAlarmSender sender;

	@Mock
	private JavaMailSender javaMailSender;

	@Mock
	private MessageMaker messageMaker;

	@Test
	@DisplayName("서버에 이메일 알람 보내기 테스트")
	void sendAlarmTest() {
		// given
		final Server server = createStubServerWithDefaults();
		final String email = "yongs170@naver.com";
		final MimeMessage mockMessage = mock(MimeMessage.class);
		given(messageMaker.fillMessage(server.getServerName(), email)).willReturn(mockMessage);
		willDoNothing().given(javaMailSender).send(mockMessage);

		// expected
		assertThatCode(() -> sender.send(server, email)).doesNotThrowAnyException();
	}
}
