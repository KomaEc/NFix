package soot.grimp.toolkits.base;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.Local;
import soot.Singletons;
import soot.Unit;
import soot.Value;
import soot.grimp.Grimp;
import soot.grimp.GrimpBody;
import soot.jimple.AssignStmt;
import soot.jimple.InvokeStmt;
import soot.jimple.NewExpr;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.Stmt;
import soot.options.Options;
import soot.toolkits.scalar.LocalUses;
import soot.toolkits.scalar.UnitValueBoxPair;
import soot.util.Chain;

public class ConstructorFolder extends BodyTransformer {
   private static final Logger logger = LoggerFactory.getLogger(ConstructorFolder.class);

   public ConstructorFolder(Singletons.Global g) {
   }

   public static ConstructorFolder v() {
      return G.v().soot_grimp_toolkits_base_ConstructorFolder();
   }

   protected void internalTransform(Body b, String phaseName, Map options) {
      GrimpBody body = (GrimpBody)b;
      if (Options.v().verbose()) {
         logger.debug("[" + body.getMethod().getName() + "] Folding constructors...");
      }

      Chain units = body.getUnits();
      List<Unit> stmtList = new ArrayList();
      stmtList.addAll(units);
      Iterator<Unit> it = stmtList.iterator();
      LocalUses localUses = LocalUses.Factory.newLocalUses(b);

      label70:
      while(true) {
         Stmt s;
         Value lhs;
         Value rhs;
         do {
            do {
               do {
                  if (!it.hasNext()) {
                     return;
                  }

                  s = (Stmt)it.next();
               } while(!(s instanceof AssignStmt));

               lhs = ((AssignStmt)s).getLeftOp();
            } while(!(lhs instanceof Local));

            rhs = ((AssignStmt)s).getRightOp();
         } while(!(rhs instanceof NewExpr));

         List lu = localUses.getUsesOf(s);
         Iterator luIter = lu.iterator();
         boolean MadeNewInvokeExpr = false;

         while(true) {
            Unit use;
            InvokeStmt is;
            do {
               do {
                  do {
                     if (!luIter.hasNext()) {
                        if (MadeNewInvokeExpr) {
                           units.remove(s);
                        }
                        continue label70;
                     }

                     use = ((UnitValueBoxPair)((UnitValueBoxPair)luIter.next())).unit;
                  } while(!(use instanceof InvokeStmt));

                  is = (InvokeStmt)use;
               } while(!(is.getInvokeExpr() instanceof SpecialInvokeExpr));
            } while(lhs != ((SpecialInvokeExpr)is.getInvokeExpr()).getBase());

            SpecialInvokeExpr oldInvoke = (SpecialInvokeExpr)is.getInvokeExpr();
            LinkedList invokeArgs = new LinkedList();

            for(int i = 0; i < oldInvoke.getArgCount(); ++i) {
               invokeArgs.add(oldInvoke.getArg(i));
            }

            AssignStmt constructStmt = Grimp.v().newAssignStmt((AssignStmt)s);
            constructStmt.setRightOp(Grimp.v().newNewInvokeExpr(((NewExpr)rhs).getBaseType(), oldInvoke.getMethodRef(), invokeArgs));
            MadeNewInvokeExpr = true;
            use.redirectJumpsToThisTo(constructStmt);
            units.insertBefore((Object)constructStmt, use);
            units.remove(use);
         }
      }
   }
}
