package soot.jbco.jimpleTransformations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import soot.Body;
import soot.BodyTransformer;
import soot.BooleanType;
import soot.IntType;
import soot.Local;
import soot.PatchingChain;
import soot.PrimType;
import soot.RefType;
import soot.SootField;
import soot.SootMethod;
import soot.Trap;
import soot.Unit;
import soot.jbco.IJbcoTransform;
import soot.jbco.Main;
import soot.jbco.util.Rand;
import soot.jimple.IdentityStmt;
import soot.jimple.IfStmt;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.scalar.FlowSet;

public class AddSwitches extends BodyTransformer implements IJbcoTransform {
   int switchesadded = 0;
   public static String[] dependancies = new String[]{"wjtp.jbco_fr", "jtp.jbco_adss", "bb.jbco_ful"};
   public static String name = "jtp.jbco_adss";

   public void outputSummary() {
      out.println("Switches added: " + this.switchesadded);
   }

   public String[] getDependencies() {
      return dependancies;
   }

   public String getName() {
      return name;
   }

   public boolean checkTraps(Unit u, Body b) {
      Iterator it = b.getTraps().iterator();

      Trap t;
      do {
         if (!it.hasNext()) {
            return false;
         }

         t = (Trap)it.next();
      } while(t.getBeginUnit() != u && t.getEndUnit() != u && t.getHandlerUnit() != u);

      return true;
   }

   protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
      if (b.getMethod().getSignature().indexOf("<clinit>") < 0) {
         int weight = Main.getWeight(phaseName, b.getMethod().getSignature());
         if (weight != 0) {
            New2InitFlowAnalysis fa = new New2InitFlowAnalysis(new BriefUnitGraph(b));
            Vector<Unit> zeroheight = new Vector();
            PatchingChain<Unit> units = b.getUnits();
            Unit first = null;
            Iterator it = units.iterator();

            Unit unit;
            while(it.hasNext()) {
               unit = (Unit)it.next();
               if (!(unit instanceof IdentityStmt)) {
                  first = unit;
                  break;
               }
            }

            it = units.snapshotIterator();

            while(it.hasNext()) {
               unit = (Unit)it.next();
               if (!(unit instanceof IdentityStmt) && !this.checkTraps(unit, b) && ((FlowSet)fa.getFlowAfter(unit)).isEmpty() && ((FlowSet)fa.getFlowBefore(unit)).isEmpty()) {
                  zeroheight.add(unit);
               }
            }

            if (zeroheight.size() >= 3) {
               int idx = 0;
               Unit u = null;

               for(int i = 0; i < zeroheight.size(); ++i) {
                  idx = Rand.getInt(zeroheight.size() - 1) + 1;
                  u = (Unit)zeroheight.get(idx);
                  if (u.fallsThrough()) {
                     break;
                  }

                  u = null;
               }

               if (u != null && Rand.getInt(10) <= weight) {
                  zeroheight.remove(idx);

                  while(zeroheight.size() > (weight > 3 ? weight : 3)) {
                     zeroheight.remove(Rand.getInt(zeroheight.size()));
                  }

                  Collection<Local> locals = b.getLocals();
                  List<Unit> targs = new ArrayList();
                  targs.addAll(zeroheight);
                  SootField[] ops = FieldRenamer.getRandomOpaques();
                  Local b1 = Jimple.v().newLocal("addswitchesbool1", BooleanType.v());
                  locals.add(b1);
                  Local b2 = Jimple.v().newLocal("addswitchesbool2", BooleanType.v());
                  locals.add(b2);
                  RefType rt;
                  SootMethod m;
                  Local B;
                  if (ops[0].getType() instanceof PrimType) {
                     units.insertBefore((Unit)Jimple.v().newAssignStmt(b1, Jimple.v().newStaticFieldRef(ops[0].makeRef())), (Unit)u);
                  } else {
                     rt = (RefType)ops[0].getType();
                     m = rt.getSootClass().getMethodByName("booleanValue");
                     B = Jimple.v().newLocal("addswitchesBOOL1", rt);
                     locals.add(B);
                     units.insertBefore((Unit)Jimple.v().newAssignStmt(B, Jimple.v().newStaticFieldRef(ops[0].makeRef())), (Unit)u);
                     units.insertBefore((Unit)Jimple.v().newAssignStmt(b1, Jimple.v().newVirtualInvokeExpr(B, m.makeRef(), Collections.emptyList())), (Unit)u);
                  }

                  if (ops[1].getType() instanceof PrimType) {
                     units.insertBefore((Unit)Jimple.v().newAssignStmt(b2, Jimple.v().newStaticFieldRef(ops[1].makeRef())), (Unit)u);
                  } else {
                     rt = (RefType)ops[1].getType();
                     m = rt.getSootClass().getMethodByName("booleanValue");
                     B = Jimple.v().newLocal("addswitchesBOOL2", rt);
                     locals.add(B);
                     units.insertBefore((Unit)Jimple.v().newAssignStmt(B, Jimple.v().newStaticFieldRef(ops[1].makeRef())), (Unit)u);
                     units.insertBefore((Unit)Jimple.v().newAssignStmt(b2, Jimple.v().newVirtualInvokeExpr(B, m.makeRef(), Collections.emptyList())), (Unit)u);
                  }

                  IfStmt ifstmt = Jimple.v().newIfStmt(Jimple.v().newNeExpr(b1, b2), (Unit)u);
                  units.insertBefore((Unit)ifstmt, (Unit)u);
                  Local l = Jimple.v().newLocal("addswitchlocal", IntType.v());
                  locals.add(l);
                  units.insertBeforeNoRedirect(Jimple.v().newAssignStmt(l, IntConstant.v(0)), first);
                  units.insertAfter((Unit)Jimple.v().newTableSwitchStmt(l, 1, zeroheight.size(), targs, (Unit)u), (Unit)ifstmt);
                  this.switchesadded += zeroheight.size() + 1;
                  Iterator tit = targs.iterator();

                  while(tit.hasNext()) {
                     Unit nxt = (Unit)tit.next();
                     if (Rand.getInt(5) < 4) {
                        units.insertBefore((Unit)Jimple.v().newAssignStmt(l, Jimple.v().newAddExpr(l, IntConstant.v(Rand.getInt(3) + 1))), (Unit)nxt);
                     }
                  }

                  ifstmt.setTarget(u);
               }
            }
         }
      }
   }
}
