package org.apache.commons.collections;

import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Map.Entry;

/** @deprecated */
public final class DoubleOrderedMap extends AbstractMap {
   private static final int KEY = 0;
   private static final int VALUE = 1;
   private static final int SUM_OF_INDICES = 1;
   private static final int FIRST_INDEX = 0;
   private static final int NUMBER_OF_INDICES = 2;
   private static final String[] dataName = new String[]{"key", "value"};
   private DoubleOrderedMap.Node[] rootNode = new DoubleOrderedMap.Node[]{null, null};
   private int nodeCount = 0;
   private int modifications = 0;
   private Set[] setOfKeys = new Set[]{null, null};
   private Set[] setOfEntries = new Set[]{null, null};
   private Collection[] collectionOfValues = new Collection[]{null, null};

   public DoubleOrderedMap() {
   }

   public DoubleOrderedMap(Map map) throws ClassCastException, NullPointerException, IllegalArgumentException {
      this.putAll(map);
   }

   public Object getKeyForValue(Object value) throws ClassCastException, NullPointerException {
      return this.doGet((Comparable)value, 1);
   }

   public Object removeValue(Object value) {
      return this.doRemove((Comparable)value, 1);
   }

   public Set entrySetByValue() {
      if (this.setOfEntries[1] == null) {
         this.setOfEntries[1] = new AbstractSet() {
            public Iterator iterator() {
               return new DoubleOrderedMap.DoubleOrderedMapIterator(1) {
                  protected Object doGetNext() {
                     return super.lastReturnedNode;
                  }
               };
            }

            public boolean contains(Object o) {
               if (!(o instanceof Entry)) {
                  return false;
               } else {
                  Entry entry = (Entry)o;
                  Object key = entry.getKey();
                  DoubleOrderedMap.Node node = DoubleOrderedMap.this.lookup((Comparable)entry.getValue(), 1);
                  return node != null && node.getData(0).equals(key);
               }
            }

            public boolean remove(Object o) {
               if (!(o instanceof Entry)) {
                  return false;
               } else {
                  Entry entry = (Entry)o;
                  Object key = entry.getKey();
                  DoubleOrderedMap.Node node = DoubleOrderedMap.this.lookup((Comparable)entry.getValue(), 1);
                  if (node != null && node.getData(0).equals(key)) {
                     DoubleOrderedMap.this.doRedBlackDelete(node);
                     return true;
                  } else {
                     return false;
                  }
               }
            }

            public int size() {
               return DoubleOrderedMap.this.size();
            }

            public void clear() {
               DoubleOrderedMap.this.clear();
            }

            // $FF: synthetic method
            static DoubleOrderedMap access$000(Object x0) {
               return DoubleOrderedMap.this;
            }
         };
      }

      return this.setOfEntries[1];
   }

   public Set keySetByValue() {
      if (this.setOfKeys[1] == null) {
         this.setOfKeys[1] = new AbstractSet() {
            public Iterator iterator() {
               return new DoubleOrderedMap.DoubleOrderedMapIterator(1) {
                  protected Object doGetNext() {
                     return super.lastReturnedNode.getData(0);
                  }
               };
            }

            public int size() {
               return DoubleOrderedMap.this.size();
            }

            public boolean contains(Object o) {
               return DoubleOrderedMap.this.containsKey(o);
            }

            public boolean remove(Object o) {
               int oldnodeCount = DoubleOrderedMap.this.nodeCount;
               DoubleOrderedMap.this.remove(o);
               return DoubleOrderedMap.this.nodeCount != oldnodeCount;
            }

            public void clear() {
               DoubleOrderedMap.this.clear();
            }

            // $FF: synthetic method
            static DoubleOrderedMap access$400(Object x0) {
               return DoubleOrderedMap.this;
            }
         };
      }

      return this.setOfKeys[1];
   }

