package soot.jimple.validation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import soot.Body;
import soot.Local;
import soot.RefType;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.AssignStmt;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.NewExpr;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.Stmt;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.UnitGraph;
import soot.validation.BodyValidator;
import soot.validation.ValidationException;

public enum NewValidator implements BodyValidator {
   INSTANCE;

   private static final String errorMsg = "There is a path from '%s' to the usage '%s' where <init> does not get called in between.";
   public static boolean MUST_CALL_CONSTRUCTOR_BEFORE_RETURN = false;

   public static NewValidator v() {
      return INSTANCE;
   }

   public void validate(Body body, List<ValidationException> exceptions) {
      UnitGraph g = new BriefUnitGraph(body);
      Iterator var4 = body.getUnits().iterator();

      while(var4.hasNext()) {
         Unit u = (Unit)var4.next();
         if (u instanceof AssignStmt) {
            AssignStmt assign = (AssignStmt)u;
            if (assign.getRightOp() instanceof NewExpr) {
               if (!(assign.getLeftOp().getType() instanceof RefType)) {
                  exceptions.add(new ValidationException(u, "A new-expression must be used on reference type locals", String.format("Body of method %s contains a new-expression, which is assigned to a non-reference local", body.getMethod().getSignature())));
                  return;
               }

               LinkedHashSet<Local> locals = new LinkedHashSet();
               locals.add((Local)assign.getLeftOp());
               this.checkForInitializerOnPath(g, assign, exceptions);
            }
         }
      }

   }

   private boolean checkForInitializerOnPath(UnitGraph g, AssignStmt newStmt, List<ValidationException> exception) {
      List<Unit> workList = new ArrayList();
      Set<Unit> doneSet = new HashSet();
      workList.add(newStmt);
      Set<Local> aliasingLocals = new HashSet();
      aliasingLocals.add((Local)newStmt.getLeftOp());

      while(true) {
         Stmt curStmt;
         do {
            if (workList.isEmpty()) {
               return true;
            }

            curStmt = (Stmt)workList.remove(0);
         } while(!doneSet.add(curStmt));

         if (!newStmt.equals(curStmt)) {
            if (curStmt.containsInvokeExpr()) {
               InvokeExpr expr = curStmt.getInvokeExpr();
               if (expr.getMethod().isConstructor()) {
                  if (!(expr instanceof SpecialInvokeExpr)) {
                     exception.add(new ValidationException(curStmt, "<init> method calls may only be used with specialinvoke."));
                     return true;
                  }

                  if (!(curStmt instanceof InvokeStmt)) {
                     exception.add(new ValidationException(curStmt, "<init> methods may only be called with invoke statements."));
                     return true;
                  }

                  SpecialInvokeExpr invoke = (SpecialInvokeExpr)expr;
                  if (aliasingLocals.contains(invoke.getBase())) {
                     continue;
                  }
               }
            }

            boolean creatingAlias = false;
            if (curStmt instanceof AssignStmt) {
               AssignStmt assignCheck = (AssignStmt)curStmt;
               if (aliasingLocals.contains(assignCheck.getRightOp()) && assignCheck.getLeftOp() instanceof Local) {
                  aliasingLocals.add((Local)assignCheck.getLeftOp());
                  creatingAlias = true;
               }

               Local originalLocal = (Local)aliasingLocals.iterator().next();
               if (originalLocal.equals(assignCheck.getLeftOp())) {
                  continue;
               }

               aliasingLocals.remove(assignCheck.getLeftOp());
            }

            if (!creatingAlias) {
               Iterator var15 = curStmt.getUseBoxes().iterator();

               while(var15.hasNext()) {
                  ValueBox box = (ValueBox)var15.next();
                  Value used = box.getValue();
                  if (aliasingLocals.contains(used)) {
                     exception.add(new ValidationException(newStmt, String.format("There is a path from '%s' to the usage '%s' where <init> does not get called in between.", newStmt, curStmt)));
                     return false;
                  }
               }
            }
         }

         List<Unit> successors = g.getSuccsOf((Unit)curStmt);
         if (successors.isEmpty() && MUST_CALL_CONSTRUCTOR_BEFORE_RETURN) {
            exception.add(new ValidationException(newStmt, String.format("There is a path from '%s' to the usage '%s' where <init> does not get called in between.", newStmt, curStmt)));
            return false;
         }

         workList.addAll(successors);
      }
   }

   public boolean isBasicValidator() {
      return false;
   }
}
