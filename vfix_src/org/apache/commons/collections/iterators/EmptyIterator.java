package org.apache.commons.collections.iterators;

import java.util.Iterator;
import org.apache.commons.collections.ResettableIterator;

public class EmptyIterator extends AbstractEmptyIterator implements ResettableIterator {
   public static final ResettableIterator RESETTABLE_INSTANCE = new EmptyIterator();
   public static final Iterator INSTANCE;

   protected EmptyIterator() {
   }

   static {
      INSTANCE = RESETTABLE_INSTANCE;
   }
}