   public Collection valuesByValue() {
      if (this.collectionOfValues[1] == null) {
         this.collectionOfValues[1] = new AbstractCollection() {
            public Iterator iterator() {
               return new DoubleOrderedMap.DoubleOrderedMapIterator(1) {
                  protected Object doGetNext() {
                     return super.lastReturnedNode.getData(1);
                  }
               };
            }

            public int size() {
               return DoubleOrderedMap.this.size();
            }

            public boolean contains(Object o) {
               return DoubleOrderedMap.this.containsValue(o);
            }

            public boolean remove(Object o) {
               int oldnodeCount = DoubleOrderedMap.this.nodeCount;
               DoubleOrderedMap.this.removeValue(o);
               return DoubleOrderedMap.this.nodeCount != oldnodeCount;
            }

            public boolean removeAll(Collection c) {
               boolean modified = false;
               Iterator iter = c.iterator();

               while(iter.hasNext()) {
                  if (DoubleOrderedMap.this.removeValue(iter.next()) != null) {
                     modified = true;
                  }
               }

               return modified;
            }

            public void clear() {
               DoubleOrderedMap.this.clear();
            }

            // $FF: synthetic method
            static DoubleOrderedMap access$600(Object x0) {
               return DoubleOrderedMap.this;
            }
         };
      }

      return this.collectionOfValues[1];
   }

   private Object doRemove(Comparable o, int index) {
      DoubleOrderedMap.Node node = this.lookup(o, index);
      Object rval = null;
      if (node != null) {
         rval = node.getData(this.oppositeIndex(index));
         this.doRedBlackDelete(node);
      }

      return rval;
   }

   private Object doGet(Comparable o, int index) {
      checkNonNullComparable(o, index);
      DoubleOrderedMap.Node node = this.lookup(o, index);
      return node == null ? null : node.getData(this.oppositeIndex(index));
   }

   private int oppositeIndex(int index) {
      return 1 - index;
   }

   private DoubleOrderedMap.Node lookup(Comparable data, int index) {
      DoubleOrderedMap.Node rval = null;

      int cmp;
      for(DoubleOrderedMap.Node node = this.rootNode[index]; node != null; node = cmp < 0 ? node.getLeft(index) : node.getRight(index)) {
         cmp = compare(data, node.getData(index));
         if (cmp == 0) {
            rval = node;
            break;
         }
      }

      return rval;
   }

   private static int compare(Comparable o1, Comparable o2) {
      return o1.compareTo(o2);
   }

   private static DoubleOrderedMap.Node leastNode(DoubleOrderedMap.Node node, int index) {
      DoubleOrderedMap.Node rval = node;
      if (node != null) {
         while(rval.getLeft(index) != null) {
            rval = rval.getLeft(index);
         }
      }

      return rval;
   }

   private DoubleOrderedMap.Node nextGreater(DoubleOrderedMap.Node node, int index) {
      DoubleOrderedMap.Node rval = null;
      if (node == null) {
         rval = null;
      } else if (node.getRight(index) != null) {
         rval = leastNode(node.getRight(index), index);
      } else {
         DoubleOrderedMap.Node parent = node.getParent(index);

         for(DoubleOrderedMap.Node child = node; parent != null && child == parent.getRight(index); parent = parent.getParent(index)) {
            child = parent;
         }

         rval = parent;
      }

      return rval;
   }

   private static void copyColor(DoubleOrderedMap.Node from, DoubleOrderedMap.Node to, int index) {
      if (to != null) {
         if (from == null) {
            to.setBlack(index);
         } else {
            to.copyColor(from, index);
         }
      }

   }

   private static boolean isRed(DoubleOrderedMap.Node node, int index) {
      return node == null ? false : node.isRed(index);
   }

   private static boolean isBlack(DoubleOrderedMap.Node node, int index) {
      return node == null ? true : node.isBlack(index);
   }

   private static void makeRed(DoubleOrderedMap.Node node, int index) {
      if (node != null) {
         node.setRed(index);
      }

   }

   private static void makeBlack(DoubleOrderedMap.Node node, int index) {
      if (node != null) {
         node.setBlack(index);
      }

   }

   private static DoubleOrderedMap.Node getGrandParent(DoubleOrderedMap.Node node, int index) {
      return getParent(getParent(node, index), index);
   }

