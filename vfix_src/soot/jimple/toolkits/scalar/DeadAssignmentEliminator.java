package soot.jimple.toolkits.scalar;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.IntType;
import soot.Local;
import soot.LongType;
import soot.NullType;
import soot.PhaseOptions;
import soot.RefType;
import soot.Scene;
import soot.Singletons;
import soot.Timers;
import soot.Trap;
import soot.Type;
import soot.Unit;
import soot.UnknownType;
import soot.Value;
import soot.ValueBox;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.BinopExpr;
import soot.jimple.CastExpr;
import soot.jimple.DivExpr;
import soot.jimple.FieldRef;
import soot.jimple.InstanceFieldRef;
import soot.jimple.IntConstant;
import soot.jimple.InvokeExpr;
import soot.jimple.Jimple;
import soot.jimple.LongConstant;
import soot.jimple.NewArrayExpr;
import soot.jimple.NewExpr;
import soot.jimple.NewMultiArrayExpr;
import soot.jimple.NopStmt;
import soot.jimple.NullConstant;
import soot.jimple.RemExpr;
import soot.jimple.Stmt;
import soot.options.Options;
import soot.toolkits.scalar.LocalDefs;
import soot.toolkits.scalar.LocalUses;
import soot.toolkits.scalar.UnitValueBoxPair;
import soot.util.Chain;

public class DeadAssignmentEliminator extends BodyTransformer {
   private static final Logger logger = LoggerFactory.getLogger(DeadAssignmentEliminator.class);

   public DeadAssignmentEliminator(Singletons.Global g) {
   }

   public static DeadAssignmentEliminator v() {
      return G.v().soot_jimple_toolkits_scalar_DeadAssignmentEliminator();
   }

   protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
      boolean eliminateOnlyStackLocals = PhaseOptions.getBoolean(options, "only-stack-locals");
      Options soptions = Options.v();
      if (soptions.verbose()) {
         logger.debug("[" + b.getMethod().getName() + "] Eliminating dead code...");
      }

      if (soptions.time()) {
         Timers.v().deadCodeTimer.start();
      }

      Chain<Unit> units = b.getUnits();
      Deque<Unit> q = new ArrayDeque(units.size());
      boolean isStatic = b.getMethod().isStatic();
      boolean allEssential = true;
      boolean checkInvoke = false;
      Local thisLocal = null;
      Iterator it = units.iterator();

