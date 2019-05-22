package fj.data;

import fj.Equal;
import fj.Hash;
import fj.Unit;
import java.util.Collection;
import java.util.Iterator;

public final class HashSet<A> implements Iterable<A> {
   private final HashMap<A, Unit> m;

   public Iterator<A> iterator() {
      return this.toCollection().iterator();
   }

   public HashSet(Equal<A> e, Hash<A> h) {
      this.m = new HashMap(e, h);
   }

   public HashSet(Equal<A> e, Hash<A> h, int initialCapacity) {
      this.m = new HashMap(e, h, initialCapacity);
   }

   public HashSet(Equal<A> e, Hash<A> h, int initialCapacity, float loadFactor) {
      this.m = new HashMap(e, h, initialCapacity, loadFactor);
   }

   public boolean eq(A a1, A a2) {
      return this.m.eq(a1, a2);
   }

   public int hash(A a) {
      return this.m.hash(a);
   }

   public boolean contains(A a) {
      return this.m.contains(a);
   }

   public void set(A a) {
      this.m.set(a, Unit.unit());
   }

   public void clear() {
      this.m.clear();
   }

   public boolean isEmpty() {
      return this.m.isEmpty();
   }

   public int size() {
      return this.m.size();
   }

   public boolean delete(A a) {
      return this.m.getDelete(a).isSome();
   }

   public List<A> toList() {
      return this.m.keys();
   }

   public Collection<A> toCollection() {
      return this.toList().toCollection();
   }
}
