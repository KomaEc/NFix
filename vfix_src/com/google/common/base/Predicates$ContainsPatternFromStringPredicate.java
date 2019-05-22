package com.google.common.base;

import com.google.common.annotations.GwtIncompatible;
import java.util.regex.Pattern;

@GwtIncompatible("Only used by other GWT-incompatible code.")
class Predicates$ContainsPatternFromStringPredicate extends Predicates.ContainsPatternPredicate {
   private static final long serialVersionUID = 0L;

   Predicates$ContainsPatternFromStringPredicate(String string) {
      super(Pattern.compile(string));
   }

   public String toString() {
      String var1 = String.valueOf(String.valueOf(this.pattern.pattern()));
      return (new StringBuilder(28 + var1.length())).append("Predicates.containsPattern(").append(var1).append(")").toString();
   }
}
