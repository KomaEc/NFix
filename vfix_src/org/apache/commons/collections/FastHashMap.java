package org.apache.commons.collections;

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class FastHashMap extends HashMap {
   protected HashMap map = null;
   protected boolean fast = false;

   public FastHashMap() {
      this.map = new HashMap();
   }

   public FastHashMap(int capacity) {
      this.map = new HashMap(capacity);
   }

   public FastHashMap(int capacity, float factor) {
      this.map = new HashMap(capacity, factor);
   }

   public FastHashMap(Map map) {
      this.map = new HashMap(map);
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
         HashMap var2 = this.map;
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
         HashMap var1 = this.map;
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
         HashMap var1 = this.map;
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
         HashMap var2 = this.map;
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
         HashMap var2 = this.map;
         synchronized(var2) {
            boolean var3 = this.map.containsValue(value);
            return var3;
         }
      }
   }

   public Object put(Object key, Object value) {
      if (this.fast) {
         synchronized(this) {
            HashMap temp = (HashMap)this.map.clone();
            Object result = temp.put(key, value);
            this.map = temp;
            return result;
         }
      } else {
         HashMap var3 = this.map;
         synchronized(var3) {
            Object var4 = this.map.put(key, value);
            return var4;
         }
      }
   }

   public void putAll(Map in) {
      if (this.fast) {
         synchronized(this) {
            HashMap temp = (HashMap)this.map.clone();
            temp.putAll(in);
            this.map = temp;
         }
      } else {
         HashMap var2 = this.map;
         synchronized(var2) {
            this.map.putAll(in);
         }
      }

   }

   public Object remove(Object key) {
      if (this.fast) {
         synchronized(this) {
            HashMap temp = (HashMap)this.map.clone();
            Object result = temp.remove(key);
            this.map = temp;
            return result;
         }
      } else {
         HashMap var2 = this.map;
         synchronized(var2) {
            Object var3 = this.map.remove(key);
            return var3;
         }
      }
   }

   public void clear() {
      if (this.fast) {
         synchronized(this) {
            this.map = new HashMap();
         }
      } else {
         HashMap var1 = this.map;
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
            HashMap var3 = this.map;
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
         HashMap var1 = this.map;
         synchronized(var1) {
            int h = 0;

            for(Iterator i = this.map.entrySet().iterator(); i.hasNext(); h += i.next().hashCode()) {
            }

            return h;
         }
      }
   }

   public Object clone() {
      FastHashMap results = null;
      if (this.fast) {
         results = new FastHashMap(this.map);
      } else {
         HashMap var2 = this.map;
         synchronized(var2) {
            results = new FastHashMap(this.map);
         }
      }

      results.setFast(this.getFast());
      return results;
   }

   public Set entrySet() {
      return new FastHashMap.EntrySet();
   }

   public Set keySet() {
      return new FastHashMap.KeySet();
   }

   public Collection values() {
      return new FastHashMap.Values();
   }

   private class EntrySet extends FastHashMap.CollectionView implements Set {
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

   private class Values extends FastHashMap.CollectionView {
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

   private class KeySet extends FastHashMap.CollectionView implements Set {
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
         if (FastHashMap.this.fast) {
            FastHashMap var1 = FastHashMap.this;
            synchronized(var1) {
               FastHashMap.this.map = new HashMap();
            }
         } else {
            HashMap var6 = FastHashMap.this.map;
            synchronized(var6) {
               this.get(FastHashMap.this.map).clear();
            }
         }

      }

      public boolean remove(Object o) {
         if (FastHashMap.this.fast) {
            FastHashMap var10 = FastHashMap.this;
            synchronized(var10) {
               HashMap temp = (HashMap)FastHashMap.this.map.clone();
               boolean r = this.get(temp).remove(o);
               FastHashMap.this.map = temp;
               return r;
            }
         } else {
            HashMap var2 = FastHashMap.this.map;
            synchronized(var2) {
               boolean var3 = this.get(FastHashMap.this.map).remove(o);
               return var3;
            }
         }
      }

      public boolean removeAll(Collection o) {
         if (FastHashMap.this.fast) {
            FastHashMap var10 = FastHashMap.this;
            synchronized(var10) {
               HashMap temp = (HashMap)FastHashMap.this.map.clone();
               boolean r = this.get(temp).removeAll(o);
               FastHashMap.this.map = temp;
               return r;
            }
         } else {
            HashMap var2 = FastHashMap.this.map;
            synchronized(var2) {
               boolean var3 = this.get(FastHashMap.this.map).removeAll(o);
               return var3;
            }
         }
      }

      public boolean retainAll(Collection o) {
         if (FastHashMap.this.fast) {
            FastHashMap var10 = FastHashMap.this;
            synchronized(var10) {
               HashMap temp = (HashMap)FastHashMap.this.map.clone();
               boolean r = this.get(temp).retainAll(o);
               FastHashMap.this.map = temp;
               return r;
            }
         } else {
            HashMap var2 = FastHashMap.this.map;
            synchronized(var2) {
               boolean var3 = this.get(FastHashMap.this.map).retainAll(o);
               return var3;
            }
         }
      }

      public int size() {
         if (FastHashMap.this.fast) {
            return this.get(FastHashMap.this.map).size();
         } else {
            HashMap var1 = FastHashMap.this.map;
            synchronized(var1) {
               int var2 = this.get(FastHashMap.this.map).size();
               return var2;
            }
         }
      }

      public boolean isEmpty() {
         if (FastHashMap.this.fast) {
            return this.get(FastHashMap.this.map).isEmpty();
         } else {
            HashMap var1 = FastHashMap.this.map;
            synchronized(var1) {
               boolean var2 = this.get(FastHashMap.this.map).isEmpty();
               return var2;
            }
         }
      }

      public boolean contains(Object o) {
         if (FastHashMap.this.fast) {
            return this.get(FastHashMap.this.map).contains(o);
         } else {
            HashMap var2 = FastHashMap.this.map;
            synchronized(var2) {
               boolean var3 = this.get(FastHashMap.this.map).contains(o);
               return var3;
            }
         }
      }

      public boolean containsAll(Collection o) {
         if (FastHashMap.this.fast) {
            return this.get(FastHashMap.this.map).containsAll(o);
         } else {
            HashMap var2 = FastHashMap.this.map;
            synchronized(var2) {
               boolean var3 = this.get(FastHashMap.this.map).containsAll(o);
               return var3;
            }
         }
      }

      public Object[] toArray(Object[] o) {
         if (FastHashMap.this.fast) {
            return this.get(FastHashMap.this.map).toArray(o);
         } else {
            HashMap var2 = FastHashMap.this.map;
            synchronized(var2) {
               Object[] var3 = this.get(FastHashMap.this.map).toArray(o);
               return var3;
            }
         }
      }

      public Object[] toArray() {
         if (FastHashMap.this.fast) {
            return this.get(FastHashMap.this.map).toArray();
         } else {
            HashMap var1 = FastHashMap.this.map;
            synchronized(var1) {
               Object[] var2 = this.get(FastHashMap.this.map).toArray();
               return var2;
            }
         }
      }

      public boolean equals(Object o) {
         if (o == this) {
            return true;
         } else if (FastHashMap.this.fast) {
            return this.get(FastHashMap.this.map).equals(o);
         } else {
            HashMap var2 = FastHashMap.this.map;
            synchronized(var2) {
               boolean var3 = this.get(FastHashMap.this.map).equals(o);
               return var3;
            }
         }
      }

      public int hashCode() {
         if (FastHashMap.this.fast) {
            return this.get(FastHashMap.this.map).hashCode();
         } else {
            HashMap var1 = FastHashMap.this.map;
            synchronized(var1) {
               int var2 = this.get(FastHashMap.this.map).hashCode();
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
         return new FastHashMap.CollectionView.CollectionViewIterator();
      }

      private class CollectionViewIterator implements Iterator {
         private Map expected;
         private Entry lastReturned = null;
         private Iterator iterator;

         public CollectionViewIterator() {
            this.expected = FastHashMap.this.map;
            this.iterator = this.expected.entrySet().iterator();
         }

         public boolean hasNext() {
            if (this.expected != FastHashMap.this.map) {
               throw new ConcurrentModificationException();
            } else {
               return this.iterator.hasNext();
            }
         }

         public Object next() {
            if (this.expected != FastHashMap.this.map) {
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
               if (FastHashMap.this.fast) {
                  FastHashMap var1 = FastHashMap.this;
                  synchronized(var1) {
                     if (this.expected != FastHashMap.this.map) {
                        throw new ConcurrentModificationException();
                     }

                     FastHashMap.this.remove(this.lastReturned.getKey());
                     this.lastReturned = null;
                     this.expected = FastHashMap.this.map;
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
