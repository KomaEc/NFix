package org.apache.commons.collections.functors;

import java.io.Serializable;
import org.apache.commons.collections.Closure;

public class ForClosure implements Closure, Serializable {
   private static final long serialVersionUID = -1190120533393621674L;
   private final int iCount;
   private final Closure iClosure;

   public static Closure getInstance(int count, Closure closure) {
      if (count > 0 && closure != null) {
         return (Closure)(count == 1 ? closure : new ForClosure(count, closure));
      } else {
         return NOPClosure.INSTANCE;
      }
   }

   public ForClosure(int count, Closure closure) {
      this.iCount = count;
      this.iClosure = closure;
   }

   public void execute(Object input) {
      for(int i = 0; i < this.iCount; ++i) {
         this.iClosure.execute(input);
      }

   }

   public Closure getClosure() {
      return this.iClosure;
   }

   public int getCount() {
      return this.iCount;
   }
}
