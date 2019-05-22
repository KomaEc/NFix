package com.google.common.collect;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;

class FilteredEntryMultimap$AsMap$1EntrySetImpl$1 extends AbstractIterator<Entry<K, Collection<V>>> {
   final Iterator<Entry<K, Collection<V>>> backingIterator;
   // $FF: synthetic field
   final FilteredEntryMultimap$AsMap$1EntrySetImpl this$2;

   FilteredEntryMultimap$AsMap$1EntrySetImpl$1(FilteredEntryMultimap$AsMap$1EntrySetImpl this$2) {
      this.this$2 = this$2;
      this.backingIterator = this.this$2.this$1.this$0.unfiltered.asMap().entrySet().iterator();
   }

   protected Entry<K, Collection<V>> computeNext() {
      while(true) {
         if (this.backingIterator.hasNext()) {
            Entry<K, Collection<V>> entry = (Entry)this.backingIterator.next();
            K key = entry.getKey();
            Collection<V> collection = FilteredEntryMultimap.filterCollection((Collection)entry.getValue(), this.this$2.this$1.this$0.new ValuePredicate(key));
            if (collection.isEmpty()) {
               continue;
            }

            return Maps.immutableEntry(key, collection);
         }

         return (Entry)this.endOfData();
      }
   }
}
