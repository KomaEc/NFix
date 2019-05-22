package bsh.collection;

import bsh.BshIterator;
import bsh.CollectionManager;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class CollectionManagerImpl extends CollectionManager {
   public BshIterator getBshIterator(Object var1) throws IllegalArgumentException {
      return (BshIterator)(!(var1 instanceof Collection) && !(var1 instanceof Iterator) ? new CollectionManager.BasicBshIterator(var1) : new CollectionIterator(var1));
   }

   public boolean isMap(Object var1) {
      return var1 instanceof Map ? true : super.isMap(var1);
   }

   public Object getFromMap(Object var1, Object var2) {
      return ((Map)var1).get(var2);
   }

   public Object putInMap(Object var1, Object var2, Object var3) {
      return ((Map)var1).put(var2, var3);
   }
}
