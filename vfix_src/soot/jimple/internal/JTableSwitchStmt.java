package soot.jimple.internal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import soot.Unit;
import soot.UnitBox;
import soot.UnitPrinter;
import soot.Value;
import soot.ValueBox;
import soot.baf.Baf;
import soot.baf.PlaceholderInst;
import soot.jimple.ConvertToBaf;
import soot.jimple.Jimple;
import soot.jimple.JimpleToBafContext;
import soot.jimple.StmtSwitch;
import soot.jimple.TableSwitchStmt;
import soot.util.Switch;

public class JTableSwitchStmt extends AbstractSwitchStmt implements TableSwitchStmt {
   int lowIndex;
   int highIndex;

   private static UnitBox[] getTargetBoxesArray(List<? extends Unit> targets) {
      UnitBox[] targetBoxes = new UnitBox[targets.size()];

      for(int i = 0; i < targetBoxes.length; ++i) {
         targetBoxes[i] = Jimple.v().newStmtBox((Unit)targets.get(i));
      }

      return targetBoxes;
   }

   public Object clone() {
      return new JTableSwitchStmt(Jimple.cloneIfNecessary(this.getKey()), this.lowIndex, this.highIndex, this.getTargets(), this.getDefaultTarget());
   }

   public JTableSwitchStmt(Value key, int lowIndex, int highIndex, List<? extends Unit> targets, Unit defaultTarget) {
      this(Jimple.v().newImmediateBox(key), lowIndex, highIndex, getTargetBoxesArray(targets), Jimple.v().newStmtBox(defaultTarget));
   }

   public JTableSwitchStmt(Value key, int lowIndex, int highIndex, List<? extends UnitBox> targets, UnitBox defaultTarget) {
      this(Jimple.v().newImmediateBox(key), lowIndex, highIndex, (UnitBox[])targets.toArray(new UnitBox[targets.size()]), defaultTarget);
   }

   protected JTableSwitchStmt(ValueBox keyBox, int lowIndex, int highIndex, UnitBox[] targetBoxes, UnitBox defaultTargetBox) {
      super(keyBox, defaultTargetBox, targetBoxes);
      if (lowIndex > highIndex) {
         throw new RuntimeException("Error creating tableswitch: lowIndex(" + lowIndex + ") can't be greater than highIndex(" + highIndex + ").");
      } else {
         this.lowIndex = lowIndex;
         this.highIndex = highIndex;
      }
   }

   public String toString() {
      StringBuffer buffer = new StringBuffer();
      String endOfLine = " ";
      buffer.append("tableswitch(" + this.keyBox.getValue().toString() + ")" + endOfLine);
      buffer.append("{" + endOfLine);

      for(int i = this.lowIndex; i < this.highIndex; ++i) {
         Unit target = this.getTarget(i - this.lowIndex);
         buffer.append("    case " + i + ": " + "goto" + " " + (target == this ? "self" : target) + ";" + endOfLine);
      }

      Unit target = this.getTarget(this.highIndex - this.lowIndex);
      buffer.append("    case " + this.highIndex + ": " + "goto" + " " + (target == this ? "self" : target) + ";" + endOfLine);
      target = this.getDefaultTarget();
      buffer.append("    default: goto " + (target == this ? "self" : target) + ";" + endOfLine);
      buffer.append("}");
      return buffer.toString();
   }

   public void toString(UnitPrinter up) {
      up.literal("tableswitch");
      up.literal("(");
      this.keyBox.toString(up);
      up.literal(")");
      up.newline();
      up.literal("{");
      up.newline();

      for(int i = this.lowIndex; i < this.highIndex; ++i) {
         this.printCaseTarget(up, i);
      }

      this.printCaseTarget(up, this.highIndex);
      up.literal("    ");
      up.literal("default");
      up.literal(": ");
      up.literal("goto");
      up.literal(" ");
      this.defaultTargetBox.toString(up);
      up.literal(";");
      up.newline();
      up.literal("}");
   }

   private void printCaseTarget(UnitPrinter up, int targetIndex) {
      up.literal("    ");
      up.literal("case");
      up.literal(" ");
      up.literal(Integer.toString(targetIndex));
      up.literal(": ");
      up.literal("goto");
      up.literal(" ");
      this.targetBoxes[targetIndex - this.lowIndex].toString(up);
      up.literal(";");
      up.newline();
   }

   public void setLowIndex(int lowIndex) {
      this.lowIndex = lowIndex;
   }

   public void setHighIndex(int highIndex) {
      this.highIndex = highIndex;
   }

   public int getLowIndex() {
      return this.lowIndex;
   }

   public int getHighIndex() {
      return this.highIndex;
   }

   public void apply(Switch sw) {
      ((StmtSwitch)sw).caseTableSwitchStmt(this);
   }

   public void convertToBaf(JimpleToBafContext context, List<Unit> out) {
      List<PlaceholderInst> targetPlaceholders = new ArrayList();
      ((ConvertToBaf)this.getKey()).convertToBaf(context, out);
      Iterator var4 = this.getTargets().iterator();

      while(var4.hasNext()) {
         Unit target = (Unit)var4.next();
         targetPlaceholders.add(Baf.v().newPlaceholderInst(target));
      }

      Unit u = Baf.v().newTableSwitchInst(Baf.v().newPlaceholderInst(this.getDefaultTarget()), this.lowIndex, this.highIndex, targetPlaceholders);
      u.addAllTagsOf(this);
      out.add(u);
   }
}
