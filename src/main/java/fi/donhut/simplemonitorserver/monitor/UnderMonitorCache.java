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
package fi.donhut.simplemonitorserver.monitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Caches latest received data.
 *
 * @author Nhut Do (mr.nhut.dev@gmail.com)
 */
public final class UnderMonitorCache {

    private static final Logger LOG = LoggerFactory.getLogger(UnderMonitorCache.class);

    private static final UnderMonitorCache INSTANCE = new UnderMonitorCache();
    private static final int MAX_COMPUTER_TO_MONITOR = 1_000_000;
    private final Map<String, MonitorData> cache = new HashMap<>(); //key: computerName, value: Monitordata-class.

    public static UnderMonitorCache getInstance() {
        return INSTANCE;
    }

    public Map<String, MonitorData> getCache() {
        return new HashMap<>(cache);
    }

    public void add(final Computer computer) {
        final String computerName = computer.getName();
        if (cache.get(computerName) != null && cache.size() >= MAX_COMPUTER_TO_MONITOR) {
            LOG.warn("Rejected (cache limit exceeded: {} pcs) to monitor new computer: {}",
                MAX_COMPUTER_TO_MONITOR, computer);
            return;
        }

        cache.put(computerName, new MonitorData(computer, NetworkStatus.ONLINE));
    }

    public void reset() {
        cache.clear();
    }

    public void updateStatus(final String name, NetworkStatus networkStatus) {
        final MonitorData monitorData = cache.get(name);
        if (monitorData == null) {
            return;
        }
        monitorData.setNetworkStatus(networkStatus);
    }
}
