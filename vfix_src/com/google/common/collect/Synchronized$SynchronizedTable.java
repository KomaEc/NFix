package com.google.common.collect;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

final class Synchronized$SynchronizedTable<R, C, V> extends Synchronized.SynchronizedObject implements Table<R, C, V> {
   Synchronized$SynchronizedTable(Table<R, C, V> delegate, Object mutex) {
      super(delegate, mutex);
   }

   Table<R, C, V> delegate() {
      return (Table)super.delegate();
   }

   public boolean contains(@Nullable Object rowKey, @Nullable Object columnKey) {
      synchronized(this.mutex) {
         return this.delegate().contains(rowKey, columnKey);
      }
   }

   public boolean containsRow(@Nullable Object rowKey) {
      synchronized(this.mutex) {
         return this.delegate().containsRow(rowKey);
      }
   }

   public boolean containsColumn(@Nullable Object columnKey) {
      synchronized(this.mutex) {
         return this.delegate().containsColumn(columnKey);
      }
   }

   public boolean containsValue(@Nullable Object value) {
      synchronized(this.mutex) {
         return this.delegate().containsValue(value);
      }
   }

   public V get(@Nullable Object rowKey, @Nullable Object columnKey) {
      synchronized(this.mutex) {
         return this.delegate().get(rowKey, columnKey);
      }
   }

   public boolean isEmpty() {
      synchronized(this.mutex) {
         return this.delegate().isEmpty();
      }
   }

   public int size() {
      synchronized(this.mutex) {
         return this.delegate().size();
      }
   }

   public void clear() {
      synchronized(this.mutex) {
         this.delegate().clear();
      }
   }

   public V put(@Nullable R rowKey, @Nullable C columnKey, @Nullable V value) {
      synchronized(this.mutex) {
         return this.delegate().put(rowKey, columnKey, value);
      }
   }

   public void putAll(Table<? extends R, ? extends C, ? extends V> table) {
      synchronized(this.mutex) {
         this.delegate().putAll(table);
      }
   }

   public V remove(@Nullable Object rowKey, @Nullable Object columnKey) {
      synchronized(this.mutex) {
         return this.delegate().remove(rowKey, columnKey);
      }
   }

   public Map<C, V> row(@Nullable R rowKey) {
      synchronized(this.mutex) {
         return Synchronized.map(this.delegate().row(rowKey), this.mutex);
      }
   }

   public Map<R, V> column(@Nullable C columnKey) {
      synchronized(this.mutex) {
         return Synchronized.map(this.delegate().column(columnKey), this.mutex);
      }
   }

   public Set<Table.Cell<R, C, V>> cellSet() {
      synchronized(this.mutex) {
         return Synchronized.set(this.delegate().cellSet(), this.mutex);
      }
   }

   public Set<R> rowKeySet() {
      synchronized(this.mutex) {
         return Synchronized.set(this.delegate().rowKeySet(), this.mutex);
      }
   }

   public Set<C> columnKeySet() {
      synchronized(this.mutex) {
         return Synchronized.set(this.delegate().columnKeySet(), this.mutex);
      }
   }

   public Collection<V> values() {
      synchronized(this.mutex) {
         return Synchronized.access$500(this.delegate().values(), this.mutex);
      }
   }

   public Map<R, Map<C, V>> rowMap() {
      synchronized(this.mutex) {
         return Synchronized.map(Maps.transformValues((Map)this.delegate().rowMap(), new Synchronized$SynchronizedTable$1(this)), this.mutex);
      }
   }

   public Map<C, Map<R, V>> columnMap() {
      synchronized(this.mutex) {
         return Synchronized.map(Maps.transformValues((Map)this.delegate().columnMap(), new Synchronized$SynchronizedTable$2(this)), this.mutex);
      }
   }

   public int hashCode() {
      synchronized(this.mutex) {
         return this.delegate().hashCode();
      }
   }

   public boolean equals(@Nullable Object obj) {
      if (this == obj) {
         return true;
      } else {
         synchronized(this.mutex) {
            return this.delegate().equals(obj);
         }
      }
   }
}
