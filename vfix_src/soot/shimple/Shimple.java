package soot.shimple;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.G;
import soot.Local;
import soot.PhaseOptions;
import soot.Singletons;
import soot.SootMethod;
import soot.Unit;
import soot.UnitBox;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.JimpleBody;
import soot.options.Options;
import soot.shimple.internal.SPhiExpr;
import soot.shimple.internal.SPiExpr;
import soot.toolkits.graph.Block;
import soot.toolkits.scalar.ValueUnitPair;
import soot.util.Chain;

public class Shimple {
   private static final Logger logger = LoggerFactory.getLogger(Shimple.class);
   public static final String IFALIAS = "IfAlias";
   public static final String MAYMODIFY = "MayModify";
   public static final String PHI = "Phi";
   public static final String PI = "Pi";
   public static final String PHASE = "shimple";

   public Shimple(Singletons.Global g) {
   }

   public static Shimple v() {
      return G.v().soot_shimple_Shimple();
   }

   public ShimpleBody newBody(SootMethod m) {
      Map<String, String> options = PhaseOptions.v().getPhaseOptions("shimple");
      return new ShimpleBody(m, options);
   }

   public ShimpleBody newBody(SootMethod m, Map<String, String> options) {
      return new ShimpleBody(m, options);
   }

   public ShimpleBody newBody(Body b) {
      Map<String, String> options = PhaseOptions.v().getPhaseOptions("shimple");
      return new ShimpleBody(b, options);
   }

   public ShimpleBody newBody(Body b, Map<String, String> options) {
      return new ShimpleBody(b, options);
   }

   public PhiExpr newPhiExpr(Local leftLocal, List<Block> preds) {
      return new SPhiExpr(leftLocal, preds);
   }

   public PiExpr newPiExpr(Local local, Unit predicate, Object targetKey) {
      return new SPiExpr(local, predicate, targetKey);
   }

   public PhiExpr newPhiExpr(List<Value> args, List<Unit> preds) {
      return new SPhiExpr(args, preds);
   }

   public JimpleBody newJimpleBody(ShimpleBody body) {
      return body.toJimpleBody();
   }

   public static boolean isPhiExpr(Value value) {
      return value instanceof PhiExpr;
   }

   public static boolean isPhiNode(Unit unit) {
      return getPhiExpr(unit) != null;
   }

   public static PhiExpr getPhiExpr(Unit unit) {
      if (!(unit instanceof AssignStmt)) {
         return null;
      } else {
         Value right = ((AssignStmt)unit).getRightOp();
         return isPhiExpr(right) ? (PhiExpr)right : null;
      }
   }

   public static boolean isPiExpr(Value value) {
      return value instanceof PiExpr;
   }

   public static boolean isPiNode(Unit unit) {
      return getPiExpr(unit) != null;
   }

   public static PiExpr getPiExpr(Unit unit) {
      if (!(unit instanceof AssignStmt)) {
         return null;
      } else {
         Value right = ((AssignStmt)unit).getRightOp();
         return isPiExpr(right) ? (PiExpr)right : null;
      }
   }

   public static Local getLhsLocal(Unit unit) {
      if (!(unit instanceof AssignStmt)) {
         return null;
      } else {
         Value right = ((AssignStmt)unit).getRightOp();
         if (right instanceof ShimpleExpr) {
            Value left = ((AssignStmt)unit).getLeftOp();
            return (Local)left;
         } else {
            return null;
         }
      }
   }

   public static void redirectToPreds(Body body, Unit remove) {
      boolean debug = Options.v().debug();
      if (body instanceof ShimpleBody) {
         debug |= ((ShimpleBody)body).getOptions().debug();
      }

      Chain<Unit> units = body.getUnits();
      List<UnitBox> boxesPointingToThis = remove.getBoxesPointingToThis();
      if (!boxesPointingToThis.isEmpty()) {
         Iterator var5 = boxesPointingToThis.iterator();

         while(var5.hasNext()) {
            UnitBox pointer = (UnitBox)var5.next();
            if (!pointer.isBranchTarget()) {
               break;
            }
         }

         Set<Unit> preds = new HashSet();
         Set<PhiExpr> phis = new HashSet();
         Unit succ;
         if (!remove.equals(units.getFirst())) {
            succ = (Unit)units.getPredOf(remove);
            if (succ.fallsThrough()) {
               preds.add(succ);
            }
         }

         Iterator phiIt = units.iterator();

         while(phiIt.hasNext()) {
            Unit unit = (Unit)phiIt.next();
            Iterator var9 = unit.getUnitBoxes().iterator();

            while(var9.hasNext()) {
               UnitBox targetBox = (UnitBox)var9.next();
               if (remove.equals(targetBox.getUnit())) {
                  if (targetBox.isBranchTarget()) {
                     preds.add(unit);
                  } else {
                     PhiExpr phiExpr = getPhiExpr(unit);
                     if (phiExpr != null) {
                        phis.add(phiExpr);
                     }
                  }
               }
            }
         }

         if (phis.size() == 0) {
            if (debug) {
               logger.warn("Orphaned UnitBoxes to " + remove + "? Shimple.redirectToPreds is giving up.");
            }

         } else {
            if (preds.size() == 0) {
               if (debug) {
                  logger.warn("Shimple.redirectToPreds couldn't find any predecessors for " + remove + " in " + body.getMethod() + ".");
               }

               if (!remove.equals(units.getFirst())) {
                  succ = (Unit)units.getPredOf(remove);
                  if (debug) {
                     logger.warn("Falling back to immediate chain predecessor: " + succ + ".");
                  }

                  preds.add(succ);
               } else {
                  if (remove.equals(units.getLast())) {
                     throw new RuntimeException("Assertion failed.");
                  }

                  succ = (Unit)units.getSuccOf(remove);
                  if (debug) {
                     logger.warn("Falling back to immediate chain successor: " + succ + ".");
                  }

                  preds.add(succ);
               }
            }

            phiIt = phis.iterator();

            while(phiIt.hasNext()) {
               PhiExpr phiExpr = (PhiExpr)phiIt.next();
               ValueUnitPair argBox = phiExpr.getArgBox(remove);
               if (argBox == null) {
                  throw new RuntimeException("Assertion failed.");
               }

               Value arg = argBox.getValue();
               phiExpr.removeArg(argBox);
               Iterator predsIt = preds.iterator();

               while(predsIt.hasNext()) {
                  Unit pred = (Unit)predsIt.next();
                  phiExpr.addArg(arg, pred);
               }
            }

         }
      }
   }

   public static void redirectPointers(Unit oldLocation, Unit newLocation) {
      List<UnitBox> boxesPointing = oldLocation.getBoxesPointingToThis();
      UnitBox[] boxes = (UnitBox[])boxesPointing.toArray(new UnitBox[boxesPointing.size()]);
      UnitBox[] var4 = boxes;
      int var5 = boxes.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         UnitBox box = var4[var6];
         if (box.getUnit() != oldLocation) {
            throw new RuntimeException("Something weird's happening");
         }

         if (!box.isBranchTarget()) {
            box.setUnit(newLocation);
         }
      }

   }
}
