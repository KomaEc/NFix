package scm;

class Mapcar extends Procedure implements Obj {
   Obj apply(Cell args, Env f) throws Exception {
      Obj ftmp = args.car;
      if (ftmp != null) {
         ftmp = ftmp.eval(f);
      }

      if (ftmp == null) {
         throw new SchemeError("null function for mapcar");
      } else if (!(ftmp instanceof Procedure)) {
         throw new SchemeError("expected a procedure for mapcar");
      } else {
         Procedure fn = (Procedure)ftmp;
         Cell t = (Cell)((Cell)args.cdr.car.eval(f));
         Cell res = null;

         for(Cell tail = null; t != null; t = t.cdr) {
            if (tail == null) {
               res = new Cell(fn.apply(new Cell(t.car, (Cell)null), f), (Cell)null);
               tail = res;
            } else {
               tail.cdr = new Cell(fn.apply(new Cell(t.car, (Cell)null), f), (Cell)null);
            }
         }

         return res;
      }
   }
}
