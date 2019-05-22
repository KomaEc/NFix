package com.gzoltar.shaded.org.pitest.reloc.xstream.converters.reflection;

import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.ConversionException;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.basic.AbstractSingleValueConverter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.util.Fields;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.AttributedCharacterIterator.Attribute;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class AbstractAttributedCharacterIteratorAttributeConverter extends AbstractSingleValueConverter {
   private static final Map instanceMaps = new HashMap();
   private static final Method getName;
   private final Class type;
   private transient Map attributeMap;

   public AbstractAttributedCharacterIteratorAttributeConverter(Class type) {
      if (!Attribute.class.isAssignableFrom(type)) {
         throw new IllegalArgumentException(type.getName() + " is not a " + Attribute.class.getName());
      } else {
         this.type = type;
         this.readResolve();
      }
   }

   public boolean canConvert(Class type) {
      return type == this.type && !this.attributeMap.isEmpty();
   }

   public String toString(Object source) {
      return this.getName((Attribute)source);
   }

   private String getName(Attribute attribute) {
      Exception ex = null;
      if (getName != null) {
         try {
            return (String)getName.invoke(attribute, (Object[])null);
         } catch (IllegalAccessException var5) {
            ex = var5;
         } catch (InvocationTargetException var6) {
            ex = var6;
         }
      }

      String s = attribute.toString();
      String className = attribute.getClass().getName();
      if (s.startsWith(className)) {
         return s.substring(className.length() + 1, s.length() - 1);
      } else {
         throw new ConversionException("Cannot find name of attribute of type " + className, (Throwable)ex);
      }
   }

   public Object fromString(String str) {
      if (this.attributeMap.containsKey(str)) {
         return this.attributeMap.get(str);
      } else {
         throw new ConversionException("Cannot find attribute of type " + this.type.getName() + " with name " + str);
      }
   }

   private Object readResolve() {
      this.attributeMap = (Map)instanceMaps.get(this.type.getName());
      if (this.attributeMap == null) {
         this.attributeMap = new HashMap();
         Field instanceMap = Fields.locate(this.type, Map.class, true);
         if (instanceMap != null) {
            try {
               Map map = (Map)Fields.read(instanceMap, (Object)null);
               if (map != null) {
                  boolean valid = true;

                  Entry entry;
                  for(Iterator iter = map.entrySet().iterator(); valid && iter.hasNext(); valid = entry.getKey().getClass() == String.class && entry.getValue().getClass() == this.type) {
                     entry = (Entry)iter.next();
                  }

                  if (valid) {
                     this.attributeMap.putAll(map);
                  }
               }
            } catch (ObjectAccessException var9) {
            }
         }

         if (this.attributeMap.isEmpty()) {
            try {
               Field[] fields = this.type.getDeclaredFields();

               for(int i = 0; i < fields.length; ++i) {
                  if (fields[i].getType() == this.type == Modifier.isStatic(fields[i].getModifiers())) {
                     Attribute attribute = (Attribute)Fields.read(fields[i], (Object)null);
                     this.attributeMap.put(this.toString(attribute), attribute);
                  }
               }
            } catch (SecurityException var6) {
               this.attributeMap.clear();
            } catch (ObjectAccessException var7) {
               this.attributeMap.clear();
            } catch (NoClassDefFoundError var8) {
               this.attributeMap.clear();
            }
         }

         instanceMaps.put(this.type.getName(), this.attributeMap);
      }

      return this;
   }

   static {
      Method method = null;

      try {
         method = Attribute.class.getDeclaredMethod("getName", (Class[])null);
         if (!method.isAccessible()) {
            method.setAccessible(true);
         }
      } catch (SecurityException var2) {
      } catch (NoSuchMethodException var3) {
      }

      getName = method;
   }
}
