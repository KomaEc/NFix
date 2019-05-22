package scm;

import jas.ExceptAttr;

class scmExceptAttr extends Procedure implements Obj {
   Obj apply(Cell args, Env f) throws Exception {
      return new primnode(new ExceptAttr());
   }

   public String toString() {
      return "<#make-exception#>";
   }
}
