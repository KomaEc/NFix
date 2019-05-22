package org.apache.commons.collections.list;

import java.util.AbstractList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import org.apache.commons.collections.OrderedIterator;

public class TreeList extends AbstractList {
   private TreeList.AVLNode root;
   private int size;

   public TreeList() {
   }

   public TreeList(Collection coll) {
      this.addAll(coll);
   }

   public Object get(int index) {
      this.checkInterval(index, 0, this.size() - 1);
      return this.root.get(index).getValue();
   }

   public int size() {
      return this.size;
   }

   public Iterator iterator() {
      return this.listIterator(0);
   }

   public ListIterator listIterator() {
      return this.listIterator(0);
   }

   public ListIterator listIterator(int fromIndex) {
      this.checkInterval(fromIndex, 0, this.size());
      return new TreeList.TreeListIterator(this, fromIndex);
   }

   public int indexOf(Object object) {
      return this.root == null ? -1 : this.root.indexOf(object, this.root.relativePosition);
   }

   public boolean contains(Object object) {
      return this.indexOf(object) >= 0;
   }

   public Object[] toArray() {
      Object[] array = new Object[this.size()];
      if (this.root != null) {
         this.root.toArray(array, this.root.relativePosition);
      }

      return array;
   }

   public void add(int index, Object obj) {
      ++super.modCount;
      this.checkInterval(index, 0, this.size());
      if (this.root == null) {
         this.root = new TreeList.AVLNode(index, obj, (TreeList.AVLNode)null, (TreeList.AVLNode)null);
      } else {
         this.root = this.root.insert(index, obj);
      }

      ++this.size;
   }

   public Object set(int index, Object obj) {
      this.checkInterval(index, 0, this.size() - 1);
      TreeList.AVLNode node = this.root.get(index);
      Object result = node.value;
      node.setValue(obj);
      return result;
   }

   public Object remove(int index) {
      ++super.modCount;
      this.checkInterval(index, 0, this.size() - 1);
      Object result = this.get(index);
      this.root = this.root.remove(index);
      --this.size;
      return result;
   }

   public void clear() {
      ++super.modCount;
      this.root = null;
      this.size = 0;
   }

   private void checkInterval(int index, int startIndex, int endIndex) {
      if (index < startIndex || index > endIndex) {
         throw new IndexOutOfBoundsException("Invalid index:" + index + ", size=" + this.size());
      }
   }

   // $FF: synthetic method
   static int access$301(TreeList x0) {
      return x0.modCount;
   }

   // $FF: synthetic method
   static int access$501(TreeList x0) {
      return x0.modCount;
   }

   static class TreeListIterator implements ListIterator, OrderedIterator {
      protected final TreeList parent;
      protected TreeList.AVLNode next;
      protected int nextIndex;
      protected TreeList.AVLNode current;
      protected int currentIndex;
      protected int expectedModCount;

      protected TreeListIterator(TreeList parent, int fromIndex) throws IndexOutOfBoundsException {
         this.parent = parent;
         this.expectedModCount = TreeList.access$301(parent);
         this.next = parent.root == null ? null : parent.root.get(fromIndex);
         this.nextIndex = fromIndex;
         this.currentIndex = -1;
      }

      protected void checkModCount() {
         if (TreeList.access$501(this.parent) != this.expectedModCount) {
            throw new ConcurrentModificationException();
         }
      }

      public boolean hasNext() {
         return this.nextIndex < this.parent.size();
      }

      public Object next() {
         this.checkModCount();
         if (!this.hasNext()) {
            throw new NoSuchElementException("No element at index " + this.nextIndex + ".");
         } else {
            if (this.next == null) {
               this.next = this.parent.root.get(this.nextIndex);
            }

            Object value = this.next.getValue();
            this.current = this.next;
            this.currentIndex = this.nextIndex++;
            this.next = this.next.next();
            return value;
         }
      }

      public boolean hasPrevious() {
         return this.nextIndex > 0;
      }

