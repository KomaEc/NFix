package org.codehaus.groovy.ast.stmt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.codehaus.groovy.ast.GroovyCodeVisitor;
import org.codehaus.groovy.ast.VariableScope;

public class BlockStatement extends Statement {
   private List<Statement> statements;
   private VariableScope scope;

   public BlockStatement() {
      this((List)(new ArrayList()), new VariableScope());
   }

   public BlockStatement(List<Statement> statements, VariableScope scope) {
      this.statements = new ArrayList();
      this.statements = statements;
      this.scope = scope;
   }

   public BlockStatement(Statement[] statements, VariableScope scope) {
      this.statements = new ArrayList();
      this.statements.addAll(Arrays.asList(statements));
      this.scope = scope;
   }

   public void visit(GroovyCodeVisitor visitor) {
      visitor.visitBlockStatement(this);
   }

   public List<Statement> getStatements() {
      return this.statements;
   }

   public void addStatement(Statement statement) {
      this.statements.add(statement);
   }

   public void addStatements(List<Statement> listOfStatements) {
      this.statements.addAll(listOfStatements);
   }

   public String toString() {
      return super.toString() + this.statements;
   }

   public String getText() {
      StringBuffer buffer = new StringBuffer("{ ");
      boolean first = true;

      Statement statement;
      for(Iterator i$ = this.statements.iterator(); i$.hasNext(); buffer.append(statement.getText())) {
         statement = (Statement)i$.next();
         if (first) {
            first = false;
         } else {
            buffer.append("; ");
         }
      }

      buffer.append(" }");
      return buffer.toString();
   }

   public boolean isEmpty() {
      return this.statements.isEmpty();
   }

   public void setVariableScope(VariableScope scope) {
      this.scope = scope;
   }

   public VariableScope getVariableScope() {
      return this.scope;
   }
}
