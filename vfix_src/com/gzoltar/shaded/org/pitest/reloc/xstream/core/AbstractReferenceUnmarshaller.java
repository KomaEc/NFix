package com.gzoltar.shaded.org.pitest.reloc.xstream.core;

import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.ConversionException;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.Converter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.ConverterLookup;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.util.FastStack;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamReader;
import com.gzoltar.shaded.org.pitest.reloc.xstream.mapper.Mapper;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractReferenceUnmarshaller extends TreeUnmarshaller {
   private static final Object NULL = new Object();
   private Map values = new HashMap();
   private FastStack parentStack = new FastStack(16);

   public AbstractReferenceUnmarshaller(Object root, HierarchicalStreamReader reader, ConverterLookup converterLookup, Mapper mapper) {
      super(root, reader, converterLookup, mapper);
   }

   protected Object convert(Object parent, Class type, Converter converter) {
      Object result;
      if (this.parentStack.size() > 0) {
         result = this.parentStack.peek();
         if (result != null && !this.values.containsKey(result)) {
            this.values.put(result, parent);
         }
      }

      String attributeName = this.getMapper().aliasForSystemAttribute("reference");
      String reference = attributeName == null ? null : this.reader.getAttribute(attributeName);
      Object cache;
      if (reference != null) {
         cache = this.values.get(this.getReferenceKey(reference));
         if (cache == null) {
            ConversionException ex = new ConversionException("Invalid reference");
            ex.add("reference", reference);
            throw ex;
         }

         result = cache == NULL ? null : cache;
      } else {
         cache = this.getCurrentReferenceKey();
         this.parentStack.push(cache);
         result = super.convert(parent, type, converter);
         if (cache != null) {
            this.values.put(cache, result == null ? NULL : result);
         }

         this.parentStack.popSilently();
      }

      return result;
   }

   protected abstract Object getReferenceKey(String var1);

   protected abstract Object getCurrentReferenceKey();
}
