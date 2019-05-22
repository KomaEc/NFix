package org.apache.commons.collections;

import java.util.Collection;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;

public class FastTreeMap extends TreeMap {
   protected TreeMap map = null;
   protected boolean fast = false;

   public FastTreeMap() {
      this.map = new TreeMap();
   }

   public FastTreeMap(Comparator comparator) {
      this.map = new TreeMap(comparator);
   }

   public FastTreeMap(Map map) {
      this.map = new TreeMap(map);
   }

   public FastTreeMap(SortedMap map) {
      this.map = new TreeMap(map);
   }

   public boolean getFast() {
      return this.fast;
   }

   public void setFast(boolean fast) {
      this.fast = fast;
   }

   public Object get(Object key) {
      if (this.fast) {
         return this.map.get(key);
      } else {
         TreeMap var2 = this.map;
         synchronized(var2) {
            Object var3 = this.map.get(key);
            return var3;
         }
      }
   }

   public int size() {
      if (this.fast) {
         return this.map.size();
      } else {
         TreeMap var1 = this.map;
         synchronized(var1) {
            int var2 = this.map.size();
            return var2;
         }
      }
   }

   public boolean isEmpty() {
      if (this.fast) {
         return this.map.isEmpty();
      } else {
         TreeMap var1 = this.map;
         synchronized(var1) {
            boolean var2 = this.map.isEmpty();
            return var2;
         }
      }
   }

   public boolean containsKey(Object key) {
      if (this.fast) {
         return this.map.containsKey(key);
      } else {
         TreeMap var2 = this.map;
         synchronized(var2) {
            boolean var3 = this.map.containsKey(key);
            return var3;
         }
      }
   }

   public boolean containsValue(Object value) {
      if (this.fast) {
         return this.map.containsValue(value);
      } else {
         TreeMap var2 = this.map;
         synchronized(var2) {
            boolean var3 = this.map.containsValue(value);
            return var3;
         }
      }
   }

   public Comparator comparator() {
      if (this.fast) {
         return this.map.comparator();
      } else {
         TreeMap var1 = this.map;
         synchronized(var1) {
            Comparator var2 = this.map.comparator();
            return var2;
         }
      }
   }

   public Object firstKey() {
      if (this.fast) {
         return this.map.firstKey();
      } else {
         TreeMap var1 = this.map;
         synchronized(var1) {
            Object var2 = this.map.firstKey();
            return var2;
         }
      }
   }

   public Object lastKey() {
      if (this.fast) {
         return this.map.lastKey();
      } else {
         TreeMap var1 = this.map;
         synchronized(var1) {
            Object var2 = this.map.lastKey();
            return var2;
         }
      }
   }

   public Object put(Object key, Object value) {
      if (this.fast) {
         synchronized(this) {
            TreeMap temp = (TreeMap)this.map.clone();
            Object result = temp.put(key, value);
            this.map = temp;
            return result;
         }
      } else {
         TreeMap var3 = this.map;
         synchronized(var3) {
            Object var4 = this.map.put(key, value);
            return var4;
         }
      }
   }

   public void putAll(Map in) {
      if (this.fast) {
         synchronized(this) {
            TreeMap temp = (TreeMap)this.map.clone();
            temp.putAll(in);
            this.map = temp;
         }
      } else {
         TreeMap var2 = this.map;
         synchronized(var2) {
            this.map.putAll(in);
         }
      }

   }

   public Object remove(Object key) {
      if (this.fast) {
         synchronized(this) {
            TreeMap temp = (TreeMap)this.map.clone();
            Object result = temp.remove(key);
            this.map = temp;
            return result;
         }
      } else {
         TreeMap var2 = this.map;
         synchronized(var2) {
            Object var3 = this.map.remove(key);
            return var3;
         }
      }
   }

