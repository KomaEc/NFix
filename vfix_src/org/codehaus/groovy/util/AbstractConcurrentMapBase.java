package org.codehaus.groovy.util;

public abstract class AbstractConcurrentMapBase {
   protected static final int MAXIMUM_CAPACITY = 1073741824;
   static final int MAX_SEGMENTS = 65536;
   static final int RETRIES_BEFORE_LOCK = 2;
   final int segmentMask;
   final int segmentShift;
   protected final AbstractConcurrentMapBase.Segment[] segments;

   public AbstractConcurrentMapBase(Object segmentInfo) {
      int sshift = 0;

      int ssize;
      for(ssize = 1; ssize < 16; ssize <<= 1) {
         ++sshift;
      }

      this.segmentShift = 32 - sshift;
      this.segmentMask = ssize - 1;
      this.segments = new AbstractConcurrentMapBase.Segment[ssize];
      int c = 512 / ssize;
      if (c * ssize < 512) {
         ++c;
      }

      int cap;
      for(cap = 1; cap < c; cap <<= 1) {
      }

      for(int i = 0; i < this.segments.length; ++i) {
         this.segments[i] = this.createSegment(segmentInfo, cap);
      }

   }

   protected abstract AbstractConcurrentMapBase.Segment createSegment(Object var1, int var2);

   protected static <K> int hash(K key) {
      int h = System.identityHashCode(key);
      h += ~(h << 9);
      h ^= h >>> 14;
      h += h << 4;
      h ^= h >>> 10;
      return h;
   }

   public AbstractConcurrentMapBase.Segment segmentFor(int hash) {
      return this.segments[hash >>> this.segmentShift & this.segmentMask];
   }

   public int fullSize() {
      int count = 0;

      for(int i = 0; i < this.segments.length; ++i) {
         this.segments[i].lock();

         try {
            for(int j = 0; j < this.segments[i].table.length; ++j) {
               Object o = this.segments[i].table[j];
               if (o != null) {
                  if (o instanceof AbstractConcurrentMapBase.Entry) {
                     ++count;
                  } else {
                     Object[] arr = (Object[])((Object[])o);
                     count += arr.length;
                  }
               }
            }
         } finally {
            this.segments[i].unlock();
         }
      }

      return count;
   }

   public int size() {
      int count = 0;

      for(int i = 0; i < this.segments.length; ++i) {
         this.segments[i].lock();

         try {
            for(int j = 0; j < this.segments[i].table.length; ++j) {
               Object o = this.segments[i].table[j];
               if (o != null) {
                  if (o instanceof AbstractConcurrentMapBase.Entry) {
                     AbstractConcurrentMapBase.Entry e = (AbstractConcurrentMapBase.Entry)o;
                     if (e.isValid()) {
                        ++count;
                     }
                  } else {
                     Object[] arr = (Object[])((Object[])o);

                     for(int k = 0; k < arr.length; ++k) {
                        AbstractConcurrentMapBase.Entry info = (AbstractConcurrentMapBase.Entry)arr[k];
                        if (info != null && info.isValid()) {
                           ++count;
                        }
                     }
                  }
               }
            }
         } finally {
            this.segments[i].unlock();
         }
      }

      return count;
   }

   public interface Entry<V> {
      V getValue();

      void setValue(V var1);

      int getHash();

      boolean isValid();
   }

   public static class Segment extends LockableObject {
      volatile int count;
      int threshold;
      protected volatile Object[] table;

      protected Segment(int initialCapacity) {
         this.setTable(new Object[initialCapacity]);
      }

      void setTable(Object[] newTable) {
         this.threshold = (int)((float)newTable.length * 0.75F);
         this.table = newTable;
      }

