package org.apache.maven.surefire.booter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.maven.surefire.util.ReflectionUtils;
import org.apache.maven.surefire.util.TestsToRun;

class LazyTestsToRun extends TestsToRun {
   private final List<Class> workQueue = new ArrayList();
   private BufferedReader inputReader;
   private boolean streamClosed = false;
   private PrintStream originalOutStream;

   public LazyTestsToRun(InputStream testSource, PrintStream originalOutStream) {
      super(Collections.emptyList());
      this.originalOutStream = originalOutStream;
      this.inputReader = new BufferedReader(new InputStreamReader(testSource));
   }

   protected void addWorkItem(String className) {
      synchronized(this.workQueue) {
         this.workQueue.add(ReflectionUtils.loadClass(Thread.currentThread().getContextClassLoader(), className));
      }
   }

   protected void requestNextTest() {
      StringBuilder sb = new StringBuilder();
      sb.append('N').append(",0,want more!\n");
      this.originalOutStream.print(sb.toString());
   }

   public Iterator<Class> iterator() {
      return new LazyTestsToRun.BlockingIterator();
   }

   public String toString() {
      StringBuilder sb = new StringBuilder("LazyTestsToRun ");
      synchronized(this.workQueue) {
         sb.append("(more items expected: ").append(!this.streamClosed).append("): ");
         sb.append(this.workQueue);
      }

      return sb.toString();
   }

   public boolean allowEagerReading() {
      return false;
   }

   private class BlockingIterator implements Iterator<Class> {
      private int lastPos;

      private BlockingIterator() {
         this.lastPos = -1;
      }

      public boolean hasNext() {
         int nextPos = this.lastPos + 1;
         synchronized(LazyTestsToRun.this.workQueue) {
            if (LazyTestsToRun.this.workQueue.size() > nextPos) {
               return true;
            } else {
               if (this.needsToWaitForInput(nextPos)) {
                  LazyTestsToRun.this.requestNextTest();

                  String nextClassName;
                  try {
                     nextClassName = LazyTestsToRun.this.inputReader.readLine();
                  } catch (IOException var6) {
                     LazyTestsToRun.this.streamClosed = true;
                     return false;
                  }

                  if (null == nextClassName) {
                     LazyTestsToRun.this.streamClosed = true;
                  } else {
                     LazyTestsToRun.this.addWorkItem(nextClassName);
                  }
               }

               return LazyTestsToRun.this.workQueue.size() > nextPos;
            }
         }
      }

      private boolean needsToWaitForInput(int nextPos) {
         return LazyTestsToRun.this.workQueue.size() == nextPos && !LazyTestsToRun.this.streamClosed;
      }

      public Class next() {
         synchronized(LazyTestsToRun.this.workQueue) {
            return (Class)LazyTestsToRun.this.workQueue.get(++this.lastPos);
         }
      }

      public void remove() {
         throw new UnsupportedOperationException();
      }

      // $FF: synthetic method
      BlockingIterator(Object x1) {
         this();
      }
   }
}
