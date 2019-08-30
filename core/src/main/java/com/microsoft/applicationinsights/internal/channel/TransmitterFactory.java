/*
 * ApplicationInsights-Java
 * Copyright (c) Microsoft Corporation
 * All rights reserved.
 *
 * MIT License
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the ""Software""), to deal in the Software
 * without restriction, including without limitation the rights to use, copy, modify, merge,
 * publish, distribute, sublicense, and/or sell copies of the Software, and to permit
 * persons to whom the Software is furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED *AS IS*, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package com.microsoft.applicationinsights.internal.channel;

import com.microsoft.applicationinsights.TelemetryConfiguration;

import javax.annotation.Nullable;

/**
 * Created by gupele on 12/21/2014.
 */
public interface TransmitterFactory<T> {
    /**
     * Creates the {@link TelemetriesTransmitter} for use by the {@link com.microsoft.applicationinsights.channel.TelemetryChannel}
     * @param endpoint HTTP Endpoint to send telemetry to
     * @param maxTransmissionStorageCapacity Max amount of disk space in KB for persistent storage to use
     * @param throttlingIsEnabled Allow the network telemetry sender to be throttled
     * @param maxInstantRetries Number of instant retries in case of a temporary network outage
     * @return The {@link TelemetriesTransmitter} object
     * @deprecated Use {@link ConfiguredTransmitterFactory#create(TelemetryConfiguration, String, String, boolean, int)}
     */
    @Deprecated
    TelemetriesTransmitter<T> create(@Nullable String endpoint, String maxTransmissionStorageCapacity, boolean throttlingIsEnabled, int maxInstantRetries);
}
