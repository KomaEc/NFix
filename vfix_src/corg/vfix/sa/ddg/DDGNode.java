package corg.vfix.sa.ddg;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import soot.Local;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.SpecialInvokeExpr;
import soot.toolkits.scalar.ValueUnitPair;

public class DDGNode {
   private Unit unit;
   private ArrayList<Value> defs;
   private ArrayList<Value> uses;

   public DDGNode(Unit u) {
      this.unit = u;
      this.defs = this.setDefs(this.unit);
      this.uses = this.setUses(this.unit);
   }

   public Unit getUnit() {
      return this.unit;
   }

   public ArrayList<Value> getDefs() {
      return this.defs;
   }

   public ArrayList<Value> getUses() {
      return this.uses;
   }

   private ArrayList<Value> setDefs(Unit unit) {
      if (unit instanceof InvokeStmt) {
         InvokeExpr invokeExpr = ((InvokeStmt)unit).getInvokeExpr();
         if (invokeExpr instanceof SpecialInvokeExpr) {
            ValueBox vb = new ValueUnitPair(this.handleSpecialInvoke((SpecialInvokeExpr)invokeExpr), unit);
            List<ValueBox> defBox = new ArrayList();
            defBox.add(vb);
            return this.getLocalAndInstanceFieldRef(defBox);
         }
      }

      return this.getLocalAndInstanceFieldRef(unit.getDefBoxes());
   }

   private Value handleSpecialInvoke(SpecialInvokeExpr expr) {
      return expr.getBase();
   }

   private ArrayList<Value> setUses(Unit unit) {
      return this.getLocalAndInstanceFieldRef(unit.getUseBoxes());
   }

   private ArrayList<Value> getLocalAndInstanceFieldRef(List<ValueBox> valueboxes) {
      ArrayList<Value> values = new ArrayList();
      Iterator var4 = valueboxes.iterator();

      while(true) {
         Value v;
         do {
            if (!var4.hasNext()) {
               return values;
            }

            ValueBox valuebox = (ValueBox)var4.next();
            v = valuebox.getValue();
         } while(!(v instanceof Local) && !(v instanceof InstanceFieldRef));

         if (!values.contains(v)) {
            values.add(v);
         }
      }
   }
}
