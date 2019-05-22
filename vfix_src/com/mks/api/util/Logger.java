package com.mks.api.util;

import java.security.AccessController;
import java.security.Principal;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import javax.security.auth.Subject;

final class Logger implements Runnable {
   public static final String DEBUG = "DEBUG";
   public static final String FATAL = "FATAL";
   public static final String ERROR = "ERROR";
   public static final String GENERAL = "GENERAL";
   public static final String IGNORE = "IGNORE";
   public static final String WARNING = "WARNING";
   public static final String LOCKFILE = "LOCKFILE";
   public static final int LOWEST = 20;
   public static final int LOW = 10;
   public static final int MEDIUM = 5;
   public static final int HIGH = 0;
   public static final int OFF = -1;
   private static Logger defaultLogger;
   private List logListeners;
   private static ThreadLocal threadData = new ThreadLocal();
   private Queue messageQueue;
   private int maxQueueSize;
   private Thread loggerThread;
   private boolean runLogger;

   public Logger(boolean makeAppDefault) {
      this(makeAppDefault, "Logger");
   }

   public Logger(boolean makeAppDefault, String threadName) {
      this.runLogger = true;
      this.logListeners = new ArrayList();
      this.messageQueue = new Queue();
      this.maxQueueSize = 10000;
      this.loggerThread = new Thread(this, threadName);
      this.loggerThread.setDaemon(true);
      this.loggerThread.start();
      if (makeAppDefault) {
         setApplicationLogger(this);
      }

   }

   public void stop() {
      if (this == defaultLogger) {
         (new Exception("Trying to stop the default logger!!")).printStackTrace(System.out);
      } else {
         this.runLogger = false;
         this.loggerThread.interrupt();
      }

   }

   public static Logger getApplicationLogger() {
      return defaultLogger;
   }

   public static synchronized void setApplicationLogger(Logger logger) {
      if (defaultLogger != null && logger == null) {
         (new Exception("Someone is trying to set the default logger to null!!!")).printStackTrace(System.out);
      } else {
         defaultLogger = logger;
      }

   }

   public void setPriority(int priority) {
      this.loggerThread.setPriority(priority);
   }

   public void setMessageQueueSize(int maxsize) {
      this.maxQueueSize = maxsize;
   }

   public static void setApplicationMessageQueueSize(int maxsize) {
      if (defaultLogger != null) {
         defaultLogger.setMessageQueueSize(maxsize);
      }

   }

   public void addLogListener(InternalAPILogListener listener) {
      synchronized(this.logListeners) {
         if (!this.logListeners.contains(listener)) {
            this.logListeners.add(listener);
         }

      }
   }

   public void removeLogListener(InternalAPILogListener listener) {
      synchronized(this.logListeners) {
         this.logListeners.remove(listener);
      }
   }

   public List getLogListeners() {
      synchronized(this.logListeners) {
         return new ArrayList(this.logListeners);
      }
   }

   public void logMessage(Class klass, Object obj, String category, int level, String message) {
      if (message == null) {
         this.logMessage((Class)null, (Object)null, "ERROR", 0, "Logger.logMessage called with null error message from: " + StackTrace.getStackTrace());
      } else if (!this.logListeners.isEmpty()) {
         if (category == null) {
            category = "GENERAL";
         }

         Object threadData = this.defaultThreadData();
         synchronized(this.messageQueue) {
            if (this.maxQueueSize != 0 && this.messageQueue.size() >= this.maxQueueSize) {
               this.processNextMessage(false);
            }

            this.messageQueue.enqueue(new Logger.LogMessage(klass, obj, category, level, threadData, message));
            this.messageQueue.notify();
         }
      }
   }

   public Object defaultThreadData() {
      Object data = threadData.get();
      if (data != null) {
         return data;
      } else {
         Thread t = Thread.currentThread();
         Subject subject = Subject.getSubject(AccessController.getContext());
         return subject == null ? t.getName() : this.getName(subject) + "[" + t.getName() + "]";
      }
   }

