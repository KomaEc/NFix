package org.jf.dexlib2.dexbacked.util;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import javax.annotation.Nonnull;

public abstract class FixedSizeSet<T> extends AbstractSet<T> {
   public Iterator<T> iterator() {
      return new Iterator<T>() {
         int index = 0;

         public boolean hasNext() {
            return this.index < FixedSizeSet.this.size();
         }

         public void remove() {
            throw new UnsupportedOperationException();
         }

         public T next() {
            if (!this.hasNext()) {
               throw new NoSuchElementException();
            } else {
               return FixedSizeSet.this.readItem(this.index++);
            }
         }
      };
   }

   @Nonnull
   public abstract T readItem(int var1);
}
