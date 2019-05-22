package scm;

class Or extends Procedure implements Obj {
   Obj apply(Cell args, Env f) throws Exception {
      Obj l1 = args.car.eval(f);
      Obj l2 = args.cdr.car.eval(f);
      return new Selfrep((double)((int)Math.round(((Selfrep)l1).num) | (int)Math.round(((Selfrep)l2).num)));
   }

   public String toString() {
      return "<#or#>";
   }
}
