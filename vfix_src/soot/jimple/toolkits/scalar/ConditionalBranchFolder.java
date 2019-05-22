package soot.jimple.toolkits.scalar;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.Singletons;
import soot.Unit;
import soot.Value;
import soot.jimple.IfStmt;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;
import soot.jimple.Stmt;
import soot.jimple.StmtBody;
import soot.options.Options;
import soot.util.Chain;

public class ConditionalBranchFolder extends BodyTransformer {
   private static final Logger logger = LoggerFactory.getLogger(ConditionalBranchFolder.class);

   public ConditionalBranchFolder(Singletons.Global g) {
   }

   public static ConditionalBranchFolder v() {
      return G.v().soot_jimple_toolkits_scalar_ConditionalBranchFolder();
   }

   protected void internalTransform(Body body, String phaseName, Map<String, String> options) {
      StmtBody stmtBody = (StmtBody)body;
      int numTrue = 0;
      int numFalse = 0;
      if (Options.v().verbose()) {
         logger.debug("[" + stmtBody.getMethod().getName() + "] Folding conditional branches...");
      }

      Chain<Unit> units = stmtBody.getUnits();
      Unit[] var8 = (Unit[])units.toArray(new Unit[units.size()]);
      int var9 = var8.length;

      for(int var10 = 0; var10 < var9; ++var10) {
         Unit stmt = var8[var10];
         if (stmt instanceof IfStmt) {
            IfStmt ifs = (IfStmt)stmt;
            Value cond = ifs.getCondition();
            if (Evaluator.isValueConstantValued(cond)) {
               cond = Evaluator.getConstantValueOf(cond);
               if (((IntConstant)cond).value == 1) {
                  Stmt newStmt = Jimple.v().newGotoStmt((Unit)ifs.getTarget());
                  units.insertAfter((Object)newStmt, stmt);
                  ++numTrue;
               } else {
                  ++numFalse;
               }

               units.remove(stmt);
            }
         }
      }

      if (Options.v().verbose()) {
         logger.debug("[" + stmtBody.getMethod().getName() + "]     Folded " + numTrue + " true, " + numFalse + " conditional branches");
      }

   }
}
