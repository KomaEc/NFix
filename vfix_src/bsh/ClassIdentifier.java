package bsh;

public class ClassIdentifier {
   Class clas;

   public ClassIdentifier(Class var1) {
      this.clas = var1;
   }

   public Class getTargetClass() {
      return this.clas;
   }

   public String toString() {
      return "Class Identifier: " + this.clas.getName();
   }
}
