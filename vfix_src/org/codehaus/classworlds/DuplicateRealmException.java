package org.codehaus.classworlds;

public class DuplicateRealmException extends ClassWorldException {
   private String id;

   public DuplicateRealmException(ClassWorld world, String id) {
      super(world, id);
      this.id = id;
   }

   public String getId() {
      return this.id;
   }
}
