package com.beust.jcommander.converters;

import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.internal.Lists;
import java.util.Iterator;
import java.util.List;

public class DefaultListConverter<T> implements IStringConverter<List<T>> {
   private final IParameterSplitter splitter;
   private final IStringConverter<T> converter;

   public DefaultListConverter(IParameterSplitter var1, IStringConverter<T> var2) {
      this.splitter = var1;
      this.converter = var2;
   }

   public List<T> convert(String var1) {
      List var2 = Lists.newArrayList();
      Iterator var3 = this.splitter.split(var1).iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         var2.add(this.converter.convert(var4));
      }

      return var2;
   }
}
