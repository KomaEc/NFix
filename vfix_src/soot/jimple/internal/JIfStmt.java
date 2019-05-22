package soot.jimple.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import soot.Unit;
import soot.UnitBox;
import soot.UnitPrinter;
import soot.Value;
import soot.ValueBox;
import soot.baf.Baf;
import soot.jimple.AbstractJimpleValueSwitch;
import soot.jimple.BinopExpr;
import soot.jimple.ConvertToBaf;
import soot.jimple.EqExpr;
import soot.jimple.GeExpr;
import soot.jimple.GtExpr;
import soot.jimple.IfStmt;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;
import soot.jimple.JimpleToBafContext;
import soot.jimple.LeExpr;
import soot.jimple.LtExpr;
import soot.jimple.NeExpr;
import soot.jimple.NullConstant;
import soot.jimple.Stmt;
import soot.jimple.StmtSwitch;
import soot.util.Switch;

public class JIfStmt extends AbstractStmt implements IfStmt {
   final ValueBox conditionBox;
   final UnitBox targetBox;
   final List<UnitBox> targetBoxes;

   public JIfStmt(Value condition, Unit target) {
      this(condition, Jimple.v().newStmtBox(target));
   }

   public JIfStmt(Value condition, UnitBox target) {
      this(Jimple.v().newConditionExprBox(condition), target);
   }

   protected JIfStmt(ValueBox conditionBox, UnitBox targetBox) {
      this.conditionBox = conditionBox;
      this.targetBox = targetBox;
      this.targetBoxes = Collections.singletonList(targetBox);
   }

   public Object clone() {
      return new JIfStmt(Jimple.cloneIfNecessary(this.getCondition()), this.getTarget());
   }

   public String toString() {
      Unit t = this.getTarget();
      String target = "(branch)";
      if (!t.branches()) {
         target = t.toString();
      }

      return "if " + this.getCondition().toString() + " " + "goto" + " " + target;
   }

   public void toString(UnitPrinter up) {
      up.literal("if");
      up.literal(" ");
      this.conditionBox.toString(up);
      up.literal(" ");
      up.literal("goto");
      up.literal(" ");
      this.targetBox.toString(up);
   }

   public Value getCondition() {
      return this.conditionBox.getValue();
   }

   public void setCondition(Value condition) {
      this.conditionBox.setValue(condition);
   }

   public ValueBox getConditionBox() {
      return this.conditionBox;
   }

   public Stmt getTarget() {
      return (Stmt)this.targetBox.getUnit();
   }

   public void setTarget(Unit target) {
      this.targetBox.setUnit(target);
   }

   public UnitBox getTargetBox() {
      return this.targetBox;
   }

   public List<ValueBox> getUseBoxes() {
      List<ValueBox> useBoxes = new ArrayList();
      useBoxes.addAll(this.conditionBox.getValue().getUseBoxes());
      useBoxes.add(this.conditionBox);
      return useBoxes;
   }

   public final List<UnitBox> getUnitBoxes() {
      return this.targetBoxes;
   }

   public void apply(Switch sw) {
      ((StmtSwitch)sw).caseIfStmt(this);
   }

