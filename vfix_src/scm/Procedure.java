package scm;

class Procedure implements Obj {
   Cell body;
   Cell formals;
   Env procenv;

   Env extendargs(Cell args, Env f) throws Exception {
      Cell params = null;

      for(Cell tail = null; args != null; args = args.cdr) {
         Obj now = args.car;
         if (now != null) {
            now = now.eval(f);
         }

         if (tail != null) {
            tail.cdr = new Cell(now, (Cell)null);
            tail = tail.cdr;
         } else {
            params = new Cell(now, params);
            tail = params;
         }
      }

      return this.procenv.extendenv(this.formals, params);
   }

   Obj apply(Cell args, Env f) throws Exception {
      Env newEnv = this.extendargs(args, f);
      Cell expr = this.body;

      Obj ret;
      for(ret = null; expr != null; expr = expr.cdr) {
         ret = expr.car;
         if (ret != null) {
            ret = ret.eval(newEnv);
         }
      }

      return ret;
   }

   public Obj eval(Env e) {
      throw new SchemeError("Cant eval procedures directly");
   }

   public String toString() {
      return "<lambda generated> " + this.body;
   }
}
