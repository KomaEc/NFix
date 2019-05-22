package scm;

class StringP extends Procedure implements Obj {
   Obj apply(Cell args, Env f) throws Exception {
      if (args == null) {
         return null;
      } else {
         Obj target = args.car;
         if (target != null) {
            target = target.eval(f);
         }

         if (target == null) {
            return null;
         } else {
            return target instanceof Selfrep && ((Selfrep)target).val != null ? target : null;
         }
      }
   }

   public String toString() {
      return "<#string?#>";
   }
}
