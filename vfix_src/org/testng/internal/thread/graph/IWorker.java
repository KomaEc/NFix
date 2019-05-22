package org.testng.internal.thread.graph;

import java.util.List;

public interface IWorker<T> extends Runnable, Comparable<IWorker<T>> {
   List<T> getTasks();

   long getTimeOut();

   int getPriority();
}
