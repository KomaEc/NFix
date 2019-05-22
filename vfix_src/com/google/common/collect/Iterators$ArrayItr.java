package com.google.common.collect;

final class Iterators$ArrayItr<T> extends AbstractIndexedListIterator<T> {
   static final UnmodifiableListIterator<Object> EMPTY = new Iterators$ArrayItr(new Object[0], 0, 0, 0);
   private final T[] array;
   private final int offset;

   Iterators$ArrayItr(T[] array, int offset, int length, int index) {
      super(length, index);
      this.array = array;
      this.offset = offset;
   }

   protected T get(int index) {
      return this.array[this.offset + index];
   }
}
