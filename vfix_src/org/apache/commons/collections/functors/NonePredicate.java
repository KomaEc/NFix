package org.apache.commons.collections.functors;

import java.io.Serializable;
import java.util.Collection;
import org.apache.commons.collections.Predicate;

public final class NonePredicate implements Predicate, PredicateDecorator, Serializable {
   private static final long serialVersionUID = 2007613066565892961L;
   private final Predicate[] iPredicates;

   public static Predicate getInstance(Predicate[] predicates) {
      FunctorUtils.validate(predicates);
      if (predicates.length == 0) {
         return TruePredicate.INSTANCE;
      } else {
         predicates = FunctorUtils.copy(predicates);
         return new NonePredicate(predicates);
      }
   }

   public static Predicate getInstance(Collection predicates) {
      Predicate[] preds = FunctorUtils.validate(predicates);
      return (Predicate)(preds.length == 0 ? TruePredicate.INSTANCE : new NonePredicate(preds));
   }

   public NonePredicate(Predicate[] predicates) {
      this.iPredicates = predicates;
   }

   public boolean evaluate(Object object) {
      for(int i = 0; i < this.iPredicates.length; ++i) {
         if (this.iPredicates[i].evaluate(object)) {
            return false;
         }
      }

      return true;
   }

   public Predicate[] getPredicates() {
      return this.iPredicates;
   }
}