   private static DoubleOrderedMap.Node getParent(DoubleOrderedMap.Node node, int index) {
      return node == null ? null : node.getParent(index);
   }

   private static DoubleOrderedMap.Node getRightChild(DoubleOrderedMap.Node node, int index) {
      return node == null ? null : node.getRight(index);
   }

   private static DoubleOrderedMap.Node getLeftChild(DoubleOrderedMap.Node node, int index) {
      return node == null ? null : node.getLeft(index);
   }

   private static boolean isLeftChild(DoubleOrderedMap.Node node, int index) {
      return node == null ? true : (node.getParent(index) == null ? false : node == node.getParent(index).getLeft(index));
   }

   private static boolean isRightChild(DoubleOrderedMap.Node node, int index) {
      return node == null ? true : (node.getParent(index) == null ? false : node == node.getParent(index).getRight(index));
   }

   private void rotateLeft(DoubleOrderedMap.Node node, int index) {
      DoubleOrderedMap.Node rightChild = node.getRight(index);
      node.setRight(rightChild.getLeft(index), index);
      if (rightChild.getLeft(index) != null) {
         rightChild.getLeft(index).setParent(node, index);
      }

      rightChild.setParent(node.getParent(index), index);
      if (node.getParent(index) == null) {
         this.rootNode[index] = rightChild;
      } else if (node.getParent(index).getLeft(index) == node) {
         node.getParent(index).setLeft(rightChild, index);
      } else {
         node.getParent(index).setRight(rightChild, index);
      }

      rightChild.setLeft(node, index);
      node.setParent(rightChild, index);
   }

   private void rotateRight(DoubleOrderedMap.Node node, int index) {
      DoubleOrderedMap.Node leftChild = node.getLeft(index);
      node.setLeft(leftChild.getRight(index), index);
      if (leftChild.getRight(index) != null) {
         leftChild.getRight(index).setParent(node, index);
      }

      leftChild.setParent(node.getParent(index), index);
      if (node.getParent(index) == null) {
         this.rootNode[index] = leftChild;
      } else if (node.getParent(index).getRight(index) == node) {
         node.getParent(index).setRight(leftChild, index);
      } else {
         node.getParent(index).setLeft(leftChild, index);
      }

      leftChild.setRight(node, index);
      node.setParent(leftChild, index);
   }

   private void doRedBlackInsert(DoubleOrderedMap.Node insertedNode, int index) {
      DoubleOrderedMap.Node currentNode = insertedNode;
      makeRed(insertedNode, index);

      while(currentNode != null && currentNode != this.rootNode[index] && isRed(currentNode.getParent(index), index)) {
         DoubleOrderedMap.Node y;
         if (isLeftChild(getParent(currentNode, index), index)) {
            y = getRightChild(getGrandParent(currentNode, index), index);
            if (isRed(y, index)) {
               makeBlack(getParent(currentNode, index), index);
               makeBlack(y, index);
               makeRed(getGrandParent(currentNode, index), index);
               currentNode = getGrandParent(currentNode, index);
            } else {
               if (isRightChild(currentNode, index)) {
                  currentNode = getParent(currentNode, index);
                  this.rotateLeft(currentNode, index);
               }

               makeBlack(getParent(currentNode, index), index);
               makeRed(getGrandParent(currentNode, index), index);
               if (getGrandParent(currentNode, index) != null) {
                  this.rotateRight(getGrandParent(currentNode, index), index);
               }
            }
         } else {
            y = getLeftChild(getGrandParent(currentNode, index), index);
            if (isRed(y, index)) {
               makeBlack(getParent(currentNode, index), index);
               makeBlack(y, index);
               makeRed(getGrandParent(currentNode, index), index);
               currentNode = getGrandParent(currentNode, index);
            } else {
               if (isLeftChild(currentNode, index)) {
                  currentNode = getParent(currentNode, index);
                  this.rotateRight(currentNode, index);
               }

               makeBlack(getParent(currentNode, index), index);
               makeRed(getGrandParent(currentNode, index), index);
               if (getGrandParent(currentNode, index) != null) {
                  this.rotateLeft(getGrandParent(currentNode, index), index);
               }
            }
         }
      }

      makeBlack(this.rootNode[index], index);
   }

