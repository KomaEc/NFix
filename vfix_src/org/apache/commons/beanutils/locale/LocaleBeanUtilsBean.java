package org.apache.commons.beanutils.locale;

import java.beans.IndexedPropertyDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Locale;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ContextClassLoaderLocal;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaClass;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.beanutils.MappedPropertyDescriptor;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LocaleBeanUtilsBean extends BeanUtilsBean {
   private static final ContextClassLoaderLocal localeBeansByClassLoader = new ContextClassLoaderLocal() {
      protected Object initialValue() {
         return new LocaleBeanUtilsBean();
      }
   };
   private static Log log;
   private LocaleConvertUtilsBean localeConvertUtils;
   // $FF: synthetic field
   static Class class$org$apache$commons$beanutils$locale$LocaleBeanUtilsBean;

   public static synchronized LocaleBeanUtilsBean getLocaleBeanUtilsInstance() {
      return (LocaleBeanUtilsBean)localeBeansByClassLoader.get();
   }

   public static synchronized void setInstance(LocaleBeanUtilsBean newInstance) {
      localeBeansByClassLoader.set(newInstance);
   }

   public LocaleBeanUtilsBean() {
      this.localeConvertUtils = new LocaleConvertUtilsBean();
   }

   public LocaleBeanUtilsBean(LocaleConvertUtilsBean localeConvertUtils, ConvertUtilsBean convertUtilsBean, PropertyUtilsBean propertyUtilsBean) {
      super(convertUtilsBean, propertyUtilsBean);
      this.localeConvertUtils = localeConvertUtils;
   }

   public LocaleBeanUtilsBean(LocaleConvertUtilsBean localeConvertUtils) {
      this.localeConvertUtils = localeConvertUtils;
   }

   public LocaleConvertUtilsBean getLocaleConvertUtils() {
      return this.localeConvertUtils;
   }

   public Locale getDefaultLocale() {
      return this.getLocaleConvertUtils().getDefaultLocale();
   }

   public void setDefaultLocale(Locale locale) {
      this.getLocaleConvertUtils().setDefaultLocale(locale);
   }

   public boolean getApplyLocalized() {
      return this.getLocaleConvertUtils().getApplyLocalized();
   }

   public void setApplyLocalized(boolean newApplyLocalized) {
      this.getLocaleConvertUtils().setApplyLocalized(newApplyLocalized);
   }

   public String getIndexedProperty(Object bean, String name, String pattern) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
      Object value = this.getPropertyUtils().getIndexedProperty(bean, name);
      return this.getLocaleConvertUtils().convert(value, pattern);
   }

   public String getIndexedProperty(Object bean, String name) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
      return this.getIndexedProperty(bean, name, (String)null);
   }

   public String getIndexedProperty(Object bean, String name, int index, String pattern) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
      Object value = this.getPropertyUtils().getIndexedProperty(bean, name, index);
      return this.getLocaleConvertUtils().convert(value, pattern);
   }

   public String getIndexedProperty(Object bean, String name, int index) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
      return this.getIndexedProperty(bean, name, index, (String)null);
   }

   public String getSimpleProperty(Object bean, String name, String pattern) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
      Object value = this.getPropertyUtils().getSimpleProperty(bean, name);
      return this.getLocaleConvertUtils().convert(value, pattern);
   }

   public String getSimpleProperty(Object bean, String name) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
      return this.getSimpleProperty(bean, name, (String)null);
   }

   public String getMappedProperty(Object bean, String name, String key, String pattern) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
      Object value = this.getPropertyUtils().getMappedProperty(bean, name, key);
      return this.getLocaleConvertUtils().convert(value, pattern);
   }

   public String getMappedProperty(Object bean, String name, String key) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
      return this.getMappedProperty(bean, name, key, (String)null);
   }

   public String getMappedPropertyLocale(Object bean, String name, String pattern) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
      Object value = this.getPropertyUtils().getMappedProperty(bean, name);
      return this.getLocaleConvertUtils().convert(value, pattern);
   }

   public String getMappedProperty(Object bean, String name) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
      return this.getMappedPropertyLocale(bean, name, (String)null);
   }

   public String getNestedProperty(Object bean, String name, String pattern) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
      Object value = this.getPropertyUtils().getNestedProperty(bean, name);
      return this.getLocaleConvertUtils().convert(value, pattern);
   }

   public String getNestedProperty(Object bean, String name) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
      return this.getNestedProperty(bean, name, (String)null);
   }

   public String getProperty(Object bean, String name, String pattern) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
      return this.getNestedProperty(bean, name, pattern);
   }

   public String getProperty(Object bean, String name) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
      return this.getNestedProperty(bean, name);
   }

   public void setProperty(Object bean, String name, Object value) throws IllegalAccessException, InvocationTargetException {
      this.setProperty(bean, name, value, (String)null);
   }

   public void setProperty(Object bean, String name, Object value, String pattern) throws IllegalAccessException, InvocationTargetException {
      if (log.isTraceEnabled()) {
         StringBuffer sb = new StringBuffer("  setProperty(");
         sb.append(bean);
         sb.append(", ");
         sb.append(name);
         sb.append(", ");
         if (value == null) {
            sb.append("<NULL>");
         } else if (value instanceof String) {
            sb.append((String)value);
         } else if (!(value instanceof String[])) {
            sb.append(value.toString());
         } else {
            String[] values = (String[])value;
            sb.append('[');

            for(int i = 0; i < values.length; ++i) {
               if (i > 0) {
                  sb.append(',');
               }

               sb.append(values[i]);
            }

            sb.append(']');
         }

         sb.append(')');
         log.trace(sb.toString());
      }

      LocaleBeanUtilsBean.Descriptor propInfo = this.calculate(bean, name);
      if (propInfo != null) {
         Class type = this.definePropertyType(propInfo.getTarget(), name, propInfo.getPropName());
         if (type != null) {
            Object newValue = this.convert(type, propInfo.getIndex(), value, pattern);
            this.invokeSetter(propInfo.getTarget(), propInfo.getPropName(), propInfo.getKey(), propInfo.getIndex(), newValue);
         }
      }

   }

   protected Class definePropertyType(Object target, String name, String propName) throws IllegalAccessException, InvocationTargetException {
      Class type = null;
      if (target instanceof DynaBean) {
         DynaClass dynaClass = ((DynaBean)target).getDynaClass();
         DynaProperty dynaProperty = dynaClass.getDynaProperty(propName);
         if (dynaProperty == null) {
            return null;
         }

         type = dynaProperty.getType();
      } else {
         PropertyDescriptor descriptor = null;

         try {
            descriptor = this.getPropertyUtils().getPropertyDescriptor(target, name);
            if (descriptor == null) {
               return null;
            }
         } catch (NoSuchMethodException var7) {
            return null;
         }

         if (descriptor instanceof MappedPropertyDescriptor) {
            type = ((MappedPropertyDescriptor)descriptor).getMappedPropertyType();
         } else if (descriptor instanceof IndexedPropertyDescriptor) {
            type = ((IndexedPropertyDescriptor)descriptor).getIndexedPropertyType();
         } else {
            type = descriptor.getPropertyType();
         }
      }

      return type;
   }

   protected Object convert(Class type, int index, Object value, String pattern) {
      if (log.isTraceEnabled()) {
         log.trace("Converting value '" + value + "' to type:" + type);
      }

      Object newValue = null;
      if (type.isArray() && index < 0) {
         if (value instanceof String) {
            String[] values = new String[]{(String)value};
            newValue = this.getLocaleConvertUtils().convert((String[])values, type, pattern);
         } else if (value instanceof String[]) {
            newValue = this.getLocaleConvertUtils().convert((String[])value, type, pattern);
         } else {
            newValue = value;
         }
      } else if (type.isArray()) {
         if (value instanceof String) {
            newValue = this.getLocaleConvertUtils().convert((String)value, type.getComponentType(), pattern);
         } else if (value instanceof String[]) {
            newValue = this.getLocaleConvertUtils().convert(((String[])value)[0], type.getComponentType(), pattern);
         } else {
            newValue = value;
         }
      } else if (value instanceof String) {
         newValue = this.getLocaleConvertUtils().convert((String)value, type, pattern);
      } else if (value instanceof String[]) {
         newValue = this.getLocaleConvertUtils().convert(((String[])value)[0], type, pattern);
      } else {
         newValue = value;
      }

      return newValue;
   }

   protected Object convert(Class type, int index, Object value) {
      Object newValue = null;
      if (type.isArray() && index < 0) {
         if (value instanceof String) {
            String[] values = new String[]{(String)value};
            newValue = ConvertUtils.convert((String[])values, type);
         } else if (value instanceof String[]) {
            newValue = ConvertUtils.convert((String[])value, type);
         } else {
            newValue = value;
         }
      } else if (type.isArray()) {
         if (value instanceof String) {
            newValue = ConvertUtils.convert((String)value, type.getComponentType());
         } else if (value instanceof String[]) {
            newValue = ConvertUtils.convert(((String[])value)[0], type.getComponentType());
         } else {
            newValue = value;
         }
      } else if (value instanceof String) {
         newValue = ConvertUtils.convert((String)value, type);
      } else if (value instanceof String[]) {
         newValue = ConvertUtils.convert(((String[])value)[0], type);
      } else {
         newValue = value;
      }

      return newValue;
   }

   protected void invokeSetter(Object target, String propName, String key, int index, Object newValue) throws IllegalAccessException, InvocationTargetException {
      try {
         if (index >= 0) {
            this.getPropertyUtils().setIndexedProperty(target, propName, index, newValue);
         } else if (key != null) {
            this.getPropertyUtils().setMappedProperty(target, propName, key, newValue);
         } else {
            this.getPropertyUtils().setProperty(target, propName, newValue);
         }

      } catch (NoSuchMethodException var7) {
         throw new InvocationTargetException(var7, "Cannot set " + propName);
      }
   }

   protected LocaleBeanUtilsBean.Descriptor calculate(Object bean, String name) throws IllegalAccessException, InvocationTargetException {
      String propName = null;
      int index = -1;
      String key = null;
      Object target = bean;
      int delim = name.lastIndexOf(46);
      if (delim >= 0) {
         try {
            target = this.getPropertyUtils().getProperty(bean, name.substring(0, delim));
         } catch (NoSuchMethodException var14) {
            return null;
         }

         name = name.substring(delim + 1);
         if (log.isTraceEnabled()) {
            log.trace("    Target bean = " + target);
            log.trace("    Target name = " + name);
         }
      }

      propName = name;
      int i = name.indexOf(91);
      int j;
      if (i >= 0) {
         j = name.indexOf(93);

         try {
            index = Integer.parseInt(propName.substring(i + 1, j));
         } catch (NumberFormatException var13) {
         }

         propName = name.substring(0, i);
      }

      j = propName.indexOf(40);
      if (j >= 0) {
         int k = propName.indexOf(41);

         try {
            key = propName.substring(j + 1, k);
         } catch (IndexOutOfBoundsException var12) {
         }

         propName = propName.substring(0, j);
      }

      return new LocaleBeanUtilsBean.Descriptor(target, name, propName, key, index);
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
      log = LogFactory.getLog(class$org$apache$commons$beanutils$locale$LocaleBeanUtilsBean == null ? (class$org$apache$commons$beanutils$locale$LocaleBeanUtilsBean = class$("org.apache.commons.beanutils.locale.LocaleBeanUtilsBean")) : class$org$apache$commons$beanutils$locale$LocaleBeanUtilsBean);
   }

   protected class Descriptor {
      private int index = -1;
      private String name;
      private String propName;
      private String key;
      private Object target;

      public Descriptor(Object target, String name, String propName, String key, int index) {
         this.setTarget(target);
         this.setName(name);
         this.setPropName(propName);
         this.setKey(key);
         this.setIndex(index);
      }

      public Object getTarget() {
         return this.target;
      }

      public void setTarget(Object target) {
         this.target = target;
      }

      public String getKey() {
         return this.key;
      }

      public void setKey(String key) {
         this.key = key;
      }

      public int getIndex() {
         return this.index;
      }

      public void setIndex(int index) {
         this.index = index;
      }

      public String getName() {
         return this.name;
      }

      public void setName(String name) {
         this.name = name;
      }

      public String getPropName() {
         return this.propName;
      }

      public void setPropName(String propName) {
         this.propName = propName;
      }
   }
}
