package scm;

class Cdr extends Procedure implements Obj {
   Obj apply(Cell args, Env f) throws Exception {
      Cell tmp = (Cell)args.car.eval(f);
      return tmp.cdr;
   }

   public String toString() {
      return "<#cdr#>";
   }
}
