package com.google.common.collect;

import com.google.common.base.Function;

class Range$UpperBoundFn implements Function<Range, Cut> {
   static final Range$UpperBoundFn INSTANCE = new Range$UpperBoundFn();

   public Cut apply(Range range) {
      return range.upperBound;
   }
}
