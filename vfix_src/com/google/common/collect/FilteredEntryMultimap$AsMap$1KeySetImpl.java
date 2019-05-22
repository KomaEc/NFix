package com.google.common.collect;

import com.google.common.base.Predicates;
import java.util.Collection;
import javax.annotation.Nullable;

class FilteredEntryMultimap$AsMap$1KeySetImpl extends Maps.KeySet<K, Collection<V>> {
   // $FF: synthetic field
   final FilteredEntryMultimap.AsMap this$1;

   FilteredEntryMultimap$AsMap$1KeySetImpl(FilteredEntryMultimap.AsMap this$1) {
      super(this$1);
      this.this$1 = this$1;
   }

   public boolean removeAll(Collection<?> c) {
      return this.this$1.this$0.removeEntriesIf(Maps.keyPredicateOnEntries(Predicates.in(c)));
   }

   public boolean retainAll(Collection<?> c) {
      return this.this$1.this$0.removeEntriesIf(Maps.keyPredicateOnEntries(Predicates.not(Predicates.in(c))));
   }

   public boolean remove(@Nullable Object o) {
      return this.this$1.remove(o) != null;
   }
}
