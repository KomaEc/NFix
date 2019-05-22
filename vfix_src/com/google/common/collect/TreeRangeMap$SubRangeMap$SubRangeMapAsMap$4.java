package com.google.common.collect;

import com.google.common.base.Predicates;
import java.util.Collection;
import java.util.Map;

class TreeRangeMap$SubRangeMap$SubRangeMapAsMap$4 extends Maps.Values<Range<K>, V> {
   // $FF: synthetic field
   final TreeRangeMap.SubRangeMap.SubRangeMapAsMap this$2;

   TreeRangeMap$SubRangeMap$SubRangeMapAsMap$4(TreeRangeMap.SubRangeMap.SubRangeMapAsMap this$2, Map map) {
      super(map);
      this.this$2 = this$2;
   }

   public boolean removeAll(Collection<?> c) {
      return TreeRangeMap.SubRangeMap.SubRangeMapAsMap.access$400(this.this$2, Predicates.compose(Predicates.in(c), Maps.valueFunction()));
   }

   public boolean retainAll(Collection<?> c) {
      return TreeRangeMap.SubRangeMap.SubRangeMapAsMap.access$400(this.this$2, Predicates.compose(Predicates.not(Predicates.in(c)), Maps.valueFunction()));
   }
}
