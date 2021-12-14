package fi.donhut.simplemonitorserver.monitor;

import fi.donhut.simplemonitorserver.email.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OfflineMonitorTaskSchedulerTest {

    private final int offlineInSeconds = 10;
    private final EmailService emailService;
    private final UnderMonitorCache cache;

    private final OfflineMonitorTaskScheduler sut;

    public OfflineMonitorTaskSchedulerTest() {
        emailService = mock(EmailService.class);
        cache = new UnderMonitorCache();

        sut = new OfflineMonitorTaskScheduler(offlineInSeconds, emailService) {
            @Override
            UnderMonitorCache getUnderMonitorCache() {
                return cache;
            }
        };
    }

    @BeforeEach
    void setUp() {
        when(emailService.isEnabled()).thenReturn(true);
    }

    @Test
    void computerStatusMonitor_doesNothing_emailServiceDisabled() {
        when(emailService.isEnabled()).thenReturn(false);

        sut.computerStatusMonitor();

        verify(emailService).isEnabled();
        verify(emailService, never()).sendEmail(any(), any());
    }

    @Test
    void computerStatusMonitor_sendsEmailOnPc2GoneOffline_computerGoneOffline() {
        when(emailService.isEnabled()).thenReturn(true);

        final LocalDateTime currentTime = LocalDateTime.now();
        final Computer computerOnline = new Computer("pc1", "10.0.0.2", 11.1, 1_000L, 100_000_000L, currentTime);
        cache.add(computerOnline);

        final Computer computerOffline = new Computer("pc2", "10.0.0.3", 22.5, 2_000L, 200_000_000L,
            currentTime.minusSeconds(offlineInSeconds).minusSeconds(5));
        cache.add(computerOffline);

        sut.computerStatusMonitor();

        verify(emailService).isEnabled();
        verify(emailService).sendEmail(eq(cache.getCache().get("pc2")), anyString());
    }
}
