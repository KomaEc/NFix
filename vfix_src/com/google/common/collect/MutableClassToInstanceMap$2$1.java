package com.google.common.collect;

import java.util.Iterator;
import java.util.Map.Entry;

class MutableClassToInstanceMap$2$1 extends TransformedIterator<Entry<Class<? extends B>, B>, Entry<Class<? extends B>, B>> {
   // $FF: synthetic field
   final MutableClassToInstanceMap$2 this$1;

   MutableClassToInstanceMap$2$1(MutableClassToInstanceMap$2 this$1, Iterator backingIterator) {
      super(backingIterator);
      this.this$1 = this$1;
   }

   Entry<Class<? extends B>, B> transform(Entry<Class<? extends B>, B> from) {
      return MutableClassToInstanceMap.access$100(from);
   }
}