   private void doRedBlackDelete(DoubleOrderedMap.Node deletedNode) {
      for(int index = 0; index < 2; ++index) {
         if (deletedNode.getLeft(index) != null && deletedNode.getRight(index) != null) {
            this.swapPosition(this.nextGreater(deletedNode, index), deletedNode, index);
         }

         DoubleOrderedMap.Node replacement = deletedNode.getLeft(index) != null ? deletedNode.getLeft(index) : deletedNode.getRight(index);
         if (replacement != null) {
            replacement.setParent(deletedNode.getParent(index), index);
            if (deletedNode.getParent(index) == null) {
               this.rootNode[index] = replacement;
            } else if (deletedNode == deletedNode.getParent(index).getLeft(index)) {
               deletedNode.getParent(index).setLeft(replacement, index);
            } else {
               deletedNode.getParent(index).setRight(replacement, index);
            }

            deletedNode.setLeft((DoubleOrderedMap.Node)null, index);
            deletedNode.setRight((DoubleOrderedMap.Node)null, index);
            deletedNode.setParent((DoubleOrderedMap.Node)null, index);
            if (isBlack(deletedNode, index)) {
               this.doRedBlackDeleteFixup(replacement, index);
            }
         } else if (deletedNode.getParent(index) == null) {
            this.rootNode[index] = null;
         } else {
            if (isBlack(deletedNode, index)) {
               this.doRedBlackDeleteFixup(deletedNode, index);
            }

            if (deletedNode.getParent(index) != null) {
               if (deletedNode == deletedNode.getParent(index).getLeft(index)) {
                  deletedNode.getParent(index).setLeft((DoubleOrderedMap.Node)null, index);
               } else {
                  deletedNode.getParent(index).setRight((DoubleOrderedMap.Node)null, index);
               }

               deletedNode.setParent((DoubleOrderedMap.Node)null, index);
            }
         }
      }

      this.shrink();
   }

   private void doRedBlackDeleteFixup(DoubleOrderedMap.Node replacementNode, int index) {
      DoubleOrderedMap.Node currentNode = replacementNode;

      while(currentNode != this.rootNode[index] && isBlack(currentNode, index)) {
         DoubleOrderedMap.Node siblingNode;
         if (isLeftChild(currentNode, index)) {
            siblingNode = getRightChild(getParent(currentNode, index), index);
            if (isRed(siblingNode, index)) {
               makeBlack(siblingNode, index);
               makeRed(getParent(currentNode, index), index);
               this.rotateLeft(getParent(currentNode, index), index);
               siblingNode = getRightChild(getParent(currentNode, index), index);
            }

            if (isBlack(getLeftChild(siblingNode, index), index) && isBlack(getRightChild(siblingNode, index), index)) {
               makeRed(siblingNode, index);
               currentNode = getParent(currentNode, index);
            } else {
               if (isBlack(getRightChild(siblingNode, index), index)) {
                  makeBlack(getLeftChild(siblingNode, index), index);
                  makeRed(siblingNode, index);
                  this.rotateRight(siblingNode, index);
                  siblingNode = getRightChild(getParent(currentNode, index), index);
               }

               copyColor(getParent(currentNode, index), siblingNode, index);
               makeBlack(getParent(currentNode, index), index);
               makeBlack(getRightChild(siblingNode, index), index);
               this.rotateLeft(getParent(currentNode, index), index);
               currentNode = this.rootNode[index];
            }
         } else {
            siblingNode = getLeftChild(getParent(currentNode, index), index);
            if (isRed(siblingNode, index)) {
               makeBlack(siblingNode, index);
               makeRed(getParent(currentNode, index), index);
               this.rotateRight(getParent(currentNode, index), index);
               siblingNode = getLeftChild(getParent(currentNode, index), index);
            }

            if (isBlack(getRightChild(siblingNode, index), index) && isBlack(getLeftChild(siblingNode, index), index)) {
               makeRed(siblingNode, index);
               currentNode = getParent(currentNode, index);
            } else {
               if (isBlack(getLeftChild(siblingNode, index), index)) {
                  makeBlack(getRightChild(siblingNode, index), index);
                  makeRed(siblingNode, index);
                  this.rotateLeft(siblingNode, index);
                  siblingNode = getLeftChild(getParent(currentNode, index), index);
               }

               copyColor(getParent(currentNode, index), siblingNode, index);
               makeBlack(getParent(currentNode, index), index);
               makeBlack(getLeftChild(siblingNode, index), index);
               this.rotateRight(getParent(currentNode, index), index);
               currentNode = this.rootNode[index];
            }
         }
      }

      makeBlack(currentNode, index);
   }

