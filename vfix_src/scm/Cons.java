package scm;

class Cons extends Procedure implements Obj {
   Obj apply(Cell args, Env f) throws Exception {
      Obj ncar = args.car.eval(f);
      Obj ncdr = args.cdr.car.eval(f);
      return new Cell(ncar, (Cell)ncdr);
   }

   public String toString() {
      return "<#cons#>";
   }
}
