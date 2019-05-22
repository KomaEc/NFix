package org.codehaus.groovy.util;

public abstract class AbstractConcurrentMap<K, V> extends AbstractConcurrentMapBase {
   public AbstractConcurrentMap(Object segmentInfo) {
      super(segmentInfo);
   }

   public AbstractConcurrentMap.Segment segmentFor(int hash) {
      return (AbstractConcurrentMap.Segment)super.segmentFor(hash);
   }

   public V get(K key) {
      int hash = hash(key);
      return this.segmentFor(hash).get(key, hash);
   }

   public AbstractConcurrentMap.Entry<K, V> getOrPut(K key, V value) {
      int hash = hash(key);
      return this.segmentFor(hash).getOrPut(key, hash, value);
   }

   public void put(K key, V value) {
      int hash = hash(key);
      this.segmentFor(hash).put(key, hash, value);
   }

   public void remove(K key) {
      int hash = hash(key);
      this.segmentFor(hash).remove(key, hash);
   }

   public interface Entry<K, V> extends AbstractConcurrentMapBase.Entry<V> {
      boolean isEqual(K var1, int var2);
   }

   public abstract static class Segment<K, V> extends AbstractConcurrentMapBase.Segment {
      protected Segment(int initialCapacity) {
         super(initialCapacity);
      }

      public final V get(K key, int hash) {
         Object[] tab = this.table;
         Object o = tab[hash & tab.length - 1];
         if (o != null) {
            if (o instanceof AbstractConcurrentMap.Entry) {
               AbstractConcurrentMap.Entry<K, V> e = (AbstractConcurrentMap.Entry)o;
               if (e.isEqual(key, hash)) {
                  return e.getValue();
               }
            } else {
               Object[] arr = (Object[])((Object[])o);

               for(int i = 0; i < arr.length; ++i) {
                  AbstractConcurrentMap.Entry<K, V> e = (AbstractConcurrentMap.Entry)arr[i];
                  if (e != null && e.isEqual(key, hash)) {
                     return e.getValue();
                  }
               }
            }
         }

         return null;
      }

      public final AbstractConcurrentMap.Entry<K, V> getOrPut(K key, int hash, V value) {
         Object[] tab = this.table;
         Object o = tab[hash & tab.length - 1];
         if (o != null) {
            if (o instanceof AbstractConcurrentMap.Entry) {
               AbstractConcurrentMap.Entry<K, V> e = (AbstractConcurrentMap.Entry)o;
               if (e.isEqual(key, hash)) {
                  return e;
               }
            } else {
               Object[] arr = (Object[])((Object[])o);

               for(int i = 0; i < arr.length; ++i) {
                  AbstractConcurrentMap.Entry<K, V> e = (AbstractConcurrentMap.Entry)arr[i];
                  if (e != null && e.isEqual(key, hash)) {
                     return e;
                  }
               }
            }
         }

         return this.put(key, hash, value);
      }

      public final AbstractConcurrentMap.Entry put(K key, int hash, V value) {
         this.lock();

         try {
            int c = this.count;
            if (c++ > this.threshold) {
               this.rehash();
            }

            Object[] tab = this.table;
            int index = hash & tab.length - 1;
            Object o = tab[index];
            AbstractConcurrentMap.Entry e;
            AbstractConcurrentMap.Entry ee;
            if (o == null) {
               e = this.createEntry(key, hash, value);
               tab[index] = e;
               this.count = c;
               ee = e;
               return ee;
            } else {
               AbstractConcurrentMap.Entry e;
               AbstractConcurrentMap.Entry e;
               if (o instanceof AbstractConcurrentMap.Entry) {
                  e = (AbstractConcurrentMap.Entry)o;
                  if (e.isEqual(key, hash)) {
                     e.setValue(value);
                     ee = e;
                     return ee;
                  } else {
                     Object[] arr = new Object[2];
                     e = this.createEntry(key, hash, value);
                     arr[0] = e;
                     arr[1] = e;
                     tab[index] = arr;
                     this.count = c;
                     e = e;
                     return e;
                  }
               } else {
                  Object[] arr = (Object[])((Object[])o);

                  for(int i = 0; i < arr.length; ++i) {
                     e = (AbstractConcurrentMap.Entry)arr[i];
                     if (e != null && e.isEqual(key, hash)) {
                        e.setValue(value);
                        e = e;
                        return e;
                     }
                  }

                  ee = this.createEntry(key, hash, value);

                  for(int i = 0; i < arr.length; ++i) {
                     e = (AbstractConcurrentMap.Entry)arr[i];
                     if (e == null) {
                        arr[i] = ee;
                        this.count = c;
                        AbstractConcurrentMap.Entry var12 = ee;
                        return var12;
                     }
                  }

                  Object[] newArr = new Object[arr.length + 1];
                  newArr[0] = ee;
                  System.arraycopy(arr, 0, newArr, 1, arr.length);
                  tab[index] = newArr;
                  this.count = c;
                  e = ee;
                  return e;
               }
            }
         } finally {
            this.unlock();
         }
      }

      public void remove(K key, int hash) {
         this.lock();

         try {
            int c = this.count - 1;
            Object[] tab = this.table;
            int index = hash & tab.length - 1;
            Object o = tab[index];
            if (o != null) {
               if (o instanceof AbstractConcurrentMap.Entry) {
                  if (((AbstractConcurrentMap.Entry)o).isEqual(key, hash)) {
                     tab[index] = null;
                     this.count = c;
                  }
               } else {
                  Object[] arr = (Object[])((Object[])o);

                  for(int i = 0; i < arr.length; ++i) {
                     AbstractConcurrentMap.Entry<K, V> e = (AbstractConcurrentMap.Entry)arr[i];
                     if (e != null && e.isEqual(key, hash)) {
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

      protected abstract AbstractConcurrentMap.Entry<K, V> createEntry(K var1, int var2, V var3);
   }
}
