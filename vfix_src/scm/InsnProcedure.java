package scm;

import jas.CP;
import jas.Insn;
import jas.Label;
import jas.RuntimeConstants;

class InsnProcedure extends Procedure implements Obj, RuntimeConstants {
   int opc;

   Obj apply(Cell args, Env f) throws Exception {
      if (args == null) {
         return new primnode(new Insn(this.opc));
      } else {
         Obj t = args.car.eval(f);
         if (t instanceof Selfrep) {
            int val = (int)((Selfrep)t).num;
            return new primnode(new Insn(this.opc, val));
         } else {
            if (t instanceof primnode) {
               Object tprime = ((primnode)t).val;
               if (tprime instanceof CP) {
                  return new primnode(new Insn(this.opc, (CP)tprime));
               }

               if (tprime instanceof Label) {
                  return new primnode(new Insn(this.opc, (Label)tprime));
               }
            }

            throw new SchemeError("Sorry, not yet implemented " + this.toString());
         }
      }
   }

   InsnProcedure(int opc) {
      this.opc = opc;
   }

   public String toString() {
      return "<#insn " + opcNames[this.opc] + "#>";
   }
}
