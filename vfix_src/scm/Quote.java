package scm;

class Quote extends Procedure implements Obj {
   Obj apply(Cell args, Env f) throws Exception {
      if (args == null) {
         throw new SchemeError("null args to Quote");
      } else {
         return args.car;
      }
   }

   public String toString() {
      return "<#Quote#>";
   }
}
