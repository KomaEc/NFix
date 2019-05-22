package scm;

class Progn extends Procedure implements Obj {
   Obj apply(Cell args, Env f) throws Exception {
      Cell t = args;

      Obj result;
      for(result = null; t != null; t = t.cdr) {
         if (t.car == null) {
            result = null;
         } else {
            result = t.car.eval(f);
         }
      }

      return result;
   }

   public String toString() {
      return "<#progn#>";
   }
}
