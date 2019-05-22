package org.codehaus.groovy.util;

public abstract class AbstractConcurrentDoubleKeyMap<K1, K2, V> extends AbstractConcurrentMapBase {
   public AbstractConcurrentDoubleKeyMap(Object segmentInfo) {
      super(segmentInfo);
   }

   static <K1, K2> int hash(K1 key1, K2 key2) {
      int h = 31 * key1.hashCode() + key2.hashCode();
      h += ~(h << 9);
      h ^= h >>> 14;
      h += h << 4;
      h ^= h >>> 10;
      return h;
   }

   public V get(K1 key1, K2 key2) {
      int hash = hash(key1, key2);
      return this.segmentFor(hash).get(key1, key2, hash);
   }

   public AbstractConcurrentDoubleKeyMap.Entry<K1, K2, V> getOrPut(K1 key1, K2 key2, V value) {
      int hash = hash(key1, key2);
      return this.segmentFor(hash).getOrPut(key1, key2, hash, value);
   }

   public void put(K1 key1, K2 key2, V value) {
      int hash = hash(key1, key2);
      this.segmentFor(hash).put(key1, key2, hash).setValue(value);
   }

   public void remove(K1 key1, K2 key2) {
      int hash = hash(key1, key2);
      this.segmentFor(hash).remove(key1, key2, hash);
   }

   public final AbstractConcurrentDoubleKeyMap.Segment<K1, K2, V> segmentFor(int hash) {
      return (AbstractConcurrentDoubleKeyMap.Segment)this.segments[hash >>> this.segmentShift & this.segmentMask];
   }

   interface Entry<K1, K2, V> extends AbstractConcurrentMapBase.Entry<V> {
      boolean isEqual(K1 var1, K2 var2, int var3);
   }

   abstract static class Segment<K1, K2, V> extends AbstractConcurrentMapBase.Segment {
      Segment(int initialCapacity) {
         super(initialCapacity);
      }

      V get(K1 key1, K2 key2, int hash) {
         Object[] tab = this.table;
         Object o = tab[hash & tab.length - 1];
         if (o != null) {
            if (o instanceof AbstractConcurrentDoubleKeyMap.Entry) {
               AbstractConcurrentDoubleKeyMap.Entry<K1, K2, V> e = (AbstractConcurrentDoubleKeyMap.Entry)o;
               if (e.isEqual(key1, key2, hash)) {
                  return e.getValue();
               }
            } else {
               Object[] arr = (Object[])((Object[])o);

               for(int i = 0; i != arr.length; ++i) {
                  AbstractConcurrentDoubleKeyMap.Entry<K1, K2, V> e = (AbstractConcurrentDoubleKeyMap.Entry)arr[i];
                  if (e != null && e.isEqual(key1, key2, hash)) {
                     return e.getValue();
                  }
               }
            }
         }

         return null;
      }

      AbstractConcurrentDoubleKeyMap.Entry<K1, K2, V> getOrPut(K1 key1, K2 key2, int hash, V value) {
         Object[] tab = this.table;
         Object o = tab[hash & tab.length - 1];
         AbstractConcurrentDoubleKeyMap.Entry e;
         if (o != null) {
            if (o instanceof AbstractConcurrentDoubleKeyMap.Entry) {
               e = (AbstractConcurrentDoubleKeyMap.Entry)o;
               if (e.isEqual(key1, key2, hash)) {
                  return e;
               }
            } else {
               Object[] arr = (Object[])((Object[])o);

               for(int i = 0; i != arr.length; ++i) {
                  AbstractConcurrentDoubleKeyMap.Entry<K1, K2, V> e = (AbstractConcurrentDoubleKeyMap.Entry)arr[i];
                  if (e != null && e.isEqual(key1, key2, hash)) {
                     return e;
                  }
               }
            }
         }

         e = this.put(key1, key2, hash);
         e.setValue(value);
         return e;
      }

      AbstractConcurrentDoubleKeyMap.Entry<K1, K2, V> put(K1 key1, K2 key2, int hash) {
         this.lock();

         AbstractConcurrentDoubleKeyMap.Entry var9;
         try {
            int c = this.count;
            if (c++ > this.threshold) {
               this.rehash();
            }

            Object[] tab = this.table;
            int index = hash & tab.length - 1;
            Object o = tab[index];
            AbstractConcurrentDoubleKeyMap.Entry e;
            if (o == null) {
               e = this.createEntry(key1, key2, hash);
               tab[index] = e;
               this.count = c;
               var9 = e;
               return var9;
            }

            AbstractConcurrentDoubleKeyMap.Entry e;
            AbstractConcurrentDoubleKeyMap.Entry var11;
            Object[] newArr;
            if (!(o instanceof AbstractConcurrentDoubleKeyMap.Entry)) {
               Object[] arr = (Object[])((Object[])o);

               for(int i = 0; i != arr.length; ++i) {
                  e = (AbstractConcurrentDoubleKeyMap.Entry)arr[i];
                  if (e != null && e.isEqual(key1, key2, hash)) {
                     var11 = e;
                     return var11;
                  }
               }

               newArr = new Object[arr.length + 1];
               e = this.createEntry(key1, key2, hash);
               arr[0] = e;
               System.arraycopy(arr, 0, newArr, 1, arr.length);
               tab[index] = arr;
               this.count = c;
               var11 = e;
               return var11;
            }

            e = (AbstractConcurrentDoubleKeyMap.Entry)o;
            if (!e.isEqual(key1, key2, hash)) {
               newArr = new Object[2];
               e = this.createEntry(key1, key2, hash);
               newArr[0] = e;
               newArr[1] = e;
               tab[index] = newArr;
               this.count = c;
               var11 = e;
               return var11;
            }

            var9 = e;
         } finally {
            this.unlock();
         }

         return var9;
      }

      public void remove(K1 key1, K2 key2, int hash) {
         this.lock();

         try {
            int c = this.count - 1;
            Object[] tab = this.table;
            int index = hash & tab.length - 1;
            Object o = tab[index];
            if (o != null) {
               if (o instanceof AbstractConcurrentDoubleKeyMap.Entry) {
                  if (((AbstractConcurrentDoubleKeyMap.Entry)o).isEqual(key1, key2, hash)) {
                     tab[index] = null;
                     this.count = c;
                  }
               } else {
                  Object[] arr = (Object[])((Object[])o);

                  for(int i = 0; i < arr.length; ++i) {
                     AbstractConcurrentDoubleKeyMap.Entry<K1, K2, V> e = (AbstractConcurrentDoubleKeyMap.Entry)arr[i];
                     if (e != null && e.isEqual(key1, key2, hash)) {
                        arr[i] = null;
                        this.count = c;
                        break;
                     }
                  }
               }
            }
         } finally {
            this.unlock();
         }

      }

      protected abstract AbstractConcurrentDoubleKeyMap.Entry<K1, K2, V> createEntry(K1 var1, K2 var2, int var3);
   }
}
