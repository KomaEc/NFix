package scm;

class Setvar extends Procedure implements Obj {
   Obj apply(Cell args, Env f) throws Exception {
      if (args == null) {
         throw new SchemeError("null args to define");
      } else if (args.car instanceof Symbol) {
         Symbol v = (Symbol)args.car;
         if (v == null) {
            throw new SchemeError("null symbol value");
         } else {
            Cell val = args.cdr;
            if (val == null) {
               throw new SchemeError("not enough args to set!");
            } else {
               Obj ret = val.car;
               if (ret != null) {
                  ret = ret.eval(f);
               }

               f.setvar(v, ret);
               return ret;
            }
         }
      } else {
         throw new SchemeError("bad argtype to set!" + args.car);
      }
   }

   public String toString() {
      return "<#set!#>";
   }
}
