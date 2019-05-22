package scm;

import jas.CP;
import jas.ConstAttr;
import jas.Var;

class scmVar extends Procedure implements Obj {
   Obj apply(Cell args, Env f) throws Exception {
      if (args == null) {
         throw new SchemeError("make-field expects 4 arguments");
      } else {
         Obj tmp = args.car != null ? args.car.eval(f) : null;
         Cell t = args.cdr;
         if (!(tmp instanceof Selfrep)) {
            throw new SchemeError("make-field expects a number for arg #1");
         } else {
            short arg0 = (short)((int)Math.round(((Selfrep)tmp).num));
            if (t == null) {
               throw new SchemeError("make-field expects 4 arguments");
            } else {
               tmp = t.car != null ? t.car.eval(f) : null;
               t = t.cdr;
               if (tmp != null && !(tmp instanceof primnode)) {
                  throw new SchemeError("make-field expects a CP for arg #2");
               } else if (tmp != null && !(((primnode)tmp).val instanceof CP)) {
                  throw new SchemeError("make-field expects a CP for arg #2");
               } else {
                  CP arg1 = tmp != null ? (CP)((CP)((primnode)tmp).val) : null;
                  if (t == null) {
                     throw new SchemeError("make-field expects 4 arguments");
                  } else {
                     tmp = t.car != null ? t.car.eval(f) : null;
                     t = t.cdr;
                     if (tmp != null && !(tmp instanceof primnode)) {
                        throw new SchemeError("make-field expects a CP for arg #3");
                     } else if (tmp != null && !(((primnode)tmp).val instanceof CP)) {
                        throw new SchemeError("make-field expects a CP for arg #3");
                     } else {
                        CP arg2 = tmp != null ? (CP)((CP)((primnode)tmp).val) : null;
                        if (t == null) {
                           throw new SchemeError("make-field expects 4 arguments");
                        } else {
                           tmp = t.car != null ? t.car.eval(f) : null;
                           t = t.cdr;
                           if (tmp != null && !(tmp instanceof primnode)) {
                              throw new SchemeError("make-field expects a ConstAttr for arg #4");
                           } else if (tmp != null && !(((primnode)tmp).val instanceof ConstAttr)) {
                              throw new SchemeError("make-field expects a ConstAttr for arg #4");
                           } else {
                              ConstAttr arg3 = tmp != null ? (ConstAttr)((ConstAttr)((primnode)tmp).val) : null;
                              return new primnode(new Var(arg0, arg1, arg2, arg3));
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public String toString() {
      return "<#make-field#>";
   }
}
