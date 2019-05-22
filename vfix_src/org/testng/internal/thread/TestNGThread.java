package org.testng.internal.thread;

public class TestNGThread extends Thread {
   public TestNGThread(String methodName) {
      super("TestNGInvoker-" + methodName + "()");
   }

   public TestNGThread(Runnable target, String methodName) {
      super(target, "TestNGInvoker-" + methodName + "()");
   }
}