   private void swapPosition(DoubleOrderedMap.Node x, DoubleOrderedMap.Node y, int index) {
      DoubleOrderedMap.Node xFormerParent = x.getParent(index);
      DoubleOrderedMap.Node xFormerLeftChild = x.getLeft(index);
      DoubleOrderedMap.Node xFormerRightChild = x.getRight(index);
      DoubleOrderedMap.Node yFormerParent = y.getParent(index);
      DoubleOrderedMap.Node yFormerLeftChild = y.getLeft(index);
      DoubleOrderedMap.Node yFormerRightChild = y.getRight(index);
      boolean xWasLeftChild = x.getParent(index) != null && x == x.getParent(index).getLeft(index);
      boolean yWasLeftChild = y.getParent(index) != null && y == y.getParent(index).getLeft(index);
      if (x == yFormerParent) {
         x.setParent(y, index);
         if (yWasLeftChild) {
            y.setLeft(x, index);
            y.setRight(xFormerRightChild, index);
         } else {
            y.setRight(x, index);
            y.setLeft(xFormerLeftChild, index);
         }
      } else {
         x.setParent(yFormerParent, index);
         if (yFormerParent != null) {
            if (yWasLeftChild) {
               yFormerParent.setLeft(x, index);
            } else {
               yFormerParent.setRight(x, index);
            }
         }

         y.setLeft(xFormerLeftChild, index);
         y.setRight(xFormerRightChild, index);
      }

      if (y == xFormerParent) {
         y.setParent(x, index);
         if (xWasLeftChild) {
            x.setLeft(y, index);
            x.setRight(yFormerRightChild, index);
         } else {
            x.setRight(y, index);
            x.setLeft(yFormerLeftChild, index);
         }
      } else {
         y.setParent(xFormerParent, index);
         if (xFormerParent != null) {
            if (xWasLeftChild) {
               xFormerParent.setLeft(y, index);
            } else {
               xFormerParent.setRight(y, index);
            }
         }

         x.setLeft(yFormerLeftChild, index);
         x.setRight(yFormerRightChild, index);
      }

      if (x.getLeft(index) != null) {
         x.getLeft(index).setParent(x, index);
      }

      if (x.getRight(index) != null) {
         x.getRight(index).setParent(x, index);
      }

      if (y.getLeft(index) != null) {
         y.getLeft(index).setParent(y, index);
      }

      if (y.getRight(index) != null) {
         y.getRight(index).setParent(y, index);
      }

      x.swapColors(y, index);
      if (this.rootNode[index] == x) {
         this.rootNode[index] = y;
      } else if (this.rootNode[index] == y) {
         this.rootNode[index] = x;
      }

   }

   private static void checkNonNullComparable(Object o, int index) {
      if (o == null) {
         throw new NullPointerException(dataName[index] + " cannot be null");
      } else if (!(o instanceof Comparable)) {
         throw new ClassCastException(dataName[index] + " must be Comparable");
      }
   }

   private static void checkKey(Object key) {
      checkNonNullComparable(key, 0);
   }

   private static void checkValue(Object value) {
      checkNonNullComparable(value, 1);
   }

