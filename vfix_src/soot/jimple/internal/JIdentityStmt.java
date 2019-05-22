package soot.jimple.internal;

import java.util.List;
import soot.Local;
import soot.RefType;
import soot.Unit;
import soot.UnitPrinter;
import soot.Value;
import soot.ValueBox;
import soot.baf.Baf;
import soot.jimple.CaughtExceptionRef;
import soot.jimple.IdentityStmt;
import soot.jimple.Jimple;
import soot.jimple.JimpleToBafContext;
import soot.jimple.ParameterRef;
import soot.jimple.StmtSwitch;
import soot.jimple.ThisRef;
import soot.util.Switch;

public class JIdentityStmt extends AbstractDefinitionStmt implements IdentityStmt {
   public JIdentityStmt(Value local, Value identityValue) {
      this(Jimple.v().newLocalBox(local), Jimple.v().newIdentityRefBox(identityValue));
   }

   protected JIdentityStmt(ValueBox localBox, ValueBox identityValueBox) {
      super(localBox, identityValueBox);
   }

   public Object clone() {
      return new JIdentityStmt(Jimple.cloneIfNecessary(this.getLeftOp()), Jimple.cloneIfNecessary(this.getRightOp()));
   }

   public String toString() {
      return this.leftBox.getValue().toString() + " := " + this.rightBox.getValue().toString();
   }

   public void toString(UnitPrinter up) {
      this.leftBox.toString(up);
      up.literal(" := ");
      this.rightBox.toString(up);
   }

   public void setLeftOp(Value local) {
      this.leftBox.setValue(local);
   }

   public void setRightOp(Value identityRef) {
      this.rightBox.setValue(identityRef);
   }

   public void apply(Switch sw) {
      ((StmtSwitch)sw).caseIdentityStmt(this);
   }

   public void convertToBaf(JimpleToBafContext context, List<Unit> out) {
      Value currentRhs = this.getRightOp();
      Object newRhs;
      if (currentRhs instanceof ThisRef) {
         newRhs = Baf.v().newThisRef((RefType)((ThisRef)currentRhs).getType());
      } else {
         if (!(currentRhs instanceof ParameterRef)) {
            if (currentRhs instanceof CaughtExceptionRef) {
               Unit u = Baf.v().newStoreInst(RefType.v(), context.getBafLocalOfJimpleLocal((Local)this.getLeftOp()));
               u.addAllTagsOf(this);
               out.add(u);
               return;
            }

            throw new RuntimeException("Don't know how to convert unknown rhs");
         }

         newRhs = Baf.v().newParameterRef(((ParameterRef)currentRhs).getType(), ((ParameterRef)currentRhs).getIndex());
      }

      Unit u = Baf.v().newIdentityInst(context.getBafLocalOfJimpleLocal((Local)this.getLeftOp()), (Value)newRhs);
      u.addAllTagsOf(this);
      out.add(u);
   }
}
