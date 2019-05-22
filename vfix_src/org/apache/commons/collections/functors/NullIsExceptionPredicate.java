package org.apache.commons.collections.functors;

import java.io.Serializable;
import org.apache.commons.collections.FunctorException;
import org.apache.commons.collections.Predicate;

public final class NullIsExceptionPredicate implements Predicate, PredicateDecorator, Serializable {
   private static final long serialVersionUID = 3243449850504576071L;
   private final Predicate iPredicate;

   public static Predicate getInstance(Predicate predicate) {
      if (predicate == null) {
         throw new IllegalArgumentException("Predicate must not be null");
      } else {
         return new NullIsExceptionPredicate(predicate);
      }
   }

   public NullIsExceptionPredicate(Predicate predicate) {
      this.iPredicate = predicate;
   }

   public boolean evaluate(Object object) {
      if (object == null) {
         throw new FunctorException("Input Object must not be null");
      } else {
         return this.iPredicate.evaluate(object);
      }
   }

   public Predicate[] getPredicates() {
      return new Predicate[]{this.iPredicate};
   }
}
