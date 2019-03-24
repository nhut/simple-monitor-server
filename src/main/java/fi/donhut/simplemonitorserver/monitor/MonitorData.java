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
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Nhut Do (mr.nhut@gmail.com)
 */
@AllArgsConstructor
@Getter
public class MonitorData {

    private Computer computer;
    private NetworkStatus networkStatus;
}
