package scm;

class Lambda extends Procedure implements Obj {
   Obj apply(Cell args, Env f) throws Exception {
      Procedure ret = new Procedure();
      if (args == null) {
         throw new SchemeError("null args to Lambda");
      } else {
         ret.formals = (Cell)args.car;
         ret.body = args.cdr;
         ret.procenv = f;
         return ret;
      }
   }

   public String toString() {
      return "<#Lambda#>";
   }
}
