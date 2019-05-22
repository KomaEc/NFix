package com.gzoltar.shaded.uk.org.lidalia.sysoutslf4j.system;

import com.gzoltar.shaded.uk.org.lidalia.sysoutslf4j.common.LoggerAppender;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class LoggerAppenderStore {
   private final Map<ClassLoader, WeakReference<LoggerAppender>> loggerAppenderMap = new WeakHashMap();
   private final ReadWriteLock lock = new ReentrantReadWriteLock();
   private final Lock readLock;
   private final Lock writeLock;

   LoggerAppenderStore() {
      this.readLock = this.lock.readLock();
      this.writeLock = this.lock.writeLock();
   }

   LoggerAppender get() {
      this.readLock.lock();

      LoggerAppender var1;
      try {
         var1 = this.get(this.contextClassLoader());
      } finally {
         this.readLock.unlock();
      }

      return var1;
   }

   private LoggerAppender get(ClassLoader classLoader) {
      WeakReference<LoggerAppender> loggerAppenderReference = (WeakReference)this.loggerAppenderMap.get(classLoader);
      LoggerAppender result;
      if (loggerAppenderReference == null) {
         if (classLoader == null) {
            result = null;
         } else {
            result = this.get(classLoader.getParent());
         }
      } else {
         result = (LoggerAppender)loggerAppenderReference.get();
      }

      return result;
   }

   void put(LoggerAppender loggerAppender) {
      this.writeLock.lock();

      try {
         this.loggerAppenderMap.put(this.contextClassLoader(), new WeakReference(loggerAppender));
      } finally {
         this.writeLock.unlock();
      }

   }

   void remove() {
      this.writeLock.lock();

      try {
         this.loggerAppenderMap.remove(this.contextClassLoader());
      } finally {
         this.writeLock.unlock();
      }

   }

   private ClassLoader contextClassLoader() {
      return Thread.currentThread().getContextClassLoader();
   }
}
