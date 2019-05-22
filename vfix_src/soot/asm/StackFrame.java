package soot.asm;

import java.util.ArrayList;
import soot.Local;
import soot.Unit;
import soot.ValueBox;
import soot.jimple.AssignStmt;
import soot.jimple.DefinitionStmt;
import soot.jimple.Jimple;

final class StackFrame {
   private Operand[] out;
   private Local[] inStackLocals;
   private ValueBox[] boxes;
   private ArrayList<Operand[]> in;
   private final AsmMethodSource src;

   StackFrame(AsmMethodSource src) {
      this.src = src;
   }

   Operand[] out() {
      return this.out;
   }

   void in(Operand... oprs) {
      ArrayList<Operand[]> in = this.in;
      if (in == null) {
         in = this.in = new ArrayList(1);
      } else {
         in.clear();
      }

      in.add(oprs);
      this.inStackLocals = new Local[oprs.length];
   }

   void boxes(ValueBox... boxes) {
      this.boxes = boxes;
   }

   void out(Operand... oprs) {
      this.out = oprs;
   }

   void mergeIn(Operand... oprs) {
      ArrayList<Operand[]> in = this.in;
      if (((Operand[])in.get(0)).length != oprs.length) {
         throw new IllegalArgumentException("Invalid in operands length!");
      } else {
         int nrIn = in.size();
         boolean diff = false;

         for(int i = 0; i != oprs.length; ++i) {
            Operand newOp = oprs[i];
            diff = true;
            Local stack = this.inStackLocals[i];
            if (stack != null) {
               AssignStmt as;
               if (newOp.stack == null) {
                  newOp.stack = stack;
                  as = Jimple.v().newAssignStmt(stack, newOp.value);
                  this.src.setUnit(newOp.insn, as);
                  newOp.updateBoxes();
               } else {
                  as = Jimple.v().newAssignStmt(stack, newOp.stackOrValue());
                  this.src.mergeUnits(newOp.insn, as);
                  newOp.addBox(as.getRightOpBox());
               }
            } else {
               for(int j = 0; j != nrIn; ++j) {
                  stack = ((Operand[])in.get(j))[i].stack;
                  if (stack != null) {
                     break;
                  }
               }

               if (stack == null) {
                  stack = newOp.stack;
                  if (stack == null) {
                     stack = this.src.newStackLocal();
                  }
               }

               ValueBox box = this.boxes == null ? null : this.boxes[i];

               for(int j = 0; j != nrIn; ++j) {
                  Operand prevOp = ((Operand[])in.get(j))[i];
                  if (prevOp.stack != stack) {
                     prevOp.removeBox(box);
                     if (prevOp.stack == null) {
                        prevOp.stack = stack;
                        AssignStmt as = Jimple.v().newAssignStmt(stack, prevOp.value);
                        this.src.setUnit(prevOp.insn, as);
                     } else {
                        Unit u = this.src.getUnit(prevOp.insn);
                        DefinitionStmt as = (DefinitionStmt)((DefinitionStmt)(u instanceof UnitContainer ? ((UnitContainer)u).getFirstUnit() : u));
                        ValueBox lvb = as.getLeftOpBox();

                        assert lvb.getValue() == prevOp.stack : "Invalid stack local!";

                        lvb.setValue(stack);
                        prevOp.stack = stack;
                     }

                     prevOp.updateBoxes();
                  }
               }

               if (newOp.stack != stack) {
                  if (newOp.stack == null) {
                     newOp.stack = stack;
                     AssignStmt as = Jimple.v().newAssignStmt(stack, newOp.value);
                     this.src.setUnit(newOp.insn, as);
                  } else {
                     Unit u = this.src.getUnit(newOp.insn);
                     DefinitionStmt as = (DefinitionStmt)((DefinitionStmt)(u instanceof UnitContainer ? ((UnitContainer)u).getFirstUnit() : u));
                     ValueBox lvb = as.getLeftOpBox();

                     assert lvb.getValue() == newOp.stack : "Invalid stack local!";

                     lvb.setValue(stack);
                     newOp.stack = stack;
                  }

                  newOp.updateBoxes();
               }

               if (box != null) {
                  box.setValue(stack);
               }

               this.inStackLocals[i] = stack;
            }
         }

         if (diff) {
            in.add(oprs);
         }

      }
   }
}
