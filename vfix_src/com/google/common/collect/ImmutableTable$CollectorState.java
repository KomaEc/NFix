package com.google.common.collect;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.BinaryOperator;

final class ImmutableTable$CollectorState<R, C, V> {
   final List<ImmutableTable$MutableCell<R, C, V>> insertionOrder;
   final Table<R, C, ImmutableTable$MutableCell<R, C, V>> table;

   private ImmutableTable$CollectorState() {
      this.insertionOrder = new ArrayList();
      this.table = HashBasedTable.create();
   }

   void put(R row, C column, V value, BinaryOperator<V> merger) {
      ImmutableTable$MutableCell<R, C, V> oldCell = (ImmutableTable$MutableCell)this.table.get(row, column);
      if (oldCell == null) {
         ImmutableTable$MutableCell<R, C, V> cell = new ImmutableTable$MutableCell(row, column, value);
         this.insertionOrder.add(cell);
         this.table.put(row, column, cell);
      } else {
         oldCell.merge(value, merger);
      }

   }

   ImmutableTable$CollectorState<R, C, V> combine(ImmutableTable$CollectorState<R, C, V> other, BinaryOperator<V> merger) {
      Iterator var3 = other.insertionOrder.iterator();

      while(var3.hasNext()) {
         ImmutableTable$MutableCell<R, C, V> cell = (ImmutableTable$MutableCell)var3.next();
         this.put(cell.getRowKey(), cell.getColumnKey(), cell.getValue(), merger);
      }

      return this;
   }

   ImmutableTable<R, C, V> toTable() {
      return ImmutableTable.access$000(this.insertionOrder);
   }

   // $FF: synthetic method
   ImmutableTable$CollectorState(ImmutableTable$1 x0) {
      this();
   }
}
