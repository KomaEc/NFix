package soot.dava.internal.AST;

import java.util.ArrayList;
import java.util.List;
import soot.UnitPrinter;
import soot.dava.internal.SET.SETNodeLabel;
import soot.dava.toolkits.base.AST.analysis.Analysis;
import soot.jimple.ConditionExpr;

public class ASTWhileNode extends ASTControlFlowNode {
   private List<Object> body;

   public ASTWhileNode(SETNodeLabel label, ConditionExpr ce, List<Object> body) {
      super(label, ce);
      this.body = body;
      this.subBodies.add(body);
   }

   public ASTWhileNode(SETNodeLabel label, ASTCondition ce, List<Object> body) {
      super(label, ce);
      this.body = body;
      this.subBodies.add(body);
   }

   public void replaceBody(List<Object> body) {
      this.body = body;
      this.subBodies = new ArrayList();
      this.subBodies.add(body);
   }

   public Object clone() {
      return new ASTWhileNode(this.get_Label(), this.get_Condition(), this.body);
   }

   public void toString(UnitPrinter up) {
      this.label_toString(up);
      up.literal("while");
      up.literal(" ");
      up.literal("(");
      this.condition.toString(up);
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
      b.append("while (");
      b.append(this.get_Condition().toString());
      b.append(")");
      b.append("\n");
      b.append("{");
      b.append("\n");
      b.append(this.body_toString(this.body));
      b.append("}");
      b.append("\n");
      return b.toString();
   }

   public void apply(Analysis a) {
      a.caseASTWhileNode(this);
   }
}
