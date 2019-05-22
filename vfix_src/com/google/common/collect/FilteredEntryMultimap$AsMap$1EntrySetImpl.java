package com.google.common.collect;

import com.google.common.base.Predicates;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

class FilteredEntryMultimap$AsMap$1EntrySetImpl extends Maps.EntrySet<K, Collection<V>> {
   // $FF: synthetic field
   final FilteredEntryMultimap.AsMap this$1;

   FilteredEntryMultimap$AsMap$1EntrySetImpl(FilteredEntryMultimap.AsMap this$1) {
      this.this$1 = this$1;
   }

   Map<K, Collection<V>> map() {
      return this.this$1;
   }

   public Iterator<Entry<K, Collection<V>>> iterator() {
      return new FilteredEntryMultimap$AsMap$1EntrySetImpl$1(this);
   }

   public boolean removeAll(Collection<?> c) {
      return this.this$1.this$0.removeEntriesIf(Predicates.in(c));
   }

   public boolean retainAll(Collection<?> c) {
      return this.this$1.this$0.removeEntriesIf(Predicates.not(Predicates.in(c)));
   }

   public int size() {
      return Iterators.size(this.iterator());
   }
}
