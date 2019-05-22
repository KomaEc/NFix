package org.jboss.util;

import java.lang.ref.SoftReference;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import org.jboss.logging.Logger;
import org.jboss.util.collection.ConcurrentReferenceHashMap;

public class JBossObject implements JBossInterface {
   private static final Map<Class<?>, Logger> loggers = new ConcurrentReferenceHashMap();
   protected Logger log;
   protected transient SoftReference toString;
   protected transient int hashCode = Integer.MIN_VALUE;

   public JBossObject() {
      this.log = this.createLog();
   }

   public JBossObject(Logger log) {
      this.log = log != null ? log : this.createLog();
   }

   private Logger createLog() {
      Class<?> clazz = this.getClass();
      Logger logger = (Logger)loggers.get(clazz);
      if (logger == null) {
         logger = Logger.getLogger(clazz);
         loggers.put(clazz, logger);
      }

      return logger;
   }

   public static boolean equals(Object one, Object two) {
      if (one == null && two != null) {
         return false;
      } else {
         return one == null || one.equals(two);
      }
   }

   public static boolean notEqual(Object one, Object two) {
      return !equals(one, two);
   }

   public static void list(JBossStringBuilder buffer, Collection objects) {
      if (objects != null) {
         buffer.append('[');
         if (!objects.isEmpty()) {
            Iterator i = objects.iterator();

            while(i.hasNext()) {
               Object object = i.next();
               if (object instanceof JBossObject) {
                  ((JBossObject)object).toShortString(buffer);
               } else {
                  buffer.append(object.toString());
               }

               if (i.hasNext()) {
                  buffer.append(", ");
               }
            }
         }

         buffer.append(']');
      }
   }

   public String toString() {
      if (!this.cacheToString()) {
         return this.toStringImplementation();
      } else {
         String result = null;
         if (this.toString != null) {
            result = (String)this.toString.get();
         }

         if (result == null) {
            result = this.toStringImplementation();
            this.toString = new SoftReference(result);
         }

         return result;
      }
   }

   public int hashCode() {
      if (this.hashCode == Integer.MIN_VALUE || !this.cacheGetHashCode()) {
         this.hashCode = this.getHashCode();
      }

      return this.hashCode;
   }

   public Object clone() {
      try {
         return super.clone();
      } catch (CloneNotSupportedException var2) {
         throw new RuntimeException(var2);
      }
   }

   public String toShortString() {
      JBossStringBuilder buffer = new JBossStringBuilder();
      this.toShortString(buffer);
      return buffer.toString();
   }

   public void toShortString(JBossStringBuilder buffer) {
   }

   public String getClassShortName() {
      String longName = this.getClass().getName();
      int dot = longName.lastIndexOf(46);
      return dot != -1 ? longName.substring(dot + 1) : longName;
   }

   protected String toStringImplementation() {
      JBossStringBuilder buffer = new JBossStringBuilder();
      buffer.append(this.getClassShortName()).append('@');
      buffer.append(Integer.toHexString(System.identityHashCode(this)));
      buffer.append('{');
      this.toString(buffer);
      buffer.append('}');
      return buffer.toString();
   }

   protected void flushJBossObjectCache() {
      this.toString = null;
      this.hashCode = Integer.MIN_VALUE;
   }

   protected void toString(JBossStringBuilder buffer) {
   }

   protected int getHashCode() {
      return super.hashCode();
   }

   protected boolean cacheToString() {
      return true;
   }

   protected boolean cacheGetHashCode() {
      return true;
   }
}
