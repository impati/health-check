package com.example.healthcheck.service.alarm;

import jakarta.mail.Message;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Arrays;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;


class MessageMakerTest {

    @Test
    @DisplayName("메시지 채우기 테스트")
    public void fillMessageTest() throws Exception{
        // given
        JavaMailSender mailSender = stubjavaMailSender();
        MessageMaker messageMaker = new MessageMaker(mailSender);
        String serverName = "서비스 허브";
        String email = "yongs170@naver.com";

        // when
        MimeMessage mimeMessage = messageMaker.fillMessage(serverName, email);

        // then
        assertThat(Arrays.toString(mimeMessage.getFrom()))
                        .isEqualTo("[Impati_Health_Checker]");
        assertThat(Arrays.toString(mimeMessage.getRecipients(Message.RecipientType.TO)))
                        .isEqualTo(String.format("[%s]",email));

        assertThat(mimeMessage.getContent())
                .isEqualTo(String.format("Fail to get a normal response from the [%s] \n server check the server",serverName));

        assertThat(mimeMessage.getSubject())
                .isEqualTo(String.format("Fail to get a normal response from the [%s]",serverName));
    }

    public JavaMailSender stubjavaMailSender(){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        mailSender.setUsername("impatient0716@gmail.com");
        mailSender.setPassword("test");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");
        return mailSender;
    }
}