   private String getName(Subject s) {
      if (s != null) {
         Iterator i = s.getPrincipals().iterator();
         if (i.hasNext()) {
            Principal p = (Principal)i.next();
            return p.getName();
         }
      }

      return "unknown";
   }

   public boolean logMessageCheck(String category, int level) {
      try {
         Iterator i = this.logListeners.iterator();

         InternalAPILogListener listener;
         do {
            if (!i.hasNext()) {
               return false;
            }

            listener = (InternalAPILogListener)i.next();
         } while(!listener.willLogMessage(category, level));

         return true;
      } catch (ConcurrentModificationException var5) {
         return this.logMessageCheck(category, level);
      }
   }

   public static void message(Class klass, Object obj, String category, int level, String message) {
      if (defaultLogger != null) {
         defaultLogger.logMessage(klass, obj, category, level, message);
      }
   }

   public void logMessage(Class klass, String category, int level, String message) {
      this.logMessage(klass, (Object)null, category, level, message);
   }

   public static void message(Class klass, String category, int level, String message) {
      if (defaultLogger != null) {
         defaultLogger.logMessage(klass, category, level, message);
      }
   }

   public void logMessage(Object obj, String category, int level, String message) {
      this.logMessage(obj == null ? null : obj.getClass(), obj, category, level, message);
   }

   public static void message(Object obj, String category, int level, String message) {
      if (defaultLogger != null) {
         defaultLogger.logMessage(obj, category, level, message);
      }
   }

   public static boolean messageCheck(String category, int level) {
      return defaultLogger == null ? false : defaultLogger.logMessageCheck(category, level);
   }

   public void logMessage(String category, int level, String message) {
      this.logMessage((Class)null, (Object)null, category, level, message);
   }

   public static void message(String category, int level, String message) {
      if (defaultLogger != null) {
         defaultLogger.logMessage(category, level, message);
      }
   }

   public static void multiLineMessage(String category, int level, String message) {
      if (defaultLogger != null) {
         StringTokenizer st = new StringTokenizer(message, "\n");

         while(st.hasMoreTokens()) {
            defaultLogger.logMessage(category, level, st.nextToken());
         }

      }
   }

   public void logMessage(String category, String message) {
      this.logMessage((Class)null, (Object)null, category, 0, message);
   }

   public boolean logMessageCheck(String category) {
      return this.logMessageCheck(category, 0);
   }

   public static void message(String category, String message) {
      if (defaultLogger != null) {
         defaultLogger.logMessage(category, message);
      }
   }

   public static boolean messageCheck(String category) {
      return defaultLogger == null ? false : defaultLogger.logMessageCheck(category);
   }

   public void logMessage(String message) {
      this.logMessage((Class)null, (Object)null, (String)null, 0, message);
   }

   public boolean logMessageCheck() {
      return this.logMessageCheck((String)null, 0);
   }

   public static void message(String message) {
      if (defaultLogger != null) {
         defaultLogger.logMessage(message);
      }
   }

   public static boolean messageCheck() {
      return defaultLogger == null ? false : defaultLogger.logMessageCheck();
   }

   public void logException(Class klass, Object obj, String category, int level, Throwable exception) {
      if (exception == null) {
         this.logMessage((Class)null, (Object)null, "ERROR", 0, "Logger.logException called with null exception from: " + StackTrace.getStackTrace());
      } else if (!this.logListeners.isEmpty()) {
         if (category == null) {
            category = "GENERAL";
         }

         Object threadData = this.defaultThreadData();
         synchronized(this.messageQueue) {
            if (this.maxQueueSize != 0 && this.messageQueue.size() >= this.maxQueueSize) {
               this.processNextMessage(false);
            }

            this.messageQueue.enqueue(new Logger.LogException(klass, obj, category, level, threadData, exception));
            this.messageQueue.notify();
         }
      }
   }

