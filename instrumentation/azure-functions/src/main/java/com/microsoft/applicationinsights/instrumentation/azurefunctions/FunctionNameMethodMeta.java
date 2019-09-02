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

import java.lang.reflect.Method;

import com.microsoft.azure.functions.annotation.FunctionName;
import org.glowroot.instrumentation.api.MethodInfo;
import org.glowroot.instrumentation.api.util.Reflection;

public class FunctionNameMethodMeta {

    private final String functionName;

    public FunctionNameMethodMeta(MethodInfo methodInfo) {
        this.functionName = initFunctionName(methodInfo);
    }

    public String getFunctionName() {
        return functionName;
    }

    private static String initFunctionName(MethodInfo methodInfo) {
        Class<?> clazz = Reflection.getClass(methodInfo.getDeclaringClassName(), methodInfo.getLoader());
        if (clazz == null) {
            return "";
        }
        Class<?>[] parameterTypes =
                methodInfo.getParameterTypes().toArray(new Class<?>[methodInfo.getParameterTypes().size()]);
        Method method = Reflection.getMethod(clazz, methodInfo.getName(), parameterTypes);
        if (method == null) {
            return "";
        }
        FunctionName functionName = method.getAnnotation(FunctionName.class);
        if (functionName == null) {
            return "";
        }
        return functionName.value();
    }
}
