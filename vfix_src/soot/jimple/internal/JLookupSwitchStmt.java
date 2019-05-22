package soot.jimple.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import soot.Unit;
import soot.UnitBox;
import soot.UnitPrinter;
import soot.Value;
import soot.ValueBox;
import soot.baf.Baf;
import soot.baf.PlaceholderInst;
import soot.jimple.Constant;
import soot.jimple.ConvertToBaf;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;
import soot.jimple.JimpleToBafContext;
import soot.jimple.LookupSwitchStmt;
import soot.jimple.StmtSwitch;
import soot.util.Switch;

public class JLookupSwitchStmt extends AbstractSwitchStmt implements LookupSwitchStmt {
   List<IntConstant> lookupValues;

   private static UnitBox[] getTargetBoxesArray(List<? extends Unit> targets) {
      UnitBox[] targetBoxes = new UnitBox[targets.size()];

      for(int i = 0; i < targetBoxes.length; ++i) {
         targetBoxes[i] = Jimple.v().newStmtBox((Unit)targets.get(i));
      }

      return targetBoxes;
   }

   public Object clone() {
      int lookupValueCount = this.lookupValues.size();
      List<IntConstant> clonedLookupValues = new ArrayList(lookupValueCount);

      for(int i = 0; i < lookupValueCount; ++i) {
         clonedLookupValues.add(i, IntConstant.v(this.getLookupValue(i)));
      }

      return new JLookupSwitchStmt(this.getKey(), clonedLookupValues, this.getTargets(), this.getDefaultTarget());
   }

   public JLookupSwitchStmt(Value key, List<IntConstant> lookupValues, List<? extends Unit> targets, Unit defaultTarget) {
      this(Jimple.v().newImmediateBox(key), lookupValues, getTargetBoxesArray(targets), Jimple.v().newStmtBox(defaultTarget));
   }

   public JLookupSwitchStmt(Value key, List<IntConstant> lookupValues, List<? extends UnitBox> targets, UnitBox defaultTarget) {
      this(Jimple.v().newImmediateBox(key), lookupValues, (UnitBox[])targets.toArray(new UnitBox[targets.size()]), defaultTarget);
   }

   protected JLookupSwitchStmt(ValueBox keyBox, List<IntConstant> lookupValues, UnitBox[] targetBoxes, UnitBox defaultTargetBox) {
      super(keyBox, defaultTargetBox, targetBoxes);
      this.setLookupValues(lookupValues);
   }

   public String toString() {
      StringBuffer buffer = new StringBuffer();
      String endOfLine = " ";
      buffer.append("lookupswitch(" + this.keyBox.getValue().toString() + ")" + endOfLine);
      buffer.append("{" + endOfLine);

      for(int i = 0; i < this.lookupValues.size(); ++i) {
         Unit target = this.getTarget(i);
         buffer.append("    case " + this.lookupValues.get(i) + ": " + "goto" + " " + (target == this ? "self" : target) + ";" + endOfLine);
      }

      Unit target = this.getDefaultTarget();
      buffer.append("    default: goto " + (target == this ? "self" : target) + ";" + endOfLine);
      buffer.append("}");
      return buffer.toString();
   }

   public void toString(UnitPrinter up) {
      up.literal("lookupswitch");
      up.literal("(");
      this.keyBox.toString(up);
      up.literal(")");
      up.newline();
      up.literal("{");
      up.newline();

      for(int i = 0; i < this.lookupValues.size(); ++i) {
         up.literal("    ");
         up.literal("case");
         up.literal(" ");
         up.constant((Constant)this.lookupValues.get(i));
         up.literal(": ");
         up.literal("goto");
         up.literal(" ");
         this.targetBoxes[i].toString(up);
         up.literal(";");
         up.newline();
      }

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

   public void setLookupValues(List<IntConstant> lookupValues) {
      this.lookupValues = new ArrayList(lookupValues);
   }

   public void setLookupValue(int index, int value) {
      this.lookupValues.set(index, IntConstant.v(value));
   }

   public int getLookupValue(int index) {
      return ((IntConstant)this.lookupValues.get(index)).value;
   }

   public List<IntConstant> getLookupValues() {
      return Collections.unmodifiableList(this.lookupValues);
   }

   public void apply(Switch sw) {
      ((StmtSwitch)sw).caseLookupSwitchStmt(this);
   }

   public void convertToBaf(JimpleToBafContext context, List<Unit> out) {
      List<PlaceholderInst> targetPlaceholders = new ArrayList();
      ((ConvertToBaf)this.getKey()).convertToBaf(context, out);
      Iterator var4 = this.getTargets().iterator();

      while(var4.hasNext()) {
         Unit target = (Unit)var4.next();
         targetPlaceholders.add(Baf.v().newPlaceholderInst(target));
      }

      Unit u = Baf.v().newLookupSwitchInst(Baf.v().newPlaceholderInst(this.getDefaultTarget()), this.getLookupValues(), targetPlaceholders);
      u.addAllTagsOf(this);
      out.add(u);
   }
}
