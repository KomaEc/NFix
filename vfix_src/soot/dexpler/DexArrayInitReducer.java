package soot.dexpler;

import java.util.Iterator;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.BodyTransformer;
import soot.Local;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.Constant;
import soot.jimple.Stmt;
import soot.options.Options;
import soot.toolkits.scalar.UnusedLocalEliminator;

public class DexArrayInitReducer extends BodyTransformer {
   private static final Logger logger = LoggerFactory.getLogger(DexArrayInitReducer.class);

   public static DexArrayInitReducer v() {
      return new DexArrayInitReducer();
   }

   protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
      if (b.getTraps().isEmpty()) {
         Unit u1 = null;
         Unit u2 = null;
         Iterator uIt = b.getUnits().snapshotIterator();

         while(true) {
            while(true) {
               while(uIt.hasNext()) {
                  Unit u = (Unit)uIt.next();
                  if (u instanceof AssignStmt && ((Stmt)u).getBoxesPointingToThis().isEmpty()) {
                     AssignStmt assignStmt = (AssignStmt)u;
                     if (assignStmt.getLeftOp() instanceof ArrayRef) {
                        if (u1 == null || u2 == null || !u2.getBoxesPointingToThis().isEmpty() || !assignStmt.getBoxesPointingToThis().isEmpty()) {
                           u1 = null;
                           u2 = null;
                           continue;
                        }

                        ArrayRef arrayRef = (ArrayRef)assignStmt.getLeftOp();
                        Value u1val = ((ValueBox)u1.getDefBoxes().get(0)).getValue();
                        Value u2val = ((ValueBox)u2.getDefBoxes().get(0)).getValue();
                        if (arrayRef.getIndex() == u1val) {
                           arrayRef.setIndex(((AssignStmt)u1).getRightOp());
                        } else if (arrayRef.getIndex() == u2val) {
                           arrayRef.setIndex(((AssignStmt)u2).getRightOp());
                        }

                        if (assignStmt.getRightOp() == u1val) {
                           assignStmt.setRightOp(((AssignStmt)u1).getRightOp());
                        } else if (assignStmt.getRightOp() == u2val) {
                           assignStmt.setRightOp(((AssignStmt)u2).getRightOp());
                        }

                        Unit checkU = u;
                        boolean foundU1 = false;
                        boolean foundU2 = false;
                        boolean doneU1 = false;

                        for(boolean doneU2 = false; (!doneU1 || !doneU2) && (!foundU1 || !foundU2) && checkU != null; checkU = b.getUnits().getSuccOf(checkU)) {
                           Iterator var17 = checkU.getUseBoxes().iterator();

                           ValueBox vb;
                           while(var17.hasNext()) {
                              vb = (ValueBox)var17.next();
                              if (!doneU1 && vb.getValue() == u1val) {
                                 foundU1 = true;
                              }

                              if (!doneU2 && vb.getValue() == u2val) {
                                 foundU2 = true;
                              }
                           }

                           var17 = checkU.getDefBoxes().iterator();

                           while(var17.hasNext()) {
                              vb = (ValueBox)var17.next();
                              if (vb.getValue() == u1val) {
                                 doneU1 = true;
                              } else if (vb.getValue() == u2val) {
                                 doneU2 = true;
                              }
                           }

                           if (checkU.branches()) {
                              foundU1 = true;
                              foundU2 = true;
                              break;
                           }
                        }

                        if (!foundU1 && u1val instanceof Local) {
                           b.getUnits().remove(u1);
                           if (Options.v().verbose()) {
                              logger.debug("[" + b.getMethod().getName() + "]    remove 1 " + u1);
                           }
                        }

                        if (!foundU2 && u2val instanceof Local) {
                           b.getUnits().remove(u2);
                           if (Options.v().verbose()) {
                              logger.debug("[" + b.getMethod().getName() + "]    remove 2 " + u2);
                           }
                        }

                        u1 = null;
                        u2 = null;
                     }

                     if (!(assignStmt.getRightOp() instanceof Constant)) {
                        u1 = null;
                        u2 = null;
                     } else if (u1 == null) {
                        u1 = assignStmt;
                     } else if (u2 == null) {
                        u2 = assignStmt;
                        if (u1 != null) {
                           Value op1 = ((AssignStmt)u1).getLeftOp();
                           if (op1 == ((AssignStmt)assignStmt).getLeftOp()) {
                              u1 = assignStmt;
                              u2 = null;
                           }
                        }
                     } else {
                        u1 = u2;
                        u2 = assignStmt;
                     }
                  } else {
                     u1 = null;
                     u2 = null;
                  }
               }

               UnusedLocalEliminator.v().transform(b);
               return;
            }
         }
      }
   }
}
