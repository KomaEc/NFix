package scm;

import jas.CodeAttr;

class scmCodeAttr extends Procedure implements Obj {
   Obj apply(Cell args, Env f) throws Exception {
      return new primnode(new CodeAttr());
   }

   public String toString() {
      return "<#make-code#>";
   }
}
