package com.google.common.collect;

import com.google.common.base.Function;

class Range$LowerBoundFn implements Function<Range, Cut> {
   static final Range$LowerBoundFn INSTANCE = new Range$LowerBoundFn();

   public Cut apply(Range range) {
      return range.lowerBound;
   }
}
