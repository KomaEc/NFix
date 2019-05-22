package scm;

import jas.LongCP;

class scmLongCP extends Procedure implements Obj {
   Obj apply(Cell args, Env f) throws Exception {
      if (args == null) {
         throw new SchemeError("make-long-cpe expects 1 arguments");
      } else {
         Obj tmp = args.car != null ? args.car.eval(f) : null;
         Cell t = args.cdr;
         if (!(tmp instanceof Selfrep)) {
            throw new SchemeError("make-long-cpe expects a number for arg #1");
         } else {
            long arg0 = Math.round(((Selfrep)tmp).num);
            return new primnode(new LongCP(arg0));
         }
      }
   }

   public String toString() {
      return "<#make-long-cpe#>";
   }
}
