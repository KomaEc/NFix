package com.gzoltar.shaded.uk.org.lidalia.sysoutslf4j.system;

import com.gzoltar.shaded.uk.org.lidalia.sysoutslf4j.common.LoggerAppender;
import com.gzoltar.shaded.uk.org.lidalia.sysoutslf4j.common.ReflectionUtils;
import java.lang.reflect.Method;

final class LoggerAppenderProxy implements LoggerAppender {
   private final Object targetLoggerAppender;
   private final Method appendMethod;
   private final Method appendAndLogMethod;

   private LoggerAppenderProxy(Object targetLoggerAppender) {
      try {
         Class<?> loggerAppenderClass = targetLoggerAppender.getClass();
         this.targetLoggerAppender = targetLoggerAppender;
         this.appendMethod = loggerAppenderClass.getDeclaredMethod("append", String.class);
         this.appendAndLogMethod = loggerAppenderClass.getDeclaredMethod("appendAndLog", String.class, String.class, Boolean.TYPE);
      } catch (NoSuchMethodException var3) {
         throw new IllegalArgumentException("Must only be instantiated with a LoggerAppender instance, got a " + targetLoggerAppender.getClass(), var3);
      }
   }

   public void append(String message) {
      ReflectionUtils.invokeMethod(this.appendMethod, this.targetLoggerAppender, message);
   }

   public void appendAndLog(String message, String className, boolean isStackTrace) {
      ReflectionUtils.invokeMethod(this.appendAndLogMethod, this.targetLoggerAppender, message, className, isStackTrace);
   }

   static LoggerAppender wrap(Object targetLoggerAppender) {
      Object result;
      if (targetLoggerAppender instanceof LoggerAppender) {
         result = (LoggerAppender)targetLoggerAppender;
      } else {
         result = new LoggerAppenderProxy(targetLoggerAppender);
      }

      return (LoggerAppender)result;
   }
}
