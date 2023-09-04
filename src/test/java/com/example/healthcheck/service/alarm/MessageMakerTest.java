package com.example.healthcheck.service.alarm;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.Properties;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import jakarta.mail.Message;
import jakarta.mail.internet.MimeMessage;

class MessageMakerTest {

	@Test
	@DisplayName("메시지 채우기 테스트")
	void fillMessageTest() throws Exception {
		// given
		final JavaMailSender mailSender = stubjavaMailSender();
		final MessageMaker messageMaker = new MessageMaker(mailSender);
		final String serverName = "서비스 허브";
		final String email = "yongs170@naver.com";

		// when
		final MimeMessage mimeMessage = messageMaker.fillMessage(serverName, email);

		// then
		assertThat(Arrays.toString(mimeMessage.getFrom()))
			.isEqualTo("[Impati_Health_Checker]");
		assertThat(Arrays.toString(mimeMessage.getRecipients(Message.RecipientType.TO)))
			.isEqualTo(String.format("[%s]", email));
		assertThat(mimeMessage.getContent())
			.isEqualTo(
				String.format("Fail to get a normal response from the [%s] \n server check the server", serverName));
		assertThat(mimeMessage.getSubject())
			.isEqualTo(String.format("Fail to get a normal response from the [%s]", serverName));
	}

	public JavaMailSender stubjavaMailSender() {
		final JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost("smtp.gmail.com");
		mailSender.setPort(587);
		mailSender.setUsername("impatient0716@gmail.com");
		mailSender.setPassword("test");

		final Properties props = mailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.debug", "true");

		return mailSender;
	}
}
