package scm;

import jas.Catchtable;
import jas.CodeAttr;

class scmsetCatchtable extends Procedure implements Obj {
   Obj apply(Cell args, Env f) throws Exception {
      if (args == null) {
         throw new SchemeError("jas-set-catchtable expects 2 arguments");
      } else {
         Obj tmp = args.car != null ? args.car.eval(f) : null;
         Cell t = args.cdr;
         if (tmp != null && !(tmp instanceof primnode)) {
            throw new SchemeError("jas-set-catchtable expects a CodeAttr for arg #1");
         } else if (tmp != null && !(((primnode)tmp).val instanceof CodeAttr)) {
            throw new SchemeError("jas-set-catchtable expects a CodeAttr for arg #1");
         } else {
            CodeAttr arg0 = tmp != null ? (CodeAttr)((CodeAttr)((primnode)tmp).val) : null;
            if (t == null) {
               throw new SchemeError("jas-set-catchtable expects 2 arguments");
            } else {
               tmp = t.car != null ? t.car.eval(f) : null;
               t = t.cdr;
               if (tmp != null && !(tmp instanceof primnode)) {
                  throw new SchemeError("jas-set-catchtable expects a Catchtable for arg #2");
               } else if (tmp != null && !(((primnode)tmp).val instanceof Catchtable)) {
                  throw new SchemeError("jas-set-catchtable expects a Catchtable for arg #2");
               } else {
                  Catchtable arg1 = tmp != null ? (Catchtable)((Catchtable)((primnode)tmp).val) : null;
                  arg0.setCatchtable(arg1);
                  return null;
               }
            }
         }
      }
   }

   public String toString() {
      return "<#jas-set-catchtable#>";
   }
}
