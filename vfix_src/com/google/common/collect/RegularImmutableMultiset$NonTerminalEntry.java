package com.google.common.collect;

final class RegularImmutableMultiset$NonTerminalEntry<E> extends Multisets.ImmutableEntry<E> {
   private final Multisets.ImmutableEntry<E> nextInBucket;

   RegularImmutableMultiset$NonTerminalEntry(E element, int count, Multisets.ImmutableEntry<E> nextInBucket) {
      super(element, count);
      this.nextInBucket = nextInBucket;
   }

   public Multisets.ImmutableEntry<E> nextInBucket() {
      return this.nextInBucket;
   }
}
