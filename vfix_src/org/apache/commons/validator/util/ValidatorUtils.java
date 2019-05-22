package org.apache.commons.validator.util;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.FastHashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.Arg;
import org.apache.commons.validator.Msg;
import org.apache.commons.validator.Var;

public class ValidatorUtils {
   private static final Log log;
   // $FF: synthetic field
   static Class class$org$apache$commons$validator$util$ValidatorUtils;

   public static String replace(String value, String key, String replaceValue) {
      if (value != null && key != null && replaceValue != null) {
         int pos = value.indexOf(key);
         if (pos < 0) {
            return value;
         } else {
            int length = value.length();
            int end = pos + key.length();
            if (length == key.length()) {
               value = replaceValue;
            } else if (end == length) {
               value = value.substring(0, pos) + replaceValue;
            } else {
               value = value.substring(0, pos) + replaceValue + replace(value.substring(end), key, replaceValue);
            }

            return value;
         }
      } else {
         return value;
      }
   }

   public static String getValueAsString(Object bean, String property) {
      Object value = null;

      try {
         value = PropertyUtils.getProperty(bean, property);
      } catch (IllegalAccessException var6) {
         log.error(var6.getMessage(), var6);
      } catch (InvocationTargetException var7) {
         log.error(var7.getMessage(), var7);
      } catch (NoSuchMethodException var8) {
         log.error(var8.getMessage(), var8);
      }

      if (value == null) {
         return null;
      } else if (value instanceof String[]) {
         return ((String[])value).length > 0 ? value.toString() : "";
      } else if (value instanceof Collection) {
         return ((Collection)value).isEmpty() ? "" : value.toString();
      } else {
         return value.toString();
      }
   }

   /** @deprecated */
   public static FastHashMap copyFastHashMap(FastHashMap map) {
      FastHashMap results = new FastHashMap();
      Iterator i = map.keySet().iterator();

      while(i.hasNext()) {
         String key = (String)i.next();
         Object value = map.get(key);
         if (value instanceof Msg) {
            results.put(key, ((Msg)value).clone());
         } else if (value instanceof Arg) {
            results.put(key, ((Arg)value).clone());
         } else if (value instanceof Var) {
            results.put(key, ((Var)value).clone());
         } else {
            results.put(key, value);
         }
      }

      results.setFast(true);
      return results;
   }

   public static Map copyMap(Map map) {
      Map results = new HashMap();
      Iterator iter = map.keySet().iterator();

      while(iter.hasNext()) {
         String key = (String)iter.next();
         Object value = map.get(key);
         if (value instanceof Msg) {
            results.put(key, ((Msg)value).clone());
         } else if (value instanceof Arg) {
            results.put(key, ((Arg)value).clone());
         } else if (value instanceof Var) {
            results.put(key, ((Var)value).clone());
         } else {
            results.put(key, value);
         }
      }

      return results;
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   static {
      log = LogFactory.getLog(class$org$apache$commons$validator$util$ValidatorUtils == null ? (class$org$apache$commons$validator$util$ValidatorUtils = class$("org.apache.commons.validator.util.ValidatorUtils")) : class$org$apache$commons$validator$util$ValidatorUtils);
   }
}
