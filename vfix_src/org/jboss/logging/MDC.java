package org.jboss.logging;

import java.util.Map;

public class MDC {
   private static final MDCProvider mdc;

   public static void put(String key, Object val) {
      mdc.put(key, val);
   }

   public static Object get(String key) {
      return mdc.get(key);
   }

   public static void remove(String key) {
      mdc.remove(key);
   }

   public static Map<String, Object> getMap() {
      return mdc.getMap();
   }

   static {
      MDCProvider m = null;
      if (MDCSupport.class.isAssignableFrom(Logger.pluginClass)) {
         try {
            m = ((MDCSupport)Logger.pluginClass.newInstance()).getMDCProvider();
         } catch (Throwable var2) {
         }
      }

      if (m == null) {
         m = new NullMDCProvider();
      }

      mdc = (MDCProvider)m;
   }
}
