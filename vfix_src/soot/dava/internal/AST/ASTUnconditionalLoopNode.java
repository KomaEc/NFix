package soot.dava.internal.AST;

import java.util.ArrayList;
import java.util.List;
import soot.UnitPrinter;
import soot.dava.internal.SET.SETNodeLabel;
import soot.dava.toolkits.base.AST.analysis.Analysis;

public class ASTUnconditionalLoopNode extends ASTLabeledNode {
   private List<Object> body;

   public ASTUnconditionalLoopNode(SETNodeLabel label, List<Object> body) {
      super(label);
      this.body = body;
      this.subBodies.add(body);
   }

   public void replaceBody(List<Object> body) {
      this.body = body;
      this.subBodies = new ArrayList();
      this.subBodies.add(body);
   }

   public Object clone() {
      return new ASTUnconditionalLoopNode(this.get_Label(), this.body);
   }

   public void toString(UnitPrinter up) {
      this.label_toString(up);
      up.literal("while");
      up.literal(" ");
      up.literal("(");
      up.literal("true");
      up.literal(")");
      up.newline();
      up.literal("{");
      up.newline();
      up.incIndent();
      this.body_toString(up, this.body);
      up.decIndent();
      up.literal("}");
      up.newline();
   }

   public String toString() {
      StringBuffer b = new StringBuffer();
      b.append(this.label_toString());
      b.append("while (true)");
      b.append("\n");
      b.append("{");
      b.append("\n");
      b.append(this.body_toString(this.body));
      b.append("}");
      b.append("\n");
      return b.toString();
   }

   public void apply(Analysis a) {
      a.caseASTUnconditionalLoopNode(this);
   }
}
