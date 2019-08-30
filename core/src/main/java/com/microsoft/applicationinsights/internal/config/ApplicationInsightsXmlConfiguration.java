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

package com.microsoft.applicationinsights.internal.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * Created by gupele on 3/13/2015.
 */
@XStreamAlias("ApplicationInsights")
public class ApplicationInsightsXmlConfiguration {

    @XStreamAlias("InstrumentationKey")
    private String instrumentationKey;

    @XStreamAlias("ConnectionString")
    private String connectionString;

    @XStreamAlias("RoleName")
    private String roleName;

    @XStreamAlias("DisableTelemetry")
    public boolean disableTelemetry;

    @XStreamAlias("TelemetryInitializers")
    private TelemetryInitializersXmlElement telemetryInitializers;

    @XStreamAlias("TelemetryProcessors")
    private TelemetryProcessorsXmlElement telemetryProcessors;

    @XStreamAlias("ContextInitializers")
    private ContextInitializersXmlElement contextInitializers;

    @XStreamAlias("Channel")
    private ChannelXmlElement channel = new ChannelXmlElement();

    @XStreamAlias("TelemetryModules")
    private TelemetryModulesXmlElement modules;

    @XStreamAlias("PerformanceCounters")
    private PerformanceCountersXmlElement performance = new PerformanceCountersXmlElement();

    @XStreamAlias("SDKLogger")
    private SDKLoggerXmlElement sdkLogger;

    @XStreamAlias("Sampling")
    private SamplerXmlElement sampler;

    @XStreamAlias("QuickPulse")
    private QuickPulseXmlElement quickPulse;

    @XStreamAsAttribute
    private String schemaVersion;

    public String getInstrumentationKey() {
        return instrumentationKey;
    }

    public void setInstrumentationKey(String instrumentationKey) {
        this.instrumentationKey = instrumentationKey;
    }

    public String getConnectionString() {
        return connectionString;
    }

    public void setConnectionString(String connectionString) {
        this.connectionString = connectionString;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getSchemaVersion() {
        return schemaVersion;
    }

    public void setSchemaVersion(String schemaVersion) {
        this.schemaVersion = schemaVersion;
    }

    public TelemetryInitializersXmlElement getTelemetryInitializers() {
        return telemetryInitializers;
    }

    public void setTelemetryInitializers(TelemetryInitializersXmlElement telemetryInitializers) {
        this.telemetryInitializers = telemetryInitializers;
    }

    public ContextInitializersXmlElement getContextInitializers() {
        return contextInitializers;
    }

    public void setTelemetryProcessors(TelemetryProcessorsXmlElement telemetryProcessors) {
        this.telemetryProcessors = telemetryProcessors;
    }

    public TelemetryProcessorsXmlElement getTelemetryProcessors() {
        return telemetryProcessors;
    }

    public void setContextInitializers(ContextInitializersXmlElement contextInitializers) {
        this.contextInitializers = contextInitializers;
    }

    public ChannelXmlElement getChannel() {
        return channel;
    }

    public void setChannel(ChannelXmlElement channel) {
        this.channel = channel;
    }

    public SamplerXmlElement getSampler() {
        return sampler;
    }

    public void setSampler(SamplerXmlElement sampler) {
        this.sampler = sampler;
    }

    public QuickPulseXmlElement getQuickPulse() {
        if (quickPulse == null) {
            quickPulse = new QuickPulseXmlElement();
        }
        return quickPulse;
    }

    public void setQuickPulse(QuickPulseXmlElement quickPulse) {
        this.quickPulse = quickPulse;
    }

    public SDKLoggerXmlElement getSdkLogger() {
        return sdkLogger;
    }

    public void setSdkLogger(SDKLoggerXmlElement sdkLogger) {
        this.sdkLogger = sdkLogger;
    }

    public boolean isDisableTelemetry() {
        return disableTelemetry;
    }

    public void setDisableTelemetry(boolean disableTelemetry) {
        this.disableTelemetry = disableTelemetry;
    }

    public TelemetryModulesXmlElement getModules() {
        return modules;
    }

    public void setModules(TelemetryModulesXmlElement modules) {
        this.modules = modules;
    }

    public PerformanceCountersXmlElement getPerformance() {
        return performance;
    }

    public void setPerformance(PerformanceCountersXmlElement performance) {
        this.performance = performance;
    }
}
