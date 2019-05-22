package com.gzoltar.shaded.uk.org.lidalia.sysoutslf4j.context;

import com.gzoltar.shaded.uk.org.lidalia.sysoutslf4j.common.ReflectionUtils;
import com.gzoltar.shaded.uk.org.lidalia.sysoutslf4j.common.SLF4JPrintStream;
import com.gzoltar.shaded.uk.org.lidalia.sysoutslf4j.common.SystemOutput;
import com.gzoltar.shaded.uk.org.lidalia.sysoutslf4j.context.exceptionhandlers.ExceptionHandlingStrategy;
import com.gzoltar.shaded.uk.org.lidalia.sysoutslf4j.context.exceptionhandlers.ExceptionHandlingStrategyFactory;
import java.io.PrintStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class SLF4JPrintStreamManager {
   private static final Logger LOG = LoggerFactory.getLogger(SysOutOverSLF4J.class);

   void sendSystemOutAndErrToSLF4J(LogLevel outLevel, LogLevel errLevel, ExceptionHandlingStrategyFactory exceptionHandlingStrategyFactory) {
      this.makeSystemOutputsSLF4JPrintStreamsIfNecessary();
      this.sendSystemOutAndErrToSLF4JForThisContext(outLevel, errLevel, exceptionHandlingStrategyFactory);
      LOG.info("Redirected System.out and System.err to SLF4J for this context");
   }

   private void makeSystemOutputsSLF4JPrintStreamsIfNecessary() {
      if (SysOutOverSLF4J.systemOutputsAreSLF4JPrintStreams()) {
         LOG.debug("System.out and System.err are already SLF4JPrintStreams");
      } else {
         PrintStreamCoordinatorFactory.createPrintStreamCoordinator().replaceSystemOutputsWithSLF4JPrintStreams();
         LOG.info("Replaced standard System.out and System.err PrintStreams with SLF4JPrintStreams");
      }

   }

   private void sendSystemOutAndErrToSLF4JForThisContext(LogLevel outLevel, LogLevel errLevel, ExceptionHandlingStrategyFactory exceptionHandlingStrategyFactory) {
      this.registerNewLoggerAppender(exceptionHandlingStrategyFactory, this.wrap(SystemOutput.OUT.get()), outLevel);
      this.registerNewLoggerAppender(exceptionHandlingStrategyFactory, this.wrap(SystemOutput.ERR.get()), errLevel);
   }

   private void registerNewLoggerAppender(ExceptionHandlingStrategyFactory exceptionHandlingStrategyFactory, SLF4JPrintStream slf4jPrintStream, LogLevel logLevel) {
      PrintStream originalPrintStream = slf4jPrintStream.getOriginalPrintStream();
      ExceptionHandlingStrategy exceptionHandlingStrategy = exceptionHandlingStrategyFactory.makeExceptionHandlingStrategy(logLevel, originalPrintStream);
      Object loggerAppender = new LoggerAppenderImpl(logLevel, exceptionHandlingStrategy, originalPrintStream);
      ReferenceHolder.preventGarbageCollectionForLifeOfClassLoader(loggerAppender);
      slf4jPrintStream.registerLoggerAppender(loggerAppender);
   }

   void stopSendingSystemOutAndErrToSLF4J() {
      if (SysOutOverSLF4J.systemOutputsAreSLF4JPrintStreams()) {
         SystemOutput[] var1;
         int var2 = (var1 = SystemOutput.values()).length;

         for(int var3 = 0; var3 < var2; ++var3) {
            SystemOutput systemOutput = var1[var3];
            SLF4JPrintStream slf4jPrintStream = this.wrap(systemOutput.get());
            slf4jPrintStream.deregisterLoggerAppender();
         }
      } else {
         LOG.warn("Cannot stop sending System.out and System.err to SLF4J - they are not being sent there at the moment");
      }

   }

   private SLF4JPrintStream wrap(PrintStream target) {
      return (SLF4JPrintStream)ReflectionUtils.wrap(target, SLF4JPrintStream.class);
   }

   void restoreOriginalSystemOutputsIfNecessary() {
      if (SysOutOverSLF4J.systemOutputsAreSLF4JPrintStreams()) {
         PrintStreamCoordinatorFactory.createPrintStreamCoordinator().restoreOriginalSystemOutputs();
         LOG.info("Restored original System.out and System.err");
      } else {
         LOG.warn("System.out and System.err are not SLF4JPrintStreams - cannot restore");
      }

   }
}
