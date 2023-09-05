package com.example.healthcheck.service.alarm;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.example.healthcheck.exception.ErrorCode;
import com.example.healthcheck.exception.HealthCheckException;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class MessageMaker {

	private static final String SENDER_NAME = "Impati_Health_Checker";
	private static final String SEND_TITLE = "Fail to get a normal response from the [%s]";
	private static final String SEND_CONTENT = "Fail to get a normal response from the [%s] \n server check the server";

	private final JavaMailSender javaMailSender;

	public MimeMessage fillMessage(final String serverName, final String email) {
		try {
			return fillMessageOrThrows(serverName, email);
		} catch (MessagingException messagingException) {
			log.error("메시지를 생성하는데 오류가 발생했습니다.");
			throw new HealthCheckException(ErrorCode.FILL_MAIL_FAIL);
		}
	}

	private MimeMessage fillMessageOrThrows(final String serverName, final String email) throws MessagingException {
		final MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "UTF-8");
		mimeMessageHelper.setFrom(SENDER_NAME);
		mimeMessageHelper.setTo(email);
		mimeMessageHelper.setSubject(sendTitle(serverName));
		mimeMessageHelper.setText(sendContent(serverName));
		return mimeMessage;
	}

	private String sendContent(final String serverName) {
		return String.format(SEND_CONTENT, serverName);
	}

	private String sendTitle(final String serverName) {
		return String.format(SEND_TITLE, serverName);
	}
}