   private static void checkKeyAndValue(Object key, Object value) {
      checkKey(key);
      checkValue(value);
   }

   private void modify() {
      ++this.modifications;
   }

   private void grow() {
      this.modify();
      ++this.nodeCount;
   }

   private void shrink() {
      this.modify();
      --this.nodeCount;
   }

   private void insertValue(DoubleOrderedMap.Node newNode) throws IllegalArgumentException {
      DoubleOrderedMap.Node node = this.rootNode[1];

      while(true) {
         int cmp = compare(newNode.getData(1), node.getData(1));
         if (cmp == 0) {
            throw new IllegalArgumentException("Cannot store a duplicate value (\"" + newNode.getData(1) + "\") in this Map");
         }

         if (cmp < 0) {
            if (node.getLeft(1) == null) {
               node.setLeft(newNode, 1);
               newNode.setParent(node, 1);
               this.doRedBlackInsert(newNode, 1);
               break;
            }

            node = node.getLeft(1);
         } else {
            if (node.getRight(1) == null) {
               node.setRight(newNode, 1);
               newNode.setParent(node, 1);
               this.doRedBlackInsert(newNode, 1);
               break;
            }

            node = node.getRight(1);
         }
      }

   }

   public int size() {
      return this.nodeCount;
   }

   public boolean containsKey(Object key) throws ClassCastException, NullPointerException {
      checkKey(key);
      return this.lookup((Comparable)key, 0) != null;
   }

   public boolean containsValue(Object value) {
      checkValue(value);
      return this.lookup((Comparable)value, 1) != null;
   }

   public Object get(Object key) throws ClassCastException, NullPointerException {
      return this.doGet((Comparable)key, 0);
   }

   public Object put(Object key, Object value) throws ClassCastException, NullPointerException, IllegalArgumentException {
      checkKeyAndValue(key, value);
      DoubleOrderedMap.Node node = this.rootNode[0];
      if (node == null) {
         DoubleOrderedMap.Node root = new DoubleOrderedMap.Node((Comparable)key, (Comparable)value);
         this.rootNode[0] = root;
         this.rootNode[1] = root;
         this.grow();
      } else {
         while(true) {
            int cmp = compare((Comparable)key, node.getData(0));
            if (cmp == 0) {
               throw new IllegalArgumentException("Cannot store a duplicate key (\"" + key + "\") in this Map");
            }

            DoubleOrderedMap.Node newNode;
            if (cmp < 0) {
               if (node.getLeft(0) == null) {
                  newNode = new DoubleOrderedMap.Node((Comparable)key, (Comparable)value);
                  this.insertValue(newNode);
                  node.setLeft(newNode, 0);
                  newNode.setParent(node, 0);
                  this.doRedBlackInsert(newNode, 0);
                  this.grow();
                  break;
               }

               node = node.getLeft(0);
            } else {
               if (node.getRight(0) == null) {
                  newNode = new DoubleOrderedMap.Node((Comparable)key, (Comparable)value);
                  this.insertValue(newNode);
                  node.setRight(newNode, 0);
                  newNode.setParent(node, 0);
                  this.doRedBlackInsert(newNode, 0);
                  this.grow();
                  break;
               }

               node = node.getRight(0);
            }
         }
      }

      return null;
   }

   public Object remove(Object key) {
      return this.doRemove((Comparable)key, 0);
   }

   public void clear() {
      this.modify();
      this.nodeCount = 0;
      this.rootNode[0] = null;
      this.rootNode[1] = null;
   }

   public Set keySet() {
      if (this.setOfKeys[0] == null) {
         this.setOfKeys[0] = new AbstractSet() {
            public Iterator iterator() {
               return new DoubleOrderedMap.DoubleOrderedMapIterator(0) {
                  protected Object doGetNext() {
                     return super.lastReturnedNode.getData(0);
                  }
               };
            }

            public int size() {
               return DoubleOrderedMap.this.size();
            }

            public boolean contains(Object o) {
               return DoubleOrderedMap.this.containsKey(o);
            }

            public boolean remove(Object o) {
               int oldNodeCount = DoubleOrderedMap.this.nodeCount;
               DoubleOrderedMap.this.remove(o);
               return DoubleOrderedMap.this.nodeCount != oldNodeCount;
            }

            public void clear() {
               DoubleOrderedMap.this.clear();
            }

            // $FF: synthetic method
            static DoubleOrderedMap access$1900(Object x0) {
               return DoubleOrderedMap.this;
            }
         };
      }

      return this.setOfKeys[0];
   }

