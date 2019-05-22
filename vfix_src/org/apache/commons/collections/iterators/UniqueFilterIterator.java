package org.apache.commons.collections.iterators;

import java.util.Iterator;
import org.apache.commons.collections.functors.UniquePredicate;

public class UniqueFilterIterator extends FilterIterator {
   public UniqueFilterIterator(Iterator iterator) {
      super(iterator, UniquePredicate.getInstance());
   }
}
