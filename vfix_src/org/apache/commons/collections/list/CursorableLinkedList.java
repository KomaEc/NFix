package org.apache.commons.collections.list;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class CursorableLinkedList extends AbstractLinkedList implements Serializable {
   private static final long serialVersionUID = 8836393098519411393L;
   protected transient List cursors = new ArrayList();

   public CursorableLinkedList() {
      this.init();
   }

   public CursorableLinkedList(Collection coll) {
      super(coll);
   }

   protected void init() {
      super.init();
      this.cursors = new ArrayList();
   }

   public Iterator iterator() {
      return super.listIterator(0);
   }

   public ListIterator listIterator() {
      return this.cursor(0);
   }

   public ListIterator listIterator(int fromIndex) {
      return this.cursor(fromIndex);
   }

   public CursorableLinkedList.Cursor cursor() {
      return this.cursor(0);
   }

   public CursorableLinkedList.Cursor cursor(int fromIndex) {
      CursorableLinkedList.Cursor cursor = new CursorableLinkedList.Cursor(this, fromIndex);
      this.registerCursor(cursor);
      return cursor;
   }

   protected void updateNode(AbstractLinkedList.Node node, Object value) {
      super.updateNode(node, value);
      this.broadcastNodeChanged(node);
   }

   protected void addNode(AbstractLinkedList.Node nodeToInsert, AbstractLinkedList.Node insertBeforeNode) {
      super.addNode(nodeToInsert, insertBeforeNode);
      this.broadcastNodeInserted(nodeToInsert);
   }

   protected void removeNode(AbstractLinkedList.Node node) {
      super.removeNode(node);
      this.broadcastNodeRemoved(node);
   }

   protected void removeAllNodes() {
      if (this.size() > 0) {
         Iterator it = this.iterator();

         while(it.hasNext()) {
            it.next();
            it.remove();
         }
      }

   }

   protected void registerCursor(CursorableLinkedList.Cursor cursor) {
      Iterator it = this.cursors.iterator();

      while(it.hasNext()) {
         WeakReference ref = (WeakReference)it.next();
         if (ref.get() == null) {
            it.remove();
         }
      }

      this.cursors.add(new WeakReference(cursor));
   }

   protected void unregisterCursor(CursorableLinkedList.Cursor cursor) {
      Iterator it = this.cursors.iterator();

      while(it.hasNext()) {
         WeakReference ref = (WeakReference)it.next();
         CursorableLinkedList.Cursor cur = (CursorableLinkedList.Cursor)ref.get();
         if (cur == null) {
            it.remove();
         } else if (cur == cursor) {
            ref.clear();
            it.remove();
            break;
         }
      }

   }

   protected void broadcastNodeChanged(AbstractLinkedList.Node node) {
      Iterator it = this.cursors.iterator();

      while(it.hasNext()) {
         WeakReference ref = (WeakReference)it.next();
         CursorableLinkedList.Cursor cursor = (CursorableLinkedList.Cursor)ref.get();
         if (cursor == null) {
            it.remove();
         } else {
            cursor.nodeChanged(node);
         }
      }

   }

   protected void broadcastNodeRemoved(AbstractLinkedList.Node node) {
      Iterator it = this.cursors.iterator();

      while(it.hasNext()) {
         WeakReference ref = (WeakReference)it.next();
         CursorableLinkedList.Cursor cursor = (CursorableLinkedList.Cursor)ref.get();
         if (cursor == null) {
            it.remove();
         } else {
            cursor.nodeRemoved(node);
         }
      }

   }

   protected void broadcastNodeInserted(AbstractLinkedList.Node node) {
      Iterator it = this.cursors.iterator();

      while(it.hasNext()) {
         WeakReference ref = (WeakReference)it.next();
         CursorableLinkedList.Cursor cursor = (CursorableLinkedList.Cursor)ref.get();
         if (cursor == null) {
            it.remove();
         } else {
            cursor.nodeInserted(node);
         }
      }

   }

   private void writeObject(ObjectOutputStream out) throws IOException {
      out.defaultWriteObject();
      this.doWriteObject(out);
   }

   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
      in.defaultReadObject();
      this.doReadObject(in);
   }

   protected ListIterator createSubListListIterator(AbstractLinkedList.LinkedSubList subList, int fromIndex) {
      CursorableLinkedList.SubCursor cursor = new CursorableLinkedList.SubCursor(subList, fromIndex);
      this.registerCursor(cursor);
      return cursor;
   }

   protected static class SubCursor extends CursorableLinkedList.Cursor {
      protected final AbstractLinkedList.LinkedSubList sub;

      protected SubCursor(AbstractLinkedList.LinkedSubList sub, int index) {
         super((CursorableLinkedList)sub.parent, index + sub.offset);
         this.sub = sub;
      }

      public boolean hasNext() {
         return this.nextIndex() < this.sub.size;
      }

      public boolean hasPrevious() {
         return this.previousIndex() >= 0;
      }

      public int nextIndex() {
         return super.nextIndex() - this.sub.offset;
      }

      public void add(Object obj) {
         super.add(obj);
         this.sub.expectedModCount = super.parent.modCount;
         ++this.sub.size;
      }

      public void remove() {
         super.remove();
         this.sub.expectedModCount = super.parent.modCount;
         --this.sub.size;
      }
   }

   public static class Cursor extends AbstractLinkedList.LinkedListIterator {
      boolean valid = true;
      boolean nextIndexValid = true;
      boolean currentRemovedByAnother = false;

      protected Cursor(CursorableLinkedList parent, int index) {
         super(parent, index);
         this.valid = true;
      }

      public void remove() {
         if (super.current != null || !this.currentRemovedByAnother) {
            this.checkModCount();
            super.parent.removeNode(this.getLastNodeReturned());
         }

         this.currentRemovedByAnother = false;
      }

      public void add(Object obj) {
         super.add(obj);
         super.next = super.next.next;
      }

      public int nextIndex() {
         if (!this.nextIndexValid) {
            if (super.next == super.parent.header) {
               super.nextIndex = super.parent.size();
            } else {
               int pos = 0;

               for(AbstractLinkedList.Node temp = super.parent.header.next; temp != super.next; temp = temp.next) {
                  ++pos;
               }

               super.nextIndex = pos;
            }

            this.nextIndexValid = true;
         }

         return super.nextIndex;
      }

      protected void nodeChanged(AbstractLinkedList.Node node) {
      }

      protected void nodeRemoved(AbstractLinkedList.Node node) {
         if (node == super.next && node == super.current) {
            super.next = node.next;
            super.current = null;
            this.currentRemovedByAnother = true;
         } else if (node == super.next) {
            super.next = node.next;
            this.currentRemovedByAnother = false;
         } else if (node == super.current) {
            super.current = null;
            this.currentRemovedByAnother = true;
            --super.nextIndex;
         } else {
            this.nextIndexValid = false;
            this.currentRemovedByAnother = false;
         }

      }

      protected void nodeInserted(AbstractLinkedList.Node node) {
         if (node.previous == super.current) {
            super.next = node;
         } else if (super.next.previous == node) {
            super.next = node;
         } else {
            this.nextIndexValid = false;
         }

      }

      protected void checkModCount() {
         if (!this.valid) {
            throw new ConcurrentModificationException("Cursor closed");
         }
      }

      public void close() {
         if (this.valid) {
            ((CursorableLinkedList)super.parent).unregisterCursor(this);
            this.valid = false;
         }

      }
   }
}
