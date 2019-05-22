package soot.jbco.jimpleTransformations;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.DoubleType;
import soot.IntType;
import soot.Local;
import soot.LongType;
import soot.PatchingChain;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.jbco.IJbcoTransform;
import soot.jbco.Main;
import soot.jbco.util.Rand;
import soot.jimple.AssignStmt;
import soot.jimple.DivExpr;
import soot.jimple.DoubleConstant;
import soot.jimple.Expr;
import soot.jimple.FloatConstant;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;
import soot.jimple.LongConstant;
import soot.jimple.MulExpr;
import soot.jimple.NumericConstant;
import soot.util.Chain;

public class ArithmeticTransformer extends BodyTransformer implements IJbcoTransform {
   private static int mulPerformed = 0;
   private static int divPerformed = 0;
   private static int total = 0;
   public static String[] dependancies = new String[]{"jtp.jbco_cae2bo"};
   public static String name = "jtp.jbco_cae2bo";

   public String[] getDependencies() {
      return dependancies;
   }

   public String getName() {
      return name;
   }

   protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
      int weight = Main.getWeight(phaseName, b.getMethod().getSignature());
      if (weight != 0) {
         PatchingChain<Unit> units = b.getUnits();
         int localCount = 0;
         Chain<Local> locals = b.getLocals();
         if (output) {
            out.println("*** Performing Arithmetic Transformation on " + b.getMethod().getSignature());
         }

         Iterator it = units.snapshotIterator();

         while(true) {
            Unit u;
            AssignStmt as;
            Value op;
            Type opType;
            int max;
            Object[] shft_rem;
            do {
               do {
                  do {
                     NumericConstant nc;
                     do {
                        do {
                           while(true) {
                              do {
                                 if (!it.hasNext()) {
                                    return;
                                 }

                                 u = (Unit)it.next();
                              } while(!(u instanceof AssignStmt));

                              as = (AssignStmt)u;
                              Value v = as.getRightOp();
                              Value op2;
                              if (v instanceof MulExpr) {
                                 ++total;
                                 MulExpr me = (MulExpr)v;
                                 op2 = me.getOp1();
                                 op = null;
                                 Value op2 = me.getOp2();
                                 nc = null;
                                 if (op2 instanceof NumericConstant) {
                                    nc = (NumericConstant)op2;
                                    op = op2;
                                 } else if (op2 instanceof NumericConstant) {
                                    nc = (NumericConstant)op2;
                                    op = op2;
                                 }
                                 break;
                              }

                              if (v instanceof DivExpr) {
                                 ++total;
                                 DivExpr de = (DivExpr)v;
                                 op2 = de.getOp2();
                                 if (op2 instanceof NumericConstant) {
                                    NumericConstant nc = (NumericConstant)op2;
                                    Type opType = de.getOp1().getType();
                                    int max = opType instanceof IntType ? 32 : (opType instanceof LongType ? 64 : 0);
                                    if (max != 0) {
                                       Object[] shft_rem = this.checkNumericValue(nc);
                                       if (shft_rem[0] != null && (shft_rem[1] == null || (Double)shft_rem[1] == 0.0D) && (Integer)shft_rem[0] < max && Rand.getInt(10) <= weight) {
                                          List<Unit> unitsBuilt = new ArrayList();
                                          int rand = Rand.getInt(16);
                                          int shift = (Integer)shft_rem[0];
                                          boolean neg = (Boolean)shft_rem[2];
                                          if (Rand.getInt() % 2 == 0) {
                                             shift += rand * max;
                                          } else {
                                             shift -= rand * max;
                                          }

                                          Expr e = Jimple.v().newShrExpr(de.getOp1(), IntConstant.v(shift));
                                          if (((Expr)e).getType().getClass() != as.getLeftOp().getType().getClass()) {
                                             Local tmp = Jimple.v().newLocal("__tmp_shft_lcl" + localCount++, ((Expr)e).getType());
                                             locals.add(tmp);
                                             Unit newU = Jimple.v().newAssignStmt(tmp, (Value)e);
                                             unitsBuilt.add(newU);
                                             units.insertAfter((Unit)newU, (Unit)u);
                                             e = Jimple.v().newCastExpr(tmp, as.getLeftOp().getType());
                                          }

                                          as.setRightOp((Value)e);
                                          unitsBuilt.add(as);
                                          if (neg) {
                                             Unit newU = Jimple.v().newAssignStmt(as.getLeftOp(), Jimple.v().newNegExpr(as.getLeftOp()));
                                             unitsBuilt.add(newU);
                                             units.insertAfter((Unit)newU, (Unit)u);
                                          }

                                          ++divPerformed;
                                          this.printOutput(unitsBuilt);
                                       }
                                    }
                                 }
                              }
                           }
                        } while(nc == null);

                        if (output) {
                           out.println("Considering: " + as + "\r");
                        }

                        opType = op.getType();
                        max = opType instanceof IntType ? 32 : (opType instanceof LongType ? 64 : 0);
                     } while(max == 0);

                     shft_rem = this.checkNumericValue(nc);
                  } while(shft_rem[0] == null);
               } while((Integer)shft_rem[0] >= max);
            } while(Rand.getInt(10) > weight);

            List<Unit> unitsBuilt = new ArrayList();
            int rand = Rand.getInt(16);
            int shift = (Integer)shft_rem[0];
            boolean neg = (Boolean)shft_rem[2];
            if (rand % 2 == 0) {
               shift += rand * max;
            } else {
               shift -= rand * max;
            }

            Local tmp2;
            Object e;
            if (shft_rem[1] == null) {
               e = Jimple.v().newShlExpr(op, IntConstant.v(shift));
            } else {
               tmp2 = null;
               Local tmp1 = Jimple.v().newLocal("__tmp_shft_lcl" + localCount++, opType);
               locals.add(tmp1);
               Unit newU = Jimple.v().newAssignStmt(tmp1, Jimple.v().newShlExpr(op, IntConstant.v(shift)));
               unitsBuilt.add(newU);
               units.insertBefore((Unit)newU, (Unit)u);
               double rem = (Double)shft_rem[1];
               if (rem != 1.0D) {
                  Object nc;
                  if (rem == (double)((int)rem) && opType instanceof IntType) {
                     nc = IntConstant.v((int)rem);
                  } else if (rem == (double)((long)rem) && opType instanceof LongType) {
                     nc = LongConstant.v((long)rem);
                  } else {
                     nc = DoubleConstant.v(rem);
                  }

                  if (nc instanceof DoubleConstant) {
                     tmp2 = Jimple.v().newLocal("__tmp_shft_lcl" + localCount++, DoubleType.v());
                     locals.add(tmp2);
                     newU = Jimple.v().newAssignStmt(tmp2, Jimple.v().newCastExpr(op, DoubleType.v()));
                     unitsBuilt.add(newU);
                     units.insertBefore((Unit)newU, (Unit)u);
                     newU = Jimple.v().newAssignStmt(tmp2, Jimple.v().newMulExpr(tmp2, (Value)nc));
                  } else {
                     tmp2 = Jimple.v().newLocal("__tmp_shft_lcl" + localCount++, ((NumericConstant)nc).getType());
                     locals.add(tmp2);
                     newU = Jimple.v().newAssignStmt(tmp2, Jimple.v().newMulExpr(op, (Value)nc));
                  }

                  unitsBuilt.add(newU);
                  units.insertBefore((Unit)newU, (Unit)u);
               }

               if (tmp2 == null) {
                  e = Jimple.v().newAddExpr(tmp1, op);
               } else if (tmp2.getType().getClass() != tmp1.getType().getClass()) {
                  Local tmp3 = Jimple.v().newLocal("__tmp_shft_lcl" + localCount++, tmp2.getType());
                  locals.add(tmp3);
                  newU = Jimple.v().newAssignStmt(tmp3, Jimple.v().newCastExpr(tmp1, tmp2.getType()));
                  unitsBuilt.add(newU);
                  units.insertBefore((Unit)newU, (Unit)u);
                  e = Jimple.v().newAddExpr(tmp3, tmp2);
               } else {
                  e = Jimple.v().newAddExpr(tmp1, tmp2);
               }
            }

            if (((Expr)e).getType().getClass() != as.getLeftOp().getType().getClass()) {
               tmp2 = Jimple.v().newLocal("__tmp_shft_lcl" + localCount++, ((Expr)e).getType());
               locals.add(tmp2);
               Unit newU = Jimple.v().newAssignStmt(tmp2, (Value)e);
               unitsBuilt.add(newU);
               units.insertAfter((Unit)newU, (Unit)u);
               e = Jimple.v().newCastExpr(tmp2, as.getLeftOp().getType());
            }

            as.setRightOp((Value)e);
            unitsBuilt.add(as);
            if (neg) {
               Unit newU = Jimple.v().newAssignStmt(as.getLeftOp(), Jimple.v().newNegExpr(as.getLeftOp()));
               unitsBuilt.add(newU);
               units.insertAfter((Unit)newU, (Unit)u);
            }

            ++mulPerformed;
            this.printOutput(unitsBuilt);
         }
      }
   }

   private void printOutput(List<Unit> unitsBuilt) {
      if (output) {
         out.println(" after as: ");
         Iterator var2 = unitsBuilt.iterator();

         while(var2.hasNext()) {
            Unit uu = (Unit)var2.next();
            out.println("\t" + uu + "\ttype : " + (uu instanceof AssignStmt ? ((AssignStmt)uu).getLeftOp().getType().toString() : ""));
         }

      }
   }

   public void outputSummary() {
      if (output) {
         out.println("Replaced mul/div expressions: " + (divPerformed + mulPerformed));
         out.println("Total mul/div expressions: " + total);
      }
   }

   private Object[] checkNumericValue(NumericConstant nc) {
      Double dnc = null;
      if (nc instanceof IntConstant) {
         dnc = (double)((IntConstant)nc).value;
      } else if (nc instanceof DoubleConstant) {
         dnc = ((DoubleConstant)nc).value;
      } else if (nc instanceof FloatConstant) {
         dnc = (double)((FloatConstant)nc).value;
      } else if (nc instanceof LongConstant) {
         dnc = (double)((LongConstant)nc).value;
      }

      Object[] shift = new Object[3];
      if (dnc != null) {
         shift[2] = dnc < 0.0D;
         double[] tmp = this.checkShiftValue(dnc);
         if (tmp[0] != 0.0D) {
            shift[0] = (int)tmp[0];
            if (tmp[1] != 0.0D) {
               shift[1] = tmp[1];
            } else {
               shift[1] = null;
            }
         } else {
            dnc = null;
         }
      }

      if (dnc == null) {
         shift[0] = null;
         shift[1] = null;
      }

      return shift;
   }

   private double[] checkShiftValue(double val) {
      double[] shift = new double[2];
      if (val != 0.0D && val != 1.0D && val != -1.0D) {
         double shift_dbl = Math.log(val) / Math.log(2.0D);
         double shift_int = Math.rint(shift_dbl);
         if (shift_dbl == shift_int) {
            shift[1] = 0.0D;
         } else {
            if (Math.pow(2.0D, shift_int) > val) {
               --shift_int;
            }

            shift[1] = val - Math.pow(2.0D, shift_int);
         }

         shift[0] = shift_int;
      } else {
         shift[0] = 0.0D;
         shift[1] = 0.0D;
      }

      return shift;
   }
}
