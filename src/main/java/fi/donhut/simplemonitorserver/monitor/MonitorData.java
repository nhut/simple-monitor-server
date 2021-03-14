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

import fi.donhut.simplemonitorserver.model.Computer;

import java.util.StringJoiner;

/**
 * @author Nhut Do (mr.nhut.dev@gmail.com)
 */
public class MonitorData {

    private Computer computer;
    private NetworkStatus networkStatus;

    public MonitorData(Computer computer, NetworkStatus networkStatus) {
        this.computer = computer;
        this.networkStatus = networkStatus;
    }

    public Computer getComputer() {
        return computer;
    }

    public NetworkStatus getNetworkStatus() {
        return networkStatus;
    }

    synchronized public void setNetworkStatus(NetworkStatus networkStatus) {
        this.networkStatus = networkStatus;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", MonitorData.class.getSimpleName() + "[", "]")
                .add("computer=" + computer)
                .add("networkStatus=" + networkStatus)
                .toString();
    }
}
