package org.testng;

import com.google.inject.Module;

public interface IModuleFactory {
   Module createModule(ITestContext var1, Class<?> var2);
}
