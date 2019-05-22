package soot.dava.internal.AST;

import java.util.ArrayList;
import java.util.List;
import soot.Local;
import soot.UnitPrinter;
import soot.Value;
import soot.ValueBox;
import soot.dava.internal.SET.SETNodeLabel;
import soot.dava.toolkits.base.AST.analysis.Analysis;
import soot.jimple.Jimple;

public class ASTSynchronizedBlockNode extends ASTLabeledNode {
   private List<Object> body;
   private ValueBox localBox;

   public ASTSynchronizedBlockNode(SETNodeLabel label, List<Object> body, Value local) {
      super(label);
      this.body = body;
      this.localBox = Jimple.v().newLocalBox(local);
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

   public Local getLocal() {
      return (Local)this.localBox.getValue();
   }

   public void setLocal(Local local) {
      this.localBox = Jimple.v().newLocalBox(local);
   }

   public Object clone() {
      return new ASTSynchronizedBlockNode(this.get_Label(), this.body, this.getLocal());
   }

   public void toString(UnitPrinter up) {
      this.label_toString(up);
      up.literal("synchronized (");
      this.localBox.toString(up);
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
      b.append("synchronized (");
      b.append(this.getLocal());
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
      a.caseASTSynchronizedBlockNode(this);
   }
}
