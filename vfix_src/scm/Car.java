package scm;

class Car extends Procedure implements Obj {
   Obj apply(Cell args, Env f) throws Exception {
      Cell tmp = (Cell)args.car.eval(f);
      return tmp.car;
   }

   public String toString() {
      return "<#car#>";
   }
}
