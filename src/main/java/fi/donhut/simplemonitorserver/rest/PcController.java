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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * REST API to receive outside source's data.
 *
 * @author Nhut Do (mr.nhut@gmail.com)
 */
@RestController
@RequestMapping("/api/v1")
public class PcController {

    private static final Logger LOG = LoggerFactory.getLogger(PcController.class);

    @PostMapping("/pc")
    public ResponseEntity<String> receivePcData(@RequestBody final Computer computer) {
        computer.setLastReceivedTime(LocalDateTime.now());
        LOG.debug("Received: {}", computer);
        return ResponseEntity.ok(computer.toString());
    }
}
