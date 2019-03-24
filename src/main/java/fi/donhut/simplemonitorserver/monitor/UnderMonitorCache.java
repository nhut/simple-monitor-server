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
package fi.donhut.simplemonitorserver.monitor;

import fi.donhut.simplemonitorserver.model.Computer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Caches latest received data.
 *
 * @author Nhut Do (mr.nhut@gmail.com)
 */
public final class UnderMonitorCache {

    private static final Logger LOG = LoggerFactory.getLogger(UnderMonitorCache.class);

    private static final int MAX_COMPUTER_TO_MONITOR = 1000000;
    private static final Map<String, MonitorData> monitorData = new HashMap<>(); //key: computerName, value: Monitordata-class.

    private UnderMonitorCache() {
    }

    public static void addComputer(final Computer computer) {
        final String computerName = computer.getName();
        final MonitorData prevMonitorData = UnderMonitorCache.monitorData.get(computerName);
        if (prevMonitorData != null && UnderMonitorCache.monitorData.size() >= MAX_COMPUTER_TO_MONITOR) {
            LOG.warn("Rejected (cache limit exceeded: {} pcs) to monitor new computer: {}",
                    MAX_COMPUTER_TO_MONITOR, computer);
            return;
        }
        UnderMonitorCache.monitorData.put(computerName, new MonitorData(computer, NetworkStatus.ONLINE));
    }
}
