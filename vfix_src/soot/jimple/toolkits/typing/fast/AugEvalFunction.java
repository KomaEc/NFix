package soot.jimple.toolkits.typing.fast;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import soot.ArrayType;
import soot.BooleanType;
import soot.ByteType;
import soot.CharType;
import soot.DoubleType;
import soot.FloatType;
import soot.IntType;
import soot.IntegerType;
import soot.Local;
import soot.LongType;
import soot.NullType;
import soot.RefType;
import soot.Scene;
import soot.ShortType;
import soot.TrapManager;
import soot.Type;
import soot.Value;
import soot.jimple.AddExpr;
import soot.jimple.AndExpr;
import soot.jimple.ArrayRef;
import soot.jimple.BinopExpr;
import soot.jimple.CastExpr;
import soot.jimple.CaughtExceptionRef;
import soot.jimple.ClassConstant;
import soot.jimple.CmpExpr;
import soot.jimple.CmpgExpr;
import soot.jimple.CmplExpr;
import soot.jimple.DivExpr;
import soot.jimple.DoubleConstant;
import soot.jimple.EqExpr;
import soot.jimple.FieldRef;
import soot.jimple.FloatConstant;
import soot.jimple.GeExpr;
import soot.jimple.GtExpr;
import soot.jimple.InstanceOfExpr;
import soot.jimple.IntConstant;
import soot.jimple.InvokeExpr;
import soot.jimple.JimpleBody;
import soot.jimple.LeExpr;
import soot.jimple.LengthExpr;
import soot.jimple.LongConstant;
import soot.jimple.LtExpr;
import soot.jimple.MethodHandle;
import soot.jimple.MulExpr;
import soot.jimple.NeExpr;
import soot.jimple.NegExpr;
import soot.jimple.NewArrayExpr;
import soot.jimple.NewExpr;
import soot.jimple.NewMultiArrayExpr;
import soot.jimple.NullConstant;
import soot.jimple.OrExpr;
import soot.jimple.ParameterRef;
import soot.jimple.RemExpr;
import soot.jimple.ShlExpr;
import soot.jimple.ShrExpr;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
import soot.jimple.SubExpr;
import soot.jimple.ThisRef;
import soot.jimple.UshrExpr;
import soot.jimple.XorExpr;

public class AugEvalFunction implements IEvalFunction {
   private JimpleBody jb;

   public AugEvalFunction(JimpleBody jb) {
      this.jb = jb;
   }

   public Collection<Type> eval(Typing tg, Value expr, Stmt stmt) {
      return Collections.singletonList(eval_(tg, expr, stmt, this.jb));
   }

