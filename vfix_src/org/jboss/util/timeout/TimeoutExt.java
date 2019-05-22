package org.jboss.util.timeout;

public interface TimeoutExt extends Timeout {
   TimeoutTarget getTimeoutTarget();

   long getTime();

   void done();
}
