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

package com.microsoft.applicationinsights.instrumentation.azurefunctions;

import java.util.Locale;

import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpResponseMessage.Builder;
import org.glowroot.instrumentation.api.Agent;
import org.glowroot.instrumentation.api.Getter;
import org.glowroot.instrumentation.api.MessageSupplier;
import org.glowroot.instrumentation.api.OptionalThreadContext;
import org.glowroot.instrumentation.api.OptionalThreadContext.AlreadyInTransactionBehavior;
import org.glowroot.instrumentation.api.Setter;
import org.glowroot.instrumentation.api.Span;
import org.glowroot.instrumentation.api.TimerName;
import org.glowroot.instrumentation.api.checker.Nullable;
import org.glowroot.instrumentation.api.weaving.Advice;
import org.glowroot.instrumentation.api.weaving.Bind;
import org.glowroot.instrumentation.api.weaving.Mixin;

public class AzureFunctionsInstrumentation {

    private static final TimerName TIMER_NAME = Agent.getTimerName("http request");

    private static final Getter<HttpRequestMessage> GETTER = new HttpRequestMessageGetter();

    private static final Setter<Builder> SETTER = new HttpResponseMessageBuilderSetter();

    // the field and method names are verbose since they will be mixed in to existing classes
    @Mixin("com.microsoft.azure.functions.HttpRequestMessage")
    public abstract static class HttpRequestMessageImpl implements HttpRequestMessageMixin {

        private transient volatile @Nullable Span glowroot$span;

        @Override
        public @Nullable Span glowroot$getSpan() {
            return glowroot$span;
        }

        @Override
        public void glowroot$setSpan(@Nullable Span span) {
            glowroot$span = span;
        }
    }

    // the method names are verbose since they will be mixed in to existing classes
    public interface HttpRequestMessageMixin {

        @Nullable
        Span glowroot$getSpan();

        void glowroot$setSpan(@Nullable Span span);
    }

    @Advice.Pointcut(
            methodAnnotation = "com.microsoft.azure.functions.annotation.FunctionName",
            methodParameterTypes = {".."},
            nestingGroup = "outer-servlet-or-filter")
    public static class FunctionAdvice {

        @Advice.OnMethodBefore
        public static @Nullable Span onBefore(OptionalThreadContext context,
                                              @Bind.AllArguments Object[] args,
                                              @Bind.MethodMeta FunctionNameMethodMeta functionName) {

            if (context.isInTransaction()) {
                return null;
            }
            HttpRequestMessage request = null;
            for (Object arg : args) {
                if (arg instanceof HttpRequestMessage) {
                    request = (HttpRequestMessage) arg;
                    break;
                }
            }
            if (request == null) {
                return null;
            }
            // already checked isInTransaction() above, so last arg is irrelevant
            Span span = context.startIncomingSpan("Web", functionName.getFunctionName(), GETTER, request,
                    MessageSupplier.create("{} {}", request.getHttpMethod().name(), request.getUri().toString()),
                    TIMER_NAME, AlreadyInTransactionBehavior.CAPTURE_LOCAL_SPAN);
            ((HttpRequestMessageMixin) request).glowroot$setSpan(span);
            return span;
        }

        @Advice.OnMethodReturn
        public static void onReturn(@Bind.Enter @Nullable Span span) {

            if (span == null) {
                return;
            }
            span.end();
        }

        @Advice.OnMethodThrow
        public static void onThrow(@Bind.Thrown Throwable t,
                                   @Bind.Enter @Nullable Span span) {

            if (span == null) {
                return;
            }
            span.endWithError(t);
        }
    }

    @Advice.Pointcut(
            className = "com.microsoft.azure.functions.HttpRequestMessage",
            methodName = "createResponseBuilder",
            methodParameterTypes = {".."},
            methodReturnType = "com.microsoft.azure.functions.HttpResponseMessage.Builder",
            nestingGroup = "azure-functions-create-response-builder")
    public static class ResponseBuilderAdvice {

        @Advice.OnMethodReturn
        public static void onReturn(@Bind.Return HttpResponseMessage.Builder builder,
                                    @Bind.This HttpRequestMessageMixin request) {

            Span span = request.glowroot$getSpan();
            if (span != null) {
                span.propagateToResponse(builder, SETTER);
            }
        }
    }

    private static class HttpRequestMessageGetter implements Getter<HttpRequestMessage> {

        @Override
        public @Nullable String get(HttpRequestMessage carrier, String key) {
            // TODO why is IDE giving me error assigning directly to String?
            // headers map is case-sensitive, with all lower case keys
            Object value = carrier.getHeaders().get(key.toLowerCase(Locale.ENGLISH));
            return value instanceof String ? (String) value : null;
        }
    }

    private static class HttpResponseMessageBuilderSetter implements Setter<Builder> {

        @Override
        public void put(HttpResponseMessage.Builder carrier, String key, String value) {
            carrier.header(key, value);
        }
    }
}
