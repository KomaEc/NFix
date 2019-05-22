package com.google.common.collect;

import java.util.Iterator;
import java.util.Set;
import java.util.Spliterator;
import java.util.Map.Entry;

class MutableClassToInstanceMap$2 extends ForwardingSet<Entry<Class<? extends B>, B>> {
   // $FF: synthetic field
   final MutableClassToInstanceMap this$0;

   MutableClassToInstanceMap$2(MutableClassToInstanceMap this$0) {
      this.this$0 = this$0;
   }

   protected Set<Entry<Class<? extends B>, B>> delegate() {
      return this.this$0.delegate().entrySet();
   }

   public Spliterator<Entry<Class<? extends B>, B>> spliterator() {
      return CollectSpliterators.map(this.delegate().spliterator(), (x$0) -> {
         return MutableClassToInstanceMap.access$100(x$0);
      });
   }

   public Iterator<Entry<Class<? extends B>, B>> iterator() {
      return new MutableClassToInstanceMap$2$1(this, this.delegate().iterator());
   }

   public Object[] toArray() {
      return this.standardToArray();
   }

   public <T> T[] toArray(T[] array) {
      return this.standardToArray(array);
   }
}
