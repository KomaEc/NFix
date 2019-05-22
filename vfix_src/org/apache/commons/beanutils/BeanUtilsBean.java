package org.apache.commons.beanutils;

import java.beans.IndexedPropertyDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BeanUtilsBean {
   private static final ContextClassLoaderLocal beansByClassLoader = new ContextClassLoaderLocal() {
      protected Object initialValue() {
         return new BeanUtilsBean();
      }
   };
   private Log log;
   private ConvertUtilsBean convertUtilsBean;
   private PropertyUtilsBean propertyUtilsBean;
   // $FF: synthetic field
   static Class class$org$apache$commons$beanutils$BeanUtils;

   public static synchronized BeanUtilsBean getInstance() {
      return (BeanUtilsBean)beansByClassLoader.get();
   }

   public static synchronized void setInstance(BeanUtilsBean newInstance) {
      beansByClassLoader.set(newInstance);
   }

   public BeanUtilsBean() {
      this(new ConvertUtilsBean(), new PropertyUtilsBean());
   }

   public BeanUtilsBean(ConvertUtilsBean convertUtilsBean, PropertyUtilsBean propertyUtilsBean) {
      this.log = LogFactory.getLog(class$org$apache$commons$beanutils$BeanUtils == null ? (class$org$apache$commons$beanutils$BeanUtils = class$("org.apache.commons.beanutils.BeanUtils")) : class$org$apache$commons$beanutils$BeanUtils);
      this.convertUtilsBean = convertUtilsBean;
      this.propertyUtilsBean = propertyUtilsBean;
   }

   public Object cloneBean(Object bean) throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
      if (this.log.isDebugEnabled()) {
         this.log.debug("Cloning bean: " + bean.getClass().getName());
      }

      Class clazz = bean.getClass();
      Object newBean = null;
      if (bean instanceof DynaBean) {
         newBean = ((DynaBean)bean).getDynaClass().newInstance();
      } else {
         newBean = bean.getClass().newInstance();
      }

      this.getPropertyUtils().copyProperties(newBean, bean);
      return newBean;
   }

   public void copyProperties(Object dest, Object orig) throws IllegalAccessException, InvocationTargetException {
      if (dest == null) {
         throw new IllegalArgumentException("No destination bean specified");
      } else if (orig == null) {
         throw new IllegalArgumentException("No origin bean specified");
      } else {
         if (this.log.isDebugEnabled()) {
            this.log.debug("BeanUtils.copyProperties(" + dest + ", " + orig + ")");
         }

         int i;
         String name;
         Object value;
         if (orig instanceof DynaBean) {
            DynaProperty[] origDescriptors = ((DynaBean)orig).getDynaClass().getDynaProperties();

            for(i = 0; i < origDescriptors.length; ++i) {
               name = origDescriptors[i].getName();
               if (this.getPropertyUtils().isWriteable(dest, name)) {
                  value = ((DynaBean)orig).get(name);
                  this.copyProperty(dest, name, value);
               }
            }
         } else if (orig instanceof Map) {
            Iterator names = ((Map)orig).keySet().iterator();

            while(names.hasNext()) {
               String name = (String)names.next();
               if (this.getPropertyUtils().isWriteable(dest, name)) {
                  Object value = ((Map)orig).get(name);
                  this.copyProperty(dest, name, value);
               }
            }
         } else {
            PropertyDescriptor[] origDescriptors = this.getPropertyUtils().getPropertyDescriptors(orig);

            for(i = 0; i < origDescriptors.length; ++i) {
               name = origDescriptors[i].getName();
               if (!"class".equals(name) && this.getPropertyUtils().isReadable(orig, name) && this.getPropertyUtils().isWriteable(dest, name)) {
                  try {
                     value = this.getPropertyUtils().getSimpleProperty(orig, name);
                     this.copyProperty(dest, name, value);
                  } catch (NoSuchMethodException var7) {
                  }
               }
            }
         }

      }
   }

   public void copyProperty(Object bean, String name, Object value) throws IllegalAccessException, InvocationTargetException {
      if (this.log.isTraceEnabled()) {
         StringBuffer sb = new StringBuffer("  copyProperty(");
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
         this.log.trace(sb.toString());
      }

      Object target = bean;
      int delim = name.lastIndexOf(46);
      if (delim >= 0) {
         try {
            target = this.getPropertyUtils().getProperty(bean, name.substring(0, delim));
         } catch (NoSuchMethodException var19) {
            return;
         }

         name = name.substring(delim + 1);
         if (this.log.isTraceEnabled()) {
            this.log.trace("    Target bean = " + target);
            this.log.trace("    Target name = " + name);
         }
      }

      String propName = null;
      Class type = null;
      int index = -1;
      String key = null;
      propName = name;
      int i = name.indexOf(91);
      int j;
      if (i >= 0) {
         j = name.indexOf(93);

         try {
            index = Integer.parseInt(propName.substring(i + 1, j));
         } catch (NumberFormatException var18) {
         }

         propName = name.substring(0, i);
      }

      j = propName.indexOf(40);
      if (j >= 0) {
         int k = propName.indexOf(41);

         try {
            key = propName.substring(j + 1, k);
         } catch (IndexOutOfBoundsException var17) {
         }

         propName = propName.substring(0, j);
      }

      if (target instanceof DynaBean) {
         DynaClass dynaClass = ((DynaBean)target).getDynaClass();
         DynaProperty dynaProperty = dynaClass.getDynaProperty(propName);
         if (dynaProperty == null) {
            return;
         }

         type = dynaProperty.getType();
      } else {
         PropertyDescriptor descriptor = null;

         try {
            descriptor = this.getPropertyUtils().getPropertyDescriptor(target, name);
            if (descriptor == null) {
               return;
            }
         } catch (NoSuchMethodException var20) {
            return;
         }

         type = descriptor.getPropertyType();
         if (type == null) {
            if (this.log.isTraceEnabled()) {
               this.log.trace("    target type for property '" + propName + "' is null, so skipping ths setter");
            }

            return;
         }
      }

      if (this.log.isTraceEnabled()) {
         this.log.trace("    target propName=" + propName + ", type=" + type + ", index=" + index + ", key=" + key);
      }

      Converter converter;
      if (index >= 0) {
         converter = this.getConvertUtils().lookup(type.getComponentType());
         if (converter != null) {
            this.log.trace("        USING CONVERTER " + converter);
            value = converter.convert(type, value);
         }

         try {
            this.getPropertyUtils().setIndexedProperty(target, propName, index, value);
         } catch (NoSuchMethodException var16) {
            throw new InvocationTargetException(var16, "Cannot set " + propName);
         }
      } else if (key != null) {
         try {
            this.getPropertyUtils().setMappedProperty(target, propName, key, value);
         } catch (NoSuchMethodException var15) {
            throw new InvocationTargetException(var15, "Cannot set " + propName);
         }
      } else {
         converter = this.getConvertUtils().lookup(type);
         if (converter != null) {
            this.log.trace("        USING CONVERTER " + converter);
            value = converter.convert(type, value);
         }

         try {
            this.getPropertyUtils().setSimpleProperty(target, propName, value);
         } catch (NoSuchMethodException var14) {
            throw new InvocationTargetException(var14, "Cannot set " + propName);
         }
      }

   }

   public Map describe(Object bean) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
      if (bean == null) {
         return new HashMap();
      } else {
         if (this.log.isDebugEnabled()) {
            this.log.debug("Describing bean: " + bean.getClass().getName());
         }

         Map description = new HashMap();
         int i;
         String name;
         if (bean instanceof DynaBean) {
            DynaProperty[] descriptors = ((DynaBean)bean).getDynaClass().getDynaProperties();

            for(i = 0; i < descriptors.length; ++i) {
               name = descriptors[i].getName();
               description.put(name, this.getProperty(bean, name));
            }
         } else {
            PropertyDescriptor[] descriptors = this.getPropertyUtils().getPropertyDescriptors(bean);

            for(i = 0; i < descriptors.length; ++i) {
               name = descriptors[i].getName();
               if (descriptors[i].getReadMethod() != null) {
                  description.put(name, this.getProperty(bean, name));
               }
            }
         }

         return description;
      }
   }

   public String[] getArrayProperty(Object bean, String name) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
      Object value = this.getPropertyUtils().getProperty(bean, name);
      if (value == null) {
         return null;
      } else if (value instanceof Collection) {
         ArrayList values = new ArrayList();
         Iterator items = ((Collection)value).iterator();

         while(items.hasNext()) {
            Object item = items.next();
            if (item == null) {
               values.add((String)null);
            } else {
               values.add(this.getConvertUtils().convert(item));
            }
         }

         return (String[])values.toArray(new String[values.size()]);
      } else if (value.getClass().isArray()) {
         int n = Array.getLength(value);
         String[] results = new String[n];

         for(int i = 0; i < n; ++i) {
            Object item = Array.get(value, i);
            if (item == null) {
               results[i] = null;
            } else {
               results[i] = this.getConvertUtils().convert(item);
            }
         }

         return results;
      } else {
         String[] results = new String[]{value.toString()};
         return results;
      }
   }

   public String getIndexedProperty(Object bean, String name) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
      Object value = this.getPropertyUtils().getIndexedProperty(bean, name);
      return this.getConvertUtils().convert(value);
   }

   public String getIndexedProperty(Object bean, String name, int index) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
      Object value = this.getPropertyUtils().getIndexedProperty(bean, name, index);
      return this.getConvertUtils().convert(value);
   }

   public String getMappedProperty(Object bean, String name) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
      Object value = this.getPropertyUtils().getMappedProperty(bean, name);
      return this.getConvertUtils().convert(value);
   }

   public String getMappedProperty(Object bean, String name, String key) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
      Object value = this.getPropertyUtils().getMappedProperty(bean, name, key);
      return this.getConvertUtils().convert(value);
   }

   public String getNestedProperty(Object bean, String name) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
      Object value = this.getPropertyUtils().getNestedProperty(bean, name);
      return this.getConvertUtils().convert(value);
   }

   public String getProperty(Object bean, String name) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
      return this.getNestedProperty(bean, name);
   }

   public String getSimpleProperty(Object bean, String name) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
      Object value = this.getPropertyUtils().getSimpleProperty(bean, name);
      return this.getConvertUtils().convert(value);
   }

   public void populate(Object bean, Map properties) throws IllegalAccessException, InvocationTargetException {
      if (bean != null && properties != null) {
         if (this.log.isDebugEnabled()) {
            this.log.debug("BeanUtils.populate(" + bean + ", " + properties + ")");
         }

         Iterator names = properties.keySet().iterator();

         while(names.hasNext()) {
            String name = (String)names.next();
            if (name != null) {
               Object value = properties.get(name);
               this.setProperty(bean, name, value);
            }
         }

      }
   }

   public void setProperty(Object bean, String name, Object value) throws IllegalAccessException, InvocationTargetException {
      if (this.log.isTraceEnabled()) {
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
         this.log.trace(sb.toString());
      }

      Object target = bean;
      int delim = this.findLastNestedIndex(name);
      if (delim >= 0) {
         try {
            target = this.getPropertyUtils().getProperty(bean, name.substring(0, delim));
         } catch (NoSuchMethodException var17) {
            return;
         }

         name = name.substring(delim + 1);
         if (this.log.isTraceEnabled()) {
            this.log.trace("    Target bean = " + target);
            this.log.trace("    Target name = " + name);
         }
      }

      String propName = null;
      Class type = null;
      int index = -1;
      String key = null;
      propName = name;
      int i = name.indexOf(91);
      int j;
      if (i >= 0) {
         j = name.indexOf(93);

         try {
            index = Integer.parseInt(propName.substring(i + 1, j));
         } catch (NumberFormatException var16) {
         }

         propName = name.substring(0, i);
      }

      j = propName.indexOf(40);
      if (j >= 0) {
         int k = propName.indexOf(41);

         try {
            key = propName.substring(j + 1, k);
         } catch (IndexOutOfBoundsException var15) {
         }

         propName = propName.substring(0, j);
      }

      PropertyDescriptor descriptor;
      if (target instanceof DynaBean) {
         DynaClass dynaClass = ((DynaBean)target).getDynaClass();
         DynaProperty dynaProperty = dynaClass.getDynaProperty(propName);
         if (dynaProperty == null) {
            return;
         }

         type = dynaProperty.getType();
      } else {
         descriptor = null;

         try {
            descriptor = this.getPropertyUtils().getPropertyDescriptor(target, name);
            if (descriptor == null) {
               return;
            }
         } catch (NoSuchMethodException var18) {
            return;
         }

         if (descriptor instanceof MappedPropertyDescriptor) {
            if (((MappedPropertyDescriptor)descriptor).getMappedWriteMethod() == null) {
               if (this.log.isDebugEnabled()) {
                  this.log.debug("Skipping read-only property");
               }

               return;
            }

            type = ((MappedPropertyDescriptor)descriptor).getMappedPropertyType();
         } else if (descriptor instanceof IndexedPropertyDescriptor) {
            if (((IndexedPropertyDescriptor)descriptor).getIndexedWriteMethod() == null) {
               if (this.log.isDebugEnabled()) {
                  this.log.debug("Skipping read-only property");
               }

               return;
            }

            type = ((IndexedPropertyDescriptor)descriptor).getIndexedPropertyType();
         } else {
            if (descriptor.getWriteMethod() == null) {
               if (this.log.isDebugEnabled()) {
                  this.log.debug("Skipping read-only property");
               }

               return;
            }

            type = descriptor.getPropertyType();
         }
      }

      descriptor = null;
      Object newValue;
      if (type.isArray() && index < 0) {
         String[] values;
         if (value == null) {
            values = new String[]{(String)value};
            newValue = this.getConvertUtils().convert((String[])values, type);
         } else if (value instanceof String) {
            values = new String[]{(String)value};
            newValue = this.getConvertUtils().convert((String[])values, type);
         } else if (value instanceof String[]) {
            newValue = this.getConvertUtils().convert((String[])value, type);
         } else {
            newValue = value;
         }
      } else if (type.isArray()) {
         if (value instanceof String) {
            newValue = this.getConvertUtils().convert((String)value, type.getComponentType());
         } else if (value instanceof String[]) {
            newValue = this.getConvertUtils().convert(((String[])value)[0], type.getComponentType());
         } else {
            newValue = value;
         }
      } else if (!(value instanceof String) && value != null) {
         if (value instanceof String[]) {
            newValue = this.getConvertUtils().convert(((String[])value)[0], type);
         } else if (this.getConvertUtils().lookup(value.getClass()) != null) {
            newValue = this.getConvertUtils().convert(value.toString(), type);
         } else {
            newValue = value;
         }
      } else {
         newValue = this.getConvertUtils().convert((String)value, type);
      }

      try {
         if (index >= 0) {
            this.getPropertyUtils().setIndexedProperty(target, propName, index, newValue);
         } else if (key != null) {
            this.getPropertyUtils().setMappedProperty(target, propName, key, newValue);
         } else {
            this.getPropertyUtils().setProperty(target, propName, newValue);
         }

      } catch (NoSuchMethodException var14) {
         throw new InvocationTargetException(var14, "Cannot set " + propName);
      }
   }

   private int findLastNestedIndex(String expression) {
      int bracketCount = 0;

      for(int i = expression.length() - 1; i >= 0; --i) {
         char at = expression.charAt(i);
         switch(at) {
         case '(':
         case '[':
            --bracketCount;
            break;
         case ')':
         case ']':
            ++bracketCount;
            break;
         case '.':
            if (bracketCount < 1) {
               return i;
            }
         }
      }

      return -1;
   }

   public ConvertUtilsBean getConvertUtils() {
      return this.convertUtilsBean;
   }

   public PropertyUtilsBean getPropertyUtils() {
      return this.propertyUtilsBean;
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
