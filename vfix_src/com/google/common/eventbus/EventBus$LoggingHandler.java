package com.google.common.eventbus;

import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

final class EventBus$LoggingHandler implements SubscriberExceptionHandler {
   static final EventBus$LoggingHandler INSTANCE = new EventBus$LoggingHandler();

   public void handleException(Throwable exception, SubscriberExceptionContext context) {
      Logger logger = logger(context);
      if (logger.isLoggable(Level.SEVERE)) {
         logger.log(Level.SEVERE, message(context), exception);
      }

   }

   private static Logger logger(SubscriberExceptionContext context) {
      return Logger.getLogger(EventBus.class.getName() + "." + context.getEventBus().identifier());
   }

   private static String message(SubscriberExceptionContext context) {
      Method method = context.getSubscriberMethod();
      return "Exception thrown by subscriber method " + method.getName() + '(' + method.getParameterTypes()[0].getName() + ')' + " on subscriber " + context.getSubscriber() + " when dispatching event: " + context.getEvent();
   }
}
