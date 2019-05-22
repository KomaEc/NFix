package org.jboss.util;

public interface Sync {
   void acquire() throws InterruptedException;

   void release();
}
