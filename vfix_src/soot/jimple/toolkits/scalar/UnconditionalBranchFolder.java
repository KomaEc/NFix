package soot.jimple.toolkits.scalar;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.Singletons;
import soot.Unit;
import soot.jimple.GotoStmt;
import soot.jimple.IfStmt;
import soot.jimple.Stmt;
import soot.jimple.StmtBody;
import soot.options.Options;
import soot.util.Chain;

public class UnconditionalBranchFolder extends BodyTransformer {
   private static final Logger logger = LoggerFactory.getLogger(UnconditionalBranchFolder.class);
   static final int JUMPOPT_TYPES = 6;
   int[] numFound;
   int[] numFixed;
   HashMap<Stmt, Stmt> stmtMap;

   public UnconditionalBranchFolder(Singletons.Global g) {
   }

   public static UnconditionalBranchFolder v() {
      return G.v().soot_jimple_toolkits_scalar_UnconditionalBranchFolder();
   }

   protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
      StmtBody body = (StmtBody)b;
      if (Options.v().verbose()) {
         logger.debug("[" + body.getMethod().getName() + "] Folding unconditional branches...");
      }

      if (this.numFound == null) {
         this.numFound = new int[7];
         this.numFixed = new int[7];
      }

      for(int i = 0; i <= 6; ++i) {
         this.numFound[i] = 0;
         this.numFixed[i] = 0;
      }

      Chain<Unit> units = body.getUnits();
      this.stmtMap = new HashMap();
      Iterator stmtIt = units.iterator();

      while(stmtIt.hasNext()) {
         Stmt stmt = (Stmt)stmtIt.next();
         Stmt target;
         Stmt newTarget;
         if (stmt instanceof GotoStmt) {
            target = (Stmt)((GotoStmt)stmt).getTarget();
            if (stmtIt.hasNext() && units.getSuccOf(stmt) == target) {
               stmtIt.remove();
               this.updateCounters(6, true);
            }

            if (target instanceof GotoStmt) {
               newTarget = this.getFinalTarget(target);
               if (newTarget == null) {
                  newTarget = stmt;
               }

               ((GotoStmt)stmt).setTarget(newTarget);
               this.updateCounters(1, true);
            } else if (target instanceof IfStmt) {
               this.updateCounters(3, false);
            }
         } else if (stmt instanceof IfStmt) {
            target = ((IfStmt)stmt).getTarget();
            if (target instanceof GotoStmt) {
               newTarget = this.getFinalTarget(target);
               if (newTarget == null) {
                  newTarget = stmt;
               }

               ((IfStmt)stmt).setTarget(newTarget);
               this.updateCounters(2, true);
            } else if (target instanceof IfStmt) {
               this.updateCounters(4, false);
            }
         }
      }

      if (Options.v().verbose()) {
         logger.debug("[" + body.getMethod().getName() + "]     " + this.numFixed[0] + " of " + this.numFound[0] + " branches folded.");
      }

   }

   private void updateCounters(int type, boolean fixed) {
      if (type >= 0 && type <= 6) {
         int var10002 = this.numFound[0]++;
         var10002 = this.numFound[type]++;
         if (fixed) {
            var10002 = this.numFixed[0]++;
            var10002 = this.numFixed[type]++;
         }

      }
   }

   private Stmt getFinalTarget(Stmt stmt) {
      Stmt finalTarget = null;
      if (!(stmt instanceof GotoStmt)) {
         return stmt;
      } else {
         this.stmtMap.put(stmt, stmt);
         Stmt target = (Stmt)((GotoStmt)stmt).getTarget();
         if (this.stmtMap.containsKey(target)) {
            finalTarget = (Stmt)this.stmtMap.get(target);
            if (finalTarget == target) {
               finalTarget = null;
            }
         } else {
            finalTarget = this.getFinalTarget(target);
         }

         this.stmtMap.put(stmt, finalTarget);
         return finalTarget;
      }
   }
}
