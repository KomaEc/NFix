package org.testng;

import java.lang.reflect.Constructor;

public interface IObjectFactory extends ITestObjectFactory {
   Object newInstance(Constructor var1, Object... var2);
}
