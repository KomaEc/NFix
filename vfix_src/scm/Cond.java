package scm;

class Cond extends Procedure implements Obj {
   Obj apply(Cell args, Env f) throws Exception {
      for(Cell t = args; t != null; t = t.cdr) {
         if (t.car == null) {
            throw new SchemeError("null clause for cond");
         }

         Obj clause = t.car;
         if (!(clause instanceof Cell)) {
            throw new SchemeError("need a condition body for cond clause");
         }

         Obj result = ((Cell)clause).car;
         if (result != null) {
            result = result.eval(f);
         }

         if (result != null) {
            Obj body = ((Cell)clause).cdr.car;
            return body.eval(f);
         }
      }

      return null;
   }

   public String toString() {
      return "<#cond#>";
   }
}
