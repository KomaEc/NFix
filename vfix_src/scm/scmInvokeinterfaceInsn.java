package scm;

import jas.CP;
import jas.InvokeinterfaceInsn;

class scmInvokeinterfaceInsn extends Procedure implements Obj {
   Obj apply(Cell args, Env f) throws Exception {
      if (args == null) {
         throw new SchemeError("invokeinterface expects 2 arguments");
      } else {
         Obj tmp = args.car != null ? args.car.eval(f) : null;
         Cell t = args.cdr;
         if (tmp != null && !(tmp instanceof primnode)) {
            throw new SchemeError("invokeinterface expects a CP for arg #1");
         } else if (tmp != null && !(((primnode)tmp).val instanceof CP)) {
            throw new SchemeError("invokeinterface expects a CP for arg #1");
         } else {
            CP arg0 = tmp != null ? (CP)((CP)((primnode)tmp).val) : null;
            if (t == null) {
               throw new SchemeError("invokeinterface expects 2 arguments");
            } else {
               tmp = t.car != null ? t.car.eval(f) : null;
               t = t.cdr;
               if (!(tmp instanceof Selfrep)) {
                  throw new SchemeError("invokeinterface expects a number for arg #2");
               } else {
                  int arg1 = (int)Math.round(((Selfrep)tmp).num);
                  return new primnode(new InvokeinterfaceInsn(arg0, arg1));
               }
            }
         }
      }
   }

   public String toString() {
      return "<#invokeinterface#>";
   }
}
