package org.jf.dexlib2.dexbacked.util;

import java.util.AbstractList;
import javax.annotation.Nonnull;

public abstract class FixedSizeList<T> extends AbstractList<T> {
   public T get(int index) {
      if (index >= 0 && index < this.size()) {
         return this.readItem(index);
      } else {
         throw new IndexOutOfBoundsException();
      }
   }

   @Nonnull
   public abstract T readItem(int var1);
}
