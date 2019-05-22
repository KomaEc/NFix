package soot.jimple.toolkits.base;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.BodyTransformer;
import soot.Local;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.DefinitionStmt;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.NewExpr;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.Stmt;
import soot.options.Options;
import soot.toolkits.scalar.LocalUses;
import soot.toolkits.scalar.UnitValueBoxPair;
import soot.util.Chain;

public class PartialConstructorFolder extends BodyTransformer {
   private static final Logger logger = LoggerFactory.getLogger(PartialConstructorFolder.class);
   private List<Type> types;

   public void setTypes(List<Type> t) {
      this.types = t;
   }

   public List<Type> getTypes() {
      return this.types;
   }

   public void internalTransform(Body b, String phaseName, Map<String, String> options) {
      JimpleBody body = (JimpleBody)b;
      if (Options.v().verbose()) {
         logger.debug("[" + body.getMethod().getName() + "] Folding Jimple constructors...");
      }

      Chain<Unit> units = body.getUnits();
      List<Unit> stmtList = new ArrayList();
      stmtList.addAll(units);
      Iterator<Unit> it = stmtList.iterator();
      Iterator<Unit> nextStmtIt = stmtList.iterator();
      nextStmtIt.next();
      LocalUses localUses = LocalUses.Factory.newLocalUses((Body)body);

      while(it.hasNext()) {
         Stmt s = (Stmt)it.next();
         if (s instanceof AssignStmt) {
            Value lhs = ((AssignStmt)s).getLeftOp();
            if (lhs instanceof Local) {
               Value rhs = ((AssignStmt)s).getRightOp();
               if (rhs instanceof NewExpr) {
                  if (nextStmtIt.hasNext()) {
                     Stmt next = (Stmt)nextStmtIt.next();
                     if (next instanceof InvokeStmt) {
                        InvokeStmt invoke = (InvokeStmt)next;
                        if (invoke.getInvokeExpr() instanceof SpecialInvokeExpr) {
                           SpecialInvokeExpr invokeExpr = (SpecialInvokeExpr)invoke.getInvokeExpr();
                           if (invokeExpr.getBase() == lhs) {
                              break;
                           }
                        }
                     }
                  }

                  if (this.types.contains(((NewExpr)rhs).getType())) {
                     List<UnitValueBoxPair> lu = localUses.getUsesOf(s);
                     Iterator<UnitValueBoxPair> luIter = lu.iterator();
                     boolean MadeNewInvokeExpr = false;

                     while(luIter.hasNext()) {
                        Unit use = ((UnitValueBoxPair)luIter.next()).unit;
                        if (use instanceof InvokeStmt) {
                           InvokeStmt is = (InvokeStmt)use;
                           if (is.getInvokeExpr() instanceof SpecialInvokeExpr && lhs == ((SpecialInvokeExpr)is.getInvokeExpr()).getBase()) {
                              AssignStmt constructStmt = Jimple.v().newAssignStmt(((DefinitionStmt)s).getLeftOp(), ((DefinitionStmt)s).getRightOp());
                              constructStmt.setRightOp(Jimple.v().newNewExpr(((NewExpr)rhs).getBaseType()));
                              MadeNewInvokeExpr = true;
                              use.redirectJumpsToThisTo(constructStmt);
                              units.insertBefore((Object)constructStmt, use);
                              constructStmt.addTag(s.getTag("SourceLnPosTag"));
                           }
                        }
                     }

                     if (MadeNewInvokeExpr) {
                        units.remove(s);
                     }
                  }
               }
            }
         }
      }

   }
}