   public Collection values() {
      if (this.collectionOfValues[0] == null) {
         this.collectionOfValues[0] = new AbstractCollection() {
            public Iterator iterator() {
               return new DoubleOrderedMap.DoubleOrderedMapIterator(0) {
                  protected Object doGetNext() {
                     return super.lastReturnedNode.getData(1);
                  }
               };
            }

            public int size() {
               return DoubleOrderedMap.this.size();
            }

            public boolean contains(Object o) {
               return DoubleOrderedMap.this.containsValue(o);
            }

            public boolean remove(Object o) {
               int oldNodeCount = DoubleOrderedMap.this.nodeCount;
               DoubleOrderedMap.this.removeValue(o);
               return DoubleOrderedMap.this.nodeCount != oldNodeCount;
            }

            public boolean removeAll(Collection c) {
               boolean modified = false;
               Iterator iter = c.iterator();

               while(iter.hasNext()) {
                  if (DoubleOrderedMap.this.removeValue(iter.next()) != null) {
                     modified = true;
                  }
               }

               return modified;
            }

            public void clear() {
               DoubleOrderedMap.this.clear();
            }

            // $FF: synthetic method
            static DoubleOrderedMap access$2000(Object x0) {
               return DoubleOrderedMap.this;
            }
         };
      }

      return this.collectionOfValues[0];
   }

   public Set entrySet() {
      if (this.setOfEntries[0] == null) {
         this.setOfEntries[0] = new AbstractSet() {
            public Iterator iterator() {
               return new DoubleOrderedMap.DoubleOrderedMapIterator(0) {
                  protected Object doGetNext() {
                     return super.lastReturnedNode;
                  }
               };
            }

            public boolean contains(Object o) {
               if (!(o instanceof Entry)) {
                  return false;
               } else {
                  Entry entry = (Entry)o;
                  Object value = entry.getValue();
                  DoubleOrderedMap.Node node = DoubleOrderedMap.this.lookup((Comparable)entry.getKey(), 0);
                  return node != null && node.getData(1).equals(value);
               }
            }

            public boolean remove(Object o) {
               if (!(o instanceof Entry)) {
                  return false;
               } else {
                  Entry entry = (Entry)o;
                  Object value = entry.getValue();
                  DoubleOrderedMap.Node node = DoubleOrderedMap.this.lookup((Comparable)entry.getKey(), 0);
                  if (node != null && node.getData(1).equals(value)) {
                     DoubleOrderedMap.this.doRedBlackDelete(node);
                     return true;
                  } else {
                     return false;
                  }
               }
            }

            public int size() {
               return DoubleOrderedMap.this.size();
            }

            public void clear() {
               DoubleOrderedMap.this.clear();
            }

            // $FF: synthetic method
            static DoubleOrderedMap access$2100(Object x0) {
               return DoubleOrderedMap.this;
            }
         };
      }

      return this.setOfEntries[0];
   }

   private static final class Node implements Entry, KeyValue {
      private Comparable[] data;
      private DoubleOrderedMap.Node[] leftNode;
      private DoubleOrderedMap.Node[] rightNode;
      private DoubleOrderedMap.Node[] parentNode;
      private boolean[] blackColor;
      private int hashcodeValue;
      private boolean calculatedHashCode;

      Node(Comparable key, Comparable value) {
         this.data = new Comparable[]{key, value};
         this.leftNode = new DoubleOrderedMap.Node[]{null, null};
         this.rightNode = new DoubleOrderedMap.Node[]{null, null};
         this.parentNode = new DoubleOrderedMap.Node[]{null, null};
         this.blackColor = new boolean[]{true, true};
         this.calculatedHashCode = false;
      }

