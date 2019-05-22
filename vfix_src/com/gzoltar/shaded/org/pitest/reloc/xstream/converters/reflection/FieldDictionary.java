package com.gzoltar.shaded.org.pitest.reloc.xstream.converters.reflection;

import com.gzoltar.shaded.org.pitest.reloc.xstream.core.Caching;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.JVM;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.util.OrderRetainingMap;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class FieldDictionary implements Caching {
   private transient Map keyedByFieldNameCache;
   private transient Map keyedByFieldKeyCache;
   private final FieldKeySorter sorter;

   public FieldDictionary() {
      this(new ImmutableFieldKeySorter());
   }

   public FieldDictionary(FieldKeySorter sorter) {
      this.sorter = sorter;
      this.init();
   }

   private void init() {
      this.keyedByFieldNameCache = new HashMap();
      this.keyedByFieldKeyCache = new HashMap();
      this.keyedByFieldNameCache.put(Object.class, Collections.EMPTY_MAP);
      this.keyedByFieldKeyCache.put(Object.class, Collections.EMPTY_MAP);
   }

   /** @deprecated */
   public Iterator serializableFieldsFor(Class cls) {
      return this.fieldsFor(cls);
   }

   public Iterator fieldsFor(Class cls) {
      return this.buildMap(cls, true).values().iterator();
   }

   public Field field(Class cls, String name, Class definedIn) {
      Field field = this.fieldOrNull(cls, name, definedIn);
      if (field == null) {
         throw new MissingFieldException(cls.getName(), name);
      } else {
         return field;
      }
   }

   public Field fieldOrNull(Class cls, String name, Class definedIn) {
      Map fields = this.buildMap(cls, definedIn != null);
      Field field = (Field)fields.get(definedIn != null ? new FieldKey(name, definedIn, -1) : name);
      return field;
   }

   private Map buildMap(Class type, boolean tupleKeyed) {
      Class cls = type;
      synchronized(this) {
         if (!this.keyedByFieldNameCache.containsKey(type)) {
            ArrayList superClasses;
            for(superClasses = new ArrayList(); !Object.class.equals(cls) && cls != null; cls = cls.getSuperclass()) {
               superClasses.add(0, cls);
            }

            Map lastKeyedByFieldName = Collections.EMPTY_MAP;
            Map lastKeyedByFieldKey = Collections.EMPTY_MAP;
            Iterator iter = superClasses.iterator();

            while(true) {
               while(iter.hasNext()) {
                  cls = (Class)iter.next();
                  if (!this.keyedByFieldNameCache.containsKey(cls)) {
                     Map keyedByFieldName = new HashMap((Map)lastKeyedByFieldName);
                     Map keyedByFieldKey = new OrderRetainingMap(lastKeyedByFieldKey);
                     Field[] fields = cls.getDeclaredFields();
                     int i;
                     int idx;
                     Field field;
                     if (JVM.reverseFieldDefinition()) {
                        for(i = fields.length >> 1; i-- > 0; fields[idx] = field) {
                           idx = fields.length - i - 1;
                           field = fields[i];
                           fields[i] = fields[idx];
                        }
                     }

                     for(i = 0; i < fields.length; ++i) {
                        Field field = fields[i];
                        if (!field.isAccessible()) {
                           field.setAccessible(true);
                        }

                        FieldKey fieldKey = new FieldKey(field.getName(), field.getDeclaringClass(), i);
                        Field existent = (Field)keyedByFieldName.get(field.getName());
                        if (existent == null || (existent.getModifiers() & 8) != 0 || existent != null && (field.getModifiers() & 8) == 0) {
                           keyedByFieldName.put(field.getName(), field);
                        }

                        keyedByFieldKey.put(fieldKey, field);
                     }

                     Map sortedFieldKeys = this.sorter.sort(cls, keyedByFieldKey);
                     this.keyedByFieldNameCache.put(cls, keyedByFieldName);
                     this.keyedByFieldKeyCache.put(cls, sortedFieldKeys);
                     lastKeyedByFieldName = keyedByFieldName;
                     lastKeyedByFieldKey = sortedFieldKeys;
                  } else {
                     lastKeyedByFieldName = (Map)this.keyedByFieldNameCache.get(cls);
                     lastKeyedByFieldKey = (Map)this.keyedByFieldKeyCache.get(cls);
                  }
               }

               return (Map)(tupleKeyed ? lastKeyedByFieldKey : lastKeyedByFieldName);
            }
         }
      }

      return (Map)((Map)(tupleKeyed ? this.keyedByFieldKeyCache.get(type) : this.keyedByFieldNameCache.get(type)));
   }

   public synchronized void flushCache() {
      Set objectTypeSet = Collections.singleton(Object.class);
      this.keyedByFieldNameCache.keySet().retainAll(objectTypeSet);
      this.keyedByFieldKeyCache.keySet().retainAll(objectTypeSet);
      if (this.sorter instanceof Caching) {
         ((Caching)this.sorter).flushCache();
      }

   }

   protected Object readResolve() {
      this.init();
      return this;
   }
}
