package scm;

class Minus extends Procedure implements Obj {
   Obj apply(Cell args, Env f) throws Exception {
      Obj l1 = args.car.eval(f);
      Obj l2 = args.cdr.car.eval(f);
      return new Selfrep(((Selfrep)l1).num - ((Selfrep)l2).num);
   }

   public String toString() {
      return "<#minus#>";
   }
}