   public boolean logExceptionCheck(String category, int level) {
      try {
         Iterator i = this.logListeners.iterator();

         InternalAPILogListener listener;
         do {
            if (!i.hasNext()) {
               return false;
            }

            listener = (InternalAPILogListener)i.next();
         } while(!listener.willLogException(category, level));

         return true;
      } catch (ConcurrentModificationException var5) {
         return this.logMessageCheck(category, level);
      }
   }

   public static void exception(Class klass, Object obj, String category, int level, Throwable exception) {
      if (defaultLogger != null) {
         try {
            defaultLogger.logException(klass, obj, category, level, exception);
         } catch (OutOfMemoryError var8) {
            try {
               System.err.println(exception.getMessage());
               exception.printStackTrace();
            } catch (Throwable var7) {
            }
         }

      }
   }

   public static boolean exceptionCheck(String category, int level) {
      return defaultLogger == null ? false : defaultLogger.logExceptionCheck(category, level);
   }

   public void logException(Class klass, String category, int level, Throwable exception) {
      this.logException(klass, (Object)null, category, level, exception);
   }

   public static void exception(Class klass, String category, int level, Throwable exception) {
      if (defaultLogger != null) {
         defaultLogger.logException(klass, category, level, exception);
      }
   }

   public void logException(Object obj, String category, int level, Throwable exception) {
      this.logException(obj == null ? null : obj.getClass(), obj, category, level, exception);
   }

   public static void exception(Object obj, String category, int level, Throwable exception) {
      if (defaultLogger != null) {
         defaultLogger.logException(obj, category, level, exception);
      }
   }

   public void logException(String category, int level, Throwable exception) {
      this.logException((Class)null, (Object)null, category, level, exception);
   }

   public static void exception(String category, int level, Throwable exception) {
      if (defaultLogger != null) {
         defaultLogger.logException(category, level, exception);
      }
   }

   public void logException(String category, Throwable exception) {
      this.logException((Class)null, (Object)null, category, 0, exception);
   }

   public boolean logExceptionCheck(String category) {
      return this.logExceptionCheck(category, 0);
   }

   public static void exception(String category, Throwable exception) {
      if (defaultLogger != null) {
         defaultLogger.logException(category, exception);
      }
   }

   public boolean exceptionCheck(String category) {
      return defaultLogger == null ? false : defaultLogger.logExceptionCheck(category);
   }

   public void logException(Throwable exception) {
      this.logException((Class)null, (Object)null, (String)null, 0, exception);
   }

   public boolean logExceptionCheck() {
      return this.logExceptionCheck((String)null, 0);
   }

   public static void exception(Throwable exception) {
      if (defaultLogger != null) {
         defaultLogger.logException(exception);
      }
   }

   public boolean exceptionCheck() {
      return defaultLogger == null ? false : defaultLogger.logExceptionCheck();
   }

   public int updateCategoryLevel(String category, int level) {
      List logList = this.getLogListeners();
      int n = 0;

      for(Iterator i = logList.iterator(); i.hasNext(); ++n) {
         InternalAPILogListener ll = (InternalAPILogListener)i.next();
         ll.removeCategoryIncludeFilter(0, category);
         ll.removeCategoryIncludeFilter(1, category);
         ll.removeCategoryExcludeFilter(0, category);
         ll.removeCategoryExcludeFilter(1, category);
         ll.addCategoryIncludeFilter(0, category, level);
         ll.addCategoryIncludeFilter(1, category, level);
      }

      return n;
   }

   public int getCategoryLevel(String category) {
      try {
         Iterator i = this.logListeners.iterator();
         int max = -1;

         while(true) {
            while(i.hasNext()) {
               InternalAPILogListener ll = (InternalAPILogListener)i.next();

               for(int level = 20; level >= 0; --level) {
                  if (ll.willLogMessage(category, level) || ll.willLogException(category, level)) {
                     if (level == 20) {
                        return 20;
                     }

                     if (level > max) {
                        max = level;
                        break;
                     }
                  }
               }
            }

            return max;
         }
      } catch (ConcurrentModificationException var6) {
         return this.getCategoryLevel(category);
      }
   }

