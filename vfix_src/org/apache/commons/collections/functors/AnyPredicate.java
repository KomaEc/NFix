package org.apache.commons.collections.functors;

import java.io.Serializable;
import java.util.Collection;
import org.apache.commons.collections.Predicate;

public final class AnyPredicate implements Predicate, PredicateDecorator, Serializable {
   private static final long serialVersionUID = 7429999530934647542L;
   private final Predicate[] iPredicates;

   public static Predicate getInstance(Predicate[] predicates) {
      FunctorUtils.validate(predicates);
      if (predicates.length == 0) {
         return FalsePredicate.INSTANCE;
      } else {
         return (Predicate)(predicates.length == 1 ? predicates[0] : new AnyPredicate(FunctorUtils.copy(predicates)));
      }
   }

   public static Predicate getInstance(Collection predicates) {
      Predicate[] preds = FunctorUtils.validate(predicates);
      if (preds.length == 0) {
         return FalsePredicate.INSTANCE;
      } else {
         return (Predicate)(preds.length == 1 ? preds[0] : new AnyPredicate(preds));
      }
   }

   public AnyPredicate(Predicate[] predicates) {
      this.iPredicates = predicates;
   }

   public boolean evaluate(Object object) {
      for(int i = 0; i < this.iPredicates.length; ++i) {
         if (this.iPredicates[i].evaluate(object)) {
            return true;
         }
      }

      return false;
   }

   public Predicate[] getPredicates() {
      return this.iPredicates;
   }
}