      while(true) {
         Iterator var16;
         Value rhs;
         while(it.hasNext()) {
            Unit s = (Unit)it.next();
            boolean isEssential = true;
            if (s instanceof NopStmt) {
               boolean removeNop = it.hasNext();
               if (!removeNop) {
                  removeNop = true;
                  var16 = b.getTraps().iterator();

                  while(var16.hasNext()) {
                     Trap t = (Trap)var16.next();
                     if (t.getEndUnit() == s) {
                        removeNop = false;
                        break;
                     }
                  }
               }

               if (removeNop) {
                  it.remove();
                  continue;
               }
            } else if (s instanceof AssignStmt) {
               AssignStmt as = (AssignStmt)s;
               Value lhs = as.getLeftOp();
               rhs = as.getRightOp();
               if (lhs == rhs && lhs instanceof Local) {
                  it.remove();
                  continue;
               }

               if (lhs instanceof Local && (!eliminateOnlyStackLocals || ((Local)lhs).getName().startsWith("$") || lhs.getType() instanceof NullType)) {
                  isEssential = false;
                  if (!checkInvoke) {
                     checkInvoke = as.containsInvokeExpr();
                  }

                  Type t1;
                  if (rhs instanceof CastExpr) {
                     CastExpr ce = (CastExpr)rhs;
                     t1 = ce.getCastType();
                     Value v = ce.getOp();
                     isEssential = !(v instanceof NullConstant) || !(t1 instanceof RefType);
                  } else if (!(rhs instanceof InvokeExpr) && !(rhs instanceof ArrayRef) && !(rhs instanceof NewExpr) && !(rhs instanceof NewArrayExpr) && !(rhs instanceof NewMultiArrayExpr)) {
                     if (rhs instanceof FieldRef) {
                        isEssential = true;
                        if (rhs instanceof InstanceFieldRef) {
                           InstanceFieldRef ifr = (InstanceFieldRef)rhs;
                           if (!isStatic && thisLocal == null) {
                              thisLocal = b.getThisLocal();
                           }

                           isEssential = isStatic || thisLocal != ifr.getBase();
                        }
                     } else if (rhs instanceof DivExpr || rhs instanceof RemExpr) {
                        BinopExpr expr = (BinopExpr)rhs;
                        t1 = expr.getOp1().getType();
                        Type t2 = expr.getOp2().getType();
                        boolean t2Int = t2 instanceof IntType;
                        isEssential = t2Int || t1 instanceof IntType || t1 instanceof LongType || t2 instanceof LongType || t1 instanceof UnknownType || t2 instanceof UnknownType;
                        Value v;
                        if (isEssential && t2Int) {
                           v = expr.getOp2();
                           if (v instanceof IntConstant) {
                              IntConstant i = (IntConstant)v;
                              isEssential = i.value == 0;
                           } else {
                              isEssential = true;
                           }
                        }

                        if (isEssential && t2 instanceof LongType) {
                           v = expr.getOp2();
                           if (v instanceof LongConstant) {
                              LongConstant l = (LongConstant)v;
                              isEssential = l.value == 0L;
                           } else {
                              isEssential = true;
                           }
                        }
                     }
                  } else {
                     isEssential = true;
                  }
               }
            }

            if (isEssential) {
               q.addFirst(s);
            }

            allEssential &= isEssential;
         }

         if (checkInvoke || !allEssential) {
            LocalDefs localDefs = LocalDefs.Factory.newLocalDefs(b);
            Iterator var29;
            if (!allEssential) {
               HashSet essential = new HashSet(b.getUnits().size());

               label183:
               while(true) {
                  Unit s;
                  do {
                     if (q.isEmpty()) {
                        units.retainAll(essential);
                        break label183;
                     }

                     s = (Unit)q.removeFirst();
                  } while(!essential.add(s));

                  var29 = s.getUseBoxes().iterator();

                  while(var29.hasNext()) {
                     ValueBox box = (ValueBox)var29.next();
                     rhs = box.getValue();
                     if (rhs instanceof Local) {
                        Local l = (Local)rhs;
                        List<Unit> defs = localDefs.getDefsOfAt(l, s);
                        if (defs != null) {
                           q.addAll(defs);
                        }
                     }
                  }
               }
            }

            if (checkInvoke) {
               LocalUses localUses = LocalUses.Factory.newLocalUses(b, localDefs);
               List<AssignStmt> postProcess = new ArrayList();
               var29 = units.iterator();

               label165:
               while(true) {
                  AssignStmt s;
                  do {
                     Unit u;
                     do {
                        if (!var29.hasNext()) {
                           Jimple jimple = Jimple.v();
                           var16 = postProcess.iterator();

                           while(var16.hasNext()) {
                              s = (AssignStmt)var16.next();
                              Stmt newInvoke = jimple.newInvokeStmt(s.getInvokeExpr());
                              newInvoke.addAllTagsOf(s);
                              units.swapWith(s, newInvoke);
                              if (Scene.v().hasCallGraph()) {
                                 Scene.v().getCallGraph().swapEdgesOutOf(s, newInvoke);
                              }
                           }
                           break label165;
                        }

                        u = (Unit)var29.next();
                     } while(!(u instanceof AssignStmt));

                     s = (AssignStmt)u;
                  } while(!s.containsInvokeExpr());

                  boolean deadAssignment = true;
                  Iterator var42 = localUses.getUsesOf(s).iterator();

                  while(var42.hasNext()) {
                     UnitValueBoxPair pair = (UnitValueBoxPair)var42.next();
                     if (units.contains(pair.unit)) {
                        deadAssignment = false;
                        break;
                     }
                  }

                  if (deadAssignment) {
                     postProcess.add(s);
                  }
               }
            }
         }

         if (soptions.time()) {
            Timers.v().deadCodeTimer.end();
         }

         return;
      }
   }
}
