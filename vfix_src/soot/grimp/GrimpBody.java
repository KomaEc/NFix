package soot.grimp;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.PackManager;
import soot.SootMethod;
import soot.Trap;
import soot.Unit;
import soot.ValueBox;
import soot.jimple.AbstractStmtSwitch;
import soot.jimple.AssignStmt;
import soot.jimple.BreakpointStmt;
import soot.jimple.EnterMonitorStmt;
import soot.jimple.ExitMonitorStmt;
import soot.jimple.GotoStmt;
import soot.jimple.IdentityStmt;
import soot.jimple.IfStmt;
import soot.jimple.InvokeStmt;
import soot.jimple.JimpleBody;
import soot.jimple.LookupSwitchStmt;
import soot.jimple.NopStmt;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.Stmt;
import soot.jimple.StmtBody;
import soot.jimple.TableSwitchStmt;
import soot.jimple.ThrowStmt;
import soot.jimple.internal.StmtBox;
import soot.options.Options;

public class GrimpBody extends StmtBody {
   private static final Logger logger = LoggerFactory.getLogger(GrimpBody.class);

   GrimpBody(SootMethod m) {
      super(m);
   }

   public Object clone() {
      Body b = Grimp.v().newBody(this.getMethod());
      b.importBodyContentsFrom(this);
      return b;
   }

