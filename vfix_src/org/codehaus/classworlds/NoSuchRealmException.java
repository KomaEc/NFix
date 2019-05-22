package org.codehaus.classworlds;

public class NoSuchRealmException extends ClassWorldException {
   private String id;

   public NoSuchRealmException(ClassWorld world, String id) {
      super(world, id);
      this.id = id;
   }

   public String getId() {
      return this.id;
   }
}
