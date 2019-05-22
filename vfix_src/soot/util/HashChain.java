package soot.util;

import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import soot.Unit;
import soot.UnitBox;
import soot.jimple.GotoStmt;
import soot.jimple.internal.JGotoStmt;

public class HashChain<E> extends AbstractCollection<E> implements Chain<E> {
   protected Map<E, HashChain<E>.Link<E>> map;
   protected E firstItem;
   protected E lastItem;
   protected int stateCount;
   protected static final Iterator<?> emptyIterator = new Iterator() {
      public boolean hasNext() {
         return false;
      }

      public Object next() {
         return null;
      }

      public void remove() {
      }
   };

   public void clear() {
      ++this.stateCount;
      this.firstItem = this.lastItem = null;
      this.map.clear();
   }

   public void swapWith(E out, E in) {
      this.insertBefore(in, out);
      this.remove(out);
   }

   public boolean add(E item) {
      this.addLast(item);
      return true;
   }

   public Collection<E> getElementsUnsorted() {
      return this.map.keySet();
   }

   /** @deprecated */
   @Deprecated
   public static <E> List<E> toList(Chain<E> c) {
      return new ArrayList(c);
   }

   public HashChain() {
      this.map = new ConcurrentHashMap();
      this.stateCount = 0;
      this.firstItem = this.lastItem = null;
   }

   public HashChain(Chain<E> src) {
      this();
      this.addAll(src);
   }

   public boolean follows(E someObject, E someReferenceObject) {
      Iterator it = this.iterator(someObject);

      do {
         if (!it.hasNext()) {
            return true;
         }
      } while(it.next() != someReferenceObject);

      return false;
   }

   public boolean contains(Object o) {
      return this.map.containsKey(o);
   }

   public boolean containsAll(Collection<?> c) {
      Iterator it = c.iterator();

      do {
         if (!it.hasNext()) {
            return true;
         }
      } while(this.map.containsKey(it.next()));

      return false;
   }

   public void insertAfter(E toInsert, E point) {
      if (toInsert == null) {
         throw new RuntimeException("Bad idea! You tried to insert  a null object into a Chain!");
      } else if (this.map.containsKey(toInsert)) {
         throw new RuntimeException("Chain already contains object.");
      } else {
         HashChain<E>.Link<E> temp = (HashChain.Link)this.map.get(point);
         if (temp == null) {
            throw new RuntimeException("Insertion point not found in chain!");
         } else {
            ++this.stateCount;
            HashChain<E>.Link<E> newLink = temp.insertAfter(toInsert);
            this.map.put(toInsert, newLink);
         }
      }
   }

   public void insertAfter(Collection<? extends E> toInsert, E point) {
      if (toInsert == null) {
         throw new RuntimeException("Warning! You tried to insert a null list into a Chain!");
      } else {
         E previousPoint = point;

         Object o;
         for(Iterator var4 = toInsert.iterator(); var4.hasNext(); previousPoint = o) {
            o = var4.next();
            this.insertAfter(o, previousPoint);
         }

      }
   }

   public void insertAfter(List<E> toInsert, E point) {
      this.insertAfter((Collection)toInsert, point);
   }

   public void insertAfter(Chain<E> toInsert, E point) {
      this.insertAfter((Collection)toInsert, point);
   }

   public void insertBefore(E toInsert, E point) {
      if (toInsert == null) {
         throw new RuntimeException("Bad idea! You tried to insert a null object into a Chain!");
      } else if (this.map.containsKey(toInsert)) {
         throw new RuntimeException("Chain already contains object.");
      } else {
         HashChain<E>.Link<E> temp = (HashChain.Link)this.map.get(point);
         if (temp == null) {
            throw new RuntimeException("Insertion point not found in chain!");
         } else {
            ++this.stateCount;
            HashChain<E>.Link<E> newLink = temp.insertBefore(toInsert);
            this.map.put(toInsert, newLink);
         }
      }
   }

   public void insertBefore(Collection<? extends E> toInsert, E point) {
      if (toInsert == null) {
         throw new RuntimeException("Warning! You tried to insert a null list into a Chain!");
      } else {
         Iterator var3 = toInsert.iterator();

         while(var3.hasNext()) {
            E o = var3.next();
            this.insertBefore(o, point);
         }

      }
   }

   public void insertBefore(List<E> toInsert, E point) {
      this.insertBefore((Collection)toInsert, point);
   }

