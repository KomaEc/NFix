package com.gzoltar.shaded.uk.org.lidalia.sysoutslf4j.context;

import com.gzoltar.shaded.uk.org.lidalia.sysoutslf4j.context.exceptionhandlers.ExceptionHandlingStrategyFactory;
import com.gzoltar.shaded.uk.org.lidalia.sysoutslf4j.context.exceptionhandlers.LogPerLineExceptionHandlingStrategyFactory;
import com.gzoltar.shaded.uk.org.lidalia.sysoutslf4j.system.SLF4JPrintStreamImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SysOutOverSLF4J {
   private static final LoggingSystemRegister LOGGING_SYSTEM_REGISTER = new LoggingSystemRegister();
   private static final SLF4JPrintStreamManager SLF4J_PRINT_STREAM_MANAGER = new SLF4JPrintStreamManager();

   static {
      SysOutOverSLF4JInitialiser sysOutOverSLF4JInitialiser = new SysOutOverSLF4JInitialiser(LOGGING_SYSTEM_REGISTER);
      Logger loggerImplementation = LoggerFactory.getLogger("ROOT");
      sysOutOverSLF4JInitialiser.initialise(loggerImplementation);
   }

   public static void sendSystemOutAndErrToSLF4J() {
      sendSystemOutAndErrToSLF4J(LogLevel.INFO, LogLevel.ERROR);
   }

   public static void sendSystemOutAndErrToSLF4J(LogLevel outLevel, LogLevel errLevel) {
      ExceptionHandlingStrategyFactory exceptionHandlingStrategyFactory = LogPerLineExceptionHandlingStrategyFactory.getInstance();
      sendSystemOutAndErrToSLF4J(outLevel, errLevel, exceptionHandlingStrategyFactory);
   }

   public static void sendSystemOutAndErrToSLF4J(ExceptionHandlingStrategyFactory exceptionHandlingStrategyFactory) {
      sendSystemOutAndErrToSLF4J(LogLevel.INFO, LogLevel.ERROR, exceptionHandlingStrategyFactory);
   }

   public static void sendSystemOutAndErrToSLF4J(LogLevel outLevel, LogLevel errLevel, ExceptionHandlingStrategyFactory exceptionHandlingStrategyFactory) {
      Class var3 = System.class;
      synchronized(System.class) {
         SLF4J_PRINT_STREAM_MANAGER.sendSystemOutAndErrToSLF4J(outLevel, errLevel, exceptionHandlingStrategyFactory);
      }
   }

   public static void stopSendingSystemOutAndErrToSLF4J() {
      Class var0 = System.class;
      synchronized(System.class) {
         SLF4J_PRINT_STREAM_MANAGER.stopSendingSystemOutAndErrToSLF4J();
      }
   }

   public static void restoreOriginalSystemOutputs() {
      Class var0 = System.class;
      synchronized(System.class) {
         SLF4J_PRINT_STREAM_MANAGER.restoreOriginalSystemOutputsIfNecessary();
      }
   }

   public static void registerLoggingSystem(String packageName) {
      LOGGING_SYSTEM_REGISTER.registerLoggingSystem(packageName);
   }

   public static void unregisterLoggingSystem(String packageName) {
      LOGGING_SYSTEM_REGISTER.unregisterLoggingSystem(packageName);
   }

   public static boolean isInLoggingSystem(String className) {
      return LOGGING_SYSTEM_REGISTER.isInLoggingSystem(className);
   }

   private SysOutOverSLF4J() {
      throw new UnsupportedOperationException("Not instantiable");
   }

   public static boolean systemOutputsAreSLF4JPrintStreams() {
      return System.out.getClass().getName().equals(SLF4JPrintStreamImpl.class.getName());
   }
}
