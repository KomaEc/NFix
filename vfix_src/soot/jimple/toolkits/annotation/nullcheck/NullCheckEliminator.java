package soot.jimple.toolkits.annotation.nullcheck;

import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.Immediate;
import soot.Unit;
import soot.Value;
import soot.jimple.BinopExpr;
import soot.jimple.EqExpr;
import soot.jimple.IfStmt;
import soot.jimple.Jimple;
import soot.jimple.NeExpr;
import soot.jimple.NullConstant;
import soot.jimple.Stmt;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.UnitGraph;
import soot.util.Chain;

public class NullCheckEliminator extends BodyTransformer {
   private NullCheckEliminator.AnalysisFactory analysisFactory;

   public NullCheckEliminator() {
      this(new NullCheckEliminator.AnalysisFactory());
   }

   public NullCheckEliminator(NullCheckEliminator.AnalysisFactory f) {
      this.analysisFactory = f;
   }

   public void internalTransform(Body body, String phaseName, Map<String, String> options) {
      boolean changed;
      do {
         changed = false;
         NullnessAnalysis analysis = this.analysisFactory.newAnalysis(new ExceptionalUnitGraph(body));
         Chain<Unit> units = body.getUnits();

         for(Object s = (Stmt)units.getFirst(); s != null; s = (Stmt)units.getSuccOf(s)) {
            if (s instanceof IfStmt) {
               IfStmt is = (IfStmt)s;
               Value c = is.getCondition();
               if (c instanceof EqExpr || c instanceof NeExpr) {
                  BinopExpr e = (BinopExpr)c;
                  Immediate i = null;
                  if (e.getOp1() instanceof NullConstant) {
                     i = (Immediate)e.getOp2();
                  }

                  if (e.getOp2() instanceof NullConstant) {
                     i = (Immediate)e.getOp1();
                  }

                  if (i != null) {
                     boolean alwaysNull = analysis.isAlwaysNullBefore((Unit)s, i);
                     boolean alwaysNonNull = analysis.isAlwaysNonNullBefore((Unit)s, i);
                     int elim = 0;
                     if (alwaysNonNull) {
                        elim = c instanceof EqExpr ? -1 : 1;
                     }

                     if (alwaysNull) {
                        elim = c instanceof EqExpr ? 1 : -1;
                     }

                     Stmt newstmt = null;
                     if (elim == -1) {
                        newstmt = Jimple.v().newNopStmt();
                     }

                     if (elim == 1) {
                        newstmt = Jimple.v().newGotoStmt((Unit)is.getTarget());
                     }

                     if (newstmt != null) {
                        units.swapWith(s, newstmt);
                        s = newstmt;
                        changed = true;
                     }
                  }
               }
            }
         }
      } while(changed);

   }

   public static class AnalysisFactory {
      public NullnessAnalysis newAnalysis(UnitGraph g) {
         return new NullnessAnalysis(g);
      }
   }
}
