package com.github.javaparser.ast.nodeTypes;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.Statement;
import java.util.Iterator;

public interface NodeWithStatements<N extends Node> {
   NodeList<Statement> getStatements();

   default Statement getStatement(int i) {
      return (Statement)this.getStatements().get(i);
   }

   default N setStatement(int i, Statement statement) {
      this.getStatements().set(i, (Node)statement);
      return (Node)this;
   }

   N setStatements(final NodeList<Statement> statements);

   default N addStatement(Statement statement) {
      this.getStatements().add((Node)statement);
      return (Node)this;
   }

   default N addStatement(int index, final Statement statement) {
      this.getStatements().add(index, (Node)statement);
      return (Node)this;
   }

   default N addStatement(Expression expr) {
      return this.addStatement((Statement)(new ExpressionStmt(expr)));
   }

   default N addStatement(String statement) {
      return this.addStatement(JavaParser.parseStatement(statement));
   }

   default N addStatement(int index, final Expression expr) {
      Statement stmt = new ExpressionStmt(expr);
      return this.addStatement(index, (Statement)stmt);
   }

   default <A extends Statement> A addAndGetStatement(A statement) {
      this.getStatements().add((Node)statement);
      return statement;
   }

   default Statement addAndGetStatement(int index, final Statement statement) {
      this.getStatements().add(index, (Node)statement);
      return statement;
   }

   default ExpressionStmt addAndGetStatement(Expression expr) {
      ExpressionStmt statement = new ExpressionStmt(expr);
      return (ExpressionStmt)this.addAndGetStatement((Statement)statement);
   }

   default ExpressionStmt addAndGetStatement(String statement) {
      return this.addAndGetStatement((Expression)(new NameExpr(statement)));
   }

   default boolean isEmpty() {
      return this.getStatements().isEmpty();
   }

   default N copyStatements(NodeList<Statement> nodeList) {
      Iterator var2 = nodeList.iterator();

      while(var2.hasNext()) {
         Statement n = (Statement)var2.next();
         this.addStatement(n.clone());
      }

      return (Node)this;
   }

   default N copyStatements(NodeWithStatements<?> other) {
      return this.copyStatements(other.getStatements());
   }
}
