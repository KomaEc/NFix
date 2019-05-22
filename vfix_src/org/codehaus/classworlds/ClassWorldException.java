package org.codehaus.classworlds;

public class ClassWorldException extends Exception {
   private ClassWorld world;

   public ClassWorldException(ClassWorld world) {
      this.world = world;
   }

   public ClassWorldException(ClassWorld world, String msg) {
      super(msg);
      this.world = world;
   }

   public ClassWorld getWorld() {
      return this.world;
   }
}
