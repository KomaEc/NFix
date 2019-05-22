package com.github.javaparser.utils;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.Visitable;
import com.github.javaparser.ast.visitor.VoidVisitor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

public class VisitorList<N extends Node> implements List<N> {
   protected List<VisitorList<N>.EqualsHashcodeOverridingFacade> innerList;
   protected final GenericVisitor<Integer, Void> hashcodeVisitor;
   protected final GenericVisitor<Boolean, Visitable> equalsVisitor;

   public VisitorList(GenericVisitor<Integer, Void> hashcodeVisitor, GenericVisitor<Boolean, Visitable> equalsVisitor) {
      this.hashcodeVisitor = hashcodeVisitor;
      this.equalsVisitor = equalsVisitor;
      this.innerList = new ArrayList();
   }

   public boolean add(N elem) {
      return this.innerList.add(new VisitorList.EqualsHashcodeOverridingFacade(elem));
   }

   public void add(int index, N elem) {
      this.innerList.add(index, new VisitorList.EqualsHashcodeOverridingFacade(elem));
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

   public boolean addAll(int index, Collection<? extends N> col) {
      if (col.isEmpty()) {
         return false;
      } else {
         for(Iterator var3 = col.iterator(); var3.hasNext(); ++index) {
            N elem = (Node)var3.next();
            if (index == this.size()) {
               this.add(elem);
            } else {
               this.add(index, elem);
            }
         }

         return true;
      }
   }

   public void clear() {
      this.innerList.clear();
   }

   public boolean contains(Object elem) {
      return this.innerList.contains(new VisitorList.EqualsHashcodeOverridingFacade((Node)elem));
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

   public N get(int index) {
      return ((VisitorList.EqualsHashcodeOverridingFacade)this.innerList.get(index)).overridden;
   }

   public int indexOf(Object elem) {
      return this.innerList.indexOf(new VisitorList.EqualsHashcodeOverridingFacade((Node)elem));
   }

   public boolean isEmpty() {
      return this.innerList.isEmpty();
   }

   public Iterator<N> iterator() {
      return new Iterator<N>() {
         final Iterator<VisitorList<N>.EqualsHashcodeOverridingFacade> itr;

         {
            this.itr = VisitorList.this.innerList.iterator();
         }

         public boolean hasNext() {
            return this.itr.hasNext();
         }

         public N next() {
            return ((VisitorList.EqualsHashcodeOverridingFacade)this.itr.next()).overridden;
         }

         public void remove() {
            this.itr.remove();
         }
      };
   }

   public int lastIndexOf(Object elem) {
      return this.innerList.lastIndexOf(new VisitorList.EqualsHashcodeOverridingFacade((Node)elem));
   }

   public ListIterator<N> listIterator() {
      return this.listIterator(0);
   }

   public ListIterator<N> listIterator(int index) {
      return new ListIterator<N>() {
         final ListIterator<VisitorList<N>.EqualsHashcodeOverridingFacade> itr;

         {
            this.itr = VisitorList.this.innerList.listIterator(index);
         }

         public boolean hasNext() {
            return this.itr.hasNext();
         }

         public N next() {
            return ((VisitorList.EqualsHashcodeOverridingFacade)this.itr.next()).overridden;
         }

         public void remove() {
            this.itr.remove();
         }

         public void add(N elem) {
            this.itr.add(VisitorList.this.new EqualsHashcodeOverridingFacade(elem));
         }

         public boolean hasPrevious() {
            return this.itr.hasPrevious();
         }

         public int nextIndex() {
            return this.itr.nextIndex();
         }

         public N previous() {
            return ((VisitorList.EqualsHashcodeOverridingFacade)this.itr.previous()).overridden;
         }

         public int previousIndex() {
            return this.itr.previousIndex();
         }

         public void set(N elem) {
            this.itr.set(VisitorList.this.new EqualsHashcodeOverridingFacade(elem));
         }
      };
   }

   public boolean remove(Object elem) {
      return this.innerList.remove(new VisitorList.EqualsHashcodeOverridingFacade((Node)elem));
   }

   public N remove(int index) {
      return ((VisitorList.EqualsHashcodeOverridingFacade)this.innerList.remove(index)).overridden;
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

   public N set(int index, N elem) {
      return ((VisitorList.EqualsHashcodeOverridingFacade)this.innerList.set(index, new VisitorList.EqualsHashcodeOverridingFacade(elem))).overridden;
   }

   public int size() {
      return this.innerList.size();
   }

   public List<N> subList(int fromIndex, int toIndex) {
      return new VisitorList<N>(this.hashcodeVisitor, this.equalsVisitor) {
         {
            this.innerList = VisitorList.this.innerList.subList(fromIndex, toIndex);
         }
      };
   }

   public Object[] toArray() {
      return ((List)this.innerList.stream().map((facade) -> {
         return facade.overridden;
      }).collect(Collectors.toList())).toArray();
   }

   public <T> T[] toArray(T[] arr) {
      return ((List)this.innerList.stream().map((facade) -> {
         return facade.overridden;
      }).collect(Collectors.toList())).toArray(arr);
   }

   public String toString() {
      StringBuilder sb = new StringBuilder("[");
      if (this.size() == 0) {
         return sb.append("]").toString();
      } else {
         Iterator var2 = this.innerList.iterator();

         while(var2.hasNext()) {
            VisitorList<N>.EqualsHashcodeOverridingFacade facade = (VisitorList.EqualsHashcodeOverridingFacade)var2.next();
            sb.append(facade.overridden.toString() + ", ");
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
         return (Integer)this.overridden.accept(VisitorList.this.hashcodeVisitor, (Object)null);
      }

      public boolean equals(final Object obj) {
         return obj != null && obj instanceof VisitorList.EqualsHashcodeOverridingFacade ? (Boolean)this.overridden.accept(VisitorList.this.equalsVisitor, ((VisitorList.EqualsHashcodeOverridingFacade)obj).overridden) : false;
      }
   }
}
