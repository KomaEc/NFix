package org.codehaus.groovy.runtime;

import groovy.lang.GroovyRuntimeException;
import java.util.Comparator;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class NumberAwareComparator<T> implements Comparator<T> {
   public int compare(T o1, T o2) {
      try {
         return DefaultTypeTransformation.compareTo(o1, o2);
      } catch (ClassCastException var5) {
      } catch (GroovyRuntimeException var6) {
      }

      int x1 = o1.hashCode();
      int x2 = o2.hashCode();
      if (x1 == x2) {
         return 0;
      } else {
         return x1 < x2 ? -1 : 1;
      }
   }
}
