package org.apache.commons.beanutils;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import org.apache.commons.collections.FastHashMap;

public class BeanUtils {
   private static FastHashMap dummy = new FastHashMap();
   /** @deprecated */
   private static int debug = 0;

   /** @deprecated */
   public static int getDebug() {
      return debug;
   }

   /** @deprecated */
   public static void setDebug(int newDebug) {
      debug = newDebug;
   }

   public static Object cloneBean(Object bean) throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
      return BeanUtilsBean.getInstance().cloneBean(bean);
   }

   public static void copyProperties(Object dest, Object orig) throws IllegalAccessException, InvocationTargetException {
      BeanUtilsBean.getInstance().copyProperties(dest, orig);
   }

   public static void copyProperty(Object bean, String name, Object value) throws IllegalAccessException, InvocationTargetException {
      BeanUtilsBean.getInstance().copyProperty(bean, name, value);
   }

   public static Map describe(Object bean) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
      return BeanUtilsBean.getInstance().describe(bean);
   }

   public static String[] getArrayProperty(Object bean, String name) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
      return BeanUtilsBean.getInstance().getArrayProperty(bean, name);
   }

   public static String getIndexedProperty(Object bean, String name) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
      return BeanUtilsBean.getInstance().getIndexedProperty(bean, name);
   }

   public static String getIndexedProperty(Object bean, String name, int index) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
      return BeanUtilsBean.getInstance().getIndexedProperty(bean, name, index);
   }

   public static String getMappedProperty(Object bean, String name) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
      return BeanUtilsBean.getInstance().getMappedProperty(bean, name);
   }

   public static String getMappedProperty(Object bean, String name, String key) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
      return BeanUtilsBean.getInstance().getMappedProperty(bean, name, key);
   }

   public static String getNestedProperty(Object bean, String name) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
      return BeanUtilsBean.getInstance().getNestedProperty(bean, name);
   }

   public static String getProperty(Object bean, String name) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
      return BeanUtilsBean.getInstance().getProperty(bean, name);
   }

   public static String getSimpleProperty(Object bean, String name) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
      return BeanUtilsBean.getInstance().getSimpleProperty(bean, name);
   }

   public static void populate(Object bean, Map properties) throws IllegalAccessException, InvocationTargetException {
      BeanUtilsBean.getInstance().populate(bean, properties);
   }

   public static void setProperty(Object bean, String name, Object value) throws IllegalAccessException, InvocationTargetException {
      BeanUtilsBean.getInstance().setProperty(bean, name, value);
   }
}
