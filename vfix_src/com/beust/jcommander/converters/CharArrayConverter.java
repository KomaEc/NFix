package com.beust.jcommander.converters;

import com.beust.jcommander.IStringConverter;

public class CharArrayConverter implements IStringConverter<char[]> {
   public char[] convert(String var1) {
      return var1.toCharArray();
   }
}
