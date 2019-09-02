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

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Optional;

import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import org.glowroot.instrumentation.test.harness.AppUnderTest;
import org.glowroot.instrumentation.test.harness.Container;
import org.glowroot.instrumentation.test.harness.Containers;
import org.glowroot.instrumentation.test.harness.IncomingSpan;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AzureFunctionsIT {

    private static Container container;

    @BeforeClass
    public static void setUp() throws Exception {
        container = Containers.create();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        // container.close();
    }

    @After
    public void afterEachTest() throws Exception {
        container.resetAfterEachTest();
    }

    @Test
    public void shouldExecuteFunction() throws Exception {
        IncomingSpan incomingSpan = container.execute(ExecuteFunction.class);
        assertThat(incomingSpan.transactionType()).isEqualTo("Web");
        assertThat(incomingSpan.transactionName()).isEqualTo("test-function");
        assertThat(incomingSpan.message()).isEqualTo("GET http://localhost:1234/path");
        assertThat(incomingSpan.childSpans()).isEmpty();
    }

    public static class ExecuteFunction implements AppUnderTest {

        @Override
        public void executeApp(Serializable... args) throws URISyntaxException {
            HttpRequestMessage req = new MockHttpRequestMessage(new URI("http://localhost:1234/path"),
                    HttpMethod.GET, Collections.emptyMap(), Collections.emptyMap(), null);
            new Function().run(req);
        }
    }

    public static class Function {

        @FunctionName("test-function")
        public HttpResponseMessage run(
                @HttpTrigger(name = "req", methods = {HttpMethod.GET, HttpMethod.POST},
                        authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request) {

            return request.createResponseBuilder(HttpStatus.OK)
                    .body("Hello world!")
                    .build();
        }
    }
}
