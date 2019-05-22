package org.apache.commons.collections.bidimap;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Map.Entry;
import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.KeyValue;
import org.apache.commons.collections.MapIterator;
import org.apache.commons.collections.OrderedBidiMap;
import org.apache.commons.collections.OrderedIterator;
import org.apache.commons.collections.OrderedMapIterator;
import org.apache.commons.collections.iterators.EmptyOrderedMapIterator;
import org.apache.commons.collections.keyvalue.UnmodifiableMapEntry;

public class TreeBidiMap implements OrderedBidiMap {
   private static final int KEY = 0;
   private static final int VALUE = 1;
   private static final int MAPENTRY = 2;
   private static final int INVERSEMAPENTRY = 3;
   private static final int SUM_OF_INDICES = 1;
   private static final int FIRST_INDEX = 0;
   private static final int NUMBER_OF_INDICES = 2;
   private static final String[] dataName = new String[]{"key", "value"};
   private TreeBidiMap.Node[] rootNode = new TreeBidiMap.Node[2];
   private int nodeCount = 0;
   private int modifications = 0;
   private Set keySet;
   private Set valuesSet;
   private Set entrySet;
   private TreeBidiMap.Inverse inverse = null;

   public TreeBidiMap() {
   }

   public TreeBidiMap(Map map) {
      this.putAll(map);
   }

   public int size() {
      return this.nodeCount;
   }

   public boolean isEmpty() {
      return this.nodeCount == 0;
   }

   public boolean containsKey(Object key) {
      checkKey(key);
      return this.lookup((Comparable)key, 0) != null;
   }

   public boolean containsValue(Object value) {
      checkValue(value);
      return this.lookup((Comparable)value, 1) != null;
   }

   public Object get(Object key) {
      return this.doGet((Comparable)key, 0);
   }

   public Object put(Object key, Object value) {
      return this.doPut((Comparable)key, (Comparable)value, 0);
   }

