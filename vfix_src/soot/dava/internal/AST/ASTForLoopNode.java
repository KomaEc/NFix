package soot.dava.internal.AST;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import soot.UnitPrinter;
import soot.dava.internal.SET.SETNodeLabel;
import soot.dava.internal.asg.AugmentedStmt;
import soot.dava.toolkits.base.AST.analysis.Analysis;
import soot.jimple.Stmt;

public class ASTForLoopNode extends ASTControlFlowNode {
   private List<AugmentedStmt> init;
   private List<AugmentedStmt> update;
   private List<Object> body;

   public ASTForLoopNode(SETNodeLabel label, List<AugmentedStmt> init, ASTCondition condition, List<AugmentedStmt> update, List<Object> body) {
      super(label, condition);
      this.body = body;
      this.init = init;
      this.update = update;
      this.subBodies.add(body);
   }

   public List<AugmentedStmt> getInit() {
      return this.init;
   }

   public List<AugmentedStmt> getUpdate() {
      return this.update;
   }

   public void replaceBody(List<Object> body) {
      this.body = body;
      this.subBodies = new ArrayList();
      this.subBodies.add(body);
   }

   public Object clone() {
      return new ASTForLoopNode(this.get_Label(), this.init, this.get_Condition(), this.update, this.body);
   }

   public void toString(UnitPrinter up) {
      this.label_toString(up);
      up.literal("for");
      up.literal(" ");
      up.literal("(");
      Iterator it = this.init.iterator();

      AugmentedStmt as;
      Stmt u;
      while(it.hasNext()) {
         as = (AugmentedStmt)it.next();
         u = as.get_Stmt();
         u.toString(up);
         if (it.hasNext()) {
            up.literal(" , ");
         }
      }

      up.literal("; ");
      this.condition.toString(up);
      up.literal("; ");
      it = this.update.iterator();

      while(it.hasNext()) {
         as = (AugmentedStmt)it.next();
         u = as.get_Stmt();
         u.toString(up);
         if (it.hasNext()) {
            up.literal(" , ");
         }
      }

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
      b.append("for (");
      Iterator it = this.init.iterator();

      while(it.hasNext()) {
         b.append(((AugmentedStmt)it.next()).get_Stmt().toString());
         if (it.hasNext()) {
            b.append(" , ");
         }
      }

      b.append("; ");
      b.append(this.get_Condition().toString());
      b.append("; ");
      it = this.update.iterator();

      while(it.hasNext()) {
         b.append(((AugmentedStmt)it.next()).get_Stmt().toString());
         if (it.hasNext()) {
            b.append(" , ");
         }
      }

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
      a.caseASTForLoopNode(this);
   }
}
