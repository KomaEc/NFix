package org.jboss.util.threadpool;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MinPooledExecutor extends ThreadPoolExecutor {
   protected int keepAliveSize;

   public MinPooledExecutor(int poolSize) {
      super(poolSize, 2 * poolSize, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue(1024));
   }

   public MinPooledExecutor(BlockingQueue queue, int poolSize) {
      super(poolSize, 2 * poolSize, 60L, TimeUnit.SECONDS, queue);
   }

   public int getKeepAliveSize() {
      return this.keepAliveSize;
   }

   public void setKeepAliveSize(int keepAliveSize) {
      this.keepAliveSize = keepAliveSize;
   }
}
