package org.testng;

import java.util.List;

public interface IMethodInterceptor extends ITestNGListener {
   List<IMethodInstance> intercept(List<IMethodInstance> var1, ITestContext var2);
}
