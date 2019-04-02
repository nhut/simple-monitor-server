/*
 * Copyright since 2019 Nhut Do <mr.nhut@gmail.com>
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
package fi.donhut.simplemonitorserver.scheduler;

import fi.donhut.simplemonitorserver.email.EmailService;
import fi.donhut.simplemonitorserver.model.Computer;
import fi.donhut.simplemonitorserver.monitor.MonitorData;
import fi.donhut.simplemonitorserver.monitor.NetworkStatus;
import fi.donhut.simplemonitorserver.monitor.UnderMonitorCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Monitors when computer and changes its status to OFFLINE if specified time have gone over.
 *
 * @author Nhut Do (mr.nhut@gmail.com)
 */
@Component
public final class OfflineMonitorTask {

    private static final Logger LOG = LoggerFactory.getLogger(OfflineMonitorTask.class);

    @Autowired
    private EmailService emailService;

    @Value("${app.monitor.count-as-offline.seconds}")
    private int seconds;

    @Scheduled(fixedDelay = 10000)
    public void computerStatusMonitor() {
        LOG.trace("Running...");
        final Map<String, MonitorData> cache = UnderMonitorCache.getInstance().getCache();
        for (MonitorData monitorData : cache.values()) {
            if (monitorData.getNetworkStatus() == NetworkStatus.ONLINE) {
                final Computer computer = monitorData.getComputer();
                final LocalDateTime lastDataReceived = computer.getLastReceivedTime();
                if (isGoneOffline(lastDataReceived)) {
                    monitorData.setNetworkStatus(NetworkStatus.OFFLINE);
                    final String infoText = String.format(
                            "Have gone OFFLINE! Last received data: %s", lastDataReceived);
                    LOG.info("{}: {}", computer.getName(), infoText);
                    emailService.sendEmail(monitorData, infoText);
                }
            }
        }
        LOG.trace("Stopped.");
    }

    private boolean isGoneOffline(LocalDateTime lastDataReceived) {
        final LocalDateTime currentTime = LocalDateTime.now();
        return currentTime.isAfter(lastDataReceived.plusSeconds(seconds));
    }
}