   public static Type eval_(Typing tg, Value expr, Stmt stmt, JimpleBody jb) {
      if (expr instanceof ThisRef) {
         return ((ThisRef)expr).getType();
      } else if (expr instanceof ParameterRef) {
         return ((ParameterRef)expr).getType();
      } else {
         Local av;
         if (expr instanceof Local) {
            av = (Local)expr;
            return tg == null ? null : tg.get(av);
         } else if (expr instanceof BinopExpr) {
            BinopExpr be = (BinopExpr)expr;
            Value opl = be.getOp1();
            Value opr = be.getOp2();
            Type tl = eval_(tg, opl, stmt, jb);
            Type tr = eval_(tg, opr, stmt, jb);
            if (!(expr instanceof CmpExpr) && !(expr instanceof CmpgExpr) && !(expr instanceof CmplExpr)) {
               if (!(expr instanceof GeExpr) && !(expr instanceof GtExpr) && !(expr instanceof LeExpr) && !(expr instanceof LtExpr) && !(expr instanceof EqExpr) && !(expr instanceof NeExpr)) {
                  if (expr instanceof ShlExpr) {
                     return (Type)(tl instanceof IntegerType ? IntType.v() : tl);
                  } else if (!(expr instanceof ShrExpr) && !(expr instanceof UshrExpr)) {
                     if (!(expr instanceof AddExpr) && !(expr instanceof SubExpr) && !(expr instanceof MulExpr) && !(expr instanceof DivExpr) && !(expr instanceof RemExpr)) {
                        if (!(expr instanceof AndExpr) && !(expr instanceof OrExpr) && !(expr instanceof XorExpr)) {
                           throw new RuntimeException("Unhandled binary expression: " + expr);
                        } else if (tl instanceof IntegerType && tr instanceof IntegerType) {
                           if (tl instanceof BooleanType) {
                              return (Type)(tr instanceof BooleanType ? BooleanType.v() : tr);
                           } else if (tr instanceof BooleanType) {
                              return tl;
                           } else {
                              Collection<Type> rs = AugHierarchy.lcas_(tl, tr);
                              Iterator var10 = rs.iterator();
                              if (var10.hasNext()) {
                                 Type r = (Type)var10.next();
                                 return r;
                              } else {
                                 throw new RuntimeException();
                              }
                           }
                        } else {
                           return tl;
                        }
                     } else {
                        return (Type)(tl instanceof IntegerType ? IntType.v() : tl);
                     }
                  } else {
                     return tl;
                  }
               } else {
                  return BooleanType.v();
               }
            } else {
               return ByteType.v();
            }
         } else if (expr instanceof NegExpr) {
            Type t = eval_(tg, ((NegExpr)expr).getOp(), stmt, jb);
            if (t instanceof IntegerType) {
               if (!(t instanceof Integer1Type) && !(t instanceof BooleanType) && !(t instanceof Integer127Type) && !(t instanceof ByteType)) {
                  return (Type)(!(t instanceof ShortType) && !(t instanceof Integer32767Type) ? IntType.v() : ShortType.v());
               } else {
                  return ByteType.v();
               }
            } else {
               return t;
            }
         } else if (!(expr instanceof CaughtExceptionRef)) {
            if (expr instanceof ArrayRef) {
               av = (Local)((ArrayRef)expr).getBase();
               Type at = tg.get(av);
               if (at instanceof ArrayType) {
                  return ((ArrayType)at).getElementType();
               } else if (at instanceof RefType) {
                  RefType ref = (RefType)at;
                  return (Type)(!ref.getSootClass().getName().equals("java.lang.Object") && !ref.getSootClass().getName().equals("java.io.Serializable") && !ref.getSootClass().getName().equals("java.lang.Cloneable") ? BottomType.v() : ref);
               } else {
                  return BottomType.v();
               }
            } else if (expr instanceof NewArrayExpr) {
               return ((NewArrayExpr)expr).getBaseType().makeArrayType();
            } else if (expr instanceof NewMultiArrayExpr) {
               return ((NewMultiArrayExpr)expr).getBaseType();
            } else if (expr instanceof CastExpr) {
               return ((CastExpr)expr).getCastType();
            } else if (expr instanceof InstanceOfExpr) {
               return BooleanType.v();
            } else if (expr instanceof LengthExpr) {
               return IntType.v();
            } else if (expr instanceof InvokeExpr) {
               return ((InvokeExpr)expr).getMethodRef().returnType();
            } else if (expr instanceof NewExpr) {
               return ((NewExpr)expr).getBaseType();
            } else if (expr instanceof FieldRef) {
               return ((FieldRef)expr).getType();
            } else if (expr instanceof DoubleConstant) {
               return DoubleType.v();
            } else if (expr instanceof FloatConstant) {
               return FloatType.v();
            } else if (expr instanceof IntConstant) {
               int value = ((IntConstant)expr).value;
               if (value >= 0 && value < 2) {
                  return Integer1Type.v();
               } else if (value >= 2 && value < 128) {
                  return Integer127Type.v();
               } else if (value >= -128 && value < 0) {
                  return ByteType.v();
               } else if (value >= 128 && value < 32768) {
                  return Integer32767Type.v();
               } else if (value >= -32768 && value < -128) {
                  return ShortType.v();
               } else {
                  return (Type)(value >= 32768 && value < 65536 ? CharType.v() : IntType.v());
               }
            } else if (expr instanceof LongConstant) {
               return LongType.v();
            } else if (expr instanceof NullConstant) {
               return NullType.v();
            } else if (expr instanceof StringConstant) {
               return RefType.v("java.lang.String");
            } else if (expr instanceof ClassConstant) {
               return RefType.v("java.lang.Class");
            } else if (expr instanceof MethodHandle) {
               return RefType.v("java.lang.invoke.MethodHandle");
            } else {
               throw new RuntimeException("Unhandled expression: " + expr);
            }
         } else {
            RefType r = null;
            RefType throwableType = Scene.v().getRefType("java.lang.Throwable");
            Iterator var6 = TrapManager.getExceptionTypesOf(stmt, jb).iterator();

            while(true) {
               while(var6.hasNext()) {
                  RefType t = (RefType)var6.next();
                  if (r == null) {
                     r = t;
                  } else if (!t.getSootClass().isPhantom() && !r.getSootClass().isPhantom()) {
                     r = BytecodeHierarchy.lcsc(r, t, throwableType);
                  } else {
                     r = throwableType;
                  }
               }

               if (r == null) {
                  throw new RuntimeException("Exception reference used other than as the first statement of an exception handler.");
               }

               return r;
            }
         }
      }
   }
}
