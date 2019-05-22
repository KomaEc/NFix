package org.apache.commons.collections;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import org.apache.commons.collections.keyvalue.AbstractMapEntry;
import org.apache.commons.collections.list.UnmodifiableList;
import org.apache.commons.collections.set.UnmodifiableSet;

/** @deprecated */
public class BeanMap extends AbstractMap implements Cloneable {
   private transient Object bean;
   private transient HashMap readMethods = new HashMap();
   private transient HashMap writeMethods = new HashMap();
   private transient HashMap types = new HashMap();
   public static final Object[] NULL_ARGUMENTS = new Object[0];
   public static HashMap defaultTransformers = new HashMap();

   public BeanMap() {
   }

   public BeanMap(Object bean) {
      this.bean = bean;
      this.initialise();
   }

   public String toString() {
      return "BeanMap<" + String.valueOf(this.bean) + ">";
   }

   public Object clone() throws CloneNotSupportedException {
      BeanMap newMap = (BeanMap)super.clone();
      if (this.bean == null) {
         return newMap;
      } else {
         Object newBean = null;
         Class beanClass = null;

         try {
            beanClass = this.bean.getClass();
            newBean = beanClass.newInstance();
         } catch (Exception var8) {
            throw new CloneNotSupportedException("Unable to instantiate the underlying bean \"" + beanClass.getName() + "\": " + var8);
         }

         try {
            newMap.setBean(newBean);
         } catch (Exception var7) {
            throw new CloneNotSupportedException("Unable to set bean in the cloned bean map: " + var7);
         }

         try {
            Iterator readableKeys = this.readMethods.keySet().iterator();

            while(readableKeys.hasNext()) {
               Object key = readableKeys.next();
               if (this.getWriteMethod(key) != null) {
                  newMap.put(key, this.get(key));
               }
            }

            return newMap;
         } catch (Exception var6) {
            throw new CloneNotSupportedException("Unable to copy bean values to cloned bean map: " + var6);
         }
      }
   }

   public void putAllWriteable(BeanMap map) {
      Iterator readableKeys = map.readMethods.keySet().iterator();

      while(readableKeys.hasNext()) {
         Object key = readableKeys.next();
         if (this.getWriteMethod(key) != null) {
            this.put(key, map.get(key));
         }
      }

   }

   public void clear() {
      if (this.bean != null) {
         Class beanClass = null;

         try {
            beanClass = this.bean.getClass();
            this.bean = beanClass.newInstance();
         } catch (Exception var3) {
            throw new UnsupportedOperationException("Could not create new instance of class: " + beanClass);
         }
      }
   }

   public boolean containsKey(Object name) {
      Method method = this.getReadMethod(name);
      return method != null;
   }

   public boolean containsValue(Object value) {
      return super.containsValue(value);
   }

   public Object get(Object name) {
      if (this.bean != null) {
         Method method = this.getReadMethod(name);
         if (method != null) {
            try {
               return method.invoke(this.bean, NULL_ARGUMENTS);
            } catch (IllegalAccessException var7) {
               this.logWarn(var7);
            } catch (IllegalArgumentException var8) {
               this.logWarn(var8);
            } catch (InvocationTargetException var9) {
               this.logWarn(var9);
            } catch (NullPointerException var10) {
               this.logWarn(var10);
            }
         }
      }

      return null;
   }

   public Object put(Object name, Object value) throws IllegalArgumentException, ClassCastException {
      if (this.bean != null) {
         Object oldValue = this.get(name);
         Method method = this.getWriteMethod(name);
         if (method == null) {
            throw new IllegalArgumentException("The bean of type: " + this.bean.getClass().getName() + " has no property called: " + name);
         } else {
            try {
               Object[] arguments = this.createWriteMethodArguments(method, value);
               method.invoke(this.bean, arguments);
               Object newValue = this.get(name);
               this.firePropertyChange(name, oldValue, newValue);
               return oldValue;
            } catch (InvocationTargetException var7) {
               this.logInfo(var7);
               throw new IllegalArgumentException(var7.getMessage());
            } catch (IllegalAccessException var8) {
               this.logInfo(var8);
               throw new IllegalArgumentException(var8.getMessage());
            }
         }
      } else {
         return null;
      }
   }

   public int size() {
      return this.readMethods.size();
   }

   public Set keySet() {
      return UnmodifiableSet.decorate(this.readMethods.keySet());
   }

   public Set entrySet() {
      return UnmodifiableSet.decorate(new AbstractSet() {
         public Iterator iterator() {
            return BeanMap.this.entryIterator();
         }

         public int size() {
            return BeanMap.this.readMethods.size();
         }
      });
   }

   public Collection values() {
      ArrayList answer = new ArrayList(this.readMethods.size());
      Iterator iter = this.valueIterator();

      while(iter.hasNext()) {
         answer.add(iter.next());
      }

      return UnmodifiableList.decorate(answer);
   }

   public Class getType(String name) {
      return (Class)this.types.get(name);
   }

   public Iterator keyIterator() {
      return this.readMethods.keySet().iterator();
   }

   public Iterator valueIterator() {
      final Iterator iter = this.keyIterator();
      return new Iterator() {
         public boolean hasNext() {
            return iter.hasNext();
         }

         public Object next() {
            Object key = iter.next();
            return BeanMap.this.get(key);
         }

         public void remove() {
            throw new UnsupportedOperationException("remove() not supported for BeanMap");
         }
      };
   }