   public void insertBefore(Chain<E> toInsert, E point) {
      this.insertBefore((Collection)toInsert, point);
   }

   public void insertOnEdge(E toInsert, E point_src, E point_tgt) {
      List<E> o = new ArrayList();
      o.add(toInsert);
      this.insertOnEdge((List)o, point_src, point_tgt);
   }

   public void insertOnEdge(Collection<? extends E> toInsert, E point_src, E point_tgt) {
      if (toInsert == null) {
         throw new RuntimeException("Bad idea! You tried to insert a null object into a Chain!");
      } else if (point_src == null && point_tgt != null) {
         ((Unit)point_tgt).redirectJumpsToThisTo((Unit)toInsert.iterator().next());
         this.insertBefore(toInsert, point_tgt);
      } else if (point_src != null && point_tgt == null) {
         this.insertAfter(toInsert, point_src);
      } else if (point_src == null && point_tgt == null) {
         throw new RuntimeException("insertOnEdge failed! Both source and target points are null.");
      } else if (this.getSuccOf(point_src) == point_tgt) {
         List<UnitBox> boxes = ((Unit)point_src).getUnitBoxes();
         Iterator var12 = boxes.iterator();

         while(var12.hasNext()) {
            UnitBox box = (UnitBox)var12.next();
            if (box.getUnit() == point_tgt) {
               box.setUnit((Unit)toInsert.iterator().next());
            }
         }

         this.insertAfter(toInsert, point_src);
      } else {
         boolean validEdgeFound = false;
         E originalPred = this.getPredOf(point_tgt);
         List<UnitBox> boxes = ((Unit)point_src).getUnitBoxes();
         Iterator var7 = boxes.iterator();

         while(var7.hasNext()) {
            UnitBox box = (UnitBox)var7.next();
            if (box.getUnit() == point_tgt) {
               if (point_src instanceof GotoStmt) {
                  box.setUnit((Unit)toInsert.iterator().next());
                  this.insertAfter(toInsert, point_src);
                  E goto_unit = new JGotoStmt((Unit)point_tgt);
                  if (toInsert instanceof List) {
                     List l = (List)toInsert;
                     this.insertAfter((Object)goto_unit, l.get(l.size() - 1));
                  } else {
                     this.insertAfter((Object)goto_unit, toInsert.toArray()[toInsert.size() - 1]);
                  }

                  return;
               }

               box.setUnit((Unit)toInsert.iterator().next());
               validEdgeFound = true;
            }
         }

         if (validEdgeFound) {
            this.insertBefore(toInsert, point_tgt);
            if (originalPred != point_src) {
               if (originalPred instanceof GotoStmt) {
                  return;
               }

               E goto_unit = new JGotoStmt((Unit)point_tgt);
               this.insertBefore((Object)goto_unit, toInsert.iterator().next());
            }

         } else if (this.getSuccOf(point_src) instanceof GotoStmt && ((UnitBox)((Unit)this.getSuccOf(point_src)).getUnitBoxes().get(0)).getUnit() == point_tgt) {
            ((Unit)this.getSuccOf(point_src)).redirectJumpsToThisTo((Unit)toInsert.iterator().next());
            this.insertBefore(toInsert, this.getSuccOf(point_src));
         } else {
            throw new RuntimeException("insertOnEdge failed! No such edge found. The edge on which you want to insert an instrumentation is invalid.");
         }
      }
   }

   public void insertOnEdge(List<E> toInsert, E point_src, E point_tgt) {
      this.insertOnEdge((Collection)toInsert, point_src, point_tgt);
   }

   public void insertOnEdge(Chain<E> toInsert, E point_src, E point_tgt) {
      this.insertOnEdge((Collection)toInsert, point_src, point_tgt);
   }

   public static <T> HashChain<T> listToHashChain(List<T> list) {
      HashChain<T> c = new HashChain();
      Iterator it = list.iterator();

      while(it.hasNext()) {
         c.addLast(it.next());
      }

      return c;
   }

   public boolean remove(Object item) {
      if (item == null) {
         throw new RuntimeException("Bad idea! You tried to remove  a null object from a Chain!");
      } else {
         ++this.stateCount;
         HashChain<E>.Link<E> link = (HashChain.Link)this.map.get(item);
         if (link != null) {
            link.unlinkSelf();
            this.map.remove(item);
            return true;
         } else {
            return false;
         }
      }
   }

