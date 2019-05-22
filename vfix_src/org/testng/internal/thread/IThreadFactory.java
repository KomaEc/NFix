package org.testng.internal.thread;

import java.util.List;

public interface IThreadFactory {
   Thread newThread(Runnable var1);

   Object getThreadFactory();

   List<Thread> getThreads();
}