   public void convertToBaf(JimpleToBafContext context, final List<Unit> out) {
      Value cond = this.getCondition();
      final Value op1 = ((BinopExpr)cond).getOp1();
      Value op2 = ((BinopExpr)cond).getOp2();
      context.setCurrentUnit(this);
      if (!(op2 instanceof NullConstant) && !(op1 instanceof NullConstant)) {
         if (op2 instanceof IntConstant && ((IntConstant)op2).value == 0) {
            ((ConvertToBaf)op1).convertToBaf(context, out);
            cond.apply(new AbstractJimpleValueSwitch() {
               private void add(Unit u) {
                  u.addAllTagsOf(JIfStmt.this);
                  out.add(u);
               }

               public void caseEqExpr(EqExpr expr) {
                  this.add(Baf.v().newIfEqInst(Baf.v().newPlaceholderInst(JIfStmt.this.getTarget())));
               }

               public void caseNeExpr(NeExpr expr) {
                  this.add(Baf.v().newIfNeInst(Baf.v().newPlaceholderInst(JIfStmt.this.getTarget())));
               }

               public void caseLtExpr(LtExpr expr) {
                  this.add(Baf.v().newIfLtInst(Baf.v().newPlaceholderInst(JIfStmt.this.getTarget())));
               }

               public void caseLeExpr(LeExpr expr) {
                  this.add(Baf.v().newIfLeInst(Baf.v().newPlaceholderInst(JIfStmt.this.getTarget())));
               }

               public void caseGtExpr(GtExpr expr) {
                  this.add(Baf.v().newIfGtInst(Baf.v().newPlaceholderInst(JIfStmt.this.getTarget())));
               }

               public void caseGeExpr(GeExpr expr) {
                  this.add(Baf.v().newIfGeInst(Baf.v().newPlaceholderInst(JIfStmt.this.getTarget())));
               }
            });
         } else if (op1 instanceof IntConstant && ((IntConstant)op1).value == 0) {
            ((ConvertToBaf)op2).convertToBaf(context, out);
            cond.apply(new AbstractJimpleValueSwitch() {
               private void add(Unit u) {
                  u.addAllTagsOf(JIfStmt.this);
                  out.add(u);
               }

               public void caseEqExpr(EqExpr expr) {
                  this.add(Baf.v().newIfEqInst(Baf.v().newPlaceholderInst(JIfStmt.this.getTarget())));
               }

               public void caseNeExpr(NeExpr expr) {
                  this.add(Baf.v().newIfNeInst(Baf.v().newPlaceholderInst(JIfStmt.this.getTarget())));
               }

               public void caseLtExpr(LtExpr expr) {
                  this.add(Baf.v().newIfGtInst(Baf.v().newPlaceholderInst(JIfStmt.this.getTarget())));
               }

               public void caseLeExpr(LeExpr expr) {
                  this.add(Baf.v().newIfGeInst(Baf.v().newPlaceholderInst(JIfStmt.this.getTarget())));
               }

               public void caseGtExpr(GtExpr expr) {
                  this.add(Baf.v().newIfLtInst(Baf.v().newPlaceholderInst(JIfStmt.this.getTarget())));
               }

               public void caseGeExpr(GeExpr expr) {
                  this.add(Baf.v().newIfLeInst(Baf.v().newPlaceholderInst(JIfStmt.this.getTarget())));
               }
            });
         } else {
            ((ConvertToBaf)op1).convertToBaf(context, out);
            ((ConvertToBaf)op2).convertToBaf(context, out);
            cond.apply(new AbstractJimpleValueSwitch() {
               private void add(Unit u) {
                  u.addAllTagsOf(JIfStmt.this);
                  out.add(u);
               }

               public void caseEqExpr(EqExpr expr) {
                  this.add(Baf.v().newIfCmpEqInst(op1.getType(), Baf.v().newPlaceholderInst(JIfStmt.this.getTarget())));
               }

               public void caseNeExpr(NeExpr expr) {
                  this.add(Baf.v().newIfCmpNeInst(op1.getType(), Baf.v().newPlaceholderInst(JIfStmt.this.getTarget())));
               }

               public void caseLtExpr(LtExpr expr) {
                  this.add(Baf.v().newIfCmpLtInst(op1.getType(), Baf.v().newPlaceholderInst(JIfStmt.this.getTarget())));
               }

               public void caseLeExpr(LeExpr expr) {
                  this.add(Baf.v().newIfCmpLeInst(op1.getType(), Baf.v().newPlaceholderInst(JIfStmt.this.getTarget())));
               }

               public void caseGtExpr(GtExpr expr) {
                  this.add(Baf.v().newIfCmpGtInst(op1.getType(), Baf.v().newPlaceholderInst(JIfStmt.this.getTarget())));
               }

               public void caseGeExpr(GeExpr expr) {
                  this.add(Baf.v().newIfCmpGeInst(op1.getType(), Baf.v().newPlaceholderInst(JIfStmt.this.getTarget())));
               }
            });
         }
      } else {
         if (op2 instanceof NullConstant) {
            ((ConvertToBaf)op1).convertToBaf(context, out);
         } else {
            ((ConvertToBaf)op2).convertToBaf(context, out);
         }

         Object u;
         if (cond instanceof EqExpr) {
            u = Baf.v().newIfNullInst(Baf.v().newPlaceholderInst(this.getTarget()));
         } else {
            if (!(cond instanceof NeExpr)) {
               throw new RuntimeException("invalid condition");
            }

            u = Baf.v().newIfNonNullInst(Baf.v().newPlaceholderInst(this.getTarget()));
         }

         ((Unit)u).addAllTagsOf(this);
         out.add(u);
      }
   }

   public boolean fallsThrough() {
      return true;
   }

   public boolean branches() {
      return true;
   }
}
