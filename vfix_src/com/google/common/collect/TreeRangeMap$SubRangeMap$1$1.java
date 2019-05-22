package com.google.common.collect;

import java.util.Iterator;
import java.util.Map.Entry;

class TreeRangeMap$SubRangeMap$1$1 extends AbstractIterator<Entry<Range<K>, V>> {
   // $FF: synthetic field
   final Iterator val$backingItr;
   // $FF: synthetic field
   final TreeRangeMap$SubRangeMap$1 this$2;

   TreeRangeMap$SubRangeMap$1$1(TreeRangeMap$SubRangeMap$1 this$2, Iterator var2) {
      this.this$2 = this$2;
      this.val$backingItr = var2;
   }

   protected Entry<Range<K>, V> computeNext() {
      if (this.val$backingItr.hasNext()) {
         TreeRangeMap.RangeMapEntry<K, V> entry = (TreeRangeMap.RangeMapEntry)this.val$backingItr.next();
         return entry.getUpperBound().compareTo(TreeRangeMap.SubRangeMap.access$300(this.this$2.this$1).lowerBound) <= 0 ? (Entry)this.endOfData() : Maps.immutableEntry(entry.getKey().intersection(TreeRangeMap.SubRangeMap.access$300(this.this$2.this$1)), entry.getValue());
      } else {
         return (Entry)this.endOfData();
      }
   }
}
