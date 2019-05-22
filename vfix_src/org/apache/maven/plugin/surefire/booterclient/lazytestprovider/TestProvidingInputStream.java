package org.apache.maven.plugin.surefire.booterclient.lazytestprovider;

import java.io.IOException;
import java.io.InputStream;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class TestProvidingInputStream extends InputStream {
   private final Queue<String> testItemQueue;
   private byte[] currentBuffer;
   private int currentPos;
   private Semaphore semaphore = new Semaphore(0);
   private FlushReceiverProvider flushReceiverProvider;
   private boolean closed = false;

   public TestProvidingInputStream(Queue<String> testItemQueue) {
      this.testItemQueue = testItemQueue;
   }

   public void setFlushReceiverProvider(FlushReceiverProvider flushReceiverProvider) {
      this.flushReceiverProvider = flushReceiverProvider;
   }

   public synchronized int read() throws IOException {
      if (null == this.currentBuffer) {
         if (null != this.flushReceiverProvider && null != this.flushReceiverProvider.getFlushReceiver()) {
            this.flushReceiverProvider.getFlushReceiver().flush();
         }

         this.semaphore.acquireUninterruptibly();
         if (this.closed) {
            return -1;
         }

         String currentElement = (String)this.testItemQueue.poll();
         if (null == currentElement) {
            return -1;
         }

         this.currentBuffer = currentElement.getBytes();
         this.currentPos = 0;
      }

      if (this.currentPos < this.currentBuffer.length) {
         return this.currentBuffer[this.currentPos++] & 255;
      } else {
         this.currentBuffer = null;
         return 10;
      }
   }

   public void provideNewTest() {
      this.semaphore.release();
   }

   public void close() {
      this.closed = true;
      this.semaphore.release();
   }
}
