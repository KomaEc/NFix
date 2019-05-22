package com.google.common.collect;

import java.util.Iterator;
import java.util.Map.Entry;

class TreeRangeMap$SubRangeMap$1 extends TreeRangeMap<K, V>.SubRangeMap.SubRangeMapAsMap {
   // $FF: synthetic field
   final TreeRangeMap.SubRangeMap this$1;

   TreeRangeMap$SubRangeMap$1(TreeRangeMap.SubRangeMap this$1) {
      super();
      this.this$1 = this$1;
   }

   Iterator<Entry<Range<K>, V>> entryIterator() {
      if (TreeRangeMap.SubRangeMap.access$300(this.this$1).isEmpty()) {
         return Iterators.emptyIterator();
      } else {
         Iterator<TreeRangeMap.RangeMapEntry<K, V>> backingItr = TreeRangeMap.access$000(this.this$1.this$0).headMap(TreeRangeMap.SubRangeMap.access$300(this.this$1).upperBound, false).descendingMap().values().iterator();
         return new TreeRangeMap$SubRangeMap$1$1(this, backingItr);
      }
   }
}
