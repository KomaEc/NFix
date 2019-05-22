package scm;

import jas.CatchEntry;
import jas.Catchtable;

class scmaddEntry extends Procedure implements Obj {
   Obj apply(Cell args, Env f) throws Exception {
      if (args == null) {
         throw new SchemeError("jas-add-catch-entry expects 2 arguments");
      } else {
         Obj tmp = args.car != null ? args.car.eval(f) : null;
         Cell t = args.cdr;
         if (tmp != null && !(tmp instanceof primnode)) {
            throw new SchemeError("jas-add-catch-entry expects a Catchtable for arg #1");
         } else if (tmp != null && !(((primnode)tmp).val instanceof Catchtable)) {
            throw new SchemeError("jas-add-catch-entry expects a Catchtable for arg #1");
         } else {
            Catchtable arg0 = tmp != null ? (Catchtable)((Catchtable)((primnode)tmp).val) : null;
            if (t == null) {
               throw new SchemeError("jas-add-catch-entry expects 2 arguments");
            } else {
               tmp = t.car != null ? t.car.eval(f) : null;
               t = t.cdr;
               if (tmp != null && !(tmp instanceof primnode)) {
                  throw new SchemeError("jas-add-catch-entry expects a CatchEntry for arg #2");
               } else if (tmp != null && !(((primnode)tmp).val instanceof CatchEntry)) {
                  throw new SchemeError("jas-add-catch-entry expects a CatchEntry for arg #2");
               } else {
                  CatchEntry arg1 = tmp != null ? (CatchEntry)((CatchEntry)((primnode)tmp).val) : null;
                  arg0.addEntry(arg1);
                  return null;
               }
            }
         }
      }
   }

   public String toString() {
      return "<#jas-add-catch-entry#>";
   }
}