      public Object previous() {
         this.checkModCount();
         if (!this.hasPrevious()) {
            throw new NoSuchElementException("Already at start of list.");
         } else {
            if (this.next == null) {
               this.next = this.parent.root.get(this.nextIndex - 1);
            } else {
               this.next = this.next.previous();
            }

            Object value = this.next.getValue();
            this.current = this.next;
            this.currentIndex = --this.nextIndex;
            return value;
         }
      }

      public int nextIndex() {
         return this.nextIndex;
      }

      public int previousIndex() {
         return this.nextIndex() - 1;
      }

      public void remove() {
         this.checkModCount();
         if (this.currentIndex == -1) {
            throw new IllegalStateException();
         } else {
            if (this.nextIndex == this.currentIndex) {
               this.next = this.next.next();
               this.parent.remove(this.currentIndex);
            } else {
               this.parent.remove(this.currentIndex);
               --this.nextIndex;
            }

            this.current = null;
            this.currentIndex = -1;
            ++this.expectedModCount;
         }
      }

      public void set(Object obj) {
         this.checkModCount();
         if (this.current == null) {
            throw new IllegalStateException();
         } else {
            this.current.setValue(obj);
         }
      }

      public void add(Object obj) {
         this.checkModCount();
         this.parent.add(this.nextIndex, obj);
         this.current = null;
         this.currentIndex = -1;
         ++this.nextIndex;
         ++this.expectedModCount;
      }
   }

   static class AVLNode {
      private TreeList.AVLNode left;
      private boolean leftIsPrevious;
      private TreeList.AVLNode right;
      private boolean rightIsNext;
      private int height;
      private int relativePosition;
      private Object value;

      private AVLNode(int relativePosition, Object obj, TreeList.AVLNode rightFollower, TreeList.AVLNode leftFollower) {
         this.relativePosition = relativePosition;
         this.value = obj;
         this.rightIsNext = true;
         this.leftIsPrevious = true;
         this.right = rightFollower;
         this.left = leftFollower;
      }

      Object getValue() {
         return this.value;
      }

      void setValue(Object obj) {
         this.value = obj;
      }

      TreeList.AVLNode get(int index) {
         int indexRelativeToMe = index - this.relativePosition;
         if (indexRelativeToMe == 0) {
            return this;
         } else {
            TreeList.AVLNode nextNode = indexRelativeToMe < 0 ? this.getLeftSubTree() : this.getRightSubTree();
            return nextNode == null ? null : nextNode.get(indexRelativeToMe);
         }
      }

      int indexOf(Object object, int index) {
         if (this.getLeftSubTree() != null) {
            int result = this.left.indexOf(object, index + this.left.relativePosition);
            if (result != -1) {
               return result;
            }
         }

         if (this.value == null ? this.value == object : this.value.equals(object)) {
            return index;
         } else {
            return this.getRightSubTree() != null ? this.right.indexOf(object, index + this.right.relativePosition) : -1;
         }
      }

      void toArray(Object[] array, int index) {
         array[index] = this.value;
         if (this.getLeftSubTree() != null) {
            this.left.toArray(array, index + this.left.relativePosition);
         }

         if (this.getRightSubTree() != null) {
            this.right.toArray(array, index + this.right.relativePosition);
         }

      }

      TreeList.AVLNode next() {
         return !this.rightIsNext && this.right != null ? this.right.min() : this.right;
      }

      TreeList.AVLNode previous() {
         return !this.leftIsPrevious && this.left != null ? this.left.max() : this.left;
      }

      TreeList.AVLNode insert(int index, Object obj) {
         int indexRelativeToMe = index - this.relativePosition;
         return indexRelativeToMe <= 0 ? this.insertOnLeft(indexRelativeToMe, obj) : this.insertOnRight(indexRelativeToMe, obj);
      }

