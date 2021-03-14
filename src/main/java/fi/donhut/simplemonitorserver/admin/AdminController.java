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
package fi.donhut.simplemonitorserver.admin;

import fi.donhut.simplemonitorserver.monitor.MonitorData;
import fi.donhut.simplemonitorserver.monitor.UnderMonitorCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.TreeSet;

/**
 * REST API for admin roles.
 *
 * @author Nhut Do (mr.nhut.dev@gmail.com)
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    private static final Logger LOG = LoggerFactory.getLogger(AdminController.class);
    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @GetMapping("")
    public ResponseEntity<String> getComputers() {
        final Map<String, MonitorData> pcs = UnderMonitorCache.getInstance().getCache();
        if (pcs.isEmpty()) {
            return ResponseEntity.ok("No results.");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<table border=\"1\" cellpadding=\"5\">");
        for (String key : new TreeSet<>(pcs.keySet())) {
            final MonitorData monitorData = pcs.get(key);
            sb.append("<tr>");
            sb.append("<td>").append(monitorData.getNetworkStatus().name()).append("</td>")
                    .append("<td>").append(key).append("</td>")
                    .append("<td>").append(monitorData.getComputer()
                    .getLastReceivedTime().format(DATE_TIME_FORMATTER)).append("</td>");
            sb.append("</tr>");
        }
        sb.append("</table>");
        LOG.trace("Response: {}", sb);
        return ResponseEntity.ok(sb.toString());
    }
}
