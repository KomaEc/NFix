package scm;

import jas.CP;
import jas.ConstAttr;

class scmConstAttr extends Procedure implements Obj {
   Obj apply(Cell args, Env f) throws Exception {
      if (args == null) {
         throw new SchemeError("make-const expects 1 arguments");
      } else {
         Obj tmp = args.car != null ? args.car.eval(f) : null;
         Cell t = args.cdr;
         if (tmp != null && !(tmp instanceof primnode)) {
            throw new SchemeError("make-const expects a CP for arg #1");
         } else if (tmp != null && !(((primnode)tmp).val instanceof CP)) {
            throw new SchemeError("make-const expects a CP for arg #1");
         } else {
            CP arg0 = tmp != null ? (CP)((CP)((primnode)tmp).val) : null;
            return new primnode(new ConstAttr(arg0));
         }
      }
   }

   public String toString() {
      return "<#make-const#>";
   }
}
