package com.google.common.util.concurrent;

final class ServiceManager$2 extends ListenerCallQueue.Callback<ServiceManager.Listener> {
   ServiceManager$2(String x0) {
      super(x0);
   }

   void call(ServiceManager.Listener listener) {
      listener.stopped();
   }
}
