package com.google.inject.internal;

import com.google.inject.Guice;
import com.google.inject.spi.Message;
import java.util.logging.Level;
import java.util.logging.Logger;

final class MessageProcessor extends AbstractProcessor {
   private static final Logger logger = Logger.getLogger(Guice.class.getName());

   MessageProcessor(Errors errors) {
      super(errors);
   }

   public Boolean visit(Message message) {
      if (message.getCause() != null) {
         String rootMessage = getRootMessage(message.getCause());
         Logger var10000 = logger;
         Level var10001 = Level.INFO;
         String var10003 = String.valueOf(rootMessage);
         String var10002;
         if (var10003.length() != 0) {
            var10002 = "An exception was caught and reported. Message: ".concat(var10003);
         } else {
            String var10004 = new String;
            var10002 = var10004;
            var10004.<init>("An exception was caught and reported. Message: ");
         }

         var10000.log(var10001, var10002, message.getCause());
      }

      this.errors.addMessage(message);
      return true;
   }

   public static String getRootMessage(Throwable t) {
      Throwable cause = t.getCause();
      return cause == null ? t.toString() : getRootMessage(cause);
   }
}
