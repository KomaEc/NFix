package soot.jimple.internal;

import java.util.Collections;
import java.util.List;
import soot.Unit;
import soot.UnitBox;
import soot.UnitPrinter;
import soot.baf.Baf;
import soot.jimple.GotoStmt;
import soot.jimple.Jimple;
import soot.jimple.JimpleToBafContext;
import soot.jimple.StmtSwitch;
import soot.util.Switch;

public class JGotoStmt extends AbstractStmt implements GotoStmt {
   final UnitBox targetBox;
   final List<UnitBox> targetBoxes;

   public JGotoStmt(Unit target) {
      this(Jimple.v().newStmtBox(target));
   }

   public JGotoStmt(UnitBox box) {
      this.targetBox = box;
      this.targetBoxes = Collections.singletonList(box);
   }

   public Object clone() {
      return new JGotoStmt(this.getTarget());
   }

   public String toString() {
      Unit t = this.getTarget();
      String target = "(branch)";
      if (!t.branches()) {
         target = t.toString();
      }

      return "goto [?= " + target + "]";
   }

   public void toString(UnitPrinter up) {
      up.literal("goto");
      up.literal(" ");
      this.targetBox.toString(up);
   }

   public Unit getTarget() {
      return this.targetBox.getUnit();
   }

   public void setTarget(Unit target) {
      this.targetBox.setUnit(target);
   }

   public UnitBox getTargetBox() {
      return this.targetBox;
   }

   public List<UnitBox> getUnitBoxes() {
      return this.targetBoxes;
   }

   public void apply(Switch sw) {
      ((StmtSwitch)sw).caseGotoStmt(this);
   }

   public void convertToBaf(JimpleToBafContext context, List<Unit> out) {
      Unit u = Baf.v().newGotoInst(Baf.v().newPlaceholderInst(this.getTarget()));
      u.addAllTagsOf(this);
      out.add(u);
   }

   public boolean fallsThrough() {
      return false;
   }

   public boolean branches() {
      return true;
   }
}
