package scm;

import jas.CP;
import jas.ExceptAttr;

class scmaddException extends Procedure implements Obj {
   Obj apply(Cell args, Env f) throws Exception {
      if (args == null) {
         throw new SchemeError("jas-exception-add expects 2 arguments");
      } else {
         Obj tmp = args.car != null ? args.car.eval(f) : null;
         Cell t = args.cdr;
         if (tmp != null && !(tmp instanceof primnode)) {
            throw new SchemeError("jas-exception-add expects a ExceptAttr for arg #1");
         } else if (tmp != null && !(((primnode)tmp).val instanceof ExceptAttr)) {
            throw new SchemeError("jas-exception-add expects a ExceptAttr for arg #1");
         } else {
            ExceptAttr arg0 = tmp != null ? (ExceptAttr)((ExceptAttr)((primnode)tmp).val) : null;
            if (t == null) {
               throw new SchemeError("jas-exception-add expects 2 arguments");
            } else {
               tmp = t.car != null ? t.car.eval(f) : null;
               t = t.cdr;
               if (tmp != null && !(tmp instanceof primnode)) {
                  throw new SchemeError("jas-exception-add expects a CP for arg #2");
               } else if (tmp != null && !(((primnode)tmp).val instanceof CP)) {
                  throw new SchemeError("jas-exception-add expects a CP for arg #2");
               } else {
                  CP arg1 = tmp != null ? (CP)((CP)((primnode)tmp).val) : null;
                  arg0.addException(arg1);
                  return null;
               }
            }
         }
      }
   }

   public String toString() {
      return "<#jas-exception-add#>";
   }
}
