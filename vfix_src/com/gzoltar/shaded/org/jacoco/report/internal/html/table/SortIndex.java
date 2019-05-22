package com.gzoltar.shaded.org.jacoco.report.internal.html.table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

final class SortIndex<T> {
   private final Comparator<? super T> comparator;
   private final List<SortIndex<T>.Entry> list = new ArrayList();
   private int[] positions;

   public SortIndex(Comparator<? super T> comparator) {
      this.comparator = comparator;
   }

   public void init(List<? extends T> items) {
      this.list.clear();
      int idx = 0;
      Iterator i$ = items.iterator();

      SortIndex.Entry e;
      while(i$.hasNext()) {
         T i = i$.next();
         e = new SortIndex.Entry(idx++, i);
         this.list.add(e);
      }

      Collections.sort(this.list);
      if (this.positions == null || this.positions.length < items.size()) {
         this.positions = new int[items.size()];
      }

      int pos = 0;

      for(Iterator i$ = this.list.iterator(); i$.hasNext(); this.positions[e.idx] = pos++) {
         e = (SortIndex.Entry)i$.next();
      }

   }

   public int getPosition(int idx) {
      return this.positions[idx];
   }

   private class Entry implements Comparable<SortIndex<T>.Entry> {
      final int idx;
      final T item;

      Entry(int idx, T item) {
         this.idx = idx;
         this.item = item;
      }

      public int compareTo(SortIndex<T>.Entry o) {
         return SortIndex.this.comparator.compare(this.item, o.item);
      }
   }
}
