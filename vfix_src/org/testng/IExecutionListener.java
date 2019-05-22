package org.testng;

public interface IExecutionListener extends ITestNGListener {
   void onExecutionStart();

   void onExecutionFinish();
}
