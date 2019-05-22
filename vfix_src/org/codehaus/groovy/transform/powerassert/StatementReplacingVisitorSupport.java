package org.codehaus.groovy.transform.powerassert;

import java.util.List;
import java.util.ListIterator;
import org.codehaus.groovy.ast.ClassCodeVisitorSupport;
import org.codehaus.groovy.ast.stmt.BlockStatement;
import org.codehaus.groovy.ast.stmt.CaseStatement;
import org.codehaus.groovy.ast.stmt.CatchStatement;
import org.codehaus.groovy.ast.stmt.DoWhileStatement;
import org.codehaus.groovy.ast.stmt.ForStatement;
import org.codehaus.groovy.ast.stmt.IfStatement;
import org.codehaus.groovy.ast.stmt.Statement;
import org.codehaus.groovy.ast.stmt.SwitchStatement;
import org.codehaus.groovy.ast.stmt.SynchronizedStatement;
import org.codehaus.groovy.ast.stmt.TryCatchStatement;
import org.codehaus.groovy.ast.stmt.WhileStatement;

public abstract class StatementReplacingVisitorSupport extends ClassCodeVisitorSupport {
   private Statement replacement;

   public Statement replace(Statement stat) {
      this.replacement = null;
      stat.visit(this);
      Statement result = this.replacement == null ? stat : this.replacement;
      this.replacement = null;
      return result;
   }

   protected <T extends Statement> void replaceAll(List<T> stats) {
      ListIterator iter = stats.listIterator();

      while(iter.hasNext()) {
         iter.set(this.replace((Statement)iter.next()));
      }

   }

   protected void replaceVisitedStatementWith(Statement other) {
      this.replacement = other;
   }

   public void visitBlockStatement(BlockStatement stat) {
      this.replaceAll(stat.getStatements());
   }

   public void visitForLoop(ForStatement stat) {
      stat.getCollectionExpression().visit(this);
      stat.setLoopBlock(this.replace(stat.getLoopBlock()));
   }

   public void visitWhileLoop(WhileStatement stat) {
      stat.getBooleanExpression().visit(this);
      stat.setLoopBlock(this.replace(stat.getLoopBlock()));
   }

   public void visitDoWhileLoop(DoWhileStatement stat) {
      stat.getBooleanExpression().visit(this);
      stat.setLoopBlock(this.replace(stat.getLoopBlock()));
   }

   public void visitIfElse(IfStatement stat) {
      stat.getBooleanExpression().visit(this);
      stat.setIfBlock(this.replace(stat.getIfBlock()));
      stat.setElseBlock(this.replace(stat.getElseBlock()));
   }

   public void visitTryCatchFinally(TryCatchStatement stat) {
      stat.setTryStatement(this.replace(stat.getTryStatement()));
      this.replaceAll(stat.getCatchStatements());
      stat.setFinallyStatement(this.replace(stat.getFinallyStatement()));
   }

   public void visitSwitch(SwitchStatement stat) {
      stat.getExpression().visit(this);
      this.replaceAll(stat.getCaseStatements());
      stat.setDefaultStatement(this.replace(stat.getDefaultStatement()));
   }

   public void visitCaseStatement(CaseStatement stat) {
      stat.getExpression().visit(this);
      stat.setCode(this.replace(stat.getCode()));
   }

   public void visitSynchronizedStatement(SynchronizedStatement stat) {
      stat.getExpression().visit(this);
      stat.setCode(this.replace(stat.getCode()));
   }

   public void visitCatchStatement(CatchStatement stat) {
      stat.setCode(this.replace(stat.getCode()));
   }
}
