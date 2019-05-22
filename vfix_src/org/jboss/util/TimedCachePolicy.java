package org.jboss.util;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.Map.Entry;
import org.jboss.logging.Logger;
import org.jboss.util.loading.ContextClassLoaderSwitcher;

public class TimedCachePolicy extends java.util.TimerTask implements CachePolicy {
   public static final String TIMER_CLASSLOADER_PROPERTY = "jboss.common.timedcachepolicy.timer.classloader";
   public static final String TIMER_CLASSLOADER_SYSTEM = "system";
   public static final String TIMER_CLASSLOADER_CURRENT = "current";
   public static final String TIMER_CLASSLOADER_CONTEXT = "context";
   private static final Logger log = Logger.getLogger(TimedCachePolicy.class);
   protected static Timer resolutionTimer;
   protected Map entryMap;
   protected int defaultLifetime;
   protected boolean threadSafe;
   protected long now;
   protected int resolution;
   protected TimedCachePolicy.ResolutionTimer theTimer;

   public TimedCachePolicy() {
      this(1800, false, 0);
   }

   public TimedCachePolicy(int defaultLifetime) {
      this(defaultLifetime, false, 0);
   }

   public TimedCachePolicy(int defaultLifetime, boolean threadSafe, int resolution) {
      this.defaultLifetime = defaultLifetime;
      this.threadSafe = threadSafe;
      if (resolution <= 0) {
         resolution = 60;
      }

      this.resolution = resolution;
   }

   public void create() {
      if (this.threadSafe) {
         this.entryMap = Collections.synchronizedMap(new HashMap());
      } else {
         this.entryMap = new HashMap();
      }

      this.now = System.currentTimeMillis();
   }

   public void start() {
      this.theTimer = new TimedCachePolicy.ResolutionTimer();
      resolutionTimer.scheduleAtFixedRate(this.theTimer, 0L, (long)(1000 * this.resolution));
   }

   public void stop() {
      this.theTimer.cancel();
      this.flush();
   }

   public void destroy() {
      this.entryMap.clear();
   }

   public Object get(Object key) {
      TimedCachePolicy.TimedEntry entry = (TimedCachePolicy.TimedEntry)this.entryMap.get(key);
      if (entry == null) {
         return null;
      } else if (!entry.isCurrent(this.now) && !entry.refresh()) {
         entry.destroy();
         this.entryMap.remove(key);
         return null;
      } else {
         Object value = entry.getValue();
         return value;
      }
   }

   public Object peek(Object key) {
      TimedCachePolicy.TimedEntry entry = (TimedCachePolicy.TimedEntry)this.entryMap.get(key);
      Object value = null;
      if (entry != null) {
         value = entry.getValue();
      }

      return value;
   }

   public void insert(Object key, Object value) {
      if (this.entryMap.containsKey(key)) {
         throw new IllegalStateException("Attempt to insert duplicate entry");
      } else {
         TimedCachePolicy.TimedEntry entry = null;
         if (!(value instanceof TimedCachePolicy.TimedEntry)) {
            entry = new TimedCachePolicy.DefaultTimedEntry((long)this.defaultLifetime, value);
         } else {
            entry = (TimedCachePolicy.TimedEntry)value;
         }

         ((TimedCachePolicy.TimedEntry)entry).init(this.now);
         this.entryMap.put(key, entry);
      }
   }

   public void remove(Object key) {
      TimedCachePolicy.TimedEntry entry = (TimedCachePolicy.TimedEntry)this.entryMap.remove(key);
      if (entry != null) {
         entry.destroy();
      }

   }

   public void flush() {
      Map tmpMap = null;
      synchronized(this) {
         tmpMap = this.entryMap;
         if (this.threadSafe) {
            this.entryMap = Collections.synchronizedMap(new HashMap());
         } else {
            this.entryMap = new HashMap();
         }
      }

      Iterator iter = tmpMap.values().iterator();

      while(iter.hasNext()) {
         TimedCachePolicy.TimedEntry entry = (TimedCachePolicy.TimedEntry)iter.next();
         entry.destroy();
      }

      tmpMap.clear();
   }