   public void clear() {
      if (this.fast) {
         synchronized(this) {
            this.map = new TreeMap();
         }
      } else {
         TreeMap var1 = this.map;
         synchronized(var1) {
            this.map.clear();
         }
      }

   }

   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof Map)) {
         return false;
      } else {
         Map mo = (Map)o;
         Object key;
         if (this.fast) {
            if (mo.size() != this.map.size()) {
               return false;
            } else {
               Iterator i = this.map.entrySet().iterator();

               Object key;
               label54:
               do {
                  do {
                     if (!i.hasNext()) {
                        return true;
                     }

                     Entry e = (Entry)i.next();
                     key = e.getKey();
                     key = e.getValue();
                     if (key == null) {
                        continue label54;
                     }
                  } while(key.equals(mo.get(key)));

                  return false;
               } while(mo.get(key) == null && mo.containsKey(key));

               return false;
            }
         } else {
            TreeMap var3 = this.map;
            synchronized(var3) {
               if (mo.size() != this.map.size()) {
                  boolean var12 = false;
                  return var12;
               } else {
                  Iterator i = this.map.entrySet().iterator();

                  boolean var8;
                  label67:
                  do {
                     Object value;
                     do {
                        if (!i.hasNext()) {
                           boolean var14 = true;
                           return var14;
                        }

                        Entry e = (Entry)i.next();
                        key = e.getKey();
                        value = e.getValue();
                        if (value == null) {
                           continue label67;
                        }
                     } while(value.equals(mo.get(key)));

                     var8 = false;
                     return var8;
                  } while(mo.get(key) == null && mo.containsKey(key));

                  var8 = false;
                  return var8;
               }
            }
         }
      }
   }

   public int hashCode() {
      if (this.fast) {
         int h = 0;

         for(Iterator i = this.map.entrySet().iterator(); i.hasNext(); h += i.next().hashCode()) {
         }

         return h;
      } else {
         TreeMap var1 = this.map;
         synchronized(var1) {
            int h = 0;

            for(Iterator i = this.map.entrySet().iterator(); i.hasNext(); h += i.next().hashCode()) {
            }

            return h;
         }
      }
   }

   public Object clone() {
      FastTreeMap results = null;
      if (this.fast) {
         results = new FastTreeMap(this.map);
      } else {
         TreeMap var2 = this.map;
         synchronized(var2) {
            results = new FastTreeMap(this.map);
         }
      }

      results.setFast(this.getFast());
      return results;
   }

   public SortedMap headMap(Object key) {
      if (this.fast) {
         return this.map.headMap(key);
      } else {
         TreeMap var2 = this.map;
         synchronized(var2) {
            SortedMap var3 = this.map.headMap(key);
            return var3;
         }
      }
   }

   public SortedMap subMap(Object fromKey, Object toKey) {
      if (this.fast) {
         return this.map.subMap(fromKey, toKey);
      } else {
         TreeMap var3 = this.map;
         synchronized(var3) {
            SortedMap var4 = this.map.subMap(fromKey, toKey);
            return var4;
         }
      }
   }

   public SortedMap tailMap(Object key) {
      if (this.fast) {
         return this.map.tailMap(key);
      } else {
         TreeMap var2 = this.map;
         synchronized(var2) {
            SortedMap var3 = this.map.tailMap(key);
            return var3;
         }
      }
   }

   public Set entrySet() {
      return new FastTreeMap.EntrySet();
   }

   public Set keySet() {
      return new FastTreeMap.KeySet();
   }

   public Collection values() {
      return new FastTreeMap.Values();
   }

   private class EntrySet extends FastTreeMap.CollectionView implements Set {
      private EntrySet() {
         super();
      }

      protected Collection get(Map map) {
         return map.entrySet();
      }

      protected Object iteratorNext(Entry entry) {
         return entry;
      }

      // $FF: synthetic method
      EntrySet(Object x1) {
         this();
      }
   }

   private class Values extends FastTreeMap.CollectionView {
      private Values() {
         super();
      }

      protected Collection get(Map map) {
         return map.values();
      }

      protected Object iteratorNext(Entry entry) {
         return entry.getValue();
      }

      // $FF: synthetic method
      Values(Object x1) {
         this();
      }
   }

   private class KeySet extends FastTreeMap.CollectionView implements Set {
      private KeySet() {
         super();
      }

      protected Collection get(Map map) {
         return map.keySet();
      }

      protected Object iteratorNext(Entry entry) {
         return entry.getKey();
      }

      // $FF: synthetic method
      KeySet(Object x1) {
         this();
      }
   }

   private abstract class CollectionView implements Collection {
      public CollectionView() {
      }

      protected abstract Collection get(Map var1);

      protected abstract Object iteratorNext(Entry var1);

      public void clear() {
         if (FastTreeMap.this.fast) {
            FastTreeMap var1 = FastTreeMap.this;
            synchronized(var1) {
               FastTreeMap.this.map = new TreeMap();
            }
         } else {
            TreeMap var6 = FastTreeMap.this.map;
            synchronized(var6) {
               this.get(FastTreeMap.this.map).clear();
            }
         }

      }

      public boolean remove(Object o) {
         if (FastTreeMap.this.fast) {
            FastTreeMap var10 = FastTreeMap.this;
            synchronized(var10) {
               TreeMap temp = (TreeMap)FastTreeMap.this.map.clone();
               boolean r = this.get(temp).remove(o);
               FastTreeMap.this.map = temp;
               return r;
            }
         } else {
            TreeMap var2 = FastTreeMap.this.map;
            synchronized(var2) {
               boolean var3 = this.get(FastTreeMap.this.map).remove(o);
               return var3;
            }
         }
      }

      public boolean removeAll(Collection o) {
         if (FastTreeMap.this.fast) {
            FastTreeMap var10 = FastTreeMap.this;
            synchronized(var10) {
               TreeMap temp = (TreeMap)FastTreeMap.this.map.clone();
               boolean r = this.get(temp).removeAll(o);
               FastTreeMap.this.map = temp;
               return r;
            }
         } else {
            TreeMap var2 = FastTreeMap.this.map;
            synchronized(var2) {
               boolean var3 = this.get(FastTreeMap.this.map).removeAll(o);
               return var3;
            }
         }
      }

      public boolean retainAll(Collection o) {
         if (FastTreeMap.this.fast) {
            FastTreeMap var10 = FastTreeMap.this;
            synchronized(var10) {
               TreeMap temp = (TreeMap)FastTreeMap.this.map.clone();
               boolean r = this.get(temp).retainAll(o);
               FastTreeMap.this.map = temp;
               return r;
            }
         } else {
            TreeMap var2 = FastTreeMap.this.map;
            synchronized(var2) {
               boolean var3 = this.get(FastTreeMap.this.map).retainAll(o);
               return var3;
            }
         }
      }

      public int size() {
         if (FastTreeMap.this.fast) {
            return this.get(FastTreeMap.this.map).size();
         } else {
            TreeMap var1 = FastTreeMap.this.map;
            synchronized(var1) {
               int var2 = this.get(FastTreeMap.this.map).size();
               return var2;
            }
         }
      }

      public boolean isEmpty() {
         if (FastTreeMap.this.fast) {
            return this.get(FastTreeMap.this.map).isEmpty();
         } else {
            TreeMap var1 = FastTreeMap.this.map;
            synchronized(var1) {
               boolean var2 = this.get(FastTreeMap.this.map).isEmpty();
               return var2;
            }
         }
      }

      public boolean contains(Object o) {
         if (FastTreeMap.this.fast) {
            return this.get(FastTreeMap.this.map).contains(o);
         } else {
            TreeMap var2 = FastTreeMap.this.map;
            synchronized(var2) {
               boolean var3 = this.get(FastTreeMap.this.map).contains(o);
               return var3;
            }
         }
      }

      public boolean containsAll(Collection o) {
         if (FastTreeMap.this.fast) {
            return this.get(FastTreeMap.this.map).containsAll(o);
         } else {
            TreeMap var2 = FastTreeMap.this.map;
            synchronized(var2) {
               boolean var3 = this.get(FastTreeMap.this.map).containsAll(o);
               return var3;
            }
         }
      }

      public Object[] toArray(Object[] o) {
         if (FastTreeMap.this.fast) {
            return this.get(FastTreeMap.this.map).toArray(o);
         } else {
            TreeMap var2 = FastTreeMap.this.map;
            synchronized(var2) {
               Object[] var3 = this.get(FastTreeMap.this.map).toArray(o);
               return var3;
            }
         }
      }

      public Object[] toArray() {
         if (FastTreeMap.this.fast) {
            return this.get(FastTreeMap.this.map).toArray();
         } else {
            TreeMap var1 = FastTreeMap.this.map;
            synchronized(var1) {
               Object[] var2 = this.get(FastTreeMap.this.map).toArray();
               return var2;
            }
         }
      }

      public boolean equals(Object o) {
         if (o == this) {
            return true;
         } else if (FastTreeMap.this.fast) {
            return this.get(FastTreeMap.this.map).equals(o);
         } else {
            TreeMap var2 = FastTreeMap.this.map;
            synchronized(var2) {
               boolean var3 = this.get(FastTreeMap.this.map).equals(o);
               return var3;
            }
         }
      }

      public int hashCode() {
         if (FastTreeMap.this.fast) {
            return this.get(FastTreeMap.this.map).hashCode();
         } else {
            TreeMap var1 = FastTreeMap.this.map;
            synchronized(var1) {
               int var2 = this.get(FastTreeMap.this.map).hashCode();
               return var2;
            }
         }
      }

      public boolean add(Object o) {
         throw new UnsupportedOperationException();
      }

      public boolean addAll(Collection c) {
         throw new UnsupportedOperationException();
      }

      public Iterator iterator() {
         return new FastTreeMap.CollectionView.CollectionViewIterator();
      }

      private class CollectionViewIterator implements Iterator {
         private Map expected;
         private Entry lastReturned = null;
         private Iterator iterator;

         public CollectionViewIterator() {
            this.expected = FastTreeMap.this.map;
            this.iterator = this.expected.entrySet().iterator();
         }

         public boolean hasNext() {
            if (this.expected != FastTreeMap.this.map) {
               throw new ConcurrentModificationException();
            } else {
               return this.iterator.hasNext();
            }
         }

         public Object next() {
            if (this.expected != FastTreeMap.this.map) {
               throw new ConcurrentModificationException();
            } else {
               this.lastReturned = (Entry)this.iterator.next();
               return CollectionView.this.iteratorNext(this.lastReturned);
            }
         }

         public void remove() {
            if (this.lastReturned == null) {
               throw new IllegalStateException();
            } else {
               if (FastTreeMap.this.fast) {
                  FastTreeMap var1 = FastTreeMap.this;
                  synchronized(var1) {
                     if (this.expected != FastTreeMap.this.map) {
                        throw new ConcurrentModificationException();
                     }

                     FastTreeMap.this.remove(this.lastReturned.getKey());
                     this.lastReturned = null;
                     this.expected = FastTreeMap.this.map;
                  }
               } else {
                  this.iterator.remove();
                  this.lastReturned = null;
               }

            }
         }
      }
   }
}
