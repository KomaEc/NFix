package com.gzoltar.shaded.org.pitest.reloc.xstream.converters.reflection;

import java.util.Map;

public class ImmutableFieldKeySorter implements FieldKeySorter {
   public Map sort(Class type, Map keyedByFieldKey) {
      return keyedByFieldKey;
   }
}
