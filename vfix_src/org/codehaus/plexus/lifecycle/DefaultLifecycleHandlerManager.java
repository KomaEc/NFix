package org.codehaus.plexus.lifecycle;

import java.util.Iterator;
import java.util.List;

public class DefaultLifecycleHandlerManager implements LifecycleHandlerManager {
   private List lifecycleHandlers = null;
   private String defaultLifecycleHandlerId = "plexus";

   public void initialize() {
      Iterator iterator = this.lifecycleHandlers.iterator();

      while(iterator.hasNext()) {
         LifecycleHandler lifecycleHandler = (LifecycleHandler)iterator.next();
         lifecycleHandler.initialize();
      }

   }

   public LifecycleHandler getLifecycleHandler(String id) throws UndefinedLifecycleHandlerException {
      LifecycleHandler lifecycleHandler = null;
      Iterator iterator = this.lifecycleHandlers.iterator();

      do {
         if (!iterator.hasNext()) {
            throw new UndefinedLifecycleHandlerException("Specified lifecycle handler cannot be found: " + id);
         }

         lifecycleHandler = (LifecycleHandler)iterator.next();
      } while(!id.equals(lifecycleHandler.getId()));

      return lifecycleHandler;
   }

   public LifecycleHandler getDefaultLifecycleHandler() throws UndefinedLifecycleHandlerException {
      return this.getLifecycleHandler(this.defaultLifecycleHandlerId);
   }
}
