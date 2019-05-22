package soot;

import soot.javaToJimple.IInitialResolver;

public abstract class ClassSource {
   protected String className;

   public ClassSource(String className) {
      if (className == null) {
         throw new IllegalStateException("Error: The class name must not be null.");
      } else {
         this.className = className;
      }
   }

   public abstract IInitialResolver.Dependencies resolve(SootClass var1);

   public void close() {
   }
}
