package soot.dava.internal.javaRep;

import soot.RefType;
import soot.jimple.ThisRef;

public class DThisRef extends ThisRef {
   public DThisRef(RefType thisType) {
      super(thisType);
   }

   public String toString() {
      return "this: " + this.getType();
   }

   public Object clone() {
      return new DThisRef((RefType)this.getType());
   }
}
