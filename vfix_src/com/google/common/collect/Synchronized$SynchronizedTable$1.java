package com.google.common.collect;

import com.google.common.base.Function;
import java.util.Map;

class Synchronized$SynchronizedTable$1 implements Function<Map<C, V>, Map<C, V>> {
   // $FF: synthetic field
   final Synchronized$SynchronizedTable this$0;

   Synchronized$SynchronizedTable$1(Synchronized$SynchronizedTable this$0) {
      this.this$0 = this$0;
   }

   public Map<C, V> apply(Map<C, V> t) {
      return Synchronized.map(t, this.this$0.mutex);
   }
}
