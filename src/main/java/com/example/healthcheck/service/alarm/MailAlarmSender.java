package com.example.healthcheck.service.alarm;

import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;

import com.example.healthcheck.entity.server.Server;
import com.example.healthcheck.exception.ErrorCode;
import com.example.healthcheck.exception.HealthCheckException;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class MailAlarmSender implements AlarmSender {

	private final JavaMailSender javaMailSender;
	private final MessageMaker messageMaker;

	@Override
	public void send(final Server server, final String email) {
		try {
			sendOrThrow(server.getServerName(), email);
		} catch (MailException e) {
			log.error("MailAlarmSender.send() :: FAILED", e);
			throw new HealthCheckException(ErrorCode.SEND_MAIL_FAIL);
		}
	}

	private void sendOrThrow(final String serverName, final String email) {
		final MimeMessage mimeMessage = messageMaker.fillMessage(serverName, email);
		javaMailSender.send(mimeMessage);
	}
}
