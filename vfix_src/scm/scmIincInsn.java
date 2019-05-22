package scm;

import jas.IincInsn;

class scmIincInsn extends Procedure implements Obj {
   Obj apply(Cell args, Env f) throws Exception {
      if (args == null) {
         throw new SchemeError("iinc expects 2 arguments");
      } else {
         Obj tmp = args.car != null ? args.car.eval(f) : null;
         Cell t = args.cdr;
         if (!(tmp instanceof Selfrep)) {
            throw new SchemeError("iinc expects a number for arg #1");
         } else {
            int arg0 = (int)Math.round(((Selfrep)tmp).num);
            if (t == null) {
               throw new SchemeError("iinc expects 2 arguments");
            } else {
               tmp = t.car != null ? t.car.eval(f) : null;
               t = t.cdr;
               if (!(tmp instanceof Selfrep)) {
                  throw new SchemeError("iinc expects a number for arg #2");
               } else {
                  int arg1 = (int)Math.round(((Selfrep)tmp).num);
                  return new primnode(new IincInsn(arg0, arg1));
               }
            }
         }
      }
   }

   public String toString() {
      return "<#iinc#>";
   }
}
