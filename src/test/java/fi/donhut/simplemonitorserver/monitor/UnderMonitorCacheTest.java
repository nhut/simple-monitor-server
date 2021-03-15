package fi.donhut.simplemonitorserver.monitor;

import org.junit.After;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UnderMonitorCacheTest {

    private final UnderMonitorCache sut;

    public UnderMonitorCacheTest() {
        sut = UnderMonitorCache.getInstance();
    }

    @After
    public void tearDown() {
        sut.reset();
    }

    @Test
    public void addIntoCache_adds2ComputerIntoCache() {
        final Map<String, MonitorData> cacheBeforeAdd = sut.getCache();
        assertTrue(cacheBeforeAdd.isEmpty());

        final Computer computer1 = new Computer("pc1", "10.0.0.1", 11.00, 1_000L, 1_000_000L, LocalDateTime.now());
        final Computer computer2 = new Computer("pc2", "10.0.0.2", 22.00, 2_000L, 2_000_000L, LocalDateTime.now());
        sut.add(computer1);
        sut.add(computer2);

        final Map<String, MonitorData> cacheAfterAdd = sut.getCache();
        assertEquals(2, cacheAfterAdd.size());
        assertMonitorDataInCache(cacheAfterAdd, computer1);
        assertMonitorDataInCache(cacheAfterAdd, computer2);
    }

    private static void assertMonitorDataInCache(final Map<String, MonitorData> cacheContents, final Computer computer) {
        final MonitorData monitorData = cacheContents.get(computer.getName());
        assertEquals(computer, monitorData.getComputer());
        assertEquals(NetworkStatus.ONLINE, monitorData.getNetworkStatus());
    }

    @Test
    public void reset_clearsCache() {
        addIntoCache_adds2ComputerIntoCache();

        sut.reset();

        assertTrue(sut.getCache().isEmpty());
    }
}
