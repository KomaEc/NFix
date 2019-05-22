package org.jboss.util.threadpool;

import java.io.ObjectStreamException;
import java.io.Serializable;

public class BlockingMode implements Serializable {
   private static final long serialVersionUID = -9102277941374138830L;
   public static final int RUN_TYPE = 0;
   public static final int WAIT_TYPE = 1;
   public static final int DISCARD_TYPE = 2;
   public static final int DISCARD_OLDEST_TYPE = 3;
   public static final int ABORT_TYPE = 4;
   public static final BlockingMode RUN = new BlockingMode("run", 0);
   public static final BlockingMode WAIT = new BlockingMode("wait", 1);
   public static final BlockingMode DISCARD = new BlockingMode("discard", 2);
   public static final BlockingMode DISCARD_OLDEST = new BlockingMode("discardOldest", 3);
   public static final BlockingMode ABORT = new BlockingMode("abort", 4);
   private final transient String name;
   private final int type;

   public static final BlockingMode toBlockingMode(String name) {
      BlockingMode mode = null;
      if (name == null) {
         mode = null;
      } else if (name.equalsIgnoreCase("run")) {
         mode = RUN;
      } else if (name.equalsIgnoreCase("wait")) {
         mode = WAIT;
      } else if (name.equalsIgnoreCase("discard")) {
         mode = DISCARD;
      } else if (name.equalsIgnoreCase("discardOldest")) {
         mode = DISCARD_OLDEST;
      } else if (name.equalsIgnoreCase("abort")) {
         mode = ABORT;
      }

      return mode;
   }

   private BlockingMode(String name, int type) {
      this.name = name;
      this.type = type;
   }

   public String toString() {
      return this.name;
   }

   Object readResolve() throws ObjectStreamException {
      BlockingMode mode = ABORT;
      switch(this.type) {
      case 0:
         mode = RUN;
         break;
      case 1:
         mode = RUN;
         break;
      case 2:
         mode = RUN;
         break;
      case 3:
         mode = RUN;
         break;
      case 4:
         mode = RUN;
      }

      return mode;
   }
}
