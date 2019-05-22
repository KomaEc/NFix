package org.testng.internal.thread.graph;

import java.util.List;

public interface IThreadWorkerFactory<T> {
   List<IWorker<T>> createWorkers(List<T> var1);
}
