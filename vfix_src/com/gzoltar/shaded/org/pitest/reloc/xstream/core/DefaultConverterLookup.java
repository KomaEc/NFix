package com.gzoltar.shaded.org.pitest.reloc.xstream.core;

import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.ConversionException;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.Converter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.ConverterLookup;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.ConverterRegistry;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.util.PrioritizedList;
import com.gzoltar.shaded.org.pitest.reloc.xstream.mapper.Mapper;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;

public class DefaultConverterLookup implements ConverterLookup, ConverterRegistry, Caching {
   private final PrioritizedList converters = new PrioritizedList();
   private transient Map typeToConverterMap;

   public DefaultConverterLookup() {
      this.readResolve();
   }

   /** @deprecated */
   public DefaultConverterLookup(Mapper mapper) {
   }

   public Converter lookupConverterForType(Class type) {
      Converter cachedConverter = (Converter)this.typeToConverterMap.get(type);
      if (cachedConverter != null) {
         return cachedConverter;
      } else {
         Iterator iterator = this.converters.iterator();

         Converter converter;
         do {
            if (!iterator.hasNext()) {
               throw new ConversionException("No converter specified for " + type);
            }

            converter = (Converter)iterator.next();
         } while(!converter.canConvert(type));

         this.typeToConverterMap.put(type, converter);
         return converter;
      }
   }

   public void registerConverter(Converter converter, int priority) {
      this.converters.add(converter, priority);
      Iterator iter = this.typeToConverterMap.keySet().iterator();

      while(iter.hasNext()) {
         Class type = (Class)iter.next();
         if (converter.canConvert(type)) {
            iter.remove();
         }
      }

   }

   public void flushCache() {
      this.typeToConverterMap.clear();
      Iterator iterator = this.converters.iterator();

      while(iterator.hasNext()) {
         Converter converter = (Converter)iterator.next();
         if (converter instanceof Caching) {
            ((Caching)converter).flushCache();
         }
      }

   }

   private Object readResolve() {
      this.typeToConverterMap = Collections.synchronizedMap(new WeakHashMap());
      return this;
   }
}
