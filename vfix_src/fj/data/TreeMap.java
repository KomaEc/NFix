package fj.data;

import fj.F;
import fj.F1Functions;
import fj.Function;
import fj.Ord;
import fj.P;
import fj.P2;
import fj.P3;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public final class TreeMap<K, V> implements Iterable<P2<K, V>> {
   private final Set<P2<K, Option<V>>> tree;

   private TreeMap(Set<P2<K, Option<V>>> tree) {
      this.tree = tree;
   }

   private static <K, V> Ord<P2<K, V>> ord(Ord<K> keyOrd) {
      return keyOrd.comap(P2.__1());
   }

   public static <K, V> TreeMap<K, V> empty(Ord<K> keyOrd) {
      return new TreeMap(Set.empty(ord(keyOrd)));
   }

   public Option<V> get(K k) {
      Option<P2<K, Option<V>>> x = (Option)this.tree.split(P.p(k, Option.none()))._2();
      return x.bind(P2.__2());
   }

   public TreeMap<K, V> set(K k, V v) {
      return new TreeMap(this.tree.insert(P.p(k, Option.some(v))));
   }

   public TreeMap<K, V> delete(K k) {
      return new TreeMap(this.tree.delete(P.p(k, Option.none())));
   }

   public int size() {
      return this.tree.size();
   }

   public boolean isEmpty() {
      return this.tree.isEmpty();
   }

   public List<V> values() {
      return List.iterableList(IterableW.join(this.tree.toList().map(Function.compose(IterableW.wrap(), P2.__2()))));
   }

   public List<K> keys() {
      return this.tree.toList().map(P2.__1());
   }

   public boolean contains(K k) {
      return this.tree.member(P.p(k, Option.none()));
   }

   public Iterator<P2<K, V>> iterator() {
      return IterableW.join(this.tree.toStream().map(P2.map2_(IterableW.wrap())).map(P2.tuple(Function.compose(IterableW.map(), P.p2())))).iterator();
   }

   public Map<K, V> toMutableMap() {
      Map<K, V> m = new java.util.TreeMap();
      Iterator var2 = this.iterator();

      while(var2.hasNext()) {
         P2<K, V> e = (P2)var2.next();
         m.put(e._1(), e._2());
      }

      return m;
   }

   public static <K, V> TreeMap<K, V> fromMutableMap(Ord<K> ord, Map<K, V> m) {
      TreeMap<K, V> t = empty(ord);

      Entry e;
      for(Iterator var3 = m.entrySet().iterator(); var3.hasNext(); t = t.set(e.getKey(), e.getValue())) {
         e = (Entry)var3.next();
      }

      return t;
   }

   public F<K, Option<V>> get() {
      return new F<K, Option<V>>() {
         public Option<V> f(K k) {
            return TreeMap.this.get(k);
         }
      };
   }

   public P2<Boolean, TreeMap<K, V>> update(K k, F<V, V> f) {
      P2<Boolean, Set<P2<K, Option<V>>>> up = this.tree.update(P.p(k, Option.none()), P2.map2_((F)Option.map().f(f)));
      return P.p(up._1(), new TreeMap((Set)up._2()));
   }

   public TreeMap<K, V> update(K k, F<V, V> f, V v) {
      P2<Boolean, TreeMap<K, V>> up = this.update(k, f);
      return (Boolean)up._1() ? (TreeMap)up._2() : this.set(k, v);
   }

   public P3<Set<V>, Option<V>, Set<V>> split(K k) {
      F<Set<P2<K, Option<V>>>, Set<V>> getSome = F1Functions.mapSet(F1Functions.o(Option.fromSome(), P2.__2()), this.tree.ord().comap(F1Functions.o((F)P.p2().f(k), Option.some_())));
      return this.tree.split(P.p(k, Option.none())).map1(getSome).map3(getSome).map2(F1Functions.o(Option.join(), F1Functions.mapOption(P2.__2())));
   }

   public <W> TreeMap<K, W> map(F<V, W> f) {
      F<P2<K, Option<V>>, P2<K, Option<W>>> g = P2.map2_(F1Functions.mapOption(f));
      F<K, P2<K, Option<V>>> coord = (F)Function.flip(P.p2()).f(Option.none());
      Ord<K> o = this.tree.ord().comap(coord);
      return new TreeMap(this.tree.map(ord(o), g));
   }
}
