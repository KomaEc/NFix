package org.codehaus.groovy.ast.stmt;

import java.util.ArrayList;
import java.util.List;
import org.codehaus.groovy.ast.GroovyCodeVisitor;
import org.codehaus.groovy.ast.expr.Expression;

public class SwitchStatement extends Statement {
   private Expression expression;
   private List<CaseStatement> caseStatements;
   private Statement defaultStatement;

   public SwitchStatement(Expression expression) {
      this(expression, EmptyStatement.INSTANCE);
   }

   public SwitchStatement(Expression expression, Statement defaultStatement) {
      this.caseStatements = new ArrayList();
      this.expression = expression;
      this.defaultStatement = defaultStatement;
   }

   public SwitchStatement(Expression expression, List<CaseStatement> caseStatements, Statement defaultStatement) {
      this.caseStatements = new ArrayList();
      this.expression = expression;
      this.caseStatements = caseStatements;
      this.defaultStatement = defaultStatement;
   }

   public void visit(GroovyCodeVisitor visitor) {
      visitor.visitSwitch(this);
   }

   public List<CaseStatement> getCaseStatements() {
      return this.caseStatements;
   }

   public Expression getExpression() {
      return this.expression;
   }

   public void setExpression(Expression e) {
      this.expression = e;
   }

   public Statement getDefaultStatement() {
      return this.defaultStatement;
   }

   public void setDefaultStatement(Statement defaultStatement) {
      this.defaultStatement = defaultStatement;
   }

   public void addCase(CaseStatement caseStatement) {
      this.caseStatements.add(caseStatement);
   }

   public CaseStatement getCaseStatement(int idx) {
      return idx >= 0 && idx < this.caseStatements.size() ? (CaseStatement)this.caseStatements.get(idx) : null;
   }
}
