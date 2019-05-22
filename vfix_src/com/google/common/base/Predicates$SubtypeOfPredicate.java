package com.google.common.base;

import com.google.common.annotations.GwtIncompatible;
import java.io.Serializable;
import javax.annotation.Nullable;

@GwtIncompatible
class Predicates$SubtypeOfPredicate implements Predicate<Class<?>>, Serializable {
   private final Class<?> clazz;
   private static final long serialVersionUID = 0L;

   private Predicates$SubtypeOfPredicate(Class<?> clazz) {
      this.clazz = (Class)Preconditions.checkNotNull(clazz);
   }

   public boolean apply(Class<?> input) {
      return this.clazz.isAssignableFrom(input);
   }

   public int hashCode() {
      return this.clazz.hashCode();
   }

   public boolean equals(@Nullable Object obj) {
      if (obj instanceof Predicates$SubtypeOfPredicate) {
         Predicates$SubtypeOfPredicate that = (Predicates$SubtypeOfPredicate)obj;
         return this.clazz == that.clazz;
      } else {
         return false;
      }
   }

   public String toString() {
      return "Predicates.subtypeOf(" + this.clazz.getName() + ")";
   }

   // $FF: synthetic method
   Predicates$SubtypeOfPredicate(Class x0, Object x1) {
      this(x0);
   }
}
