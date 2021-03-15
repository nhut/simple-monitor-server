package fi.donhut.simplemonitorserver.email;

import fi.donhut.simplemonitorserver.monitor.Computer;
import fi.donhut.simplemonitorserver.monitor.MonitorData;
import fi.donhut.simplemonitorserver.monitor.NetworkStatus;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.LocalDateTime;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class EmailServiceImplTest {

    private static final Computer PC_1 = new Computer("pc1", "10.0.0.2", 11.11, 1_000L, 1_000_000_000L, LocalDateTime.now());

    private final JavaMailSender emailSender;

    private final ArgumentCaptor<SimpleMailMessage> messageCaptor;

    private EmailServiceImpl sut;

    private final String emailTo = "jaska@omaposti.fi";

    public EmailServiceImplTest() {
        emailSender = mock(JavaMailSender.class);

        messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);

        sut = new EmailServiceImpl(emailTo, emailSender);
    }

    @Test
    public void sendEmail_sendsEmail_serviceEnabled() {
        final MonitorData monitorData = new MonitorData(PC_1, NetworkStatus.OFFLINE);

        final String subjectMsg = "Gone offline!";
        sut.sendEmail(monitorData, subjectMsg);

        verify(emailSender).send(messageCaptor.capture());

        final SimpleMailMessage mailMessage = messageCaptor.getValue();
        assertArrayEquals(new String[] {emailTo}, mailMessage.getTo());
        final String expectedSubject = PC_1.getName() + ": " + subjectMsg;
        assertEquals(expectedSubject, mailMessage.getSubject());
        assertEquals(monitorData.toString(), mailMessage.getText());
    }

    @Test
    public void sendEmail_doesNothing_serviceDisabled() {
        sut = new EmailServiceImpl(emailTo, null);

        final MonitorData monitorData = new MonitorData(PC_1, NetworkStatus.OFFLINE);

        final String subjectMsg = "Gone offline!";
        sut.sendEmail(monitorData, subjectMsg);

        verify(emailSender, never()).send(any(SimpleMailMessage.class));
    }

    @Test
    public void isEnabled_returnsFalse_emailServiceDisabled() {
        sut = new EmailServiceImpl("", null);

        assertFalse(sut.isEnabled());
    }

    @Test
    public void isEnabled_returnsTrue_emailServiceEnabled() {
        assertTrue(sut.isEnabled());
    }

}