      private Comparable getData(int index) {
         return this.data[index];
      }

      private void setLeft(DoubleOrderedMap.Node node, int index) {
         this.leftNode[index] = node;
      }

      private DoubleOrderedMap.Node getLeft(int index) {
         return this.leftNode[index];
      }

      private void setRight(DoubleOrderedMap.Node node, int index) {
         this.rightNode[index] = node;
      }

      private DoubleOrderedMap.Node getRight(int index) {
         return this.rightNode[index];
      }

      private void setParent(DoubleOrderedMap.Node node, int index) {
         this.parentNode[index] = node;
      }

      private DoubleOrderedMap.Node getParent(int index) {
         return this.parentNode[index];
      }

      private void swapColors(DoubleOrderedMap.Node node, int index) {
         boolean[] var10000 = this.blackColor;
         var10000[index] ^= node.blackColor[index];
         var10000 = node.blackColor;
         var10000[index] ^= this.blackColor[index];
         var10000 = this.blackColor;
         var10000[index] ^= node.blackColor[index];
      }

      private boolean isBlack(int index) {
         return this.blackColor[index];
      }

      private boolean isRed(int index) {
         return !this.blackColor[index];
      }

      private void setBlack(int index) {
         this.blackColor[index] = true;
      }

      private void setRed(int index) {
         this.blackColor[index] = false;
      }

      private void copyColor(DoubleOrderedMap.Node node, int index) {
         this.blackColor[index] = node.blackColor[index];
      }

      public Object getKey() {
         return this.data[0];
      }

      public Object getValue() {
         return this.data[1];
      }

      public Object setValue(Object ignored) throws UnsupportedOperationException {
         throw new UnsupportedOperationException("Map.Entry.setValue is not supported");
      }

      public boolean equals(Object o) {
         if (this == o) {
            return true;
         } else if (!(o instanceof Entry)) {
            return false;
         } else {
            Entry e = (Entry)o;
            return this.data[0].equals(e.getKey()) && this.data[1].equals(e.getValue());
         }
      }

      public int hashCode() {
         if (!this.calculatedHashCode) {
            this.hashcodeValue = this.data[0].hashCode() ^ this.data[1].hashCode();
            this.calculatedHashCode = true;
         }

         return this.hashcodeValue;
      }
   }

   private abstract class DoubleOrderedMapIterator implements Iterator {
      private int expectedModifications;
      protected DoubleOrderedMap.Node lastReturnedNode;
      private DoubleOrderedMap.Node nextNode;
      private int iteratorType;

      DoubleOrderedMapIterator(int type) {
         this.iteratorType = type;
         this.expectedModifications = DoubleOrderedMap.this.modifications;
         this.lastReturnedNode = null;
         this.nextNode = DoubleOrderedMap.leastNode(DoubleOrderedMap.this.rootNode[this.iteratorType], this.iteratorType);
      }

      protected abstract Object doGetNext();

      public final boolean hasNext() {
         return this.nextNode != null;
      }

      public final Object next() throws NoSuchElementException, ConcurrentModificationException {
         if (this.nextNode == null) {
            throw new NoSuchElementException();
         } else if (DoubleOrderedMap.this.modifications != this.expectedModifications) {
            throw new ConcurrentModificationException();
         } else {
            this.lastReturnedNode = this.nextNode;
            this.nextNode = DoubleOrderedMap.this.nextGreater(this.nextNode, this.iteratorType);
            return this.doGetNext();
         }
      }

      public final void remove() throws IllegalStateException, ConcurrentModificationException {
         if (this.lastReturnedNode == null) {
            throw new IllegalStateException();
         } else if (DoubleOrderedMap.this.modifications != this.expectedModifications) {
            throw new ConcurrentModificationException();
         } else {
            DoubleOrderedMap.this.doRedBlackDelete(this.lastReturnedNode);
            ++this.expectedModifications;
            this.lastReturnedNode = null;
         }
      }
   }
}
