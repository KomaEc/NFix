package com.google.common.collect;

import com.google.common.base.Predicates;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;
import javax.annotation.Nullable;

class FilteredEntryMultimap$AsMap$1ValuesImpl extends Maps.Values<K, Collection<V>> {
   // $FF: synthetic field
   final FilteredEntryMultimap.AsMap this$1;

   FilteredEntryMultimap$AsMap$1ValuesImpl(FilteredEntryMultimap.AsMap this$1) {
      super(this$1);
      this.this$1 = this$1;
   }

   public boolean remove(@Nullable Object o) {
      if (o instanceof Collection) {
         Collection<?> c = (Collection)o;
         Iterator entryIterator = this.this$1.this$0.unfiltered.asMap().entrySet().iterator();

         while(entryIterator.hasNext()) {
            Entry<K, Collection<V>> entry = (Entry)entryIterator.next();
            K key = entry.getKey();
            Collection<V> collection = FilteredEntryMultimap.filterCollection((Collection)entry.getValue(), this.this$1.this$0.new ValuePredicate(key));
            if (!collection.isEmpty() && c.equals(collection)) {
               if (collection.size() == ((Collection)entry.getValue()).size()) {
                  entryIterator.remove();
               } else {
                  collection.clear();
               }

               return true;
            }
         }
      }

      return false;
   }

   public boolean removeAll(Collection<?> c) {
      return this.this$1.this$0.removeEntriesIf(Maps.valuePredicateOnEntries(Predicates.in(c)));
   }

   public boolean retainAll(Collection<?> c) {
      return this.this$1.this$0.removeEntriesIf(Maps.valuePredicateOnEntries(Predicates.not(Predicates.in(c))));
   }
}
