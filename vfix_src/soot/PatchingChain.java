package soot;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import soot.util.Chain;

public class PatchingChain<E extends Unit> extends AbstractCollection<E> implements Chain<E> {
   protected Chain<E> innerChain;

   public PatchingChain(Chain<E> aChain) {
      this.innerChain = aChain;
   }

   public Chain<E> getNonPatchingChain() {
      return this.innerChain;
   }

   public boolean add(E o) {
      return this.innerChain.add(o);
   }

   public void swapWith(E out, E in) {
      this.innerChain.swapWith(out, in);
      out.redirectJumpsToThisTo(in);
   }

   public void insertAfter(E toInsert, E point) {
      this.innerChain.insertAfter((Object)toInsert, point);
   }

   public void insertAfter(List<E> toInsert, E point) {
      this.innerChain.insertAfter((List)toInsert, point);
   }

   public void insertAfter(Chain<E> toInsert, E point) {
      this.innerChain.insertAfter((Chain)toInsert, point);
   }

   public void insertBefore(List<E> toInsert, E point) {
      E previousPoint = point;

      for(int i = toInsert.size() - 1; i >= 0; --i) {
         E o = (Unit)toInsert.get(i);
         this.insertBeforeNoRedirect(o, previousPoint);
         previousPoint = o;
      }

      point.redirectJumpsToThisTo((Unit)toInsert.get(0));
   }

   public void insertBefore(Chain<E> toInsert, E point) {
      Object[] obj = toInsert.toArray();
      E previousPoint = point;

      for(int i = obj.length - 1; i >= 0; --i) {
         E o = (Unit)obj[i];
         this.insertBefore(o, previousPoint);
         previousPoint = o;
      }

   }

   public void insertBefore(E toInsert, E point) {
      point.redirectJumpsToThisTo(toInsert);
      this.innerChain.insertBefore((Object)toInsert, point);
   }

   public void insertBeforeNoRedirect(E toInsert, E point) {
      this.innerChain.insertBefore((Object)toInsert, point);
   }

   public void insertOnEdge(E toInsert, E point_src, E point_tgt) {
      this.innerChain.insertOnEdge((Object)toInsert, point_src, point_tgt);
   }

   public void insertOnEdge(List<E> toInsert, E point_src, E point_tgt) {
      this.innerChain.insertOnEdge((List)toInsert, point_src, point_tgt);
   }

   public void insertOnEdge(Chain<E> toInsert, E point_src, E point_tgt) {
      this.innerChain.insertOnEdge((Chain)toInsert, point_src, point_tgt);
   }

   public boolean follows(E a, E b) {
      return this.innerChain.follows(a, b);
   }

   public boolean remove(Object obj) {
      boolean res = false;
      if (this.contains(obj)) {
         Unit successor = this.getSuccOf((Unit)obj);
         if (successor == null) {
            successor = this.getPredOf((Unit)obj);
         }

         res = this.innerChain.remove(obj);
         ((Unit)obj).redirectJumpsToThisTo(successor);
      }

      return res;
   }

   public boolean contains(Object u) {
      return this.innerChain.contains(u);
   }

   public void addFirst(E u) {
      this.innerChain.addFirst(u);
   }

   public void addLast(E u) {
      this.innerChain.addLast(u);
   }

   public void removeFirst() {
      this.remove(this.innerChain.getFirst());
   }

   public void removeLast() {
      this.remove(this.innerChain.getLast());
   }

   public E getFirst() {
      return (Unit)this.innerChain.getFirst();
   }

   public E getLast() {
      return (Unit)this.innerChain.getLast();
   }

   public E getSuccOf(E point) {
      return (Unit)this.innerChain.getSuccOf(point);
   }

   public E getPredOf(E point) {
      return (Unit)this.innerChain.getPredOf(point);
   }

   public Iterator<E> snapshotIterator() {
      List<E> l = new LinkedList(this);
      return l.iterator();
   }

   public Iterator<E> iterator() {
      return new PatchingChain.PatchingIterator(this.innerChain);
   }

   public Iterator<E> iterator(E u) {
      return new PatchingChain.PatchingIterator(this.innerChain, u);
   }

   public Iterator<E> iterator(E head, E tail) {
      return new PatchingChain.PatchingIterator(this.innerChain, head, tail);
   }

   public int size() {
      return this.innerChain.size();
   }

   public long getModificationCount() {
      return this.innerChain.getModificationCount();
   }

   public Collection<E> getElementsUnsorted() {
      return this.innerChain.getElementsUnsorted();
   }

   protected class PatchingIterator implements Iterator<E> {
      protected Iterator<E> innerIterator = null;
      protected E lastObject;
      protected boolean state = false;

      protected PatchingIterator(Chain<E> innerChain) {
         this.innerIterator = innerChain.iterator();
      }

      protected PatchingIterator(Chain<E> innerChain, E u) {
         this.innerIterator = innerChain.iterator(u);
      }

      protected PatchingIterator(Chain<E> innerChain, E head, E tail) {
         this.innerIterator = innerChain.iterator(head, tail);
      }

      public boolean hasNext() {
         return this.innerIterator.hasNext();
      }

      public E next() {
         this.lastObject = (Unit)this.innerIterator.next();
         this.state = true;
         return this.lastObject;
      }

      public void remove() {
         if (!this.state) {
            throw new IllegalStateException("remove called before first next() call");
         } else {
            Unit successor;
            if ((successor = PatchingChain.this.getSuccOf(this.lastObject)) == null) {
               successor = PatchingChain.this.getPredOf(this.lastObject);
            }

            this.innerIterator.remove();
            this.lastObject.redirectJumpsToThisTo(successor);
         }
      }
   }
}
