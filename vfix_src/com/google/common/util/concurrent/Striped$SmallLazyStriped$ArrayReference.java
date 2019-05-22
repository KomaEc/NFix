package com.google.common.util.concurrent;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

final class Striped$SmallLazyStriped$ArrayReference<L> extends WeakReference<L> {
   final int index;

   Striped$SmallLazyStriped$ArrayReference(L referent, int index, ReferenceQueue<L> queue) {
      super(referent, queue);
      this.index = index;
   }
}
