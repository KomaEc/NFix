package org.codehaus.groovy.control;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Janitor implements HasCleanup {
   private final Set pending = new HashSet();

   public void register(HasCleanup object) {
      this.pending.add(object);
   }

   public void cleanup() {
      Iterator iterator = this.pending.iterator();

      while(iterator.hasNext()) {
         HasCleanup object = (HasCleanup)iterator.next();

         try {
            object.cleanup();
         } catch (Exception var4) {
         }
      }

      this.pending.clear();
   }
}
