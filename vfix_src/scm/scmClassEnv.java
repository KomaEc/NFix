package scm;

import jas.ClassEnv;

class scmClassEnv extends Procedure implements Obj {
   Obj apply(Cell args, Env f) throws Exception {
      return new primnode(new ClassEnv());
   }

   public String toString() {
      return "<#make-class-env#>";
   }
}
