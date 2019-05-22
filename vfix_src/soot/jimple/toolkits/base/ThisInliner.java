package soot.jimple.toolkits.base;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.Local;
import soot.Trap;
import soot.Unit;
import soot.ValueBox;
import soot.jimple.AssignStmt;
import soot.jimple.CaughtExceptionRef;
import soot.jimple.GotoStmt;
import soot.jimple.IdentityStmt;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.ParameterRef;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.ThisRef;
import soot.jimple.toolkits.scalar.LocalNameStandardizer;
import soot.util.Chain;

public class ThisInliner extends BodyTransformer {
   public void internalTransform(Body b, String phaseName, Map options) {
      if (b.getMethod().getName().equals("<init>")) {
         InvokeStmt invokeStmt = this.getFirstSpecialInvoke(b);
         if (invokeStmt != null) {
            SpecialInvokeExpr specInvokeExpr = (SpecialInvokeExpr)invokeStmt.getInvokeExpr();
            if (specInvokeExpr.getMethod().getDeclaringClass().equals(b.getMethod().getDeclaringClass())) {
               if (!specInvokeExpr.getMethod().hasActiveBody()) {
                  specInvokeExpr.getMethod().retrieveActiveBody();
               }

               HashMap<Local, Local> oldLocalsToNew = new HashMap();
               Iterator var7 = specInvokeExpr.getMethod().getActiveBody().getLocals().iterator();

               while(var7.hasNext()) {
                  Local l = (Local)var7.next();
                  Local newLocal = (Local)l.clone();
                  b.getLocals().add(newLocal);
                  oldLocalsToNew.put(l, newLocal);
               }

               IdentityStmt origIdStmt = this.findIdentityStmt(b);
               HashMap<Stmt, Stmt> oldStmtsToNew = new HashMap();
               Chain<Unit> containerUnits = b.getUnits();
               Iterator var10 = specInvokeExpr.getMethod().getActiveBody().getUnits().iterator();

               while(true) {
                  while(true) {
                     Unit u;
                     Stmt inlineeStmt;
                     Stmt newInlinee;
                     Stmt newInlinee;
                     while(var10.hasNext()) {
                        u = (Unit)var10.next();
                        inlineeStmt = (Stmt)u;
                        if (inlineeStmt instanceof IdentityStmt) {
                           IdentityStmt idStmt = (IdentityStmt)inlineeStmt;
                           AssignStmt newParam;
                           if (idStmt.getRightOp() instanceof ThisRef) {
                              newParam = Jimple.v().newAssignStmt((Local)oldLocalsToNew.get(idStmt.getLeftOp()), origIdStmt.getLeftOp());
                              containerUnits.insertBefore((Object)newParam, invokeStmt);
                              oldStmtsToNew.put(inlineeStmt, newParam);
                           } else if (!(idStmt.getRightOp() instanceof CaughtExceptionRef)) {
                              if (idStmt.getRightOp() instanceof ParameterRef) {
                                 newParam = Jimple.v().newAssignStmt((Local)oldLocalsToNew.get(idStmt.getLeftOp()), specInvokeExpr.getArg(((ParameterRef)idStmt.getRightOp()).getIndex()));
                                 containerUnits.insertBefore((Object)newParam, invokeStmt);
                                 oldStmtsToNew.put(inlineeStmt, newParam);
                              }
                           } else {
                              newInlinee = (Stmt)inlineeStmt.clone();
                              Iterator var25 = newInlinee.getUseAndDefBoxes().iterator();

                              while(var25.hasNext()) {
                                 ValueBox next = (ValueBox)var25.next();
                                 if (next.getValue() instanceof Local) {
                                    next.setValue((Local)oldLocalsToNew.get(next.getValue()));
                                 }
                              }

                              containerUnits.insertBefore((Object)newInlinee, invokeStmt);
                              oldStmtsToNew.put(inlineeStmt, newInlinee);
                           }
                        } else if (inlineeStmt instanceof ReturnVoidStmt) {
                           Stmt newRet = Jimple.v().newGotoStmt((Unit)((Stmt)containerUnits.getSuccOf(invokeStmt)));
                           containerUnits.insertBefore((Object)newRet, invokeStmt);
                           System.out.println("adding to stmt map: " + inlineeStmt + " and " + newRet);
                           oldStmtsToNew.put(inlineeStmt, newRet);
                        } else {
                           newInlinee = (Stmt)inlineeStmt.clone();
                           Iterator var14 = newInlinee.getUseAndDefBoxes().iterator();

                           while(var14.hasNext()) {
                              ValueBox next = (ValueBox)var14.next();
                              if (next.getValue() instanceof Local) {
                                 next.setValue((Local)oldLocalsToNew.get(next.getValue()));
                              }
                           }

                           containerUnits.insertBefore((Object)newInlinee, invokeStmt);
                           oldStmtsToNew.put(inlineeStmt, newInlinee);
                        }
                     }

                     var10 = specInvokeExpr.getMethod().getActiveBody().getTraps().iterator();

                     while(var10.hasNext()) {
                        Trap t = (Trap)var10.next();
                        System.out.println("begin: " + t.getBeginUnit());
                        inlineeStmt = (Stmt)oldStmtsToNew.get(t.getBeginUnit());
                        System.out.println("end: " + t.getEndUnit());
                        newInlinee = (Stmt)oldStmtsToNew.get(t.getEndUnit());
                        System.out.println("handler: " + t.getHandlerUnit());
                        newInlinee = (Stmt)oldStmtsToNew.get(t.getHandlerUnit());
                        if (inlineeStmt == null || newInlinee == null || newInlinee == null) {
                           throw new RuntimeException("couldn't map trap!");
                        }

                        b.getTraps().add(Jimple.v().newTrap(t.getException(), (Unit)inlineeStmt, (Unit)newInlinee, (Unit)newInlinee));
                     }

                     var10 = specInvokeExpr.getMethod().getActiveBody().getUnits().iterator();

                     while(var10.hasNext()) {
                        u = (Unit)var10.next();
                        inlineeStmt = (Stmt)u;
                        if (inlineeStmt instanceof GotoStmt) {
                           System.out.println("inlinee goto target: " + ((GotoStmt)inlineeStmt).getTarget());
                           ((GotoStmt)oldStmtsToNew.get(inlineeStmt)).setTarget((Unit)oldStmtsToNew.get(((GotoStmt)inlineeStmt).getTarget()));
                        }
                     }

                     containerUnits.remove(invokeStmt);
                     LocalNameStandardizer.v().transform(b, "ji.lns");
                     return;
                  }
               }
            }
         }
      }
   }

   private InvokeStmt getFirstSpecialInvoke(Body b) {
      Iterator var2 = b.getUnits().iterator();

      while(var2.hasNext()) {
         Unit u = (Unit)var2.next();
         Stmt s = (Stmt)u;
         if (s instanceof InvokeStmt) {
            InvokeExpr invokeExpr = ((InvokeStmt)s).getInvokeExpr();
            if (invokeExpr instanceof SpecialInvokeExpr) {
               return (InvokeStmt)s;
            }
         }
      }

      return null;
   }

   private IdentityStmt findIdentityStmt(Body b) {
      Iterator var2 = b.getUnits().iterator();

      Stmt s;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         Unit u = (Unit)var2.next();
         s = (Stmt)u;
      } while(!(s instanceof IdentityStmt) || !(((IdentityStmt)s).getRightOp() instanceof ThisRef));

      return (IdentityStmt)s;
   }
}
