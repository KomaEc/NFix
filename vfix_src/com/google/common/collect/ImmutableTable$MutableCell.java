package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.util.function.BinaryOperator;

final class ImmutableTable$MutableCell<R, C, V> extends Tables.AbstractCell<R, C, V> {
   private final R row;
   private final C column;
   private V value;

   ImmutableTable$MutableCell(R row, C column, V value) {
      this.row = Preconditions.checkNotNull(row);
      this.column = Preconditions.checkNotNull(column);
      this.value = Preconditions.checkNotNull(value);
   }

   public R getRowKey() {
      return this.row;
   }

   public C getColumnKey() {
      return this.column;
   }

   public V getValue() {
      return this.value;
   }

   void merge(V value, BinaryOperator<V> mergeFunction) {
      Preconditions.checkNotNull(value);
      this.value = Preconditions.checkNotNull(mergeFunction.apply(this.value, value));
   }
}
