package com.gzoltar.shaded.org.pitest.reloc.xstream.converters.javabean;

import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.reflection.MissingFieldException;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.reflection.ObjectAccessException;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.Caching;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.util.OrderRetainingMap;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class PropertyDictionary implements Caching {
   private transient Map propertyNameCache;
   private final PropertySorter sorter;

   public PropertyDictionary() {
      this(new NativePropertySorter());
   }

   public PropertyDictionary(PropertySorter sorter) {
      this.propertyNameCache = Collections.synchronizedMap(new HashMap());
      this.sorter = sorter;
   }

   /** @deprecated */
   public Iterator serializablePropertiesFor(Class type) {
      Collection beanProperties = new ArrayList();
      Collection descriptors = this.buildMap(type).values();
      Iterator iter = descriptors.iterator();

      while(iter.hasNext()) {
         PropertyDescriptor descriptor = (PropertyDescriptor)iter.next();
         if (descriptor.getReadMethod() != null && descriptor.getWriteMethod() != null) {
            beanProperties.add(new BeanProperty(type, descriptor.getName(), descriptor.getPropertyType()));
         }
      }

      return beanProperties.iterator();
   }

   /** @deprecated */
   public BeanProperty property(Class cls, String name) {
      BeanProperty beanProperty = null;
      PropertyDescriptor descriptor = (PropertyDescriptor)this.buildMap(cls).get(name);
      if (descriptor == null) {
         throw new MissingFieldException(cls.getName(), name);
      } else {
         if (descriptor.getReadMethod() != null && descriptor.getWriteMethod() != null) {
            beanProperty = new BeanProperty(cls, descriptor.getName(), descriptor.getPropertyType());
         }

         return beanProperty;
      }
   }

   public Iterator propertiesFor(Class type) {
      return this.buildMap(type).values().iterator();
   }

   public PropertyDescriptor propertyDescriptor(Class type, String name) {
      PropertyDescriptor descriptor = (PropertyDescriptor)this.buildMap(type).get(name);
      if (descriptor == null) {
         throw new MissingFieldException(type.getName(), name);
      } else {
         return descriptor;
      }
   }

   private Map buildMap(Class type) {
      Map nameMap = (Map)this.propertyNameCache.get(type);
      if (nameMap == null) {
         BeanInfo beanInfo;
         try {
            beanInfo = Introspector.getBeanInfo(type, Object.class);
         } catch (IntrospectionException var7) {
            throw new ObjectAccessException("Cannot get BeanInfo of type " + type.getName(), var7);
         }

         Map nameMap = new OrderRetainingMap();
         PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

         for(int i = 0; i < propertyDescriptors.length; ++i) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            nameMap.put(descriptor.getName(), descriptor);
         }

         nameMap = this.sorter.sort(type, nameMap);
         this.propertyNameCache.put(type, nameMap);
      }

      return nameMap;
   }

   public void flushCache() {
      this.propertyNameCache.clear();
   }
}
