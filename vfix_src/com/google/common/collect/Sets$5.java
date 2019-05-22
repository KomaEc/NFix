package com.google.common.collect;

import com.google.common.math.IntMath;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;
import javax.annotation.Nullable;

final class Sets$5 extends AbstractSet<Set<E>> {
   // $FF: synthetic field
   final int val$size;
   // $FF: synthetic field
   final ImmutableMap val$index;

   Sets$5(int var1, ImmutableMap var2) {
      this.val$size = var1;
      this.val$index = var2;
   }

   public boolean contains(@Nullable Object o) {
      if (!(o instanceof Set)) {
         return false;
      } else {
         Set<?> s = (Set)o;
         return s.size() == this.val$size && this.val$index.keySet().containsAll(s);
      }
   }

   public Iterator<Set<E>> iterator() {
      return new Sets$5$1(this);
   }

   public int size() {
      return IntMath.binomial(this.val$index.size(), this.val$size);
   }

   public String toString() {
      return "Sets.combinations(" + this.val$index.keySet() + ", " + this.val$size + ")";
   }
}
