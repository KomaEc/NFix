package org.jboss.util.collection;

public class EmptyCollectionException extends CollectionException {
   private static final long serialVersionUID = -4562591066479152759L;

   public EmptyCollectionException(String msg) {
      super(msg);
   }

   public EmptyCollectionException() {
   }
}
