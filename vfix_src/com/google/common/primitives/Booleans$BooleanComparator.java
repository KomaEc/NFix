package com.google.common.primitives;

import java.util.Comparator;

enum Booleans$BooleanComparator implements Comparator<Boolean> {
   TRUE_FIRST(1, "Booleans.trueFirst()"),
   FALSE_FIRST(-1, "Booleans.falseFirst()");

   private final int trueValue;
   private final String toString;

   private Booleans$BooleanComparator(int trueValue, String toString) {
      this.trueValue = trueValue;
      this.toString = toString;
   }

   public int compare(Boolean a, Boolean b) {
      int aVal = a ? this.trueValue : 0;
      int bVal = b ? this.trueValue : 0;
      return bVal - aVal;
   }

   public String toString() {
      return this.toString;
   }
}
