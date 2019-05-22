package scm;

import jas.Catchtable;

class scmCatchtable extends Procedure implements Obj {
   Obj apply(Cell args, Env f) throws Exception {
      return new primnode(new Catchtable());
   }

   public String toString() {
      return "<#make-catchtable#>";
   }
}
