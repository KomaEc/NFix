package scm;

class EqP extends Procedure implements Obj {
   Obj apply(Cell args, Env f) throws Exception {
      if (args == null) {
         return null;
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

         if (target1 == null && target2 == null) {
            return new Selfrep(1.0D);
         } else if (target1 != null && target2 != null) {
            if (target1 == target2) {
               return target1;
            } else {
               if (target1 instanceof Selfrep && target2 instanceof Selfrep) {
                  if (((Selfrep)target1).val == null) {
                     if (((Selfrep)target1).num == ((Selfrep)target2).num) {
                        return new Selfrep(1.0D);
                     }
                  } else if (((Selfrep)target1).val.equals(((Selfrep)target2).val)) {
                     return new Selfrep(1.0D);
                  }
               }

               return null;
            }
         } else {
            return null;
         }
      }
   }

   public String toString() {
      return "<#eq?#>";
   }
}
