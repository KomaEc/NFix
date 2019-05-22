package scm;

import jas.IntegerCP;

class scmIntegerCP extends Procedure implements Obj {
   Obj apply(Cell args, Env f) throws Exception {
      if (args == null) {
         throw new SchemeError("make-integer-cpe expects 1 arguments");
      } else {
         Obj tmp = args.car != null ? args.car.eval(f) : null;
         Cell t = args.cdr;
         if (!(tmp instanceof Selfrep)) {
            throw new SchemeError("make-integer-cpe expects a number for arg #1");
         } else {
            int arg0 = (int)Math.round(((Selfrep)tmp).num);
            return new primnode(new IntegerCP(arg0));
         }
      }
   }

   public String toString() {
      return "<#make-integer-cpe#>";
   }
}
