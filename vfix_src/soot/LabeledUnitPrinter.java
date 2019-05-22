package soot;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import soot.jimple.IdentityRef;
import soot.util.Chain;

public abstract class LabeledUnitPrinter extends AbstractUnitPrinter {
   protected Map<Unit, String> labels;
   protected Map<Unit, String> references;
   protected String labelIndent = "     ";

   public LabeledUnitPrinter(Body b) {
      this.createLabelMaps(b);
   }

   public Map<Unit, String> labels() {
      return this.labels;
   }

   public Map<Unit, String> references() {
      return this.references;
   }

   public abstract void literal(String var1);

   public abstract void methodRef(SootMethodRef var1);

   public abstract void fieldRef(SootFieldRef var1);

   public abstract void identityRef(IdentityRef var1);

   public abstract void type(Type var1);

   public void unitRef(Unit u, boolean branchTarget) {
      String oldIndent = this.getIndent();
      String label;
      if (branchTarget) {
         this.setIndent(this.labelIndent);
         this.handleIndent();
         this.setIndent(oldIndent);
         label = (String)this.labels.get(u);
         if (label == null || "<unnamed>".equals(label)) {
            label = "[?= " + u + "]";
         }

         this.output.append(label);
      } else {
         label = (String)this.references.get(u);
         if (this.startOfLine) {
            String newIndent = "(" + label + ")" + this.indent.substring(label.length() + 2);
            this.setIndent(newIndent);
            this.handleIndent();
            this.setIndent(oldIndent);
         } else {
            this.output.append(label);
         }
      }

   }

   private void createLabelMaps(Body body) {
      Chain<Unit> units = body.getUnits();
      this.labels = new HashMap(units.size() * 2 + 1, 0.7F);
      this.references = new HashMap(units.size() * 2 + 1, 0.7F);
      Set<Unit> labelStmts = new HashSet();
      Set<Unit> refStmts = new HashSet();
      Iterator var5 = body.getAllUnitBoxes().iterator();

      while(var5.hasNext()) {
         UnitBox box = (UnitBox)var5.next();
         Unit stmt = box.getUnit();
         if (box.isBranchTarget()) {
            labelStmts.add(stmt);
         } else {
            refStmts.add(stmt);
         }
      }

      int maxDigits = 1 + (int)Math.log10((double)labelStmts.size());
      String formatString = "label%0" + maxDigits + "d";
      int labelCount = 0;
      int refCount = 0;
      Iterator var9 = units.iterator();

      while(var9.hasNext()) {
         Unit s = (Unit)var9.next();
         if (labelStmts.contains(s)) {
            Map var10000 = this.labels;
            Object[] var10003 = new Object[1];
            ++labelCount;
            var10003[0] = labelCount;
            var10000.put(s, String.format(formatString, var10003));
         }

         if (refStmts.contains(s)) {
            this.references.put(s, Integer.toString(refCount++));
         }
      }

   }
}
