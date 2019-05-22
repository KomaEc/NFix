package com.gzoltar.shaded.uk.org.lidalia.sysoutslf4j.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class SysOutOverSLF4JInitialiser {
   private static final Logger LOG = LoggerFactory.getLogger(SysOutOverSLF4JInitialiser.class);
   private static final String UNKNOWN_LOGGING_SYSTEM_MESSAGE = "Your logging framework {} is not known - if it needs access to the standard println methods on the console you will need to register it by calling registerLoggingSystemPackage";
   private static final String LOGGING_SYSTEM_DOES_NOT_NEED_PRINTLN_MESSAGE = "Your logging framework {} should not need access to the standard println methods on the console, so you should not need to register a logging system package.";
   private static final String[] LOGGING_SYSTEMS_THAT_DO_NOT_ACCESS_CONSOLE = new String[]{"ch.qos.logback.", "org.slf4j.impl.Log4jLoggerAdapter", "org.slf4j.impl.JDK14LoggerAdapter", "org.apache.log4j."};
   private static final String[] LOGGING_SYSTEMS_THAT_MIGHT_ACCESS_CONSOLE = new String[]{"org.x4juli.", "org.grlea.log.", "org.slf4j.impl.SimpleLogger"};
   private final LoggingSystemRegister loggingSystemRegister;

   SysOutOverSLF4JInitialiser(LoggingSystemRegister loggingSystemRegister) {
      this.loggingSystemRegister = loggingSystemRegister;
   }

   void initialise(Logger currentLoggerImplementation) {
      if (this.loggingSystemKnownAndMightAccessConsoleViaPrintln(currentLoggerImplementation)) {
         this.registerCurrentLoggingSystemPackage(currentLoggerImplementation);
      } else if (this.loggingSystemDoesNotAccessConsoleViaPrintln(currentLoggerImplementation)) {
         LOG.debug((String)"Your logging framework {} should not need access to the standard println methods on the console, so you should not need to register a logging system package.", (Object)currentLoggerImplementation.getClass());
      } else {
         LOG.warn((String)"Your logging framework {} is not known - if it needs access to the standard println methods on the console you will need to register it by calling registerLoggingSystemPackage", (Object)currentLoggerImplementation.getClass());
      }

   }

   private boolean loggingSystemDoesNotAccessConsoleViaPrintln(Logger currentLoggerImplementation) {
      boolean loggingSystemDoesNotAccessConsoleViaPrintln = false;
      String[] var3;
      int var4 = (var3 = LOGGING_SYSTEMS_THAT_DO_NOT_ACCESS_CONSOLE).length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String loggingPackage = var3[var5];
         if (this.usingLogFramework(currentLoggerImplementation, loggingPackage)) {
            loggingSystemDoesNotAccessConsoleViaPrintln = true;
            break;
         }
      }

      return loggingSystemDoesNotAccessConsoleViaPrintln;
   }

   private boolean loggingSystemKnownAndMightAccessConsoleViaPrintln(Logger currentLoggerImplementation) {
      boolean loggingSystemKnownAndMightAccessConsoleViaPrintln = false;
      String[] var3;
      int var4 = (var3 = LOGGING_SYSTEMS_THAT_MIGHT_ACCESS_CONSOLE).length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String loggingPackage = var3[var5];
         if (this.usingLogFramework(currentLoggerImplementation, loggingPackage)) {
            loggingSystemKnownAndMightAccessConsoleViaPrintln = true;
            break;
         }
      }

      return loggingSystemKnownAndMightAccessConsoleViaPrintln;
   }

   private void registerCurrentLoggingSystemPackage(Logger currentLoggerImplementation) {
      String[] var2;
      int var3 = (var2 = LOGGING_SYSTEMS_THAT_MIGHT_ACCESS_CONSOLE).length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String loggingPackage = var2[var4];
         if (this.usingLogFramework(currentLoggerImplementation, loggingPackage)) {
            this.loggingSystemRegister.registerLoggingSystem(loggingPackage);
         }
      }

   }

   private boolean usingLogFramework(Logger currentLoggerImplementation, String packageName) {
      return currentLoggerImplementation.getClass().getName().startsWith(packageName);
   }
}
