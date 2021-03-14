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

import fi.donhut.simplemonitorserver.Constants;
import fi.donhut.simplemonitorserver.email.EmailService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * REST API to receive outside source's data.
 *
 * @author Nhut Do (mr.nhut.dev@gmail.com)
 */
@RestController
@RequestMapping("/api/v1")
public class ComputerController {

    private static final Logger LOG = LoggerFactory.getLogger(ComputerController.class);

    private static final UnderMonitorCache underMonitorCache = UnderMonitorCache.getInstance();

    @Autowired
    private EmailService emailService;

    @PutMapping("/pc")
    @ApiOperation(value = "Receives client sent data.",
        authorizations = {@Authorization(value = Constants.APP_BASIC_AUTH_ID)})
    public ResponseEntity<Void> receivePcData(@Validated @RequestBody final Computer computer) {
        computer.setLastReceivedTime(LocalDateTime.now());
        final String computerName = computer.getName();
        LOG.debug("Received new data from: {}", LOG.isTraceEnabled() ? computer : computerName);
        final boolean computerBackOnline = isComputerBackOnline(computerName);
        underMonitorCache.add(computer);
        if (computerBackOnline) {
            final String infoText = "Is back ONLINE!";
            LOG.info("{}: {}", computerName, infoText);
            emailService.sendEmail(getMonitorDataFromCache(computerName), infoText);
        }
        return ResponseEntity.ok().build();
    }

    private MonitorData getMonitorDataFromCache(final String computerName) {
        return underMonitorCache.getCache().get(computerName);
    }

    private boolean isComputerBackOnline(final String computerName) {
        final MonitorData prevMonitorData = getMonitorDataFromCache(computerName);
        if (prevMonitorData == null) {
            return false;
        }
        return prevMonitorData.getNetworkStatus() == NetworkStatus.OFFLINE;
    }
}
