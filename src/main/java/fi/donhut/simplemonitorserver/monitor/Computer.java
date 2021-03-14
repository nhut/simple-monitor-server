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

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.StringJoiner;
import javax.validation.constraints.NotNull;

/**
 * Model for general computers.
 *
 * @author Nhut Do (mr.nhut.dev@gmail.com)
 */
public class Computer implements Serializable {

    private static final long serialVersionUID = -9088154767294581258L;

    @NotNull
    private String name;
    private String ipAddress;
    private Double cpuLoadPercent;
    private Long memoryUsageInBytes;
    private Long freeSpaceLeftInBytes;
    @JsonIgnore
    private LocalDateTime lastReceivedTime = LocalDateTime.now();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Double getCpuLoadPercent() {
        return cpuLoadPercent;
    }

    public void setCpuLoadPercent(Double cpuLoadPercent) {
        this.cpuLoadPercent = cpuLoadPercent;
    }

    public Long getMemoryUsageInBytes() {
        return memoryUsageInBytes;
    }

    public void setMemoryUsageInBytes(Long memoryUsageInBytes) {
        this.memoryUsageInBytes = memoryUsageInBytes;
    }

    public Long getFreeSpaceLeftInBytes() {
        return freeSpaceLeftInBytes;
    }

    public void setFreeSpaceLeftInBytes(Long freeSpaceLeftInBytes) {
        this.freeSpaceLeftInBytes = freeSpaceLeftInBytes;
    }

    public LocalDateTime getLastReceivedTime() {
        return lastReceivedTime;
    }

    public void setLastReceivedTime(LocalDateTime lastReceivedTime) {
        this.lastReceivedTime = lastReceivedTime;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Computer.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("ipAddress='" + ipAddress + "'")
                .add("cpuLoadPercent=" + cpuLoadPercent)
                .add("memoryUsageInBytes=" + memoryUsageInBytes)
                .add("freeSpaceLeftInBytes=" + freeSpaceLeftInBytes)
                .add("lastReceivedTime=" + lastReceivedTime)
                .toString();
    }
}
