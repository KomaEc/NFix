package com.google.common.collect;

import java.io.Serializable;

class Range$RangeLexOrdering extends Ordering<Range<?>> implements Serializable {
   static final Ordering<Range<?>> INSTANCE = new Range$RangeLexOrdering();
   private static final long serialVersionUID = 0L;

   private Range$RangeLexOrdering() {
   }

   public int compare(Range<?> left, Range<?> right) {
      return ComparisonChain.start().compare(left.lowerBound, right.lowerBound).compare(left.upperBound, right.upperBound).result();
   }
}
