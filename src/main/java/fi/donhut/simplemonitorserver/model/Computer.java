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
package fi.donhut.simplemonitorserver.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Model.
 *
 * @author Nhut Do (mr.nhut@gmail.com)
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class Computer implements Serializable {

    private static final long serialVersionUID = -9088154767294581258L;

    @NotNull
    private String name;
    private String ipAddress;
    private Double cpuLoadPercent;
    private Double memoryUsageInBytes;
    private Long freeSpaceLeftInBytes;
    private LocalDateTime lastReceivedTime = LocalDateTime.now();

}
