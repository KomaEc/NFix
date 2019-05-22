package org.testng.internal.thread;

public interface IFutureResult {
   Object get() throws InterruptedException, ThreadExecutionException;
}
