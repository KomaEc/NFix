package org.codehaus.groovy.ast.stmt;

import java.util.ArrayList;
import java.util.List;
import org.codehaus.groovy.ast.GroovyCodeVisitor;

public class TryCatchStatement extends Statement {
   private Statement tryStatement;
   private List<CatchStatement> catchStatements = new ArrayList();
   private Statement finallyStatement;

   public TryCatchStatement(Statement tryStatement, Statement finallyStatement) {
      this.tryStatement = tryStatement;
      this.finallyStatement = finallyStatement;
   }

   public void visit(GroovyCodeVisitor visitor) {
      visitor.visitTryCatchFinally(this);
   }

   public List<CatchStatement> getCatchStatements() {
      return this.catchStatements;
   }

   public Statement getFinallyStatement() {
      return this.finallyStatement;
   }

   public Statement getTryStatement() {
      return this.tryStatement;
   }

   public void addCatch(CatchStatement catchStatement) {
      this.catchStatements.add(catchStatement);
   }

   public CatchStatement getCatchStatement(int idx) {
      return idx >= 0 && idx < this.catchStatements.size() ? (CatchStatement)this.catchStatements.get(idx) : null;
   }

   public void setTryStatement(Statement tryStatement) {
      this.tryStatement = tryStatement;
   }

   public void setCatchStatement(int idx, CatchStatement catchStatement) {
      this.catchStatements.set(idx, catchStatement);
   }

   public void setFinallyStatement(Statement finallyStatement) {
      this.finallyStatement = finallyStatement;
   }
}