   public void addFirst(E item) {
      if (item == null) {
         throw new RuntimeException("Bad idea!  You tried to insert a null object into a Chain!");
      } else {
         ++this.stateCount;
         if (this.map.containsKey(item)) {
            throw new RuntimeException("Chain already contains object.");
         } else {
            HashChain.Link newLink;
            if (this.firstItem != null) {
               HashChain<E>.Link<E> temp = (HashChain.Link)this.map.get(this.firstItem);
               newLink = temp.insertBefore(item);
            } else {
               newLink = new HashChain.Link(item);
               this.firstItem = this.lastItem = item;
            }

            this.map.put(item, newLink);
         }
      }
   }

   public void addLast(E item) {
      if (item == null) {
         throw new RuntimeException("Bad idea! You tried to insert  a null object into a Chain!");
      } else {
         ++this.stateCount;
         if (this.map.containsKey(item)) {
            throw new RuntimeException("Chain already contains object: " + item);
         } else {
            HashChain.Link newLink;
            if (this.lastItem != null) {
               HashChain<E>.Link<E> temp = (HashChain.Link)this.map.get(this.lastItem);
               newLink = temp.insertAfter(item);
            } else {
               newLink = new HashChain.Link(item);
               this.firstItem = this.lastItem = item;
            }

            this.map.put(item, newLink);
         }
      }
   }

   public void removeFirst() {
      ++this.stateCount;
      Object item = this.firstItem;
      ((HashChain.Link)this.map.get(this.firstItem)).unlinkSelf();
      this.map.remove(item);
   }

   public void removeLast() {
      ++this.stateCount;
      Object item = this.lastItem;
      ((HashChain.Link)this.map.get(this.lastItem)).unlinkSelf();
      this.map.remove(item);
   }

   public E getFirst() {
      if (this.firstItem == null) {
         throw new NoSuchElementException();
      } else {
         return this.firstItem;
      }
   }

   public E getLast() {
      if (this.lastItem == null) {
         throw new NoSuchElementException();
      } else {
         return this.lastItem;
      }
   }

   public E getSuccOf(E point) throws NoSuchElementException {
      HashChain.Link link = (HashChain.Link)this.map.get(point);

      try {
         link = link.getNext();
      } catch (NullPointerException var4) {
         throw new NoSuchElementException();
      }

      return link == null ? null : link.getItem();
   }

   public E getPredOf(E point) throws NoSuchElementException {
      HashChain<E>.Link<E> link = (HashChain.Link)this.map.get(point);
      if (point == null) {
         throw new RuntimeException("trying to hash null value.");
      } else {
         try {
            link = link.getPrevious();
         } catch (NullPointerException var4) {
            throw new NoSuchElementException();
         }

         return link == null ? null : link.getItem();
      }
   }

   public Iterator<E> snapshotIterator() {
      return (new ArrayList(this)).iterator();
   }

   public Iterator<E> snapshotIterator(E item) {
      List<E> l = new ArrayList(this.map.size());
      HashChain.LinkIterator it = new HashChain.LinkIterator(item);

      while(it.hasNext()) {
         l.add(it.next());
      }

      return l.iterator();
   }

   public Iterator<E> iterator() {
      return (Iterator)(this.firstItem != null && !this.isEmpty() ? new HashChain.LinkIterator(this.firstItem) : emptyIterator);
   }

   public Iterator<E> iterator(E item) {
      return (Iterator)(this.firstItem != null && !this.isEmpty() ? new HashChain.LinkIterator(item) : emptyIterator);
   }

   public Iterator<E> iterator(E head, E tail) {
      if (this.firstItem != null && !this.isEmpty()) {
         return (Iterator)(head != null && this.getPredOf(head) == tail ? emptyIterator : new HashChain.LinkIterator(head, tail));
      } else {
         return emptyIterator;
      }
   }

   public int size() {
      return this.map.size();
   }

   public String toString() {
      StringBuilder strBuf = new StringBuilder();
      Iterator<E> it = this.iterator();
      boolean b = false;
      strBuf.append("[");

      for(; it.hasNext(); strBuf.append(it.next().toString())) {
         if (!b) {
            b = true;
         } else {
            strBuf.append(", ");
         }
      }

      strBuf.append("]");
      return strBuf.toString();
   }