   public void putAll(Map map) {
      Iterator it = map.entrySet().iterator();

      while(it.hasNext()) {
         Entry entry = (Entry)it.next();
         this.put(entry.getKey(), entry.getValue());
      }

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

   public Object getKey(Object value) {
      return this.doGet((Comparable)value, 1);
   }

   public Object removeValue(Object value) {
      return this.doRemove((Comparable)value, 1);
   }

   public Object firstKey() {
      if (this.nodeCount == 0) {
         throw new NoSuchElementException("Map is empty");
      } else {
         return leastNode(this.rootNode[0], 0).getKey();
      }
   }

   public Object lastKey() {
      if (this.nodeCount == 0) {
         throw new NoSuchElementException("Map is empty");
      } else {
         return greatestNode(this.rootNode[0], 0).getKey();
      }
   }

   public Object nextKey(Object key) {
      checkKey(key);
      TreeBidiMap.Node node = this.nextGreater(this.lookup((Comparable)key, 0), 0);
      return node == null ? null : node.getKey();
   }

   public Object previousKey(Object key) {
      checkKey(key);
      TreeBidiMap.Node node = this.nextSmaller(this.lookup((Comparable)key, 0), 0);
      return node == null ? null : node.getKey();
   }

   public Set keySet() {
      if (this.keySet == null) {
         this.keySet = new TreeBidiMap.View(this, 0, 0);
      }

      return this.keySet;
   }

   public Collection values() {
      if (this.valuesSet == null) {
         this.valuesSet = new TreeBidiMap.View(this, 0, 1);
      }

      return this.valuesSet;
   }

   public Set entrySet() {
      return (Set)(this.entrySet == null ? new TreeBidiMap.EntryView(this, 0, 2) : this.entrySet);
   }

   public MapIterator mapIterator() {
      return (MapIterator)(this.isEmpty() ? EmptyOrderedMapIterator.INSTANCE : new TreeBidiMap.ViewMapIterator(this, 0));
   }

   public OrderedMapIterator orderedMapIterator() {
      return (OrderedMapIterator)(this.isEmpty() ? EmptyOrderedMapIterator.INSTANCE : new TreeBidiMap.ViewMapIterator(this, 0));
   }

   public BidiMap inverseBidiMap() {
      return this.inverseOrderedBidiMap();
   }

   public OrderedBidiMap inverseOrderedBidiMap() {
      if (this.inverse == null) {
         this.inverse = new TreeBidiMap.Inverse(this);
      }

      return this.inverse;
   }

   public boolean equals(Object obj) {
      return this.doEquals(obj, 0);
   }

   public int hashCode() {
      return this.doHashCode(0);
   }

   public String toString() {
      return this.doToString(0);
   }

   private Object doGet(Comparable obj, int index) {
      checkNonNullComparable(obj, index);
      TreeBidiMap.Node node = this.lookup(obj, index);
      return node == null ? null : node.getData(oppositeIndex(index));
   }

   private Object doPut(Comparable key, Comparable value, int index) {
      checkKeyAndValue(key, value);
      Object prev = index == 0 ? this.doGet(key, 0) : this.doGet(value, 1);
      this.doRemove(key, 0);
      this.doRemove(value, 1);
      TreeBidiMap.Node node = this.rootNode[0];
      if (node == null) {
         TreeBidiMap.Node root = new TreeBidiMap.Node(key, value);
         this.rootNode[0] = root;
         this.rootNode[1] = root;
         this.grow();
      } else {
         while(true) {
            int cmp = compare(key, node.getData(0));
            if (cmp == 0) {
               throw new IllegalArgumentException("Cannot store a duplicate key (\"" + key + "\") in this Map");
            }

            TreeBidiMap.Node newNode;
            if (cmp < 0) {
               if (node.getLeft(0) == null) {
                  newNode = new TreeBidiMap.Node(key, value);
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
                  newNode = new TreeBidiMap.Node(key, value);
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

      return prev;
   }

   private Object doRemove(Comparable o, int index) {
      TreeBidiMap.Node node = this.lookup(o, index);
      Object rval = null;
      if (node != null) {
         rval = node.getData(oppositeIndex(index));
         this.doRedBlackDelete(node);
      }

      return rval;
   }

   private TreeBidiMap.Node lookup(Comparable data, int index) {
      TreeBidiMap.Node rval = null;

      int cmp;
      for(TreeBidiMap.Node node = this.rootNode[index]; node != null; node = cmp < 0 ? node.getLeft(index) : node.getRight(index)) {
         cmp = compare(data, node.getData(index));
         if (cmp == 0) {
            rval = node;
            break;
         }
      }

      return rval;
   }

   private TreeBidiMap.Node nextGreater(TreeBidiMap.Node node, int index) {
      TreeBidiMap.Node rval = null;
      if (node == null) {
         rval = null;
      } else if (node.getRight(index) != null) {
         rval = leastNode(node.getRight(index), index);
      } else {
         TreeBidiMap.Node parent = node.getParent(index);

         for(TreeBidiMap.Node child = node; parent != null && child == parent.getRight(index); parent = parent.getParent(index)) {
            child = parent;
         }

         rval = parent;
      }

      return rval;
   }

   private TreeBidiMap.Node nextSmaller(TreeBidiMap.Node node, int index) {
      TreeBidiMap.Node rval = null;
      if (node == null) {
         rval = null;
      } else if (node.getLeft(index) != null) {
         rval = greatestNode(node.getLeft(index), index);
      } else {
         TreeBidiMap.Node parent = node.getParent(index);

         for(TreeBidiMap.Node child = node; parent != null && child == parent.getLeft(index); parent = parent.getParent(index)) {
            child = parent;
         }

         rval = parent;
      }

      return rval;
   }

   private static int oppositeIndex(int index) {
      return 1 - index;
   }

   private static int compare(Comparable o1, Comparable o2) {
      return o1.compareTo(o2);
   }

   private static TreeBidiMap.Node leastNode(TreeBidiMap.Node node, int index) {
      TreeBidiMap.Node rval = node;
      if (node != null) {
         while(rval.getLeft(index) != null) {
            rval = rval.getLeft(index);
         }
      }

      return rval;
   }

   private static TreeBidiMap.Node greatestNode(TreeBidiMap.Node node, int index) {
      TreeBidiMap.Node rval = node;
      if (node != null) {
         while(rval.getRight(index) != null) {
            rval = rval.getRight(index);
         }
      }

      return rval;
   }

   private static void copyColor(TreeBidiMap.Node from, TreeBidiMap.Node to, int index) {
      if (to != null) {
         if (from == null) {
            to.setBlack(index);
         } else {
            to.copyColor(from, index);
         }
      }

   }

   private static boolean isRed(TreeBidiMap.Node node, int index) {
      return node == null ? false : node.isRed(index);
   }

   private static boolean isBlack(TreeBidiMap.Node node, int index) {
      return node == null ? true : node.isBlack(index);
   }

   private static void makeRed(TreeBidiMap.Node node, int index) {
      if (node != null) {
         node.setRed(index);
      }

   }

   private static void makeBlack(TreeBidiMap.Node node, int index) {
      if (node != null) {
         node.setBlack(index);
      }

   }

   private static TreeBidiMap.Node getGrandParent(TreeBidiMap.Node node, int index) {
      return getParent(getParent(node, index), index);
   }

   private static TreeBidiMap.Node getParent(TreeBidiMap.Node node, int index) {
      return node == null ? null : node.getParent(index);
   }

   private static TreeBidiMap.Node getRightChild(TreeBidiMap.Node node, int index) {
      return node == null ? null : node.getRight(index);
   }

   private static TreeBidiMap.Node getLeftChild(TreeBidiMap.Node node, int index) {
      return node == null ? null : node.getLeft(index);
   }

   private static boolean isLeftChild(TreeBidiMap.Node node, int index) {
      return node == null ? true : (node.getParent(index) == null ? false : node == node.getParent(index).getLeft(index));
   }

   private static boolean isRightChild(TreeBidiMap.Node node, int index) {
      return node == null ? true : (node.getParent(index) == null ? false : node == node.getParent(index).getRight(index));
   }

   private void rotateLeft(TreeBidiMap.Node node, int index) {
      TreeBidiMap.Node rightChild = node.getRight(index);
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

   private void rotateRight(TreeBidiMap.Node node, int index) {
      TreeBidiMap.Node leftChild = node.getLeft(index);
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

   private void doRedBlackInsert(TreeBidiMap.Node insertedNode, int index) {
      TreeBidiMap.Node currentNode = insertedNode;
      makeRed(insertedNode, index);

      while(currentNode != null && currentNode != this.rootNode[index] && isRed(currentNode.getParent(index), index)) {
         TreeBidiMap.Node y;
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

   private void doRedBlackDelete(TreeBidiMap.Node deletedNode) {
      for(int index = 0; index < 2; ++index) {
         if (deletedNode.getLeft(index) != null && deletedNode.getRight(index) != null) {
            this.swapPosition(this.nextGreater(deletedNode, index), deletedNode, index);
         }

         TreeBidiMap.Node replacement = deletedNode.getLeft(index) != null ? deletedNode.getLeft(index) : deletedNode.getRight(index);
         if (replacement != null) {
            replacement.setParent(deletedNode.getParent(index), index);
            if (deletedNode.getParent(index) == null) {
               this.rootNode[index] = replacement;
            } else if (deletedNode == deletedNode.getParent(index).getLeft(index)) {
               deletedNode.getParent(index).setLeft(replacement, index);
            } else {
               deletedNode.getParent(index).setRight(replacement, index);
            }

            deletedNode.setLeft((TreeBidiMap.Node)null, index);
            deletedNode.setRight((TreeBidiMap.Node)null, index);
            deletedNode.setParent((TreeBidiMap.Node)null, index);
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
                  deletedNode.getParent(index).setLeft((TreeBidiMap.Node)null, index);
               } else {
                  deletedNode.getParent(index).setRight((TreeBidiMap.Node)null, index);
               }

               deletedNode.setParent((TreeBidiMap.Node)null, index);
            }
         }
      }

      this.shrink();
   }

   private void doRedBlackDeleteFixup(TreeBidiMap.Node replacementNode, int index) {
      TreeBidiMap.Node currentNode = replacementNode;

      while(currentNode != this.rootNode[index] && isBlack(currentNode, index)) {
         TreeBidiMap.Node siblingNode;
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

   private void swapPosition(TreeBidiMap.Node x, TreeBidiMap.Node y, int index) {
      TreeBidiMap.Node xFormerParent = x.getParent(index);
      TreeBidiMap.Node xFormerLeftChild = x.getLeft(index);
      TreeBidiMap.Node xFormerRightChild = x.getRight(index);
      TreeBidiMap.Node yFormerParent = y.getParent(index);
      TreeBidiMap.Node yFormerLeftChild = y.getLeft(index);
      TreeBidiMap.Node yFormerRightChild = y.getRight(index);
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

   private void insertValue(TreeBidiMap.Node newNode) throws IllegalArgumentException {
      TreeBidiMap.Node node = this.rootNode[1];

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

   private boolean doEquals(Object obj, int type) {
      if (obj == this) {
         return true;
      } else if (!(obj instanceof Map)) {
         return false;
      } else {
         Map other = (Map)obj;
         if (other.size() != this.size()) {
            return false;
         } else {
            if (this.nodeCount > 0) {
               try {
                  TreeBidiMap.ViewMapIterator it = new TreeBidiMap.ViewMapIterator(this, type);

                  while(it.hasNext()) {
                     Object key = it.next();
                     Object value = it.getValue();
                     if (!value.equals(other.get(key))) {
                        return false;
                     }
                  }
               } catch (ClassCastException var7) {
                  return false;
               } catch (NullPointerException var8) {
                  return false;
               }
            }

            return true;
         }
      }
   }

   private int doHashCode(int type) {
      int total = 0;
      Object key;
      Object value;
      if (this.nodeCount > 0) {
         for(TreeBidiMap.ViewMapIterator it = new TreeBidiMap.ViewMapIterator(this, type); it.hasNext(); total += key.hashCode() ^ value.hashCode()) {
            key = it.next();
            value = it.getValue();
         }
      }

      return total;
   }

   private String doToString(int type) {
      if (this.nodeCount == 0) {
         return "{}";
      } else {
         StringBuffer buf = new StringBuffer(this.nodeCount * 32);
         buf.append('{');
         MapIterator it = new TreeBidiMap.ViewMapIterator(this, type);
         boolean hasNext = it.hasNext();

         while(hasNext) {
            Object key = it.next();
            Object value = it.getValue();
            buf.append(key == this ? "(this Map)" : key).append('=').append(value == this ? "(this Map)" : value);
            hasNext = it.hasNext();
            if (hasNext) {
               buf.append(", ");
            }
         }

         buf.append('}');
         return buf.toString();
      }
   }

   static class Inverse implements OrderedBidiMap {
      private final TreeBidiMap main;
      private Set keySet;
      private Set valuesSet;
      private Set entrySet;

      Inverse(TreeBidiMap main) {
         this.main = main;
      }

      public int size() {
         return this.main.size();
      }

      public boolean isEmpty() {
         return this.main.isEmpty();
      }

      public Object get(Object key) {
         return this.main.getKey(key);
      }

      public Object getKey(Object value) {
         return this.main.get(value);
      }

      public boolean containsKey(Object key) {
         return this.main.containsValue(key);
      }

      public boolean containsValue(Object value) {
         return this.main.containsKey(value);
      }

      public Object firstKey() {
         if (this.main.nodeCount == 0) {
            throw new NoSuchElementException("Map is empty");
         } else {
            return TreeBidiMap.leastNode(this.main.rootNode[1], 1).getValue();
         }
      }

      public Object lastKey() {
         if (this.main.nodeCount == 0) {
            throw new NoSuchElementException("Map is empty");
         } else {
            return TreeBidiMap.greatestNode(this.main.rootNode[1], 1).getValue();
         }
      }

      public Object nextKey(Object key) {
         TreeBidiMap.checkKey(key);
         TreeBidiMap.Node node = this.main.nextGreater(this.main.lookup((Comparable)key, 1), 1);
         return node == null ? null : node.getValue();
      }

      public Object previousKey(Object key) {
         TreeBidiMap.checkKey(key);
         TreeBidiMap.Node node = this.main.nextSmaller(this.main.lookup((Comparable)key, 1), 1);
         return node == null ? null : node.getValue();
      }

      public Object put(Object key, Object value) {
         return this.main.doPut((Comparable)value, (Comparable)key, 1);
      }

      public void putAll(Map map) {
         Iterator it = map.entrySet().iterator();

         while(it.hasNext()) {
            Entry entry = (Entry)it.next();
            this.put(entry.getKey(), entry.getValue());
         }

      }

      public Object remove(Object key) {
         return this.main.removeValue(key);
      }

      public Object removeValue(Object value) {
         return this.main.remove(value);
      }

      public void clear() {
         this.main.clear();
      }

      public Set keySet() {
         if (this.keySet == null) {
            this.keySet = new TreeBidiMap.View(this.main, 1, 1);
         }

         return this.keySet;
      }

      public Collection values() {
         if (this.valuesSet == null) {
            this.valuesSet = new TreeBidiMap.View(this.main, 1, 0);
         }

         return this.valuesSet;
      }

      public Set entrySet() {
         return (Set)(this.entrySet == null ? new TreeBidiMap.EntryView(this.main, 1, 3) : this.entrySet);
      }

      public MapIterator mapIterator() {
         return (MapIterator)(this.isEmpty() ? EmptyOrderedMapIterator.INSTANCE : new TreeBidiMap.ViewMapIterator(this.main, 1));
      }

      public OrderedMapIterator orderedMapIterator() {
         return (OrderedMapIterator)(this.isEmpty() ? EmptyOrderedMapIterator.INSTANCE : new TreeBidiMap.ViewMapIterator(this.main, 1));
      }

      public BidiMap inverseBidiMap() {
         return this.main;
      }

      public OrderedBidiMap inverseOrderedBidiMap() {
         return this.main;
      }

      public boolean equals(Object obj) {
         return this.main.doEquals(obj, 1);
      }

      public int hashCode() {
         return this.main.doHashCode(1);
      }

      public String toString() {
         return this.main.doToString(1);
      }
   }

   static class Node implements Entry, KeyValue {
      private Comparable[] data;
      private TreeBidiMap.Node[] leftNode;
      private TreeBidiMap.Node[] rightNode;
      private TreeBidiMap.Node[] parentNode;
      private boolean[] blackColor;
      private int hashcodeValue;
      private boolean calculatedHashCode;

      Node(Comparable key, Comparable value) {
         this.data = new Comparable[]{key, value};
         this.leftNode = new TreeBidiMap.Node[2];
         this.rightNode = new TreeBidiMap.Node[2];
         this.parentNode = new TreeBidiMap.Node[2];
         this.blackColor = new boolean[]{true, true};
         this.calculatedHashCode = false;
      }

      private Comparable getData(int index) {
         return this.data[index];
      }

      private void setLeft(TreeBidiMap.Node node, int index) {
         this.leftNode[index] = node;
      }

      private TreeBidiMap.Node getLeft(int index) {
         return this.leftNode[index];
      }

      private void setRight(TreeBidiMap.Node node, int index) {
         this.rightNode[index] = node;
      }

      private TreeBidiMap.Node getRight(int index) {
         return this.rightNode[index];
      }

      private void setParent(TreeBidiMap.Node node, int index) {
         this.parentNode[index] = node;
      }

      private TreeBidiMap.Node getParent(int index) {
         return this.parentNode[index];
      }

      private void swapColors(TreeBidiMap.Node node, int index) {
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

      private void copyColor(TreeBidiMap.Node node, int index) {
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

      public boolean equals(Object obj) {
         if (obj == this) {
            return true;
         } else if (!(obj instanceof Entry)) {
            return false;
         } else {
            Entry e = (Entry)obj;
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

   static class EntryView extends TreeBidiMap.View {
      private final int oppositeType;

      EntryView(TreeBidiMap main, int orderType, int dataType) {
         super(main, orderType, dataType);
         this.oppositeType = TreeBidiMap.oppositeIndex(orderType);
      }

      public boolean contains(Object obj) {
         if (!(obj instanceof Entry)) {
            return false;
         } else {
            Entry entry = (Entry)obj;
            Object value = entry.getValue();
            TreeBidiMap.Node node = super.main.lookup((Comparable)entry.getKey(), super.orderType);
            return node != null && node.getData(this.oppositeType).equals(value);
         }
      }

      public boolean remove(Object obj) {
         if (!(obj instanceof Entry)) {
            return false;
         } else {
            Entry entry = (Entry)obj;
            Object value = entry.getValue();
            TreeBidiMap.Node node = super.main.lookup((Comparable)entry.getKey(), super.orderType);
            if (node != null && node.getData(this.oppositeType).equals(value)) {
               super.main.doRedBlackDelete(node);
               return true;
            } else {
               return false;
            }
         }
      }
   }

   static class ViewMapIterator extends TreeBidiMap.ViewIterator implements OrderedMapIterator {
      private final int oppositeType;

      ViewMapIterator(TreeBidiMap main, int orderType) {
         super(main, orderType, orderType);
         this.oppositeType = TreeBidiMap.oppositeIndex(super.dataType);
      }

      public Object getKey() {
         if (super.lastReturnedNode == null) {
            throw new IllegalStateException("Iterator getKey() can only be called after next() and before remove()");
         } else {
            return super.lastReturnedNode.getData(super.dataType);
         }
      }

      public Object getValue() {
         if (super.lastReturnedNode == null) {
            throw new IllegalStateException("Iterator getValue() can only be called after next() and before remove()");
         } else {
            return super.lastReturnedNode.getData(this.oppositeType);
         }
      }

      public Object setValue(Object obj) {
         throw new UnsupportedOperationException();
      }
   }

   static class ViewIterator implements OrderedIterator {
      protected final TreeBidiMap main;
      protected final int orderType;
      protected final int dataType;
      protected TreeBidiMap.Node lastReturnedNode;
      protected TreeBidiMap.Node nextNode;
      protected TreeBidiMap.Node previousNode;
      private int expectedModifications;

      ViewIterator(TreeBidiMap main, int orderType, int dataType) {
         this.main = main;
         this.orderType = orderType;
         this.dataType = dataType;
         this.expectedModifications = main.modifications;
         this.nextNode = TreeBidiMap.leastNode(main.rootNode[orderType], orderType);
         this.lastReturnedNode = null;
         this.previousNode = null;
      }

      public final boolean hasNext() {
         return this.nextNode != null;
      }

      public final Object next() {
         if (this.nextNode == null) {
            throw new NoSuchElementException();
         } else if (this.main.modifications != this.expectedModifications) {
            throw new ConcurrentModificationException();
         } else {
            this.lastReturnedNode = this.nextNode;
            this.previousNode = this.nextNode;
            this.nextNode = this.main.nextGreater(this.nextNode, this.orderType);
            return this.doGetData();
         }
      }

      public boolean hasPrevious() {
         return this.previousNode != null;
      }

      public Object previous() {
         if (this.previousNode == null) {
            throw new NoSuchElementException();
         } else if (this.main.modifications != this.expectedModifications) {
            throw new ConcurrentModificationException();
         } else {
            this.nextNode = this.lastReturnedNode;
            if (this.nextNode == null) {
               this.nextNode = this.main.nextGreater(this.previousNode, this.orderType);
            }

            this.lastReturnedNode = this.previousNode;
            this.previousNode = this.main.nextSmaller(this.previousNode, this.orderType);
            return this.doGetData();
         }
      }

      protected Object doGetData() {
         switch(this.dataType) {
         case 0:
            return this.lastReturnedNode.getKey();
         case 1:
            return this.lastReturnedNode.getValue();
         case 2:
            return this.lastReturnedNode;
         case 3:
            return new UnmodifiableMapEntry(this.lastReturnedNode.getValue(), this.lastReturnedNode.getKey());
         default:
            return null;
         }
      }

      public final void remove() {
         if (this.lastReturnedNode == null) {
            throw new IllegalStateException();
         } else if (this.main.modifications != this.expectedModifications) {
            throw new ConcurrentModificationException();
         } else {
            this.main.doRedBlackDelete(this.lastReturnedNode);
            ++this.expectedModifications;
            this.lastReturnedNode = null;
            if (this.nextNode == null) {
               this.previousNode = TreeBidiMap.greatestNode(this.main.rootNode[this.orderType], this.orderType);
            } else {
               this.previousNode = this.main.nextSmaller(this.nextNode, this.orderType);
            }

         }
      }
   }

   static class View extends AbstractSet {
      protected final TreeBidiMap main;
      protected final int orderType;
      protected final int dataType;

      View(TreeBidiMap main, int orderType, int dataType) {
         this.main = main;
         this.orderType = orderType;
         this.dataType = dataType;
      }

      public Iterator iterator() {
         return new TreeBidiMap.ViewIterator(this.main, this.orderType, this.dataType);
      }

      public int size() {
         return this.main.size();
      }

      public boolean contains(Object obj) {
         TreeBidiMap.checkNonNullComparable(obj, this.dataType);
         return this.main.lookup((Comparable)obj, this.dataType) != null;
      }

      public boolean remove(Object obj) {
         return this.main.doRemove((Comparable)obj, this.dataType) != null;
      }

      public void clear() {
         this.main.clear();
      }
   }
}
