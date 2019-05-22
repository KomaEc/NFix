package org.testng;

import java.io.Serializable;

public interface ISuiteResult extends Serializable {
   String getPropertyFileName();

   ITestContext getTestContext();
}
