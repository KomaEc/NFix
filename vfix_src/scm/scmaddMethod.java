package scm;

import jas.ClassEnv;
import jas.CodeAttr;
import jas.ExceptAttr;

class scmaddMethod extends Procedure implements Obj {
   Obj apply(Cell args, Env f) throws Exception {
      if (args == null) {
         throw new SchemeError("jas-class-addmethod expects 6 arguments");
      } else {
         Obj tmp = args.car != null ? args.car.eval(f) : null;
         Cell t = args.cdr;
         if (tmp != null && !(tmp instanceof primnode)) {
            throw new SchemeError("jas-class-addmethod expects a ClassEnv for arg #1");
         } else if (tmp != null && !(((primnode)tmp).val instanceof ClassEnv)) {
            throw new SchemeError("jas-class-addmethod expects a ClassEnv for arg #1");
         } else {
            ClassEnv arg0 = tmp != null ? (ClassEnv)((ClassEnv)((primnode)tmp).val) : null;
            if (t == null) {
               throw new SchemeError("jas-class-addmethod expects 6 arguments");
            } else {
               tmp = t.car != null ? t.car.eval(f) : null;
               t = t.cdr;
               if (!(tmp instanceof Selfrep)) {
                  throw new SchemeError("jas-class-addmethod expects a number for arg #2");
               } else {
                  short arg1 = (short)((int)((Selfrep)tmp).num);
                  if (t == null) {
                     throw new SchemeError("jas-class-addmethod expects 6 arguments");
                  } else {
                     tmp = t.car != null ? t.car.eval(f) : null;
                     t = t.cdr;
                     if (tmp != null && !(tmp instanceof Selfrep)) {
                        throw new SchemeError("jas-class-addmethod expects a String for arg #3");
                     } else {
                        String arg2 = tmp != null ? ((Selfrep)tmp).val : null;
                        if (t == null) {
                           throw new SchemeError("jas-class-addmethod expects 6 arguments");
                        } else {
                           tmp = t.car != null ? t.car.eval(f) : null;
                           t = t.cdr;
                           if (tmp != null && !(tmp instanceof Selfrep)) {
                              throw new SchemeError("jas-class-addmethod expects a String for arg #4");
                           } else {
                              String arg3 = tmp != null ? ((Selfrep)tmp).val : null;
                              if (t == null) {
                                 throw new SchemeError("jas-class-addmethod expects 6 arguments");
                              } else {
                                 tmp = t.car != null ? t.car.eval(f) : null;
                                 t = t.cdr;
                                 if (tmp != null && !(tmp instanceof primnode)) {
                                    throw new SchemeError("jas-class-addmethod expects a CodeAttr for arg #5");
                                 } else if (tmp != null && !(((primnode)tmp).val instanceof CodeAttr)) {
                                    throw new SchemeError("jas-class-addmethod expects a CodeAttr for arg #5");
                                 } else {
                                    CodeAttr arg4 = tmp != null ? (CodeAttr)((CodeAttr)((primnode)tmp).val) : null;
                                    if (t == null) {
                                       throw new SchemeError("jas-class-addmethod expects 6 arguments");
                                    } else {
                                       tmp = t.car != null ? t.car.eval(f) : null;
                                       t = t.cdr;
                                       if (tmp != null && !(tmp instanceof primnode)) {
                                          throw new SchemeError("jas-class-addmethod expects a ExceptAttr for arg #6");
                                       } else if (tmp != null && !(((primnode)tmp).val instanceof ExceptAttr)) {
                                          throw new SchemeError("jas-class-addmethod expects a ExceptAttr for arg #6");
                                       } else {
                                          ExceptAttr arg5 = tmp != null ? (ExceptAttr)((ExceptAttr)((primnode)tmp).val) : null;
                                          arg0.addMethod(arg1, arg2, arg3, arg4, arg5);
                                          return null;
                                       }
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public String toString() {
      return "<#jas-class-addmethod#>";
   }
}
