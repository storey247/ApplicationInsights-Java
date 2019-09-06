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

package com.microsoft.applicationinsights.agentc.internal;

import java.util.Date;

import com.google.common.base.Strings;
import com.microsoft.applicationinsights.agentc.internal.model.DistributedTraceContext;
import com.microsoft.applicationinsights.agentc.internal.model.Global;
import com.microsoft.applicationinsights.agentc.internal.model.IncomingSpanImpl;
import com.microsoft.applicationinsights.agentc.internal.model.NopThreadSpan;
import com.microsoft.applicationinsights.agentc.internal.model.TelemetryCorrelationUtilsCore;
import com.microsoft.applicationinsights.agentc.internal.model.ThreadContextImpl;
import com.microsoft.applicationinsights.agentc.internal.model.TraceContextCorrelationCore;
import com.microsoft.applicationinsights.telemetry.RequestTelemetry;
import org.glowroot.instrumentation.api.Getter;
import org.glowroot.instrumentation.api.MessageSupplier;
import org.glowroot.instrumentation.api.Span;
import org.glowroot.instrumentation.api.TimerName;
import org.glowroot.instrumentation.engine.bytecode.api.ThreadContextThreadLocal;
import org.glowroot.instrumentation.engine.spi.AgentSPI;

class AgentImpl implements AgentSPI {

    @Override
    public <C> Span startIncomingSpan(String transactionType, String transactionName, Getter<C> getter, C carrier,
                                      MessageSupplier messageSupplier, TimerName timerName,
                                      ThreadContextThreadLocal.Holder threadContextHolder, int rootNestingGroupId,
                                      int rootSuppressionKeyId) {

        if (!transactionType.equals("Web")) {
            // this is a little more complicated than desired, but part of the contract of startIncomingSpan is that it
            // sets a ThreadContext in the threadContextHolder before returning, and NopThreadSpan makes sure to clear
            // the threadContextHolder at the end of the thread
            NopThreadSpan nopThreadSpan = new NopThreadSpan(threadContextHolder);
            threadContextHolder.set(new NopThreadContext(rootNestingGroupId, rootSuppressionKeyId));
            return nopThreadSpan;
        }

        long startTimeMillis = System.currentTimeMillis();

        RequestTelemetry requestTelemetry = new RequestTelemetry();

        requestTelemetry.setName(transactionName);
        requestTelemetry.getContext().getOperation().setName(transactionName);
        requestTelemetry.setTimestamp(new Date(startTimeMillis));

        String userAgent = getter.get(carrier, "User-Agent");
        requestTelemetry.getContext().getUser().setUserAgent(userAgent);

        String instrumentationKey = Global.getTelemetryClient().getContext().getInstrumentationKey();
        DistributedTraceContext distributedTraceContext;
        if (Global.isOutboundW3CEnabled()) {
            distributedTraceContext =
                    TraceContextCorrelationCore.resolveCorrelationForRequest(carrier, getter, requestTelemetry);
            TraceContextCorrelationCore.resolveRequestSource(carrier, getter, requestTelemetry, instrumentationKey);
        } else {
            distributedTraceContext =
                    TelemetryCorrelationUtilsCore.resolveCorrelationForRequest(carrier, getter, requestTelemetry);
            TelemetryCorrelationUtilsCore.resolveRequestSource(carrier, getter, requestTelemetry, instrumentationKey);
        }
        if (requestTelemetry.getContext().getOperation().getParentId() == null) {
            requestTelemetry.getContext().getOperation().setParentId(requestTelemetry.getId());
        }

        IncomingSpanImpl incomingSpan = new IncomingSpanImpl(messageSupplier, threadContextHolder, startTimeMillis,
                requestTelemetry, distributedTraceContext);

        ThreadContextImpl mainThreadContext =
                new ThreadContextImpl(incomingSpan, rootNestingGroupId, rootSuppressionKeyId, false);
        threadContextHolder.set(mainThreadContext);

        return incomingSpan;
    }
}
