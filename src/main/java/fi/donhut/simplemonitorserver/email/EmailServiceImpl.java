/*
 * Copyright since 2019 Nhut Do <mr.nhut.dev@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fi.donhut.simplemonitorserver.email;

import fi.donhut.simplemonitorserver.monitor.MonitorData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;

/**
 * {@link EmailService} implementation.
 *
 * @author Nhut Do (mr.nhut.dev@gmail.com)
 */
@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger LOG = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Autowired
    public JavaMailSender emailSender;

    @Value("${spring.mail.enabled:false}")
    private boolean emailSendEnabled;

    @Value("${app.mail.send.to}")
    private String emailTo;

    @PostConstruct
    private void afterSetUp() {
        if (emailSendEnabled && !StringUtils.hasText(emailTo)) {
            throw new IllegalArgumentException("Email receivers (To) is NOT configured correctly!");
        }
    }

    private void send(final String subject, final String content) {
        if (!emailSendEnabled) {
            LOG.info("Email send is NOT enabled. Suppose to send subject: {}, content: {}",
                subject, content);
            return;
        }
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailTo);
        message.setSubject(subject);
        message.setText(content);
        LOG.info("Sending email to {}...", emailTo);
        emailSender.send(message);
        LOG.info("Email sent to {} with subject '{}'.", emailTo, subject);
    }

    @Async
    @Override
    public void sendEmail(final MonitorData monitorData, final String msgContent) {
        final String subject = String.format("%s: %s", monitorData.getComputer().getName(), msgContent);
        send(subject, monitorData.toString());
    }


}
