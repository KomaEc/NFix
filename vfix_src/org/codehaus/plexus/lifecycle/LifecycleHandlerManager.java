package org.codehaus.plexus.lifecycle;

public interface LifecycleHandlerManager {
   void initialize();

   LifecycleHandler getDefaultLifecycleHandler() throws UndefinedLifecycleHandlerException;

   LifecycleHandler getLifecycleHandler(String var1) throws UndefinedLifecycleHandlerException;
}