      private TreeList.AVLNode insertOnLeft(int indexRelativeToMe, Object obj) {
         if (this.getLeftSubTree() == null) {
            this.setLeft(new TreeList.AVLNode(-1, obj, this, this.left), (TreeList.AVLNode)null);
         } else {
            this.setLeft(this.left.insert(indexRelativeToMe, obj), (TreeList.AVLNode)null);
         }

         if (this.relativePosition >= 0) {
            ++this.relativePosition;
         }

         TreeList.AVLNode ret = this.balance();
         this.recalcHeight();
         return ret;
      }

      private TreeList.AVLNode insertOnRight(int indexRelativeToMe, Object obj) {
         if (this.getRightSubTree() == null) {
            this.setRight(new TreeList.AVLNode(1, obj, this.right, this), (TreeList.AVLNode)null);
         } else {
            this.setRight(this.right.insert(indexRelativeToMe, obj), (TreeList.AVLNode)null);
         }

         if (this.relativePosition < 0) {
            --this.relativePosition;
         }

         TreeList.AVLNode ret = this.balance();
         this.recalcHeight();
         return ret;
      }

      private TreeList.AVLNode getLeftSubTree() {
         return this.leftIsPrevious ? null : this.left;
      }

      private TreeList.AVLNode getRightSubTree() {
         return this.rightIsNext ? null : this.right;
      }

      private TreeList.AVLNode max() {
         return this.getRightSubTree() == null ? this : this.right.max();
      }

      private TreeList.AVLNode min() {
         return this.getLeftSubTree() == null ? this : this.left.min();
      }

      TreeList.AVLNode remove(int index) {
         int indexRelativeToMe = index - this.relativePosition;
         if (indexRelativeToMe == 0) {
            return this.removeSelf();
         } else {
            if (indexRelativeToMe > 0) {
               this.setRight(this.right.remove(indexRelativeToMe), this.right.right);
               if (this.relativePosition < 0) {
                  ++this.relativePosition;
               }
            } else {
               this.setLeft(this.left.remove(indexRelativeToMe), this.left.left);
               if (this.relativePosition > 0) {
                  --this.relativePosition;
               }
            }

            this.recalcHeight();
            return this.balance();
         }
      }

      private TreeList.AVLNode removeMax() {
         if (this.getRightSubTree() == null) {
            return this.removeSelf();
         } else {
            this.setRight(this.right.removeMax(), this.right.right);
            if (this.relativePosition < 0) {
               ++this.relativePosition;
            }

            this.recalcHeight();
            return this.balance();
         }
      }

      private TreeList.AVLNode removeMin() {
         if (this.getLeftSubTree() == null) {
            return this.removeSelf();
         } else {
            this.setLeft(this.left.removeMin(), this.left.left);
            if (this.relativePosition > 0) {
               --this.relativePosition;
            }

            this.recalcHeight();
            return this.balance();
         }
      }

      private TreeList.AVLNode removeSelf() {
         if (this.getRightSubTree() == null && this.getLeftSubTree() == null) {
            return null;
         } else {
            TreeList.AVLNode var10000;
            if (this.getRightSubTree() == null) {
               if (this.relativePosition > 0) {
                  var10000 = this.left;
                  var10000.relativePosition += this.relativePosition + (this.relativePosition > 0 ? 0 : 1);
               }

               this.left.max().setRight((TreeList.AVLNode)null, this.right);
               return this.left;
            } else if (this.getLeftSubTree() == null) {
               var10000 = this.right;
               var10000.relativePosition += this.relativePosition - (this.relativePosition < 0 ? 0 : 1);
               this.right.min().setLeft((TreeList.AVLNode)null, this.left);
               return this.right;
            } else {
               TreeList.AVLNode rightMin;
               if (this.heightRightMinusLeft() > 0) {
                  rightMin = this.right.min();
                  this.value = rightMin.value;
                  if (this.leftIsPrevious) {
                     this.left = rightMin.left;
                  }

                  this.right = this.right.removeMin();
                  if (this.relativePosition < 0) {
                     ++this.relativePosition;
                  }
               } else {
                  rightMin = this.left.max();
                  this.value = rightMin.value;
                  if (this.rightIsNext) {
                     this.right = rightMin.right;
                  }

                  TreeList.AVLNode leftPrevious = this.left.left;
                  this.left = this.left.removeMax();
                  if (this.left == null) {
                     this.left = leftPrevious;
                     this.leftIsPrevious = true;
                  }

                  if (this.relativePosition > 0) {
                     --this.relativePosition;
                  }
               }

               this.recalcHeight();
               return this;
            }
         }
      }