   public int size() {
      return this.entryMap.size();
   }

   public List getValidKeys() {
      ArrayList validKeys = new ArrayList();
      synchronized(this.entryMap) {
         Iterator iter = this.entryMap.entrySet().iterator();

         while(iter.hasNext()) {
            Entry entry = (Entry)iter.next();
            TimedCachePolicy.TimedEntry value = (TimedCachePolicy.TimedEntry)entry.getValue();
            if (value.isCurrent(this.now)) {
               validKeys.add(entry.getKey());
            }
         }

         return validKeys;
      }
   }

   public int getDefaultLifetime() {
      return this.defaultLifetime;
   }

   public synchronized void setDefaultLifetime(int defaultLifetime) {
      this.defaultLifetime = defaultLifetime;
   }

   public int getResolution() {
      return this.resolution;
   }

   public synchronized void setResolution(int resolution) {
      if (resolution <= 0) {
         resolution = 60;
      }

      if (resolution != this.resolution) {
         this.resolution = resolution;
         this.theTimer.cancel();
         this.theTimer = new TimedCachePolicy.ResolutionTimer();
         resolutionTimer.scheduleAtFixedRate(this.theTimer, 0L, (long)(1000 * resolution));
      }

   }

   public void run() {
      this.now = System.currentTimeMillis();
   }

   public long currentTimeMillis() {
      return this.now;
   }

   public TimedCachePolicy.TimedEntry peekEntry(Object key) {
      TimedCachePolicy.TimedEntry entry = (TimedCachePolicy.TimedEntry)this.entryMap.get(key);
      return entry;
   }

   static {
      ContextClassLoaderSwitcher.SwitchContext clSwitchContext = null;

      try {
         String timerCl = (String)AccessController.doPrivileged(new PrivilegedAction<String>() {
            public String run() {
               return System.getProperty("jboss.common.timedcachepolicy.timer.classloader", "system");
            }
         });
         if (!"context".equalsIgnoreCase(timerCl)) {
            ContextClassLoaderSwitcher clSwitcher = (ContextClassLoaderSwitcher)AccessController.doPrivileged(ContextClassLoaderSwitcher.INSTANTIATOR);
            if ("current".equalsIgnoreCase(timerCl)) {
               clSwitchContext = clSwitcher.getSwitchContext(TimedCachePolicy.class.getClassLoader());
            } else {
               if (!"system".equalsIgnoreCase(timerCl)) {
                  log.warn("Unknown value " + timerCl + " found for property " + "jboss.common.timedcachepolicy.timer.classloader" + " -- using the system classloader");
               }

               clSwitchContext = clSwitcher.getSwitchContext(ClassLoader.getSystemClassLoader());
            }
         }

         resolutionTimer = new Timer(true);
      } catch (SecurityException var6) {
         resolutionTimer = new Timer(true);
      } finally {
         if (clSwitchContext != null) {
            clSwitchContext.reset();
         }

      }

   }

   private class ResolutionTimer extends java.util.TimerTask {
      private ResolutionTimer() {
      }

      public void run() {
         TimedCachePolicy.this.run();
      }

      // $FF: synthetic method
      ResolutionTimer(Object x1) {
         this();
      }
   }

   static class DefaultTimedEntry implements TimedCachePolicy.TimedEntry {
      long expirationTime;
      Object value;

      DefaultTimedEntry(long lifetime, Object value) {
         this.expirationTime = 1000L * lifetime;
         this.value = value;
      }

      public void init(long now) {
         this.expirationTime += now;
      }

      public boolean isCurrent(long now) {
         return this.expirationTime > now;
      }

      public boolean refresh() {
         return false;
      }

      public void destroy() {
      }

      public Object getValue() {
         return this.value;
      }
   }

   public interface TimedEntry {
      void init(long var1);

      boolean isCurrent(long var1);

      boolean refresh();

      void destroy();

      Object getValue();
   }
}
