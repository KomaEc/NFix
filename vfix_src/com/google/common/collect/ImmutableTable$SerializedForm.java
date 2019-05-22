package com.google.common.collect;

import java.io.Serializable;

final class ImmutableTable$SerializedForm implements Serializable {
   private final Object[] rowKeys;
   private final Object[] columnKeys;
   private final Object[] cellValues;
   private final int[] cellRowIndices;
   private final int[] cellColumnIndices;
   private static final long serialVersionUID = 0L;

   private ImmutableTable$SerializedForm(Object[] rowKeys, Object[] columnKeys, Object[] cellValues, int[] cellRowIndices, int[] cellColumnIndices) {
      this.rowKeys = rowKeys;
      this.columnKeys = columnKeys;
      this.cellValues = cellValues;
      this.cellRowIndices = cellRowIndices;
      this.cellColumnIndices = cellColumnIndices;
   }

   static ImmutableTable$SerializedForm create(ImmutableTable<?, ?, ?> table, int[] cellRowIndices, int[] cellColumnIndices) {
      return new ImmutableTable$SerializedForm(table.rowKeySet().toArray(), table.columnKeySet().toArray(), table.values().toArray(), cellRowIndices, cellColumnIndices);
   }

   Object readResolve() {
      if (this.cellValues.length == 0) {
         return ImmutableTable.of();
      } else if (this.cellValues.length == 1) {
         return ImmutableTable.of(this.rowKeys[0], this.columnKeys[0], this.cellValues[0]);
      } else {
         ImmutableList.Builder<Table.Cell<Object, Object, Object>> cellListBuilder = new ImmutableList.Builder(this.cellValues.length);

         for(int i = 0; i < this.cellValues.length; ++i) {
            cellListBuilder.add((Object)ImmutableTable.cellOf(this.rowKeys[this.cellRowIndices[i]], this.columnKeys[this.cellColumnIndices[i]], this.cellValues[i]));
         }

         return RegularImmutableTable.forOrderedComponents(cellListBuilder.build(), ImmutableSet.copyOf(this.rowKeys), ImmutableSet.copyOf(this.columnKeys));
      }
   }
}
