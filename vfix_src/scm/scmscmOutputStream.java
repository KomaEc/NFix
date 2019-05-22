package scm;

class scmscmOutputStream extends Procedure implements Obj {
   Obj apply(Cell args, Env f) throws Exception {
      if (args == null) {
         throw new SchemeError("make-outputstream expects 1 arguments");
      } else {
         Obj tmp = args.car != null ? args.car.eval(f) : null;
         Cell t = args.cdr;
         if (tmp != null && !(tmp instanceof Selfrep)) {
            throw new SchemeError("make-outputstream expects a String for arg #1");
         } else {
            String arg0 = tmp != null ? ((Selfrep)tmp).val : null;
            return new primnode(new scmOutputStream(arg0));
         }
      }
   }

   public String toString() {
      return "<#make-outputstream#>";
   }
}
