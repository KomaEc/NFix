package scm;

import jas.AsciiCP;

class scmAsciiCP extends Procedure implements Obj {
   Obj apply(Cell args, Env f) throws Exception {
      if (args == null) {
         throw new SchemeError("make-ascii-cpe expects 1 arguments");
      } else {
         Obj tmp = args.car != null ? args.car.eval(f) : null;
         Cell t = args.cdr;
         if (tmp != null && !(tmp instanceof Selfrep)) {
            throw new SchemeError("make-ascii-cpe expects a String for arg #1");
         } else {
            String arg0 = tmp != null ? ((Selfrep)tmp).val : null;
            return new primnode(new AsciiCP(arg0));
         }
      }
   }

   public String toString() {
      return "<#make-ascii-cpe#>";
   }
}
