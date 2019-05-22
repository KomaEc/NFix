package soot.util;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class EmptyChain implements Chain {
   private static final Iterator emptyIterator = new Iterator() {
      public void remove() {
         throw new NoSuchElementException();
      }

      public boolean hasNext() {
         return false;
      }

      public Object next() {
         throw new NoSuchElementException();
      }
   };
   private static EmptyChain instance = null;

   public static EmptyChain v() {
      if (instance == null) {
         instance = new EmptyChain();
      }

      return instance;
   }

   public boolean isEmpty() {
      return true;
   }

   public boolean contains(Object o) {
      return false;
   }

   public Object[] toArray() {
      return new Object[0];
   }

   public Object[] toArray(Object[] a) {
      return new Object[0];
   }

   public boolean add(Object e) {
      throw new RuntimeException("Cannot add elements to an unmodifiable chain");
   }

   public boolean containsAll(Collection c) {
      return false;
   }

   public boolean addAll(Collection c) {
      throw new RuntimeException("Cannot add elements to an unmodifiable chain");
   }

   public boolean removeAll(Collection c) {
      throw new RuntimeException("Cannot remove elements from an unmodifiable chain");
   }

   public boolean retainAll(Collection c) {
      throw new RuntimeException("Cannot add elements to an unmodifiable chain or remove ones from such chain");
   }

   public void clear() {
      throw new RuntimeException("Cannot remove elements from an unmodifiable chain");
   }

   public void insertBefore(List toInsert, Object point) {
      throw new RuntimeException("Cannot add elements to an unmodifiable chain");
   }

   public void insertAfter(List toInsert, Object point) {
      throw new RuntimeException("Cannot add elements to an unmodifiable chain");
   }

   public void insertAfter(Object toInsert, Object point) {
      throw new RuntimeException("Cannot add elements to an unmodifiable chain");
   }

   public void insertBefore(Object toInsert, Object point) {
      throw new RuntimeException("Cannot add elements to an unmodifiable chain");
   }

   public void insertBefore(Chain toInsert, Object point) {
      throw new RuntimeException("Cannot add elements to an unmodifiable chain");
   }

   public void insertAfter(Chain toInsert, Object point) {
      throw new RuntimeException("Cannot add elements to an unmodifiable chain");
   }

   public void insertOnEdge(Object toInsert, Object point_src, Object point_tgt) {
      throw new RuntimeException("Cannot add elements to an unmodifiable chain");
   }

   public void insertOnEdge(List toInsert, Object point_src, Object point_tgt) {
      throw new RuntimeException("Cannot add elements to an unmodifiable chain");
   }

   public void insertOnEdge(Chain toInsert, Object point_src, Object point_tgt) {
      throw new RuntimeException("Cannot add elements to an unmodifiable chain");
   }

   public void swapWith(Object out, Object in) {
      throw new RuntimeException("Cannot replace elements in an unmodifiable chain");
   }

   public boolean remove(Object u) {
      throw new RuntimeException("Cannot remove elements from an unmodifiable chain");
   }

   public void addFirst(Object u) {
      throw new RuntimeException("Cannot add elements to an unmodifiable chain");
   }

   public void addLast(Object u) {
      throw new RuntimeException("Cannot add elements to an unmodifiable chain");
   }

   public void removeFirst() {
      throw new RuntimeException("Cannot remove elements from an unmodifiable chain");
   }

   public void removeLast() {
      throw new RuntimeException("Cannot remove elements from an unmodifiable chain");
   }

   public boolean follows(Object someObject, Object someReferenceObject) {
      return false;
   }

   public Object getFirst() {
      throw new NoSuchElementException();
   }

   public Object getLast() {
      throw new NoSuchElementException();
   }

   public Object getSuccOf(Object point) {
      throw new NoSuchElementException();
   }

   public Object getPredOf(Object point) {
      throw new NoSuchElementException();
   }

   public Iterator snapshotIterator() {
      return emptyIterator;
   }

   public Iterator iterator() {
      return emptyIterator;
   }

   public Iterator iterator(Object u) {
      return emptyIterator;
   }

   public Iterator iterator(Object head, Object tail) {
      return emptyIterator;
   }

   public int size() {
      return 0;
   }

   public long getModificationCount() {
      return 0L;
   }

   public Collection getElementsUnsorted() {
      return Collections.emptyList();
   }
}