      private TreeList.AVLNode balance() {
         switch(this.heightRightMinusLeft()) {
         case -2:
            if (this.left.heightRightMinusLeft() > 0) {
               this.setLeft(this.left.rotateLeft(), (TreeList.AVLNode)null);
            }

            return this.rotateRight();
         case -1:
         case 0:
         case 1:
            return this;
         case 2:
            if (this.right.heightRightMinusLeft() < 0) {
               this.setRight(this.right.rotateRight(), (TreeList.AVLNode)null);
            }

            return this.rotateLeft();
         default:
            throw new RuntimeException("tree inconsistent!");
         }
      }

      private int getOffset(TreeList.AVLNode node) {
         return node == null ? 0 : node.relativePosition;
      }

      private int setOffset(TreeList.AVLNode node, int newOffest) {
         if (node == null) {
            return 0;
         } else {
            int oldOffset = this.getOffset(node);
            node.relativePosition = newOffest;
            return oldOffset;
         }
      }

      private void recalcHeight() {
         this.height = Math.max(this.getLeftSubTree() == null ? -1 : this.getLeftSubTree().height, this.getRightSubTree() == null ? -1 : this.getRightSubTree().height) + 1;
      }

      private int getHeight(TreeList.AVLNode node) {
         return node == null ? -1 : node.height;
      }

      private int heightRightMinusLeft() {
         return this.getHeight(this.getRightSubTree()) - this.getHeight(this.getLeftSubTree());
      }

      private TreeList.AVLNode rotateLeft() {
         TreeList.AVLNode newTop = this.right;
         TreeList.AVLNode movedNode = this.getRightSubTree().getLeftSubTree();
         int newTopPosition = this.relativePosition + this.getOffset(newTop);
         int myNewPosition = -newTop.relativePosition;
         int movedPosition = this.getOffset(newTop) + this.getOffset(movedNode);
         this.setRight(movedNode, newTop);
         newTop.setLeft(this, (TreeList.AVLNode)null);
         this.setOffset(newTop, newTopPosition);
         this.setOffset(this, myNewPosition);
         this.setOffset(movedNode, movedPosition);
         return newTop;
      }

      private TreeList.AVLNode rotateRight() {
         TreeList.AVLNode newTop = this.left;
         TreeList.AVLNode movedNode = this.getLeftSubTree().getRightSubTree();
         int newTopPosition = this.relativePosition + this.getOffset(newTop);
         int myNewPosition = -newTop.relativePosition;
         int movedPosition = this.getOffset(newTop) + this.getOffset(movedNode);
         this.setLeft(movedNode, newTop);
         newTop.setRight(this, (TreeList.AVLNode)null);
         this.setOffset(newTop, newTopPosition);
         this.setOffset(this, myNewPosition);
         this.setOffset(movedNode, movedPosition);
         return newTop;
      }

      private void setLeft(TreeList.AVLNode node, TreeList.AVLNode previous) {
         this.leftIsPrevious = node == null;
         this.left = this.leftIsPrevious ? previous : node;
         this.recalcHeight();
      }

      private void setRight(TreeList.AVLNode node, TreeList.AVLNode next) {
         this.rightIsNext = node == null;
         this.right = this.rightIsNext ? next : node;
         this.recalcHeight();
      }

      public String toString() {
         return "AVLNode(" + this.relativePosition + "," + (this.left != null) + "," + this.value + "," + (this.getRightSubTree() != null) + ", faedelung " + this.rightIsNext + " )";
      }

      // $FF: synthetic method
      AVLNode(int x0, Object x1, TreeList.AVLNode x2, TreeList.AVLNode x3, Object x4) {
         this(x0, x1, x2, x3);
      }
   }
}
