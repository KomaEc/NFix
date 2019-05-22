package com.gzoltar.shaded.uk.org.lidalia.sysoutslf4j.system;

import com.gzoltar.shaded.uk.org.lidalia.sysoutslf4j.common.LoggerAppender;
import com.gzoltar.shaded.uk.org.lidalia.sysoutslf4j.common.StringUtils;
import java.io.PrintStream;

class SLF4JPrintStreamDelegate {
   private final PrintStream originalPrintStream;
   private final LoggerAppenderStore loggerAppenderStore;

   SLF4JPrintStreamDelegate(PrintStream originalPrintStream, LoggerAppenderStore loggerAppenderStore) {
      this.originalPrintStream = originalPrintStream;
      this.loggerAppenderStore = loggerAppenderStore;
   }

   void registerLoggerAppender(LoggerAppender loggerAppender) {
      this.loggerAppenderStore.put(loggerAppender);
   }

   void deregisterLoggerAppender() {
      this.loggerAppenderStore.remove();
   }

   void delegatePrintln(String message) {
      LoggerAppender loggerAppender = this.loggerAppenderStore.get();
      if (loggerAppender == null) {
         this.originalPrintStream.println(message);
      } else {
         appendAndLog(message, loggerAppender);
      }

   }

   void delegatePrint(String message) {
      LoggerAppender loggerAppender = this.loggerAppenderStore.get();
      if (loggerAppender == null) {
         this.originalPrintStream.print(message);
      } else if (message.endsWith("\n")) {
         String messageWithoutLineBreak = StringUtils.stripEnd(message, "\r\n");
         appendAndLog(messageWithoutLineBreak, loggerAppender);
      } else {
         loggerAppender.append(message);
      }

   }

   private static void appendAndLog(String message, LoggerAppender loggerAppender) {
      StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
      String libraryPackageName = "com.gzoltar.shaded.uk.org.lidalia.sysoutslf4j";
      CallOrigin callOrigin = CallOrigin.getCallOrigin(stackTraceElements, "com.gzoltar.shaded.uk.org.lidalia.sysoutslf4j");
      loggerAppender.appendAndLog(message, callOrigin.getClassName(), callOrigin.isPrintingStackTrace());
   }
}
