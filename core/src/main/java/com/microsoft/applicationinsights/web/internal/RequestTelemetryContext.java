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

package com.microsoft.applicationinsights.web.internal;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.microsoft.applicationinsights.telemetry.RequestTelemetry;
import com.microsoft.applicationinsights.web.internal.correlation.CorrelationContext;
import com.microsoft.applicationinsights.web.internal.correlation.tracecontext.Tracestate;

/**
 * Created by yonisha on 2/2/2015.
 */
public class RequestTelemetryContext {
    private RequestTelemetry requestTelemetry;
    private long requestStartTimeTicks;
    private String sessionCookie;
    private String userCookie;
    private boolean isNewSession = false;
    private Map<String, String> requestHeaders;
    private final CorrelationContext correlationContext;
    private Tracestate tracestate;
    private int traceflag;
    private final AtomicInteger currentChildId = new AtomicInteger();

    /**
     * Constructs new RequestTelemetryContext object.
     * @param ticks The time in ticks
     */
    public RequestTelemetryContext(long ticks) {
        this(ticks, null);
    }

    public Tracestate getTracestate() {
        return tracestate;
    }

    public void setTracestate(
        Tracestate tracestate) {
        this.tracestate = tracestate;
    }

    /**
     * Constructs new RequestTelemetryContext object.
     * @param ticks The time in ticks
     * @param servletRequest The servlet request
     */
    public RequestTelemetryContext(long ticks, Map<String, String> requestHeaders) {
        requestTelemetry = new RequestTelemetry();
        requestStartTimeTicks = ticks;
        this.requestHeaders = requestHeaders;
        correlationContext = new CorrelationContext();
    }

    public int getTraceflag() {
        return traceflag;
    }

    public void setTraceflag(int traceflag) {
        this.traceflag = traceflag;
    }

    /**
     * Gets the correlation context associated with the request
     * @return The correlation context map.
     */
    public CorrelationContext getCorrelationContext() {
        return correlationContext;
    }

    /**
     * Gets the http request telemetry associated with the context.
     * @return The http request telemetry.
     */
    public RequestTelemetry getHttpRequestTelemetry() {
        return requestTelemetry;
    }

    /**
     * Gets the request start time in ticks
     * @return Request start time in ticks
     */
    public long getRequestStartTimeTicks() {
        return requestStartTimeTicks;
    }

    /**
     * Sets the session cookie.
     * @param sessionCookie The session cookie.
     */
    public void setSessionCookie(String sessionCookie) {
        this.sessionCookie = sessionCookie;
    }

    /**
     * Gets the session cookie.
     * @return Session cookie.
     */
    public String getSessionCookie() {
        return sessionCookie;
    }

    /**
     * Sets the user cookie.
     * @param userCookie The user cookie.
     */
    public void setUserCookie(String userCookie) {
        this.userCookie = userCookie;
    }

    /**
     * Gets the user cookie.
     * @return The user cookie.
     */
    public String getUserCookie() {
        return userCookie;
    }

    /**
     * Sets if the session is new or not.
     * @param isNewSession Indicates whether the session is new or not.
     */
    public void setIsNewSession(boolean isNewSession) {
        this.isNewSession = isNewSession;
    }

    /**
     * Gets a value indicating whether the session is new or not.
     * @return True if the session is new, false otherwise.
     */
    public boolean getIsNewSession() {
        return isNewSession;
    }

    public Map<String, String> getRequestHeaders() {
        return requestHeaders;
    }

    /**
     * @return the currentChildId
     */
    public int incrementChildId() {
        return this.currentChildId.addAndGet(1);
    }
}
