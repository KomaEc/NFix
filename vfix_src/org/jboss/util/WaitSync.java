package org.jboss.util;

public interface WaitSync extends Sync {
   void doWait() throws InterruptedException;

   void doNotify() throws InterruptedException;
}
