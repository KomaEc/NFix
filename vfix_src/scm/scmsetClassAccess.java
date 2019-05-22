package scm;

import jas.ClassEnv;

class scmsetClassAccess extends Procedure implements Obj {
   Obj apply(Cell args, Env f) throws Exception {
      if (args == null) {
         throw new SchemeError("jas-class-setaccess expects 2 arguments");
      } else {
         Obj tmp = args.car != null ? args.car.eval(f) : null;
         Cell t = args.cdr;
         if (tmp != null && !(tmp instanceof primnode)) {
            throw new SchemeError("jas-class-setaccess expects a ClassEnv for arg #1");
         } else if (tmp != null && !(((primnode)tmp).val instanceof ClassEnv)) {
            throw new SchemeError("jas-class-setaccess expects a ClassEnv for arg #1");
         } else {
            ClassEnv arg0 = tmp != null ? (ClassEnv)((ClassEnv)((primnode)tmp).val) : null;
            if (t == null) {
               throw new SchemeError("jas-class-setaccess expects 2 arguments");
            } else {
               tmp = t.car != null ? t.car.eval(f) : null;
               t = t.cdr;
               if (!(tmp instanceof Selfrep)) {
                  throw new SchemeError("jas-class-setaccess expects a number for arg #2");
               } else {
                  short arg1 = (short)((int)((Selfrep)tmp).num);
                  arg0.setClassAccess(arg1);
                  return null;
               }
            }
         }
      }
   }

   public String toString() {
      return "<#jas-class-setaccess#>";
   }
}
