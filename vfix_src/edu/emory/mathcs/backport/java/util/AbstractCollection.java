package edu.emory.mathcs.backport.java.util;

import edu.emory.mathcs.backport.java.util.concurrent.helpers.Utils;

public abstract class AbstractCollection extends java.util.AbstractCollection {
   protected AbstractCollection() {
   }

   public Object[] toArray() {
      return Utils.collectionToArray(this);
   }

   public Object[] toArray(Object[] a) {
      return Utils.collectionToArray(this, a);
   }
}
