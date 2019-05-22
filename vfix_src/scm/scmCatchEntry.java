package scm;

import jas.CP;
import jas.CatchEntry;
import jas.Label;

class scmCatchEntry extends Procedure implements Obj {
   Obj apply(Cell args, Env f) throws Exception {
      if (args == null) {
         throw new SchemeError("make-catch-entry expects 4 arguments");
      } else {
         Obj tmp = args.car != null ? args.car.eval(f) : null;
         Cell t = args.cdr;
         if (tmp != null && !(tmp instanceof primnode)) {
            throw new SchemeError("make-catch-entry expects a Label for arg #1");
         } else if (tmp != null && !(((primnode)tmp).val instanceof Label)) {
            throw new SchemeError("make-catch-entry expects a Label for arg #1");
         } else {
            Label arg0 = tmp != null ? (Label)((Label)((primnode)tmp).val) : null;
            if (t == null) {
               throw new SchemeError("make-catch-entry expects 4 arguments");
            } else {
               tmp = t.car != null ? t.car.eval(f) : null;
               t = t.cdr;
               if (tmp != null && !(tmp instanceof primnode)) {
                  throw new SchemeError("make-catch-entry expects a Label for arg #2");
               } else if (tmp != null && !(((primnode)tmp).val instanceof Label)) {
                  throw new SchemeError("make-catch-entry expects a Label for arg #2");
               } else {
                  Label arg1 = tmp != null ? (Label)((Label)((primnode)tmp).val) : null;
                  if (t == null) {
                     throw new SchemeError("make-catch-entry expects 4 arguments");
                  } else {
                     tmp = t.car != null ? t.car.eval(f) : null;
                     t = t.cdr;
                     if (tmp != null && !(tmp instanceof primnode)) {
                        throw new SchemeError("make-catch-entry expects a Label for arg #3");
                     } else if (tmp != null && !(((primnode)tmp).val instanceof Label)) {
                        throw new SchemeError("make-catch-entry expects a Label for arg #3");
                     } else {
                        Label arg2 = tmp != null ? (Label)((Label)((primnode)tmp).val) : null;
                        if (t == null) {
                           throw new SchemeError("make-catch-entry expects 4 arguments");
                        } else {
                           tmp = t.car != null ? t.car.eval(f) : null;
                           t = t.cdr;
                           if (tmp != null && !(tmp instanceof primnode)) {
                              throw new SchemeError("make-catch-entry expects a CP for arg #4");
                           } else if (tmp != null && !(((primnode)tmp).val instanceof CP)) {
                              throw new SchemeError("make-catch-entry expects a CP for arg #4");
                           } else {
                              CP arg3 = tmp != null ? (CP)((CP)((primnode)tmp).val) : null;
                              return new primnode(new CatchEntry(arg0, arg1, arg2, arg3));
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
      return "<#make-catch-entry#>";
   }
}
