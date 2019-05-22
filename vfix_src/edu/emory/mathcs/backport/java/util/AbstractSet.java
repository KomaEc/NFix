package edu.emory.mathcs.backport.java.util;

import edu.emory.mathcs.backport.java.util.concurrent.helpers.Utils;

public abstract class AbstractSet extends java.util.AbstractSet {
   protected AbstractSet() {
   }

   public Object[] toArray() {
      return Utils.collectionToArray(this);
   }

   public Object[] toArray(Object[] a) {
      return Utils.collectionToArray(this, a);
   }
}