   GrimpBody(Body body) {
      super(body.getMethod());
      if (Options.v().verbose()) {
         logger.debug("[" + this.getMethod().getName() + "] Constructing GrimpBody...");
      }

      JimpleBody jBody = null;
      if (!(body instanceof JimpleBody)) {
         throw new RuntimeException("Can only construct GrimpBody's from JimpleBody's (for now)");
      } else {
         jBody = (JimpleBody)body;
         Iterator localIt = jBody.getLocals().iterator();

         while(localIt.hasNext()) {
            this.getLocals().add(localIt.next());
         }

         Iterator<Unit> it = jBody.getUnits().iterator();
         final HashMap<Stmt, Stmt> oldToNew = new HashMap(this.getUnits().size() * 2 + 1, 0.7F);
         LinkedList updates = new LinkedList();

         Stmt oldStmt;
         while(it.hasNext()) {
            oldStmt = (Stmt)((Stmt)it.next());
            final StmtBox newStmtBox = (StmtBox)Grimp.v().newStmtBox((Unit)null);
            final StmtBox updateStmtBox = (StmtBox)Grimp.v().newStmtBox((Unit)null);
            oldStmt.apply(new AbstractStmtSwitch() {
               public void caseAssignStmt(AssignStmt s) {
                  newStmtBox.setUnit(Grimp.v().newAssignStmt(s));
               }

               public void caseIdentityStmt(IdentityStmt s) {
                  newStmtBox.setUnit(Grimp.v().newIdentityStmt(s));
               }

               public void caseBreakpointStmt(BreakpointStmt s) {
                  newStmtBox.setUnit(Grimp.v().newBreakpointStmt(s));
               }

               public void caseInvokeStmt(InvokeStmt s) {
                  newStmtBox.setUnit(Grimp.v().newInvokeStmt(s));
               }

               public void caseEnterMonitorStmt(EnterMonitorStmt s) {
                  newStmtBox.setUnit(Grimp.v().newEnterMonitorStmt(s));
               }

               public void caseExitMonitorStmt(ExitMonitorStmt s) {
                  newStmtBox.setUnit(Grimp.v().newExitMonitorStmt(s));
               }

               public void caseGotoStmt(GotoStmt s) {
                  newStmtBox.setUnit(Grimp.v().newGotoStmt(s));
                  updateStmtBox.setUnit(s);
               }

               public void caseIfStmt(IfStmt s) {
                  newStmtBox.setUnit(Grimp.v().newIfStmt(s));
                  updateStmtBox.setUnit(s);
               }

               public void caseLookupSwitchStmt(LookupSwitchStmt s) {
                  newStmtBox.setUnit(Grimp.v().newLookupSwitchStmt(s));
                  updateStmtBox.setUnit(s);
               }

               public void caseNopStmt(NopStmt s) {
                  newStmtBox.setUnit(Grimp.v().newNopStmt(s));
               }

               public void caseReturnStmt(ReturnStmt s) {
                  newStmtBox.setUnit(Grimp.v().newReturnStmt(s));
               }

               public void caseReturnVoidStmt(ReturnVoidStmt s) {
                  newStmtBox.setUnit(Grimp.v().newReturnVoidStmt(s));
               }

               public void caseTableSwitchStmt(TableSwitchStmt s) {
                  newStmtBox.setUnit(Grimp.v().newTableSwitchStmt(s));
                  updateStmtBox.setUnit(s);
               }

               public void caseThrowStmt(ThrowStmt s) {
                  newStmtBox.setUnit(Grimp.v().newThrowStmt(s));
               }
            });
            Stmt newStmt = (Stmt)((Stmt)newStmtBox.getUnit());
            Iterator useBoxesIt = newStmt.getUseBoxes().iterator();

            ValueBox b;
            while(useBoxesIt.hasNext()) {
               b = (ValueBox)useBoxesIt.next();
               b.setValue(Grimp.v().newExpr(b.getValue()));
            }

            useBoxesIt = newStmt.getDefBoxes().iterator();

            while(useBoxesIt.hasNext()) {
               b = (ValueBox)useBoxesIt.next();
               b.setValue(Grimp.v().newExpr(b.getValue()));
            }

            this.getUnits().add((Unit)newStmt);
            oldToNew.put(oldStmt, newStmt);
            if (updateStmtBox.getUnit() != null) {
               updates.add(updateStmtBox.getUnit());
            }

            if (oldStmt.hasTag("LineNumberTag")) {
               newStmt.addTag(oldStmt.getTag("LineNumberTag"));
            }

            if (oldStmt.hasTag("SourceLnPosTag")) {
               newStmt.addTag(oldStmt.getTag("SourceLnPosTag"));
            }
         }

         it = updates.iterator();

         while(it.hasNext()) {
            oldStmt = (Stmt)((Stmt)it.next());
            oldStmt.apply(new AbstractStmtSwitch() {
               public void caseGotoStmt(GotoStmt s) {
                  GotoStmt newStmt = (GotoStmt)((GotoStmt)oldToNew.get(s));
                  newStmt.setTarget((Unit)oldToNew.get(newStmt.getTarget()));
               }

               public void caseIfStmt(IfStmt s) {
                  IfStmt newStmt = (IfStmt)((IfStmt)oldToNew.get(s));
                  newStmt.setTarget((Unit)oldToNew.get(newStmt.getTarget()));
               }

               public void caseLookupSwitchStmt(LookupSwitchStmt s) {
                  LookupSwitchStmt newStmt = (LookupSwitchStmt)((LookupSwitchStmt)oldToNew.get(s));
                  newStmt.setDefaultTarget((Unit)oldToNew.get(newStmt.getDefaultTarget()));
                  Unit[] newTargList = new Unit[newStmt.getTargetCount()];

                  for(int i = 0; i < newStmt.getTargetCount(); ++i) {
                     newTargList[i] = (Unit)oldToNew.get(newStmt.getTarget(i));
                  }

                  newStmt.setTargets(newTargList);
               }

               public void caseTableSwitchStmt(TableSwitchStmt s) {
                  TableSwitchStmt newStmt = (TableSwitchStmt)((TableSwitchStmt)oldToNew.get(s));
                  newStmt.setDefaultTarget((Unit)oldToNew.get(newStmt.getDefaultTarget()));
                  int tc = newStmt.getHighIndex() - newStmt.getLowIndex() + 1;
                  LinkedList<Unit> newTargList = new LinkedList();

                  for(int i = 0; i < tc; ++i) {
                     newTargList.add(oldToNew.get(newStmt.getTarget(i)));
                  }

                  newStmt.setTargets(newTargList);
               }
            });
         }

         Iterator trapIt = jBody.getTraps().iterator();

         while(trapIt.hasNext()) {
            Trap oldTrap = (Trap)trapIt.next();
            this.getTraps().add(Grimp.v().newTrap(oldTrap.getException(), (Unit)oldToNew.get(oldTrap.getBeginUnit()), (Unit)oldToNew.get(oldTrap.getEndUnit()), (Unit)oldToNew.get(oldTrap.getHandlerUnit())));
         }

         PackManager.v().getPack("gb").apply(this);
      }
   }
}
