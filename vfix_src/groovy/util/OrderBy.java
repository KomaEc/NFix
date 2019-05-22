package groovy.util;

import groovy.lang.Closure;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import org.codehaus.groovy.runtime.NumberAwareComparator;

public class OrderBy<T> implements Comparator<T> {
   private final List<Closure> closures;
   private final NumberAwareComparator<Object> numberAwareComparator;

   public OrderBy() {
      this.numberAwareComparator = new NumberAwareComparator();
      this.closures = new ArrayList();
   }

   public OrderBy(Closure closure) {
      this();
      this.closures.add(closure);
   }

   public OrderBy(List<Closure> closures) {
      this.numberAwareComparator = new NumberAwareComparator();
      this.closures = closures;
   }

   public void add(Closure closure) {
      this.closures.add(closure);
   }

   public int compare(T object1, T object2) {
      Iterator i$ = this.closures.iterator();

      int result;
      do {
         if (!i$.hasNext()) {
            return 0;
         }

         Closure closure = (Closure)i$.next();
         Object value1 = closure.call(object1);
         Object value2 = closure.call(object2);
         result = this.numberAwareComparator.compare(value1, value2);
      } while(result == 0);

      return result;
   }
}
