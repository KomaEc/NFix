package org.testng.internal.thread.graph;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.testng.TestNGException;
import org.testng.collections.Lists;
import org.testng.internal.DynamicGraph;

public class GraphThreadPoolExecutor<T> extends ThreadPoolExecutor {
   private static final boolean DEBUG = false;
   private static final boolean DOT_FILES = false;
   private DynamicGraph<T> m_graph;
   private List<Runnable> m_activeRunnables = Lists.newArrayList();
   private IThreadWorkerFactory<T> m_factory;
   private List<String> m_dotFiles = Lists.newArrayList();
   private int m_threadCount;

   public GraphThreadPoolExecutor(DynamicGraph<T> graph, IThreadWorkerFactory<T> factory, int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
      super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
      this.ppp("Initializing executor with " + corePoolSize + " threads and following graph " + graph);
      this.m_threadCount = maximumPoolSize;
      this.m_graph = graph;
      this.m_factory = factory;
      if (this.m_graph.getFreeNodes().isEmpty()) {
         throw new TestNGException("The graph of methods contains a cycle:" + graph.getEdges());
      }
   }

   public void run() {
      synchronized(this.m_graph) {
         List<T> freeNodes = this.m_graph.getFreeNodes();
         this.runNodes(freeNodes);
      }
   }

   private void runNodes(List<T> freeNodes) {
      List<IWorker<T>> runnables = this.m_factory.createWorkers(freeNodes);
      Iterator i$ = runnables.iterator();

      while(i$.hasNext()) {
         IWorker<T> r = (IWorker)i$.next();
         this.m_activeRunnables.add(r);
         this.ppp("Added to active runnable");
         this.setStatus(r, DynamicGraph.Status.RUNNING);
         this.ppp("Executing: " + r);

         try {
            this.execute(r);
         } catch (Exception var6) {
            var6.printStackTrace();
         }
      }

   }

   private void setStatus(IWorker<T> worker, DynamicGraph.Status status) {
      this.ppp("Set status:" + worker + " status:" + status);
      if (status == DynamicGraph.Status.FINISHED) {
         this.m_activeRunnables.remove(worker);
      }

      synchronized(this.m_graph) {
         Iterator i$ = worker.getTasks().iterator();

         while(i$.hasNext()) {
            T m = i$.next();
            this.m_graph.setStatus(m, status);
         }

      }
   }

   public void afterExecute(Runnable r, Throwable t) {
      this.ppp("Finished runnable:" + r);
      this.setStatus((IWorker)r, DynamicGraph.Status.FINISHED);
      synchronized(this.m_graph) {
         this.ppp("Node count:" + this.m_graph.getNodeCount() + " and " + this.m_graph.getNodeCountWithStatus(DynamicGraph.Status.FINISHED) + " finished");
         if (this.m_graph.getNodeCount() == this.m_graph.getNodeCountWithStatus(DynamicGraph.Status.FINISHED)) {
            this.ppp("Shutting down executor " + this);
            this.shutdown();
         } else {
            List<T> freeNodes = this.m_graph.getFreeNodes();
            this.runNodes(freeNodes);
         }

      }
   }

   private void generateFiles(List<String> files) {
      try {
         File dir = File.createTempFile("TestNG-", "");
         dir.delete();
         dir.mkdir();

         for(int i = 0; i < files.size(); ++i) {
            File f = new File(dir, "" + (i < 10 ? "0" : "") + i + ".dot");
            BufferedWriter bw = new BufferedWriter(new FileWriter(f));
            bw.append((CharSequence)files.get(i));
            bw.close();
         }
      } catch (IOException var6) {
         var6.printStackTrace();
      }

   }

   private void ppp(String string) {
   }
}
