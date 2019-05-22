package scm;

import jas.DoubleCP;

class scmDoubleCP extends Procedure implements Obj {
   Obj apply(Cell args, Env f) throws Exception {
      if (args == null) {
         throw new SchemeError("make-double-cpe expects 1 arguments");
      } else {
         Obj tmp = args.car != null ? args.car.eval(f) : null;
         Cell t = args.cdr;
         if (!(tmp instanceof Selfrep)) {
            throw new SchemeError("make-double-cpe expects a number for arg #1");
         } else {
            double arg0 = ((Selfrep)tmp).num;
            return new primnode(new DoubleCP(arg0));
         }
      }
   }

   public String toString() {
      return "<#make-double-cpe#>";
   }
}
