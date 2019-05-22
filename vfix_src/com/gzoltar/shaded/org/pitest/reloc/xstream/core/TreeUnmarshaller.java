package com.gzoltar.shaded.org.pitest.reloc.xstream.core;

import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.ConversionException;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.Converter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.ConverterLookup;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.DataHolder;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.ErrorReporter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.ErrorWriter;
import com.gzoltar.shaded.org.pitest.reloc.xstream.converters.UnmarshallingContext;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.util.FastStack;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.util.HierarchicalStreams;
import com.gzoltar.shaded.org.pitest.reloc.xstream.core.util.PrioritizedList;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.HierarchicalStreamReader;
import com.gzoltar.shaded.org.pitest.reloc.xstream.mapper.Mapper;
import java.util.Iterator;

public class TreeUnmarshaller implements UnmarshallingContext {
   private Object root;
   protected HierarchicalStreamReader reader;
   private ConverterLookup converterLookup;
   private Mapper mapper;
   private FastStack types = new FastStack(16);
   private DataHolder dataHolder;
   private final PrioritizedList validationList = new PrioritizedList();

   public TreeUnmarshaller(Object root, HierarchicalStreamReader reader, ConverterLookup converterLookup, Mapper mapper) {
      this.root = root;
      this.reader = reader;
      this.converterLookup = converterLookup;
      this.mapper = mapper;
   }

   public Object convertAnother(Object parent, Class type) {
      return this.convertAnother(parent, type, (Converter)null);
   }

   public Object convertAnother(Object parent, Class type, Converter converter) {
      type = this.mapper.defaultImplementationOf(type);
      if (converter == null) {
         converter = this.converterLookup.lookupConverterForType(type);
      } else if (!converter.canConvert(type)) {
         ConversionException e = new ConversionException("Explicit selected converter cannot handle type");
         e.add("item-type", type.getName());
         e.add("converter-type", converter.getClass().getName());
         throw e;
      }

      return this.convert(parent, type, converter);
   }

   protected Object convert(Object parent, Class type, Converter converter) {
      try {
         this.types.push(type);
         Object result = converter.unmarshal(this.reader, this);
         this.types.popSilently();
         return result;
      } catch (ConversionException var6) {
         this.addInformationTo(var6, type, converter, parent);
         throw var6;
      } catch (RuntimeException var7) {
         ConversionException conversionException = new ConversionException(var7);
         this.addInformationTo(conversionException, type, converter, parent);
         throw conversionException;
      }
   }

   private void addInformationTo(ErrorWriter errorWriter, Class type, Converter converter, Object parent) {
      errorWriter.add("class", type.getName());
      errorWriter.add("required-type", this.getRequiredType().getName());
      errorWriter.add("converter-type", converter.getClass().getName());
      if (converter instanceof ErrorReporter) {
         ((ErrorReporter)converter).appendErrors(errorWriter);
      }

      if (parent instanceof ErrorReporter) {
         ((ErrorReporter)parent).appendErrors(errorWriter);
      }

      this.reader.appendErrors(errorWriter);
   }

   public void addCompletionCallback(Runnable work, int priority) {
      this.validationList.add(work, priority);
   }

   public Object currentObject() {
      return this.types.size() == 1 ? this.root : null;
   }

   public Class getRequiredType() {
      return (Class)this.types.peek();
   }

   public Object get(Object key) {
      this.lazilyCreateDataHolder();
      return this.dataHolder.get(key);
   }

   public void put(Object key, Object value) {
      this.lazilyCreateDataHolder();
      this.dataHolder.put(key, value);
   }

   public Iterator keys() {
      this.lazilyCreateDataHolder();
      return this.dataHolder.keys();
   }

   private void lazilyCreateDataHolder() {
      if (this.dataHolder == null) {
         this.dataHolder = new MapBackedDataHolder();
      }

   }

   public Object start(DataHolder dataHolder) {
      this.dataHolder = dataHolder;
      Class type = HierarchicalStreams.readClassType(this.reader, this.mapper);
      Object result = this.convertAnother((Object)null, type);
      Iterator validations = this.validationList.iterator();

      while(validations.hasNext()) {
         Runnable runnable = (Runnable)validations.next();
         runnable.run();
      }

      return result;
   }

   protected Mapper getMapper() {
      return this.mapper;
   }
}