   public long getModificationCount() {
      return (long)this.stateCount;
   }

   protected class LinkIterator<X extends E> implements Iterator<E> {
      private HashChain<E>.Link<E> currentLink;
      boolean state;
      private X destination;
      private int iteratorStateCount;

      public LinkIterator(X item) {
         HashChain<E>.Link<E> nextLink = (HashChain.Link)HashChain.this.map.get(item);
         if (nextLink == null && item != null) {
            throw new NoSuchElementException("HashChain.LinkIterator(obj) with obj that is not in the chain: " + item.toString());
         } else {
            this.currentLink = HashChain.this.new Link((Object)null);
            this.currentLink.setNext(nextLink);
            this.state = false;
            this.destination = null;
            this.iteratorStateCount = HashChain.this.stateCount;
         }
      }

      public LinkIterator(X from, X to) {
         this(from);
         this.destination = to;
      }

      public boolean hasNext() {
         if (HashChain.this.stateCount != this.iteratorStateCount) {
            throw new ConcurrentModificationException();
         } else if (this.destination == null) {
            return this.currentLink.getNext() != null;
         } else {
            return this.destination != this.currentLink.getItem();
         }
      }

      public E next() throws NoSuchElementException {
         if (HashChain.this.stateCount != this.iteratorStateCount) {
            throw new ConcurrentModificationException();
         } else {
            HashChain<E>.Link<E> temp = this.currentLink.getNext();
            if (temp != null) {
               this.currentLink = temp;
               this.state = true;
               return this.currentLink.getItem();
            } else {
               String exceptionMsg;
               if (this.destination != null && this.destination != this.currentLink.getItem()) {
                  exceptionMsg = "HashChain.LinkIterator.next() reached end of chain without reaching specified tail unit";
               } else {
                  exceptionMsg = "HashChain.LinkIterator.next() called past the end of the Chain";
               }

               throw new NoSuchElementException(exceptionMsg);
            }
         }
      }

      public void remove() throws IllegalStateException {
         if (HashChain.this.stateCount != this.iteratorStateCount) {
            throw new ConcurrentModificationException();
         } else {
            ++HashChain.this.stateCount;
            ++this.iteratorStateCount;
            if (!this.state) {
               throw new IllegalStateException();
            } else {
               this.currentLink.unlinkSelf();
               HashChain.this.map.remove(this.currentLink.getItem());
               this.state = false;
            }
         }
      }

      public String toString() {
         return this.currentLink == null ? "Current object under iterator is null" + super.toString() : this.currentLink.toString();
      }
   }

   protected class Link<X extends E> implements Serializable {
      private HashChain<E>.Link<X> nextLink;
      private HashChain<E>.Link<X> previousLink;
      private X item;

      public Link(X item) {
         this.item = item;
         this.nextLink = this.previousLink = null;
      }

      public HashChain<E>.Link<X> getNext() {
         return this.nextLink;
      }

      public HashChain<E>.Link<X> getPrevious() {
         return this.previousLink;
      }

      public void setNext(HashChain<E>.Link<X> link) {
         this.nextLink = link;
      }

      public void setPrevious(HashChain<E>.Link<X> link) {
         this.previousLink = link;
      }

      public void unlinkSelf() {
         this.bind(this.previousLink, this.nextLink);
      }

      public HashChain<E>.Link<X> insertAfter(X item) {
         HashChain<E>.Link<X> newLink = HashChain.this.new Link(item);
         this.bind(newLink, this.nextLink);
         this.bind(this, newLink);
         return newLink;
      }

      public HashChain<E>.Link<X> insertBefore(X item) {
         HashChain<E>.Link<X> newLink = HashChain.this.new Link(item);
         this.bind(this.previousLink, newLink);
         this.bind(newLink, this);
         return newLink;
      }

      private void bind(HashChain<E>.Link<X> a, HashChain<E>.Link<X> b) {
         if (a == null) {
            HashChain.this.firstItem = b == null ? null : b.item;
         } else {
            a.nextLink = b;
         }

         if (b == null) {
            HashChain.this.lastItem = a == null ? null : a.item;
         } else {
            b.previousLink = a;
         }

      }

      public X getItem() {
         return this.item;
      }

      public String toString() {
         return this.item != null ? this.item.toString() : "Link item is null" + super.toString();
      }
   }
}
