package scm;

import jas.FieldCP;

class scmFieldCP extends Procedure implements Obj {
   Obj apply(Cell args, Env f) throws Exception {
      if (args == null) {
         throw new SchemeError("make-field-cpe expects 3 arguments");
      } else {
         Obj tmp = args.car != null ? args.car.eval(f) : null;
         Cell t = args.cdr;
         if (tmp != null && !(tmp instanceof Selfrep)) {
            throw new SchemeError("make-field-cpe expects a String for arg #1");
         } else {
            String arg0 = tmp != null ? ((Selfrep)tmp).val : null;
            if (t == null) {
               throw new SchemeError("make-field-cpe expects 3 arguments");
            } else {
               tmp = t.car != null ? t.car.eval(f) : null;
               t = t.cdr;
               if (tmp != null && !(tmp instanceof Selfrep)) {
                  throw new SchemeError("make-field-cpe expects a String for arg #2");
               } else {
                  String arg1 = tmp != null ? ((Selfrep)tmp).val : null;
                  if (t == null) {
                     throw new SchemeError("make-field-cpe expects 3 arguments");
                  } else {
                     tmp = t.car != null ? t.car.eval(f) : null;
                     t = t.cdr;
                     if (tmp != null && !(tmp instanceof Selfrep)) {
                        throw new SchemeError("make-field-cpe expects a String for arg #3");
                     } else {
                        String arg2 = tmp != null ? ((Selfrep)tmp).val : null;
                        return new primnode(new FieldCP(arg0, arg1, arg2));
                     }
                  }
               }
            }
         }
      }
   }

   public String toString() {
      return "<#make-field-cpe#>";
   }
}
