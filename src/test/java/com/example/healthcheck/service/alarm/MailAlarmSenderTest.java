package com.example.healthcheck.service.alarm;

import com.example.healthcheck.entity.server.Server;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import static com.example.healthcheck.steps.ServerSteps.createStubServerWithDefaults;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.mock;

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
    public void sendAlarmTest() throws Exception{
        // given
        Server server = createStubServerWithDefaults();
        String email = "yongs170@naver.com";

        MimeMessage mockMessage = mock(MimeMessage.class);
        given(messageMaker.fillMessage(server.getServerName(), email)).willReturn(mockMessage);
        willDoNothing().given(javaMailSender).send(mockMessage);

        // expected
        assertThatCode(()->sender.send(server,email)).doesNotThrowAnyException();

    }


}