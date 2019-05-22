package soot.jimple.toolkits.pointer.representations;

import soot.G;
import soot.Type;

public class GeneralConstObject extends ConstantObject {
   private Type type;
   private String name;
   private int id;

   public GeneralConstObject(Type t, String n) {
      this.type = t;
      this.name = n;
      this.id = G.v().GeneralConstObject_counter++;
   }

   public Type getType() {
      return this.type;
   }

   public String toString() {
      return this.name;
   }

   public int hashCode() {
      return this.id;
   }

   public boolean equals(Object other) {
      return this == other;
   }
}
