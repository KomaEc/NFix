package scm;

import jas.CodeAttr;
import jas.Insn;

class scmaddInsn extends Procedure implements Obj {
   Obj apply(Cell args, Env f) throws Exception {
      if (args == null) {
         throw new SchemeError("jas-code-addinsn expects 2 arguments");
      } else {
         Obj tmp = args.car != null ? args.car.eval(f) : null;
         Cell t = args.cdr;
         if (tmp != null && !(tmp instanceof primnode)) {
            throw new SchemeError("jas-code-addinsn expects a CodeAttr for arg #1");
         } else if (tmp != null && !(((primnode)tmp).val instanceof CodeAttr)) {
            throw new SchemeError("jas-code-addinsn expects a CodeAttr for arg #1");
         } else {
            CodeAttr arg0 = tmp != null ? (CodeAttr)((CodeAttr)((primnode)tmp).val) : null;
            if (t == null) {
               throw new SchemeError("jas-code-addinsn expects 2 arguments");
            } else {
               tmp = t.car != null ? t.car.eval(f) : null;
               t = t.cdr;
               if (tmp != null && !(tmp instanceof primnode)) {
                  throw new SchemeError("jas-code-addinsn expects a Insn for arg #2");
               } else if (tmp != null && !(((primnode)tmp).val instanceof Insn)) {
                  throw new SchemeError("jas-code-addinsn expects a Insn for arg #2");
               } else {
                  Insn arg1 = tmp != null ? (Insn)((Insn)((primnode)tmp).val) : null;
                  arg0.addInsn(arg1);
                  return null;
               }
            }
         }
      }
   }

   public String toString() {
      return "<#jas-code-addinsn#>";
   }
}
