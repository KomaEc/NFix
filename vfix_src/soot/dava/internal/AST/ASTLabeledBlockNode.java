package soot.dava.internal.AST;

import java.util.ArrayList;
import java.util.List;
import soot.UnitPrinter;
import soot.dava.internal.SET.SETNodeLabel;
import soot.dava.toolkits.base.AST.analysis.Analysis;

public class ASTLabeledBlockNode extends ASTLabeledNode {
   private List<Object> body;

   public ASTLabeledBlockNode(SETNodeLabel label, List<Object> body) {
      super(label);
      this.body = body;
      this.subBodies.add(body);
   }

   public void replaceBody(List<Object> body) {
      this.body = body;
      this.subBodies = new ArrayList();
      this.subBodies.add(body);
   }

   public int size() {
      return this.body.size();
   }

   public Object clone() {
      return new ASTLabeledBlockNode(this.get_Label(), this.body);
   }

   public void toString(UnitPrinter up) {
      this.label_toString(up);
      up.literal("{");
      up.newline();
      up.incIndent();
      this.body_toString(up, this.body);
      up.decIndent();
      up.literal("} //end ");
      this.label_toString(up);
      up.newline();
   }

   public String toString() {
      StringBuffer b = new StringBuffer();
      b.append(this.label_toString());
      b.append("{");
      b.append("\n");
      b.append(this.body_toString(this.body));
      b.append("} //");
      b.append(this.label_toString());
      b.append("\n");
      return b.toString();
   }

   public void apply(Analysis a) {
      a.caseASTLabeledBlockNode(this);
   }
}
