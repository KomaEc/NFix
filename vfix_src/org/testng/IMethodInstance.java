package org.testng;

public interface IMethodInstance {
   ITestNGMethod getMethod();

   /** @deprecated */
   Object[] getInstances();

   Object getInstance();
}
