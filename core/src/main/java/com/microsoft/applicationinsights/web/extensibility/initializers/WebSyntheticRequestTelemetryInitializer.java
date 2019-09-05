/*
 * AppInsights-Java
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

package com.microsoft.applicationinsights.web.extensibility.initializers;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import com.microsoft.applicationinsights.common.CommonUtils;
import com.microsoft.applicationinsights.telemetry.Telemetry;
import com.microsoft.applicationinsights.web.internal.RequestTelemetryContext;
import com.microsoft.applicationinsights.web.internal.ThreadContext;

/**
 * Created by gupele on 8/17/2015.
 */
public class WebSyntheticRequestTelemetryInitializer extends WebTelemetryInitializerBase {
    final static String SYNTHETIC_SOURCE_NAME = "Application Insights Availability Monitoring";
    final static String SYNTHETIC_TEST_RUN_ID = "SyntheticTest-RunId";
    final static String SYNTHETIC_TEST_LOCATION = "SyntheticTest-Location";
    final static String SYNTHETIC_TEST_SOURCE = "SyntheticTest-Source";
    final static String SYNTHETIC_TEST_TEST_NAME = "SyntheticTest-TestName";
    final static String SYNTHETIC_TEST_SESSION_ID = "SyntheticTest-SessionId";
    final static String SYNTHETIC_TEST_USER_ID = "SyntheticTest-UserId";
    final static String SYNTHETIC_TEST_OPERATION_ID = "SyntheticTest-OperationId";

    @Override
    protected void onInitializeTelemetry(Telemetry telemetry) {
        RequestTelemetryContext telemetryContext = ThreadContext.getRequestTelemetryContext();

        if (telemetryContext == null) {
            return;
        }

        Map<String, String> requestHeaders = telemetryContext.getRequestHeaders();
        if (requestHeaders == null) {
            return;
        }

        String syntheticSourceHeader = requestHeaders.get(SYNTHETIC_TEST_SOURCE);
        if (CommonUtils.isNullOrEmpty(syntheticSourceHeader)) {
            handlePossibleGSMSyntheticRequest(telemetry, requestHeaders);
        } else {
            handleCommonSyntheticRequest(syntheticSourceHeader, telemetry, requestHeaders);
        }
    }

    private void handlePossibleGSMSyntheticRequest(Telemetry telemetry, Map<String, String> requestHeaders) {
        String gsmSyntheticTestRunId = requestHeaders.get(SYNTHETIC_TEST_RUN_ID);
        if (CommonUtils.isNullOrEmpty(gsmSyntheticTestRunId)) {
            return;
        }

        String syntheticSource = telemetry.getContext().getOperation().getSyntheticSource();
        if (CommonUtils.isNullOrEmpty(syntheticSource)) {
            telemetry.getContext().getOperation().setSyntheticSource(SYNTHETIC_SOURCE_NAME);
        }

        String sessionId = telemetry.getContext().getSession().getId();
        if (CommonUtils.isNullOrEmpty(sessionId)) {
            telemetry.getContext().getSession().setId(gsmSyntheticTestRunId);
        }

        String userId = telemetry.getContext().getUser().getId();
        if (CommonUtils.isNullOrEmpty(userId)) {
            String header = requestHeaders.get(SYNTHETIC_TEST_LOCATION);
            telemetry.getContext().getUser().setId(header);
        }
    }

    private void handleCommonSyntheticRequest(String syntheticSourceHeader, Telemetry telemetry,
                                              Map<String, String> requestHeaders) {
        String syntheticSource = telemetry.getContext().getOperation().getSyntheticSource();
        if (CommonUtils.isNullOrEmpty(syntheticSource)) {
            telemetry.getContext().getOperation().setSyntheticSource(syntheticSourceHeader);
        }

        String userId = telemetry.getContext().getUser().getId();
        if (CommonUtils.isNullOrEmpty(userId)) {
            String header = requestHeaders.get(SYNTHETIC_TEST_USER_ID);
            telemetry.getContext().getUser().setId(header);
        }

        String sessionId = telemetry.getContext().getSession().getId();
        if (CommonUtils.isNullOrEmpty(sessionId)) {
            String header = requestHeaders.get(SYNTHETIC_TEST_SESSION_ID);
            telemetry.getContext().getSession().setId(header);
        }

        String operationId = telemetry.getContext().getOperation().getId();
        if (CommonUtils.isNullOrEmpty(operationId)) {
            String header = requestHeaders.get(SYNTHETIC_TEST_OPERATION_ID);
            telemetry.getContext().getOperation().setId(header);
        }

        putInProperties(telemetry, requestHeaders, SYNTHETIC_TEST_TEST_NAME, SYNTHETIC_TEST_RUN_ID,
                SYNTHETIC_TEST_LOCATION);
    }

    private void putInProperties(Telemetry telemetry, Map<String, String> requestHeaders, String... headers) {
        ConcurrentMap<String, String> properties = telemetry.getContext().getProperties();
        for (String header : headers) {
            String headerValue = requestHeaders.get(header);
            if (headerValue != null) {
                properties.put(header, headerValue);
            }
        }
    }
}
