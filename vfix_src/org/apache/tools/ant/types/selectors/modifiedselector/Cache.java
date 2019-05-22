package org.apache.tools.ant.types.selectors.modifiedselector;

import java.util.Iterator;

public interface Cache {
   boolean isValid();

   void delete();

   void load();

   void save();

   Object get(Object var1);

   void put(Object var1, Object var2);

   Iterator iterator();
}
