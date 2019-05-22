package org.apache.velocity.app.event;

public interface EventHandlerMethodExecutor {
   void execute(EventHandler var1) throws Exception;

   boolean isDone();

   Object getReturnValue();
}
