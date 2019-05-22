package com.google.common.collect;

import com.google.common.base.Function;
import java.util.Map;

class Synchronized$SynchronizedTable$2 implements Function<Map<R, V>, Map<R, V>> {
   // $FF: synthetic field
   final Synchronized$SynchronizedTable this$0;

   Synchronized$SynchronizedTable$2(Synchronized$SynchronizedTable this$0) {
      this.this$0 = this$0;
   }

   public Map<R, V> apply(Map<R, V> t) {
      return Synchronized.map(t, this.this$0.mutex);
   }
}
