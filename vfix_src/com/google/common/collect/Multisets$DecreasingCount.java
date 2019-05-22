package com.google.common.collect;

import java.util.Comparator;

final class Multisets$DecreasingCount implements Comparator<Multiset.Entry<?>> {
   static final Multisets$DecreasingCount INSTANCE = new Multisets$DecreasingCount();

   private Multisets$DecreasingCount() {
   }

   public int compare(Multiset.Entry<?> entry1, Multiset.Entry<?> entry2) {
      return entry2.getCount() - entry1.getCount();
   }
}
