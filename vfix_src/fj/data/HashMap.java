package fj.data;

import fj.Equal;
import fj.F;
import fj.Function;
import fj.Hash;
import fj.P;
import fj.P2;
import fj.Unit;
import fj.function.Effect1;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public final class HashMap<K, V> implements Iterable<K> {
   private final java.util.HashMap<HashMap<K, V>.Key<K>, V> m;
   private final Equal<K> e;
   private final Hash<K> h;

   public Iterator<K> iterator() {
      return this.keys().iterator();
   }

   public HashMap(Equal<K> e, Hash<K> h) {
      this.m = new java.util.HashMap();
      this.e = e;
      this.h = h;
   }

   public HashMap(Map<K, V> map, Equal<K> e, Hash<K> h) {
      this(e, h);
      Iterator var4 = map.keySet().iterator();

      while(var4.hasNext()) {
         K key = var4.next();
         this.set(key, map.get(key));
      }

   }

   public HashMap(Equal<K> e, Hash<K> h, int initialCapacity) {
      this.m = new java.util.HashMap(initialCapacity);
      this.e = e;
      this.h = h;
   }

   public HashMap(Map<K, V> map) {
      this(map, Equal.anyEqual(), Hash.anyHash());
   }

   public HashMap(Equal<K> e, Hash<K> h, int initialCapacity, float loadFactor) {
      this.m = new java.util.HashMap(initialCapacity, loadFactor);
      this.e = e;
      this.h = h;
   }

   public static <K, V> HashMap<K, V> hashMap() {
      Equal<K> e = Equal.anyEqual();
      Hash<K> h = Hash.anyHash();
      return new HashMap(e, h);
   }

   public boolean eq(K k1, K k2) {
      return this.e.eq(k1, k2);
   }

   public int hash(K k) {
      return this.h.hash(k);
   }

   public Option<V> get(K k) {
      return Option.fromNull(this.m.get(new HashMap.Key(k, this.e, this.h)));
   }

   public F<K, Option<V>> get() {
      return new F<K, Option<V>>() {
         public Option<V> f(K k) {
            return HashMap.this.get(k);
         }
      };
   }

   public void clear() {
      this.m.clear();
   }

   public boolean contains(K k) {
      return this.m.containsKey(new HashMap.Key(k, this.e, this.h));
   }

   public List<K> keys() {
      List.Buffer<K> b = new List.Buffer();
      Iterator var2 = this.m.keySet().iterator();

      while(var2.hasNext()) {
         HashMap<K, V>.Key<K> k = (HashMap.Key)var2.next();
         b.snoc(k.k());
      }

      return b.toList();
   }

   public List<V> values() {
      return this.keys().map(new F<K, V>() {
         public V f(K k) {
            return HashMap.this.m.get(HashMap.this.new Key(k, HashMap.this.e, HashMap.this.h));
         }
      });
   }

   public boolean isEmpty() {
      return this.m.isEmpty();
   }

   public int size() {
      return this.m.size();
   }

   public void set(K k, V v) {
      if (v != null) {
         this.m.put(new HashMap.Key(k, this.e, this.h), v);
      }

   }

   public void delete(K k) {
      this.m.remove(new HashMap.Key(k, this.e, this.h));
   }

   public Option<V> getDelete(K k) {
      return Option.fromNull(this.m.remove(new HashMap.Key(k, this.e, this.h)));
   }

   public <A, B> HashMap<A, B> map(F<K, A> keyFunction, F<V, B> valueFunction, Equal<A> equal, Hash<A> hash) {
      HashMap<A, B> hashMap = new HashMap(equal, hash);
      Iterator var6 = this.keys().iterator();

      while(var6.hasNext()) {
         K key = var6.next();
         A newKey = keyFunction.f(key);
         B newValue = valueFunction.f(this.get(key).some());
         hashMap.set(newKey, newValue);
      }

      return hashMap;
   }

   public <A, B> HashMap<A, B> map(F<K, A> keyFunction, F<V, B> valueFunction) {
      return this.map(keyFunction, valueFunction, Equal.anyEqual(), Hash.anyHash());
   }

   public <A, B> HashMap<A, B> map(F<P2<K, V>, P2<A, B>> function, Equal<A> equal, Hash<A> hash) {
      return from(this.toStream().map(function), equal, hash);
   }

   public <A, B> HashMap<A, B> map(F<P2<K, V>, P2<A, B>> function) {
      return from(this.toStream().map(function));
   }

   public <A> HashMap<A, V> mapKeys(F<K, A> keyFunction, Equal<A> equal, Hash<A> hash) {
      return this.map(keyFunction, Function.identity(), equal, hash);
   }

   public <A> HashMap<A, V> mapKeys(F<K, A> function) {
      return this.mapKeys(function, Equal.anyEqual(), Hash.anyHash());
   }

   public <B> HashMap<K, B> mapValues(F<V, B> function) {
      return this.map(Function.identity(), function, this.e, this.h);
   }

   public void foreachDoEffect(Effect1<P2<K, V>> effect) {
      this.toStream().foreachDoEffect(effect);
   }

   public void foreach(F<P2<K, V>, Unit> function) {
      this.toStream().foreach(function);
   }

   public List<P2<K, V>> toList() {
      return this.keys().map(new F<K, P2<K, V>>() {
         public P2<K, V> f(K k) {
            return P.p(k, HashMap.this.get(k).some());
         }
      });
   }

   public Collection<P2<K, V>> toCollection() {
      return this.toList().toCollection();
   }

   public Stream<P2<K, V>> toStream() {
      return this.toList().toStream();
   }

   public Option<P2<K, V>> toOption() {
      return this.toList().toOption();
   }

   public Array<P2<K, V>> toArray() {
      return this.toList().toArray();
   }

   public Map<K, V> toMap() {
      java.util.HashMap<K, V> result = new java.util.HashMap();
      Iterator var2 = this.keys().iterator();

      while(var2.hasNext()) {
         K key = var2.next();
         result.put(key, this.get(key).some());
      }

      return result;
   }

   public static <K, V> HashMap<K, V> from(Iterable<P2<K, V>> entries) {
      return from(entries, Equal.anyEqual(), Hash.anyHash());
   }

   public static <K, V> HashMap<K, V> from(Iterable<P2<K, V>> entries, Equal<K> equal, Hash<K> hash) {
      HashMap<K, V> map = new HashMap(equal, hash);
      Iterator var4 = entries.iterator();

      while(var4.hasNext()) {
         P2<K, V> entry = (P2)var4.next();
         map.set(entry._1(), entry._2());
      }

      return map;
   }

   private final class Key<K> {
      private final K k;
      private final Equal<K> e;
      private final Hash<K> h;

      Key(K k, Equal<K> e, Hash<K> h) {
         this.k = k;
         this.e = e;
         this.h = h;
      }

      K k() {
         return this.k;
      }

      public boolean equals(Object o) {
         return o instanceof HashMap.Key && this.e.eq(this.k, ((HashMap.Key)o).k());
      }

      public int hashCode() {
         return this.h.hash(this.k);
      }
   }
}
