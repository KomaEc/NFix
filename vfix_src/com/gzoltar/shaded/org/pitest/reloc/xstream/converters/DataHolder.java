package com.gzoltar.shaded.org.pitest.reloc.xstream.converters;

import java.util.Iterator;

public interface DataHolder {
   Object get(Object var1);

   void put(Object var1, Object var2);

   Iterator keys();
}
