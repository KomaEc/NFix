package scm;

import jas.FloatCP;

class scmFloatCP extends Procedure implements Obj {
   Obj apply(Cell args, Env f) throws Exception {
      if (args == null) {
         throw new SchemeError("make-float-cpe expects 1 arguments");
      } else {
         Obj tmp = args.car != null ? args.car.eval(f) : null;
         Cell t = args.cdr;
         if (!(tmp instanceof Selfrep)) {
            throw new SchemeError("make-float-cpe expects a number for arg #1");
         } else {
            float arg0 = (float)((Selfrep)tmp).num;
            return new primnode(new FloatCP(arg0));
         }
      }
   }

   public String toString() {
      return "<#make-float-cpe#>";
   }
}
