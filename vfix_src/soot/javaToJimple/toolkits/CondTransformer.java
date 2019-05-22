package soot.javaToJimple.toolkits;

import java.util.Iterator;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.Singletons;
import soot.Unit;
import soot.jimple.AssignStmt;
import soot.jimple.BinopExpr;
import soot.jimple.EqExpr;
import soot.jimple.GotoStmt;
import soot.jimple.IfStmt;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;
import soot.jimple.Stmt;

public class CondTransformer extends BodyTransformer {
   private static final Logger logger = LoggerFactory.getLogger(CondTransformer.class);
   private static final int SEQ_LENGTH = 6;
   private Stmt[] stmtSeq = new Stmt[6];
   private boolean sameGoto = true;

   public CondTransformer(Singletons.Global g) {
   }

   public static CondTransformer v() {
      return G.v().soot_javaToJimple_toolkits_CondTransformer();
   }

   protected void internalTransform(Body b, String phaseName, Map options) {
      boolean change = true;

      while(change) {
         Iterator it = b.getUnits().iterator();
         int pos = 0;

         while(it.hasNext()) {
            change = false;
            Stmt s = (Stmt)it.next();
            if (this.testStmtSeq(s, pos)) {
               ++pos;
            }

            if (pos == 6) {
               change = true;
               break;
            }
         }

         if (change) {
            this.transformBody(b, (Stmt)it.next());
            int pos = false;
            this.stmtSeq = new Stmt[6];
         }
      }

   }

   private void transformBody(Body b, Stmt next) {
      Stmt newTarget = null;
      Stmt oldTarget = null;
      if (this.sameGoto) {
         newTarget = ((IfStmt)this.stmtSeq[5]).getTarget();
      } else {
         newTarget = next;
         oldTarget = ((IfStmt)this.stmtSeq[5]).getTarget();
      }

      ((IfStmt)this.stmtSeq[0]).setTarget(newTarget);
      ((IfStmt)this.stmtSeq[1]).setTarget(newTarget);

      for(int i = 2; i <= 5; ++i) {
         b.getUnits().remove(this.stmtSeq[i]);
      }

      if (!this.sameGoto) {
         b.getUnits().insertAfter((Unit)Jimple.v().newGotoStmt((Unit)oldTarget), (Unit)this.stmtSeq[1]);
      }

   }

   private boolean testStmtSeq(Stmt s, int pos) {
      switch(pos) {
      case 0:
         if (s instanceof IfStmt) {
            this.stmtSeq[pos] = s;
            return true;
         }
         break;
      case 1:
         if (s instanceof IfStmt && this.sameTarget(this.stmtSeq[pos - 1], s)) {
            this.stmtSeq[pos] = s;
            return true;
         }
         break;
      case 2:
         if (s instanceof AssignStmt) {
            this.stmtSeq[pos] = s;
            if (((AssignStmt)s).getRightOp() instanceof IntConstant && ((IntConstant)((AssignStmt)s).getRightOp()).value == 0) {
               this.sameGoto = false;
            }

            return true;
         }
         break;
      case 3:
         if (s instanceof GotoStmt) {
            this.stmtSeq[pos] = s;
            return true;
         }
         break;
      case 4:
         if (s instanceof AssignStmt && this.isTarget(((IfStmt)this.stmtSeq[0]).getTarget(), s) && this.sameLocal(this.stmtSeq[2], s)) {
            this.stmtSeq[pos] = s;
            return true;
         }
         break;
      case 5:
         if (s instanceof IfStmt) {
            if (this.isTarget((Stmt)((GotoStmt)this.stmtSeq[3]).getTarget(), s) && this.sameCondLocal(this.stmtSeq[4], s) && ((IfStmt)s).getCondition() instanceof EqExpr) {
               this.stmtSeq[pos] = s;
               return true;
            }

            if (this.isTarget((Stmt)((GotoStmt)this.stmtSeq[3]).getTarget(), s) && this.sameCondLocal(this.stmtSeq[4], s)) {
               this.stmtSeq[pos] = s;
               this.sameGoto = false;
               return true;
            }
         }
      }

      return false;
   }

   private boolean sameTarget(Stmt s1, Stmt s2) {
      IfStmt is1 = (IfStmt)s1;
      IfStmt is2 = (IfStmt)s2;
      return is1.getTarget().equals(is2.getTarget());
   }

   private boolean isTarget(Stmt s1, Stmt s) {
      return s1.equals(s);
   }

   private boolean sameLocal(Stmt s1, Stmt s2) {
      AssignStmt as1 = (AssignStmt)s1;
      AssignStmt as2 = (AssignStmt)s2;
      return as1.getLeftOp().equals(as2.getLeftOp());
   }

   private boolean sameCondLocal(Stmt s1, Stmt s2) {
      AssignStmt as1 = (AssignStmt)s1;
      IfStmt is2 = (IfStmt)s2;
      if (is2.getCondition() instanceof BinopExpr) {
         BinopExpr bs2 = (BinopExpr)is2.getCondition();
         if (as1.getLeftOp().equals(bs2.getOp1())) {
            return true;
         }
      }

      return false;
   }
}
