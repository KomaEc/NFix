package groovy.util;

import groovy.lang.Closure;
import java.util.Comparator;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public class ClosureComparator<T> implements Comparator<T> {
   Closure closure;

   public ClosureComparator(Closure closure) {
      this.closure = closure;
   }

   public int compare(T object1, T object2) {
      Object value = this.closure.call(new Object[]{object1, object2});
      return DefaultTypeTransformation.intUnbox(value);
   }
}
