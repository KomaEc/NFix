package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.util.AbstractSequentialList;
import java.util.ListIterator;
import java.util.Map.Entry;
import java.util.function.Consumer;

class LinkedListMultimap$1EntriesImpl extends AbstractSequentialList<Entry<K, V>> {
   // $FF: synthetic field
   final LinkedListMultimap this$0;

   LinkedListMultimap$1EntriesImpl(LinkedListMultimap this$0) {
      this.this$0 = this$0;
   }

   public int size() {
      return LinkedListMultimap.access$900(this.this$0);
   }

   public ListIterator<Entry<K, V>> listIterator(int index) {
      return this.this$0.new NodeIterator(index);
   }

   public void forEach(Consumer<? super Entry<K, V>> action) {
      Preconditions.checkNotNull(action);

      for(LinkedListMultimap.Node node = LinkedListMultimap.access$200(this.this$0); node != null; node = node.next) {
         action.accept(node);
      }

   }
}