      void removeEntry(AbstractConcurrentMapBase.Entry e) {
         this.lock();
         int newCount = this.count;

         try {
            Object[] tab = this.table;
            int index = e.getHash() & tab.length - 1;
            Object o = tab[index];
            if (o != null) {
               if (o instanceof AbstractConcurrentMapBase.Entry) {
                  if (o == e) {
                     tab[index] = null;
                     --newCount;
                  }
               } else {
                  Object[] arr = (Object[])((Object[])o);
                  Object res = null;

                  for(int i = 0; i < arr.length; ++i) {
                     AbstractConcurrentMapBase.Entry info = (AbstractConcurrentMapBase.Entry)arr[i];
                     if (info != null) {
                        if (info != e) {
                           if (info.isValid()) {
                              res = this.put(info, res);
                           } else {
                              --newCount;
                           }
                        } else {
                           --newCount;
                        }
                     }
                  }

                  tab[index] = res;
               }

               this.count = newCount;
            }
         } finally {
            this.unlock();
         }

      }

      void rehash() {
         Object[] oldTable = this.table;
         int oldCapacity = oldTable.length;
         if (oldCapacity < 1073741824) {
            int newCount = 0;

            for(int i = 0; i < oldCapacity; ++i) {
               Object o = oldTable[i];
               if (o != null) {
                  if (o instanceof AbstractConcurrentMapBase.Entry) {
                     AbstractConcurrentMapBase.Entry e = (AbstractConcurrentMapBase.Entry)o;
                     if (e.isValid()) {
                        ++newCount;
                     } else {
                        oldTable[i] = null;
                     }
                  } else {
                     Object[] arr = (Object[])((Object[])o);
                     int localCount = 0;

                     for(int index = 0; index < arr.length; ++index) {
                        AbstractConcurrentMapBase.Entry e = (AbstractConcurrentMapBase.Entry)arr[index];
                        if (e != null && e.isValid()) {
                           ++localCount;
                        } else {
                           arr[index] = null;
                        }
                     }

                     if (localCount == 0) {
                        oldTable[i] = null;
                     } else {
                        newCount += localCount;
                     }
                  }
               }
            }

            Object[] newTable = new Object[newCount + 1 < this.threshold ? oldCapacity : oldCapacity << 1];
            int sizeMask = newTable.length - 1;
            newCount = 0;

            for(int i = 0; i < oldCapacity; ++i) {
               Object o = oldTable[i];
               if (o != null) {
                  int j;
                  if (o instanceof AbstractConcurrentMapBase.Entry) {
                     AbstractConcurrentMapBase.Entry e = (AbstractConcurrentMapBase.Entry)o;
                     if (e.isValid()) {
                        j = e.getHash() & sizeMask;
                        this.put(e, j, newTable);
                        ++newCount;
                     }
                  } else {
                     Object[] arr = (Object[])((Object[])o);

                     for(j = 0; j < arr.length; ++j) {
                        AbstractConcurrentMapBase.Entry e = (AbstractConcurrentMapBase.Entry)arr[j];
                        if (e != null && e.isValid()) {
                           int index = e.getHash() & sizeMask;
                           this.put(e, index, newTable);
                           ++newCount;
                        }
                     }
                  }
               }
            }

            this.threshold = (int)((float)newTable.length * 0.75F);
            this.table = newTable;
            this.count = newCount;
         }
      }

      private void put(AbstractConcurrentMapBase.Entry ee, int index, Object[] tab) {
         Object o = tab[index];
         if (o != null) {
            Object[] arr;
            if (o instanceof AbstractConcurrentMapBase.Entry) {
               arr = new Object[]{ee, (AbstractConcurrentMapBase.Entry)o};
               tab[index] = arr;
            } else {
               arr = (Object[])((Object[])o);
               Object[] newArr = new Object[arr.length + 1];
               newArr[0] = ee;
               System.arraycopy(arr, 0, newArr, 1, arr.length);
               tab[index] = newArr;
            }
         } else {
            tab[index] = ee;
         }
      }

      private Object put(AbstractConcurrentMapBase.Entry ee, Object o) {
         if (o != null) {
            Object[] arr;
            if (o instanceof AbstractConcurrentMapBase.Entry) {
               arr = new Object[]{ee, (AbstractConcurrentMapBase.Entry)o};
               return arr;
            } else {
               arr = (Object[])((Object[])o);
               Object[] newArr = new Object[arr.length + 1];
               newArr[0] = ee;
               System.arraycopy(arr, 0, newArr, 1, arr.length);
               return newArr;
            }
         } else {
            return ee;
         }
      }
   }
}
