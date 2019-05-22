package com.gzoltar.shaded.uk.org.lidalia.sysoutslf4j.context.exceptionhandlers;

import com.gzoltar.shaded.uk.org.lidalia.sysoutslf4j.context.LogLevel;
import java.io.PrintStream;
import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public final class LogPerLineExceptionHandlingStrategyFactory implements ExceptionHandlingStrategyFactory {
   private static final ExceptionHandlingStrategyFactory INSTANCE = new LogPerLineExceptionHandlingStrategyFactory();

   public static ExceptionHandlingStrategyFactory getInstance() {
      return INSTANCE;
   }

   private LogPerLineExceptionHandlingStrategyFactory() {
   }

   public ExceptionHandlingStrategy makeExceptionHandlingStrategy(LogLevel logLevel, PrintStream originalPrintStream) {
      return new LogPerLineExceptionHandlingStrategyFactory.LogPerLineExceptionHandlingStrategy(logLevel);
   }

   private static final class LogPerLineExceptionHandlingStrategy implements ExceptionHandlingStrategy {
      private static final Marker MARKER = MarkerFactory.getMarker("stacktrace");
      private final LogLevel logLevel;

      LogPerLineExceptionHandlingStrategy(LogLevel logLevel) {
         this.logLevel = logLevel;
      }

      public void notifyNotStackTrace() {
      }

      public void handleExceptionLine(String line, Logger log) {
         this.logLevel.log(log, MARKER, line);
      }
   }
}
