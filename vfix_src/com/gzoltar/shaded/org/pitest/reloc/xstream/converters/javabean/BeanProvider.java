package com.gzoltar.shaded.org.pitest.reloc.xstream.converters.javabean;

import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.reflection.ObjectAccessException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class BeanProvider implements JavaBeanProvider {
   /** @deprecated */
   protected static final Object[] NO_PARAMS = new Object[0];
   protected PropertyDictionary propertyDictionary;

   public BeanProvider() {
      this(new PropertyDictionary(new NativePropertySorter()));
   }

   public BeanProvider(Comparator propertyNameComparator) {
      this(new PropertyDictionary(new ComparingPropertySorter(propertyNameComparator)));
   }

   public BeanProvider(PropertyDictionary propertyDictionary) {
      this.propertyDictionary = propertyDictionary;
   }

   public Object newInstance(Class type) {
      try {
         return type.newInstance();
      } catch (InstantiationException var3) {
         throw new ObjectAccessException("Cannot construct " + type.getName(), var3);
      } catch (IllegalAccessException var4) {
         throw new ObjectAccessException("Cannot construct " + type.getName(), var4);
      } catch (SecurityException var5) {
         throw new ObjectAccessException("Cannot construct " + type.getName(), var5);
      } catch (ExceptionInInitializerError var6) {
         throw new ObjectAccessException("Cannot construct " + type.getName(), var6);
      }
   }

   public void visitSerializableProperties(Object object, JavaBeanProvider.Visitor visitor) {
      PropertyDescriptor[] propertyDescriptors = this.getSerializableProperties(object);

      for(int i = 0; i < propertyDescriptors.length; ++i) {
         PropertyDescriptor property = propertyDescriptors[i];

         try {
            Method readMethod = property.getReadMethod();
            String name = property.getName();
            Class definedIn = readMethod.getDeclaringClass();
            if (visitor.shouldVisit(name, definedIn)) {
               Object value = readMethod.invoke(object);
               visitor.visit(name, property.getPropertyType(), definedIn, value);
            }
         } catch (IllegalArgumentException var10) {
            throw new ObjectAccessException("Could not get property " + object.getClass() + "." + property.getName(), var10);
         } catch (IllegalAccessException var11) {
            throw new ObjectAccessException("Could not get property " + object.getClass() + "." + property.getName(), var11);
         } catch (InvocationTargetException var12) {
            throw new ObjectAccessException("Could not get property " + object.getClass() + "." + property.getName(), var12);
         }
      }

   }

   public void writeProperty(Object object, String propertyName, Object value) {
      PropertyDescriptor property = this.getProperty(propertyName, object.getClass());

      try {
         property.getWriteMethod().invoke(object, value);
      } catch (IllegalArgumentException var6) {
         throw new ObjectAccessException("Could not set property " + object.getClass() + "." + property.getName(), var6);
      } catch (IllegalAccessException var7) {
         throw new ObjectAccessException("Could not set property " + object.getClass() + "." + property.getName(), var7);
      } catch (InvocationTargetException var8) {
         throw new ObjectAccessException("Could not set property " + object.getClass() + "." + property.getName(), var8);
      }
   }

   public Class getPropertyType(Object object, String name) {
      return this.getProperty(name, object.getClass()).getPropertyType();
   }

   public boolean propertyDefinedInClass(String name, Class type) {
      return this.getProperty(name, type) != null;
   }

   public boolean canInstantiate(Class type) {
      try {
         return this.newInstance(type) != null;
      } catch (ObjectAccessException var3) {
         return false;
      }
   }

   /** @deprecated */
   protected Constructor getDefaultConstrutor(Class type) {
      Constructor[] constructors = type.getConstructors();

      for(int i = 0; i < constructors.length; ++i) {
         Constructor c = constructors[i];
         if (c.getParameterTypes().length == 0 && Modifier.isPublic(c.getModifiers())) {
            return c;
         }
      }

      return null;
   }

   protected PropertyDescriptor[] getSerializableProperties(Object object) {
      List result = new ArrayList();
      Iterator iter = this.propertyDictionary.propertiesFor(object.getClass());

      while(iter.hasNext()) {
         PropertyDescriptor descriptor = (PropertyDescriptor)iter.next();
         if (this.canStreamProperty(descriptor)) {
            result.add(descriptor);
         }
      }

      return (PropertyDescriptor[])((PropertyDescriptor[])result.toArray(new PropertyDescriptor[result.size()]));
   }

   protected boolean canStreamProperty(PropertyDescriptor descriptor) {
      return descriptor.getReadMethod() != null && descriptor.getWriteMethod() != null;
   }

   public boolean propertyWriteable(String name, Class type) {
      PropertyDescriptor property = this.getProperty(name, type);
      return property.getWriteMethod() != null;
   }

   protected PropertyDescriptor getProperty(String name, Class type) {
      return this.propertyDictionary.propertyDescriptor(type, name);
   }

   /** @deprecated */
   public interface Visitor extends JavaBeanProvider.Visitor {
   }
}
