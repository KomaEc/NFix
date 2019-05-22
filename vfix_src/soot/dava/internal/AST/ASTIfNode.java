package soot.dava.internal.AST;

import java.util.ArrayList;
import java.util.List;
import soot.UnitPrinter;
import soot.dava.internal.SET.SETNodeLabel;
import soot.dava.toolkits.base.AST.analysis.Analysis;
import soot.jimple.ConditionExpr;

public class ASTIfNode extends ASTControlFlowNode {
   private List<Object> body;

   public ASTIfNode(SETNodeLabel label, ConditionExpr condition, List<Object> body) {
      super(label, condition);
      this.body = body;
      this.subBodies.add(body);
   }

   public ASTIfNode(SETNodeLabel label, ASTCondition condition, List<Object> body) {
      super(label, condition);
      this.body = body;
      this.subBodies.add(body);
   }

   public List<Object> getIfBody() {
      return this.body;
   }

   public Object clone() {
      return new ASTIfNode(this.get_Label(), this.get_Condition(), this.body);
   }

   public void replace(SETNodeLabel label, ASTCondition condition, List<Object> body) {
      this.body = body;
      this.subBodies = new ArrayList();
      this.subBodies.add(body);
      this.set_Condition(condition);
      this.set_Label(label);
   }

   public void replaceBody(List<Object> body) {
      this.body = body;
      this.subBodies = new ArrayList();
      this.subBodies.add(body);
   }

   public void toString(UnitPrinter up) {
      this.label_toString(up);
      up.literal("if");
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
      b.append("if (");
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
      a.caseASTIfNode(this);
   }
}
