package com.github.javaparser.utils;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.Visitable;
import com.github.javaparser.ast.visitor.VoidVisitor;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class VisitorMap<N extends Node, V> implements Map<N, V> {
   private final Map<VisitorMap<N, V>.EqualsHashcodeOverridingFacade, V> innerMap = new HashMap();
   private final GenericVisitor<Integer, Void> hashcodeVisitor;
   private final GenericVisitor<Boolean, Visitable> equalsVisitor;

   public VisitorMap(GenericVisitor<Integer, Void> hashcodeVisitor, GenericVisitor<Boolean, Visitable> equalsVisitor) {
      this.hashcodeVisitor = hashcodeVisitor;
      this.equalsVisitor = equalsVisitor;
   }

   public int size() {
      return this.innerMap.size();
   }

   public boolean isEmpty() {
      return this.innerMap.isEmpty();
   }

   public boolean containsKey(Object key) {
      return this.innerMap.containsKey(new VisitorMap.EqualsHashcodeOverridingFacade((Node)key));
   }

   public boolean containsValue(Object value) {
      return this.innerMap.containsValue(value);
   }

   public V get(Object key) {
      return this.innerMap.get(new VisitorMap.EqualsHashcodeOverridingFacade((Node)key));
   }

   public V put(N key, V value) {
      return this.innerMap.put(new VisitorMap.EqualsHashcodeOverridingFacade(key), value);
   }

   public V remove(Object key) {
      return this.innerMap.remove(new VisitorMap.EqualsHashcodeOverridingFacade((Node)key));
   }

   public void putAll(Map<? extends N, ? extends V> m) {
      m.forEach(this::put);
   }

   public void clear() {
      this.innerMap.clear();
   }

   public Set<N> keySet() {
      return (Set)this.innerMap.keySet().stream().map((k) -> {
         return k.overridden;
      }).collect(Collectors.toSet());
   }

   public Collection<V> values() {
      return this.innerMap.values();
   }

   public Set<Entry<N, V>> entrySet() {
      return (Set)this.innerMap.entrySet().stream().map((e) -> {
         return new SimpleEntry(((VisitorMap.EqualsHashcodeOverridingFacade)e.getKey()).overridden, e.getValue());
      }).collect(Collectors.toSet());
   }

   private class EqualsHashcodeOverridingFacade implements Visitable {
      private final N overridden;

      EqualsHashcodeOverridingFacade(N overridden) {
         this.overridden = overridden;
      }

      public <R, A> R accept(GenericVisitor<R, A> v, A arg) {
         throw new AssertionError();
      }

      public <A> void accept(VoidVisitor<A> v, A arg) {
         throw new AssertionError();
      }

      public final int hashCode() {
         return (Integer)this.overridden.accept(VisitorMap.this.hashcodeVisitor, (Object)null);
      }

      public boolean equals(final Object obj) {
         return obj != null && obj instanceof VisitorMap.EqualsHashcodeOverridingFacade ? (Boolean)this.overridden.accept(VisitorMap.this.equalsVisitor, ((VisitorMap.EqualsHashcodeOverridingFacade)obj).overridden) : false;
      }
   }
}
