package fi.donhut.simplemonitorserver.monitor;

import fi.donhut.simplemonitorserver.email.EmailService;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class OfflineMonitorTaskSchedulerTest {

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

    @Before
    public void setUp() {
        when(emailService.isEnabled()).thenReturn(true);
    }

    @Test
    public void computerStatusMonitor_doesNothing_emailServiceDisabled() {
        when(emailService.isEnabled()).thenReturn(false);

        sut.computerStatusMonitor();

        verify(emailService).isEnabled();
        verify(emailService, never()).sendEmail(any(), any());
    }

    @Test
    public void computerStatusMonitor_sendsEmailOnPc2GoneOffline_computerGoneOffline() {
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
