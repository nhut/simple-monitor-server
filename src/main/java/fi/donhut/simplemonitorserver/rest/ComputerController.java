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
package fi.donhut.simplemonitorserver.rest;

import fi.donhut.simplemonitorserver.model.Computer;
import fi.donhut.simplemonitorserver.monitor.MonitorData;
import fi.donhut.simplemonitorserver.monitor.NetworkStatus;
import fi.donhut.simplemonitorserver.monitor.UnderMonitorCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

/**
 * REST API to receive outside source's data.
 *
 * @author Nhut Do (mr.nhut@gmail.com)
 */
@RestController
@RequestMapping("/api/v1")
public class ComputerController {

    private static final Logger LOG = LoggerFactory.getLogger(ComputerController.class);

    private final UnderMonitorCache underMonitorCache = UnderMonitorCache.getInstance();

    @PostMapping("/pc")
    public ResponseEntity<Void> receivePcData(@Validated @RequestBody final Computer computer) {
        @NotNull final String computerName = computer.getName();
        LOG.debug("Received new data from: {}", LOG.isTraceEnabled() ? computer : computerName);
        if (isComputerBackOnline(computer)) {
            LOG.info("{} is back ONLINE!", computerName);
        }
        underMonitorCache.add(computer);
        return ResponseEntity.ok().build();
    }

    private boolean isComputerBackOnline(final Computer computer) {
        final MonitorData prevMonitorData = underMonitorCache.getCache().get(computer.getName());
        if (prevMonitorData == null) {
            return false;
        }
        return prevMonitorData.getNetworkStatus() == NetworkStatus.OFFLINE;
    }
}
