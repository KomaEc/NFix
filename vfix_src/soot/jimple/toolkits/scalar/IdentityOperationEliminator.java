package soot.jimple.toolkits.scalar;

import java.util.Iterator;
import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.DoubleType;
import soot.FloatType;
import soot.G;
import soot.IntType;
import soot.LongType;
import soot.Singletons;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.jimple.AddExpr;
import soot.jimple.AssignStmt;
import soot.jimple.BinopExpr;
import soot.jimple.DoubleConstant;
import soot.jimple.FloatConstant;
import soot.jimple.IntConstant;
import soot.jimple.LongConstant;
import soot.jimple.MulExpr;
import soot.jimple.OrExpr;
import soot.jimple.SubExpr;

public class IdentityOperationEliminator extends BodyTransformer {
   public IdentityOperationEliminator(Singletons.Global g) {
   }

   public static IdentityOperationEliminator v() {
      return G.v().soot_jimple_toolkits_scalar_IdentityOperationEliminator();
   }

   protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
      Iterator unitIt = b.getUnits().iterator();

      Unit u;
      AssignStmt assignStmt;
      while(unitIt.hasNext()) {
         u = (Unit)unitIt.next();
         if (u instanceof AssignStmt) {
            assignStmt = (AssignStmt)u;
            BinopExpr aer;
            if (assignStmt.getRightOp() instanceof AddExpr) {
               aer = (BinopExpr)assignStmt.getRightOp();
               if (this.isConstZero(aer.getOp1())) {
                  assignStmt.setRightOp(aer.getOp2());
               } else if (this.isConstZero(aer.getOp2())) {
                  assignStmt.setRightOp(aer.getOp1());
               }
            }

            if (assignStmt.getRightOp() instanceof SubExpr) {
               aer = (BinopExpr)assignStmt.getRightOp();
               if (this.isConstZero(aer.getOp2())) {
                  assignStmt.setRightOp(aer.getOp1());
               }
            }

            if (assignStmt.getRightOp() instanceof MulExpr) {
               aer = (BinopExpr)assignStmt.getRightOp();
               if (this.isConstZero(aer.getOp1())) {
                  assignStmt.setRightOp(this.getZeroConst(assignStmt.getLeftOp().getType()));
               } else if (this.isConstZero(aer.getOp2())) {
                  assignStmt.setRightOp(this.getZeroConst(assignStmt.getLeftOp().getType()));
               }
            }

            if (assignStmt.getRightOp() instanceof OrExpr) {
               OrExpr orExpr = (OrExpr)assignStmt.getRightOp();
               if (this.isConstZero(orExpr.getOp1())) {
                  assignStmt.setRightOp(orExpr.getOp2());
               } else if (this.isConstZero(orExpr.getOp2())) {
                  assignStmt.setRightOp(orExpr.getOp1());
               }
            }
         }
      }

      unitIt = b.getUnits().iterator();

      while(unitIt.hasNext()) {
         u = (Unit)unitIt.next();
         if (u instanceof AssignStmt) {
            assignStmt = (AssignStmt)u;
            if (assignStmt.getLeftOp() == assignStmt.getRightOp()) {
               unitIt.remove();
            }
         }
      }

   }

   private Value getZeroConst(Type type) {
      if (type instanceof IntType) {
         return IntConstant.v(0);
      } else if (type instanceof LongType) {
         return LongConstant.v(0L);
      } else if (type instanceof FloatType) {
         return FloatConstant.v(0.0F);
      } else if (type instanceof DoubleType) {
         return DoubleConstant.v(0.0D);
      } else {
         throw new RuntimeException("Unsupported numeric type");
      }
   }

   private boolean isConstZero(Value op) {
      if (op instanceof IntConstant) {
         IntConstant ic = (IntConstant)op;
         return ic.value == 0;
      } else {
         return false;
      }
   }
}
