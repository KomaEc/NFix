package scm;

class MoreP extends Procedure implements Obj {
   Obj apply(Cell args, Env f) throws Exception {
      if (args == null) {
         throw new SchemeError("> expects a pair of arguments");
      } else {
         Obj target1 = args.car;
         if (target1 != null) {
            target1 = target1.eval(f);
         }

         args = args.cdr;
         Obj target2 = args.car;
         if (target2 != null) {
            target2 = target2.eval(f);
         }

         if (target1 != null && target2 != null) {
            if (target1 instanceof Selfrep && target2 instanceof Selfrep) {
               return ((Selfrep)target1).num > ((Selfrep)target2).num ? target1 : null;
            } else {
               throw new SchemeError("> expects a pair of numbers as args");
            }
         } else {
            throw new SchemeError("> expects a pair of arguments");
         }
      }
   }

   public String toString() {
      return "<#>#>";
   }
}