   public static int getDefaultCategoryLevel(String category) {
      Logger logger = defaultLogger;
      return logger == null ? -1 : logger.getCategoryLevel(category);
   }

   public void logAddThreadData(Object obj) {
      threadData.set(obj);
   }

   public static void addThreadData(Object obj) {
      if (defaultLogger != null) {
         defaultLogger.logAddThreadData(obj);
      }

   }

   public Object logGetThreadData() {
      return threadData.get();
   }

   public static Object getThreadData() {
      return defaultLogger == null ? null : defaultLogger.logGetThreadData();
   }

   public void run() {
      try {
         while(this.runLogger) {
            try {
               this.processNextMessage(true);
            } catch (Throwable var6) {
               var6.printStackTrace(System.out);
            }
         }
      } finally {
         if (this == defaultLogger) {
            System.out.println(new Date() + " - Cleaning up default logger!!!, runLogger is: " + this.runLogger);
            defaultLogger = null;
         }

      }

   }

   private void processNextMessage(boolean wait) {
      Logger.LogObject obj;
      synchronized(this.messageQueue) {
         if (this.messageQueue.isEmpty()) {
            if (wait) {
               try {
                  this.messageQueue.wait();
               } catch (InterruptedException var6) {
                  return;
               }
            }

            return;
         }

         obj = (Logger.LogObject)this.messageQueue.dequeue();
      }

      boolean done = false;

      while(!done) {
         try {
            Iterator i = this.logListeners.iterator();

            while(i.hasNext()) {
               obj.log((InternalAPILogListener)i.next());
            }

            done = true;
         } catch (ConcurrentModificationException var7) {
         }
      }

   }

   public void flush() {
      Logger.LogFlush semaphore = new Logger.LogFlush();
      synchronized(semaphore) {
         synchronized(this.messageQueue) {
            this.messageQueue.enqueue(semaphore);
            this.messageQueue.notify();
         }

         try {
            semaphore.wait();
         } catch (InterruptedException var6) {
         }

      }
   }

   private static class LogFlush extends Logger.LogObject {
      private boolean notified;

      private LogFlush() {
         super(null);
         this.notified = false;
      }

      public void log(InternalAPILogListener listener) {
         if (!this.notified) {
            synchronized(this) {
               this.notify();
            }

            this.notified = true;
         }

      }

      // $FF: synthetic method
      LogFlush(Object x0) {
         this();
      }
   }

   private static class LogException extends Logger.LogObject {
      private Throwable exception;

      public LogException(Class klass, Object obj, String category, int level, Object threadData, Throwable exception) {
         super(null);
         this.klass = klass;
         this.obj = obj;
         this.category = category;
         this.level = level;
         this.threadData = threadData;
         this.exception = exception;
      }

      public void log(InternalAPILogListener listener) {
         listener.logException(this.klass, this.obj, this.category, this.level, this.threadData, this.exception);
      }
   }

   private static class LogMessage extends Logger.LogObject {
      private String message;

      public LogMessage(Class klass, Object obj, String category, int level, Object threadData, String message) {
         super(null);
         this.klass = klass;
         this.obj = obj;
         this.category = category;
         this.level = level;
         this.threadData = threadData;
         this.message = message;
      }

      public void log(InternalAPILogListener listener) {
         listener.logMessage(this.klass, this.obj, this.category, this.level, this.threadData, this.message);
      }
   }

   private abstract static class LogObject {
      protected Class klass;
      protected Object obj;
      protected String category;
      protected int level;
      protected Object threadData;

      private LogObject() {
      }

      public abstract void log(InternalAPILogListener var1);

      // $FF: synthetic method
      LogObject(Object x0) {
         this();
      }
   }
}
