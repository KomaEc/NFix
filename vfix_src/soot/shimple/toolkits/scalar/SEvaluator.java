package soot.shimple.toolkits.scalar;

import java.util.Iterator;
import java.util.Map;
import soot.Local;
import soot.Type;
import soot.UnitBoxOwner;
import soot.UnknownType;
import soot.Value;
import soot.ValueBox;
import soot.jimple.Constant;
import soot.jimple.Expr;
import soot.jimple.toolkits.scalar.Evaluator;
import soot.shimple.PhiExpr;
import soot.util.Switch;

public class SEvaluator {
   public static boolean isValueConstantValued(Value op) {
      if (op instanceof PhiExpr) {
         Iterator<Value> argsIt = ((PhiExpr)op).getValues().iterator();
         Constant firstConstant = null;

         while(argsIt.hasNext()) {
            Value arg = (Value)argsIt.next();
            if (!(arg instanceof Constant)) {
               return false;
            }

            if (firstConstant == null) {
               firstConstant = (Constant)arg;
            } else if (!firstConstant.equals(arg)) {
               return false;
            }
         }

         return true;
      } else {
         return Evaluator.isValueConstantValued(op);
      }
   }

   public static Value getConstantValueOf(Value op) {
      if (!(op instanceof PhiExpr)) {
         return Evaluator.getConstantValueOf(op);
      } else {
         return !isValueConstantValued(op) ? null : ((PhiExpr)op).getValue(0);
      }
   }

   public static Constant getFuzzyConstantValueOf(Value v) {
      if (v instanceof Constant) {
         return (Constant)v;
      } else if (v instanceof Local) {
         return SEvaluator.BottomConstant.v();
      } else if (!(v instanceof Expr)) {
         return SEvaluator.BottomConstant.v();
      } else {
         Expr expr = (Expr)v;
         Constant constant = null;
         if (expr instanceof PhiExpr) {
            PhiExpr phi = (PhiExpr)expr;
            Iterator argsIt = phi.getValues().iterator();

            while(argsIt.hasNext()) {
               Value arg = (Value)argsIt.next();
               if (arg instanceof Constant && !(arg instanceof SEvaluator.TopConstant)) {
                  if (constant == null) {
                     constant = (Constant)arg;
                  } else if (!constant.equals(arg)) {
                     constant = SEvaluator.BottomConstant.v();
                     break;
                  }
               }
            }

            if (constant == null) {
               constant = SEvaluator.TopConstant.v();
            }
         } else {
            Iterator valueBoxesIt = expr.getUseBoxes().iterator();

            while(valueBoxesIt.hasNext()) {
               Value value = ((ValueBox)valueBoxesIt.next()).getValue();
               if (value instanceof SEvaluator.BottomConstant) {
                  constant = SEvaluator.BottomConstant.v();
                  break;
               }

               if (value instanceof SEvaluator.TopConstant) {
                  constant = SEvaluator.TopConstant.v();
               }
            }

            if (constant == null) {
               constant = (Constant)getConstantValueOf(expr);
            }

            if (constant == null) {
               constant = SEvaluator.BottomConstant.v();
            }
         }

         return constant;
      }
   }

   public static Constant getFuzzyConstantValueOf(Value v, Map<Local, Constant> localToConstant) {
      if (v instanceof Constant) {
         return (Constant)v;
      } else if (v instanceof Local) {
         return (Constant)localToConstant.get(v);
      } else if (!(v instanceof Expr)) {
         return SEvaluator.BottomConstant.v();
      } else {
         Expr expr = (Expr)v.clone();
         Iterator useBoxIt = expr.getUseBoxes().iterator();

         while(useBoxIt.hasNext()) {
            ValueBox useBox = (ValueBox)useBoxIt.next();
            Value use = useBox.getValue();
            if (use instanceof Local) {
               Constant constant = (Constant)localToConstant.get(use);
               if (useBox.canContainValue(constant)) {
                  useBox.setValue(constant);
               }
            }
         }

         if (expr instanceof UnitBoxOwner) {
            ((UnitBoxOwner)expr).clearUnitBoxes();
         }

         return getFuzzyConstantValueOf(expr);
      }
   }

   public static class BottomConstant extends SEvaluator.MetaConstant {
      private static final SEvaluator.BottomConstant constant = new SEvaluator.BottomConstant();

      private BottomConstant() {
      }

      public static Constant v() {
         return constant;
      }

      public Type getType() {
         return UnknownType.v();
      }

      public void apply(Switch sw) {
         throw new RuntimeException("Not implemented.");
      }
   }

   public static class TopConstant extends SEvaluator.MetaConstant {
      private static final SEvaluator.TopConstant constant = new SEvaluator.TopConstant();

      private TopConstant() {
      }

      public static Constant v() {
         return constant;
      }

      public Type getType() {
         return UnknownType.v();
      }

      public void apply(Switch sw) {
         throw new RuntimeException("Not implemented.");
      }
   }

   public abstract static class MetaConstant extends Constant {
   }
}
