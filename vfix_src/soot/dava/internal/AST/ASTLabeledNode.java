package soot.dava.internal.AST;

import soot.UnitPrinter;
import soot.dava.internal.SET.SETNodeLabel;
import soot.dava.toolkits.base.AST.ASTAnalysis;

public abstract class ASTLabeledNode extends ASTNode {
   private SETNodeLabel label;

   public ASTLabeledNode(SETNodeLabel label) {
      this.set_Label(label);
   }

   public SETNodeLabel get_Label() {
      return this.label;
   }

   public void set_Label(SETNodeLabel label) {
      this.label = label;
   }

   public void perform_Analysis(ASTAnalysis a) {
      this.perform_AnalysisOnSubBodies(a);
   }

   public void label_toString(UnitPrinter up) {
      if (this.label.toString() != null) {
         up.literal(this.label.toString());
         up.literal(":");
         up.newline();
      }

   }

   public String label_toString() {
      if (this.label.toString() == null) {
         return new String();
      } else {
         StringBuffer b = new StringBuffer();
         b.append(this.label.toString());
         b.append(":");
         b.append("\n");
         return b.toString();
      }
   }
}
