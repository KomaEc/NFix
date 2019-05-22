package soot.dexpler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import soot.ArrayType;
import soot.Body;
import soot.BooleanType;
import soot.ByteType;
import soot.CharType;
import soot.DoubleType;
import soot.FloatType;
import soot.IntType;
import soot.Local;
import soot.LongType;
import soot.RefType;
import soot.Scene;
import soot.ShortType;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.VoidType;
import soot.javaToJimple.LocalGenerator;
import soot.jimple.AssignStmt;
import soot.jimple.DoubleConstant;
import soot.jimple.FloatConstant;
import soot.jimple.IdentityStmt;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;
import soot.jimple.LongConstant;
import soot.jimple.NullConstant;
import soot.jimple.ParameterRef;
import soot.jimple.StringConstant;
import soot.jimple.ThisRef;
import soot.jimple.toolkits.scalar.LocalCreation;

public class Util {
   public static String dottedClassName(String typeDescriptor) {
      String t;
      int idx;
      String c;
      if (!isByteCodeClassName(typeDescriptor)) {
         t = typeDescriptor;

         for(idx = 0; idx < t.length() && t.charAt(idx) == '['; ++idx) {
         }

         c = t.substring(idx);
         if (c.length() == 1 && (c.startsWith("I") || c.startsWith("B") || c.startsWith("C") || c.startsWith("S") || c.startsWith("J") || c.startsWith("D") || c.startsWith("F") || c.startsWith("Z"))) {
            Type ty = getType(t);
            return ty == null ? "" : getType(t).toString();
         } else {
            throw new IllegalArgumentException("typeDescriptor is not a class typedescriptor: '" + typeDescriptor + "'");
         }
      } else {
         t = typeDescriptor;

         for(idx = 0; idx < t.length() && t.charAt(idx) == '['; ++idx) {
         }

         c = typeDescriptor.substring(idx);
         c = c.substring(c.indexOf(76) + 1, c.indexOf(59));
         c = c.replace('/', '.');
         return c;
      }
   }

   public static Type getType(String type) {
      int idx = 0;
      int arraySize = 0;
      Type returnType = null;
      boolean notFound = true;

      label41:
      while(idx < type.length() && notFound) {
         switch(type.charAt(idx)) {
         case 'B':
            returnType = ByteType.v();
            notFound = false;
            break;
         case 'C':
            returnType = CharType.v();
            notFound = false;
            break;
         case 'D':
            returnType = DoubleType.v();
            notFound = false;
            break;
         case 'E':
         case 'G':
         case 'H':
         case 'K':
         case 'M':
         case 'N':
         case 'O':
         case 'P':
         case 'Q':
         case 'R':
         case 'T':
         case 'U':
         case 'W':
         case 'X':
         case 'Y':
         default:
            throw new RuntimeException("unknown type: '" + type + "'");
         case 'F':
            returnType = FloatType.v();
            notFound = false;
            break;
         case 'I':
            returnType = IntType.v();
            notFound = false;
            break;
         case 'J':
            returnType = LongType.v();
            notFound = false;
            break;
         case 'L':
            String objectName = type.replaceAll("^[^L]*L", "").replaceAll(";$", "");
            returnType = RefType.v(objectName.replace("/", "."));
            notFound = false;
            break;
         case 'S':
            returnType = ShortType.v();
            notFound = false;
            break;
         case 'V':
            returnType = VoidType.v();
            notFound = false;
            break;
         case 'Z':
            returnType = BooleanType.v();
            notFound = false;
            break;
         case '[':
            while(true) {
               if (idx >= type.length() || type.charAt(idx) != '[') {
                  continue label41;
               }

               ++arraySize;
               ++idx;
            }
         }

         ++idx;
      }

      if (returnType != null && arraySize > 0) {
         returnType = ArrayType.v((Type)returnType, arraySize);
      }

      return (Type)returnType;
   }

   public static boolean isByteCodeClassName(String className) {
      return (className.startsWith("L") || className.startsWith("[")) && className.endsWith(";") && (className.indexOf(47) != -1 || className.indexOf(46) == -1);
   }

   public static <T> T[] concat(T[] first, T[] second) {
      T[] result = Arrays.copyOf(first, first.length + second.length);
      System.arraycopy(second, 0, result, first.length, second.length);
      return result;
   }

   public static boolean isFloatLike(Type t) {
      return t.equals(FloatType.v()) || t.equals(DoubleType.v()) || t.equals(RefType.v("java.lang.Float")) || t.equals(RefType.v("java.lang.Double"));
   }

