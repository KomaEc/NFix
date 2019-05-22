package com.google.common.util.concurrent;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

final class Futures$8 implements Futures.FutureCombiner<V, List<V>> {
   public List<V> combine(List<Optional<V>> values) {
      List<V> result = Lists.newArrayList();
      Iterator i$ = values.iterator();

      while(i$.hasNext()) {
         Optional<V> element = (Optional)i$.next();
         result.add(element != null ? element.orNull() : null);
      }

      return Collections.unmodifiableList(result);
   }
}
