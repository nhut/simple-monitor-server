package fi.donhut.simplemonitorserver.monitor;

import fi.donhut.simplemonitorserver.email.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class ComputerControllerTest {

    private final EmailService emailService;

    private final ComputerController sut;

    private final UnderMonitorCache underMonitorCache;

    private Computer pc1;

    public ComputerControllerTest() {
        emailService = mock(EmailService.class);
        underMonitorCache = new UnderMonitorCache();

        sut = new ComputerController(emailService) {
            @Override
            UnderMonitorCache getUnderMonitorCache() {
                return underMonitorCache;
            }
        };
    }

    @BeforeEach
    void setUp() {
        final LocalDateTime lastReceivedTime = LocalDateTime.now();
        pc1 = createComputer(lastReceivedTime);
    }

    private static Computer createComputer(LocalDateTime lastReceivedTime) {
        return new Computer("pc1", "10.0.0.2", 11.11, 1234L, 1234L, lastReceivedTime);
    }

    @Test
    void receivePcData_doesNothing_wasOnlinePreviously() {
        underMonitorCache.add(pc1);

        final ResponseEntity<Void> responseEntity = sut.receivePcData(createComputer(LocalDateTime.now()));

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(emailService, never()).sendEmail(any(), any());
    }

    @Test
    void receivePcData_sendsEmailWentBackOnline_wasOfflinePreviously() {
        pc1.setLastReceivedTime(LocalDateTime.now().minusDays(1));
        underMonitorCache.add(pc1);
        underMonitorCache.updateStatus(pc1.getName(), NetworkStatus.OFFLINE);

        final ResponseEntity<Void> responseEntity = sut.receivePcData(createComputer(LocalDateTime.now()));

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(emailService).sendEmail(any(), any());
    }
}