   public static void emptyBody(Body jBody) {
      List<Unit> idStmts = new ArrayList();
      List<Local> idLocals = new ArrayList();
      Iterator var3 = jBody.getUnits().iterator();

      while(true) {
         Unit u;
         IdentityStmt i;
         do {
            do {
               if (!var3.hasNext()) {
                  jBody.getUnits().clear();
                  jBody.getLocals().clear();
                  jBody.getTraps().clear();
                  LocalGenerator lg = new LocalGenerator(jBody);
                  Iterator var9 = idStmts.iterator();

                  while(var9.hasNext()) {
                     Unit u = (Unit)var9.next();
                     jBody.getUnits().add(u);
                  }

                  var9 = idLocals.iterator();

                  while(var9.hasNext()) {
                     Local l = (Local)var9.next();
                     jBody.getLocals().add(l);
                  }

                  Type rType = jBody.getMethod().getReturnType();
                  jBody.getUnits().add((Unit)Jimple.v().newNopStmt());
                  if (rType instanceof VoidType) {
                     jBody.getUnits().add((Unit)Jimple.v().newReturnVoidStmt());
                  } else {
                     Type t = jBody.getMethod().getReturnType();
                     Local l = lg.generateLocal(t);
                     AssignStmt ass = null;
                     if (!(t instanceof RefType) && !(t instanceof ArrayType)) {
                        if (t instanceof LongType) {
                           ass = Jimple.v().newAssignStmt(l, LongConstant.v(0L));
                        } else if (t instanceof FloatType) {
                           ass = Jimple.v().newAssignStmt(l, FloatConstant.v(0.0F));
                        } else if (t instanceof IntType) {
                           ass = Jimple.v().newAssignStmt(l, IntConstant.v(0));
                        } else if (t instanceof DoubleType) {
                           ass = Jimple.v().newAssignStmt(l, DoubleConstant.v(0.0D));
                        } else {
                           if (!(t instanceof BooleanType) && !(t instanceof ByteType) && !(t instanceof CharType) && !(t instanceof ShortType)) {
                              throw new RuntimeException("error: return type unknown: " + t + " class: " + t.getClass());
                           }

                           ass = Jimple.v().newAssignStmt(l, IntConstant.v(0));
                        }
                     } else {
                        ass = Jimple.v().newAssignStmt(l, NullConstant.v());
                     }

                     jBody.getUnits().add((Unit)ass);
                     jBody.getUnits().add((Unit)Jimple.v().newReturnStmt(l));
                  }

                  return;
               }

               u = (Unit)var3.next();
            } while(!(u instanceof IdentityStmt));

            i = (IdentityStmt)u;
         } while(!(i.getRightOp() instanceof ParameterRef) && !(i.getRightOp() instanceof ThisRef));

         idStmts.add(u);
         idLocals.add((Local)i.getLeftOp());
      }
   }

   public static void addExceptionAfterUnit(Body b, String exceptionType, Unit u, String m) {
      LocalCreation lc = new LocalCreation(b.getLocals());
      Local l = lc.newLocal(RefType.v(exceptionType));
      List<Unit> newUnits = new ArrayList();
      Unit u1 = Jimple.v().newAssignStmt(l, Jimple.v().newNewExpr(RefType.v(exceptionType)));
      Unit u2 = Jimple.v().newInvokeStmt(Jimple.v().newSpecialInvokeExpr(l, Scene.v().makeMethodRef(Scene.v().getSootClass(exceptionType), "<init>", Collections.singletonList(RefType.v("java.lang.String")), VoidType.v(), false), (Value)StringConstant.v(m)));
      Unit u3 = Jimple.v().newThrowStmt(l);
      newUnits.add(u1);
      newUnits.add(u2);
      newUnits.add(u3);
      b.getUnits().insertBefore((List)newUnits, (Unit)u);
   }

   public static List<String> splitParameters(String parameters) {
      List<String> pList = new ArrayList();
      int idx = 0;
      boolean object = false;

      for(String curr = ""; idx < parameters.length(); ++idx) {
         char c = parameters.charAt(idx);
         curr = curr + c;
         switch(c) {
         case ';':
            object = false;
            pList.add(curr);
            curr = "";
            break;
         case 'L':
            object = true;
         case '[':
            break;
         default:
            if (!object) {
               pList.add(curr);
               curr = "";
            }
         }
      }

      return pList;
   }
}