   public Iterator entryIterator() {
      final Iterator iter = this.keyIterator();
      return new Iterator() {
         public boolean hasNext() {
            return iter.hasNext();
         }

         public Object next() {
            Object key = iter.next();
            Object value = BeanMap.this.get(key);
            return new BeanMap.MyMapEntry(BeanMap.this, key, value);
         }

         public void remove() {
            throw new UnsupportedOperationException("remove() not supported for BeanMap");
         }
      };
   }

   public Object getBean() {
      return this.bean;
   }

   public void setBean(Object newBean) {
      this.bean = newBean;
      this.reinitialise();
   }

   public Method getReadMethod(String name) {
      return (Method)this.readMethods.get(name);
   }

   public Method getWriteMethod(String name) {
      return (Method)this.writeMethods.get(name);
   }

   protected Method getReadMethod(Object name) {
      return (Method)this.readMethods.get(name);
   }

   protected Method getWriteMethod(Object name) {
      return (Method)this.writeMethods.get(name);
   }

   protected void reinitialise() {
      this.readMethods.clear();
      this.writeMethods.clear();
      this.types.clear();
      this.initialise();
   }

   private void initialise() {
      if (this.getBean() != null) {
         Class beanClass = this.getBean().getClass();

         try {
            BeanInfo beanInfo = Introspector.getBeanInfo(beanClass);
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            if (propertyDescriptors != null) {
               for(int i = 0; i < propertyDescriptors.length; ++i) {
                  PropertyDescriptor propertyDescriptor = propertyDescriptors[i];
                  if (propertyDescriptor != null) {
                     String name = propertyDescriptor.getName();
                     Method readMethod = propertyDescriptor.getReadMethod();
                     Method writeMethod = propertyDescriptor.getWriteMethod();
                     Class aType = propertyDescriptor.getPropertyType();
                     if (readMethod != null) {
                        this.readMethods.put(name, readMethod);
                     }

                     if (writeMethod != null) {
                        this.writeMethods.put(name, writeMethod);
                     }

                     this.types.put(name, aType);
                  }
               }
            }
         } catch (IntrospectionException var10) {
            this.logWarn(var10);
         }

      }
   }

   protected void firePropertyChange(Object key, Object oldValue, Object newValue) {
   }

   protected Object[] createWriteMethodArguments(Method method, Object value) throws IllegalAccessException, ClassCastException {
      try {
         if (value != null) {
            Class[] types = method.getParameterTypes();
            if (types != null && types.length > 0) {
               Class paramType = types[0];
               if (!paramType.isAssignableFrom(value.getClass())) {
                  value = this.convertType(paramType, value);
               }
            }
         }

         Object[] answer = new Object[]{value};
         return answer;
      } catch (InvocationTargetException var5) {
         this.logInfo(var5);
         throw new IllegalArgumentException(var5.getMessage());
      } catch (InstantiationException var6) {
         this.logInfo(var6);
         throw new IllegalArgumentException(var6.getMessage());
      }
   }

   protected Object convertType(Class newType, Object value) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
      Class[] types = new Class[]{value.getClass()};

      try {
         Constructor constructor = newType.getConstructor(types);
         Object[] arguments = new Object[]{value};
         return constructor.newInstance(arguments);
      } catch (NoSuchMethodException var6) {
         Transformer transformer = this.getTypeTransformer(newType);
         return transformer != null ? transformer.transform(value) : value;
      }
   }

   protected Transformer getTypeTransformer(Class aType) {
      return (Transformer)defaultTransformers.get(aType);
   }

   protected void logInfo(Exception ex) {
      System.out.println("INFO: Exception: " + ex);
   }

   protected void logWarn(Exception ex) {
      System.out.println("WARN: Exception: " + ex);
      ex.printStackTrace();
   }

   static {
      defaultTransformers.put(Boolean.TYPE, new Transformer() {
         public Object transform(Object input) {
            return Boolean.valueOf(input.toString());
         }
      });
      defaultTransformers.put(Character.TYPE, new Transformer() {
         public Object transform(Object input) {
            return new Character(input.toString().charAt(0));
         }
      });
      defaultTransformers.put(Byte.TYPE, new Transformer() {
         public Object transform(Object input) {
            return Byte.valueOf(input.toString());
         }
      });
      defaultTransformers.put(Short.TYPE, new Transformer() {
         public Object transform(Object input) {
            return Short.valueOf(input.toString());
         }
      });
      defaultTransformers.put(Integer.TYPE, new Transformer() {
         public Object transform(Object input) {
            return Integer.valueOf(input.toString());
         }
      });
      defaultTransformers.put(Long.TYPE, new Transformer() {
         public Object transform(Object input) {
            return Long.valueOf(input.toString());
         }
      });
      defaultTransformers.put(Float.TYPE, new Transformer() {
         public Object transform(Object input) {
            return Float.valueOf(input.toString());
         }
      });
      defaultTransformers.put(Double.TYPE, new Transformer() {
         public Object transform(Object input) {
            return Double.valueOf(input.toString());
         }
      });
   }

   protected static class MyMapEntry extends AbstractMapEntry {
      private BeanMap owner;

      protected MyMapEntry(BeanMap owner, Object key, Object value) {
         super(key, value);
         this.owner = owner;
      }

      public Object setValue(Object value) {
         Object key = this.getKey();
         Object oldValue = this.owner.get(key);
         this.owner.put(key, value);
         Object newValue = this.owner.get(key);
         super.setValue(newValue);
         return oldValue;
      }
   }
}
