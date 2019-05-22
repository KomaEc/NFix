package com.github.javaparser.utils;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.Visitable;
import com.github.javaparser.ast.visitor.VoidVisitor;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class VisitorSet<N extends Node> implements Set<N> {
   private final Set<VisitorSet<N>.EqualsHashcodeOverridingFacade> innerSet = new HashSet();
   private final GenericVisitor<Integer, Void> hashcodeVisitor;
   private final GenericVisitor<Boolean, Visitable> equalsVisitor;

   public VisitorSet(GenericVisitor<Integer, Void> hashcodeVisitor, GenericVisitor<Boolean, Visitable> equalsVisitor) {
      this.hashcodeVisitor = hashcodeVisitor;
      this.equalsVisitor = equalsVisitor;
   }

   public boolean add(N elem) {
      return this.innerSet.add(new VisitorSet.EqualsHashcodeOverridingFacade(elem));
   }

   public boolean addAll(Collection<? extends N> col) {
      boolean modified = false;
      Iterator var3 = col.iterator();

      while(var3.hasNext()) {
         N elem = (Node)var3.next();
         if (this.add(elem)) {
            modified = true;
         }
      }

      return modified;
   }

   public void clear() {
      this.innerSet.clear();
   }

   public boolean contains(Object elem) {
      return this.innerSet.contains(new VisitorSet.EqualsHashcodeOverridingFacade((Node)elem));
   }

   public boolean containsAll(Collection<?> col) {
      Iterator var2 = col.iterator();

      Object elem;
      do {
         if (!var2.hasNext()) {
            return true;
         }

         elem = var2.next();
      } while(this.contains(elem));

      return false;
   }

   public boolean isEmpty() {
      return this.innerSet.isEmpty();
   }

   public Iterator<N> iterator() {
      return new Iterator<N>() {
         final Iterator<VisitorSet<N>.EqualsHashcodeOverridingFacade> itr;

         {
            this.itr = VisitorSet.this.innerSet.iterator();
         }

         public boolean hasNext() {
            return this.itr.hasNext();
         }

         public N next() {
            return ((VisitorSet.EqualsHashcodeOverridingFacade)this.itr.next()).overridden;
         }

         public void remove() {
            this.itr.remove();
         }
      };
   }

   public boolean remove(Object elem) {
      return this.innerSet.remove(new VisitorSet.EqualsHashcodeOverridingFacade((Node)elem));
   }

   public boolean removeAll(Collection<?> col) {
      boolean modified = false;
      Iterator var3 = col.iterator();

      while(var3.hasNext()) {
         Object elem = var3.next();
         if (this.remove(elem)) {
            modified = true;
         }
      }

      return modified;
   }

   public boolean retainAll(Collection<?> col) {
      int oldSize = this.size();
      this.clear();
      this.addAll(col);
      return this.size() != oldSize;
   }

   public int size() {
      return this.innerSet.size();
   }

   public Object[] toArray() {
      return ((List)this.innerSet.stream().map((facade) -> {
         return facade.overridden;
      }).collect(Collectors.toList())).toArray();
   }

   public <T> T[] toArray(T[] arr) {
      return ((List)this.innerSet.stream().map((facade) -> {
         return facade.overridden;
      }).collect(Collectors.toList())).toArray(arr);
   }

   public String toString() {
      StringBuilder sb = new StringBuilder("[");
      if (this.size() == 0) {
         return sb.append("]").toString();
      } else {
         Iterator var2 = this.innerSet.iterator();

         while(var2.hasNext()) {
            VisitorSet<N>.EqualsHashcodeOverridingFacade facade = (VisitorSet.EqualsHashcodeOverridingFacade)var2.next();
            sb.append(facade.overridden.toString() + ",");
         }

         return sb.replace(sb.length() - 2, sb.length(), "]").toString();
      }
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
         return (Integer)this.overridden.accept(VisitorSet.this.hashcodeVisitor, (Object)null);
      }

      public boolean equals(final Object obj) {
         return obj != null && obj instanceof VisitorSet.EqualsHashcodeOverridingFacade ? (Boolean)this.overridden.accept(VisitorSet.this.equalsVisitor, ((VisitorSet.EqualsHashcodeOverridingFacade)obj).overridden) : false;
      }
   }
}
