package soot.jimple.toolkits.scalar;

import soot.Value;
import soot.jimple.AddExpr;
import soot.jimple.AndExpr;
import soot.jimple.ArithmeticConstant;
import soot.jimple.BinopExpr;
import soot.jimple.ClassConstant;
import soot.jimple.CmpExpr;
import soot.jimple.CmpgExpr;
import soot.jimple.CmplExpr;
import soot.jimple.Constant;
import soot.jimple.DivExpr;
import soot.jimple.EqExpr;
import soot.jimple.GeExpr;
import soot.jimple.GtExpr;
import soot.jimple.IntConstant;
import soot.jimple.LeExpr;
import soot.jimple.LongConstant;
import soot.jimple.LtExpr;
import soot.jimple.MulExpr;
import soot.jimple.NeExpr;
import soot.jimple.NegExpr;
import soot.jimple.NullConstant;
import soot.jimple.NumericConstant;
import soot.jimple.OrExpr;
import soot.jimple.RealConstant;
import soot.jimple.RemExpr;
import soot.jimple.ShlExpr;
import soot.jimple.ShrExpr;
import soot.jimple.StringConstant;
import soot.jimple.SubExpr;
import soot.jimple.UnopExpr;
import soot.jimple.UshrExpr;
import soot.jimple.XorExpr;

public class Evaluator {
   public static boolean isValueConstantValued(Value op) {
      if (op instanceof Constant) {
         return true;
      } else {
         Value c1;
         if (op instanceof UnopExpr) {
            c1 = ((UnopExpr)op).getOp();
            if (c1 == NullConstant.v()) {
               return false;
            }

            if (isValueConstantValued(c1)) {
               return true;
            }
         } else if (op instanceof BinopExpr) {
            if (op instanceof DivExpr || op instanceof RemExpr) {
               if (!isValueConstantValued(((BinopExpr)op).getOp1()) || !isValueConstantValued(((BinopExpr)op).getOp2())) {
                  return false;
               }

               c1 = getConstantValueOf(((BinopExpr)op).getOp1());
               Value c2 = getConstantValueOf(((BinopExpr)op).getOp2());
               if (c2 instanceof IntConstant && ((IntConstant)c2).value == 0) {
                  return false;
               }

               if (c2 instanceof LongConstant && ((LongConstant)c2).value == 0L) {
                  return false;
               }
            }

            if (isValueConstantValued(((BinopExpr)op).getOp1()) && isValueConstantValued(((BinopExpr)op).getOp2())) {
               return true;
            }
         }

         return false;
      }
   }

   public static Value getConstantValueOf(Value op) {
      if (!isValueConstantValued(op)) {
         return null;
      } else if (op instanceof Constant) {
         return op;
      } else {
         Value c1;
         if (op instanceof UnopExpr) {
            c1 = getConstantValueOf(((UnopExpr)op).getOp());
            if (op instanceof NegExpr) {
               return ((NumericConstant)c1).negate();
            }
         } else if (op instanceof BinopExpr) {
            c1 = getConstantValueOf(((BinopExpr)op).getOp1());
            Value c2 = getConstantValueOf(((BinopExpr)op).getOp2());
            if (op instanceof AddExpr) {
               return ((NumericConstant)c1).add((NumericConstant)c2);
            }

            if (op instanceof SubExpr) {
               return ((NumericConstant)c1).subtract((NumericConstant)c2);
            }

            if (op instanceof MulExpr) {
               return ((NumericConstant)c1).multiply((NumericConstant)c2);
            }

            if (op instanceof DivExpr) {
               return ((NumericConstant)c1).divide((NumericConstant)c2);
            }

            if (op instanceof RemExpr) {
               return ((NumericConstant)c1).remainder((NumericConstant)c2);
            }

            if (op instanceof EqExpr || op instanceof NeExpr) {
               if (c1 instanceof NumericConstant) {
                  if (op instanceof EqExpr) {
                     return ((NumericConstant)c1).equalEqual((NumericConstant)c2);
                  }

                  if (op instanceof NeExpr) {
                     return ((NumericConstant)c1).notEqual((NumericConstant)c2);
                  }
               } else if (c1 instanceof StringConstant || c1 instanceof NullConstant || c1 instanceof ClassConstant) {
                  boolean equality = c1.equals(c2);
                  boolean truth = op instanceof EqExpr ? equality : !equality;
                  IntConstant beauty = IntConstant.v(truth ? 1 : 0);
                  return beauty;
               }

               throw new RuntimeException("constant neither numeric nor string");
            }

            if (op instanceof GtExpr) {
               return ((NumericConstant)c1).greaterThan((NumericConstant)c2);
            }

            if (op instanceof GeExpr) {
               return ((NumericConstant)c1).greaterThanOrEqual((NumericConstant)c2);
            }

            if (op instanceof LtExpr) {
               return ((NumericConstant)c1).lessThan((NumericConstant)c2);
            }

            if (op instanceof LeExpr) {
               return ((NumericConstant)c1).lessThanOrEqual((NumericConstant)c2);
            }

            if (op instanceof AndExpr) {
               return ((ArithmeticConstant)c1).and((ArithmeticConstant)c2);
            }

            if (op instanceof OrExpr) {
               return ((ArithmeticConstant)c1).or((ArithmeticConstant)c2);
            }

            if (op instanceof XorExpr) {
               return ((ArithmeticConstant)c1).xor((ArithmeticConstant)c2);
            }

            if (op instanceof ShlExpr) {
               return ((ArithmeticConstant)c1).shiftLeft((ArithmeticConstant)c2);
            }

            if (op instanceof ShrExpr) {
               return ((ArithmeticConstant)c1).shiftRight((ArithmeticConstant)c2);
            }

            if (op instanceof UshrExpr) {
               return ((ArithmeticConstant)c1).unsignedShiftRight((ArithmeticConstant)c2);
            }

            if (op instanceof CmpExpr) {
               if (c1 instanceof LongConstant && c2 instanceof LongConstant) {
                  return ((LongConstant)c1).cmp((LongConstant)c2);
               }

               throw new IllegalArgumentException("CmpExpr: LongConstant(s) expected");
            }

            if (!(op instanceof CmpgExpr) && !(op instanceof CmplExpr)) {
               throw new RuntimeException("unknown binop: " + op);
            }

            if (!(c1 instanceof RealConstant) || !(c2 instanceof RealConstant)) {
               throw new IllegalArgumentException("CmpExpr: RealConstant(s) expected");
            }

            if (op instanceof CmpgExpr) {
               return ((RealConstant)c1).cmpg((RealConstant)c2);
            }

            if (op instanceof CmplExpr) {
               return ((RealConstant)c1).cmpl((RealConstant)c2);
            }
         }

         throw new RuntimeException("couldn't getConstantValueOf of: " + op);
      }
   }
}
