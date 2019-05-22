package org.codehaus.groovy.ast;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import org.codehaus.groovy.ast.expr.BooleanExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.ExpressionTransformer;
import org.codehaus.groovy.ast.stmt.AssertStatement;
import org.codehaus.groovy.ast.stmt.CaseStatement;
import org.codehaus.groovy.ast.stmt.DoWhileStatement;
import org.codehaus.groovy.ast.stmt.ExpressionStatement;
import org.codehaus.groovy.ast.stmt.ForStatement;
import org.codehaus.groovy.ast.stmt.IfStatement;
import org.codehaus.groovy.ast.stmt.ReturnStatement;
import org.codehaus.groovy.ast.stmt.Statement;
import org.codehaus.groovy.ast.stmt.SwitchStatement;
import org.codehaus.groovy.ast.stmt.SynchronizedStatement;
import org.codehaus.groovy.ast.stmt.ThrowStatement;
import org.codehaus.groovy.ast.stmt.WhileStatement;

public abstract class ClassCodeExpressionTransformer extends ClassCodeVisitorSupport implements ExpressionTransformer {
   protected void visitConstructorOrMethod(MethodNode node, boolean isConstructor) {
      Parameter[] arr$ = node.getParameters();
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Parameter p = arr$[i$];
         if (p.hasInitialExpression()) {
            Expression init = p.getInitialExpression();
            p.setInitialExpression(this.transform(init));
         }
      }

      super.visitConstructorOrMethod(node, isConstructor);
   }

   public void visitSwitch(SwitchStatement statement) {
      Expression exp = statement.getExpression();
      statement.setExpression(this.transform(exp));
      Iterator i$ = statement.getCaseStatements().iterator();

      while(i$.hasNext()) {
         CaseStatement caseStatement = (CaseStatement)i$.next();
         caseStatement.visit(this);
      }

      statement.getDefaultStatement().visit(this);
   }

   public void visitField(FieldNode node) {
      this.visitAnnotations(node);
      Expression init = node.getInitialExpression();
      node.setInitialValueExpression(this.transform(init));
   }

   public void visitProperty(PropertyNode node) {
      this.visitAnnotations(node);
      Statement statement = node.getGetterBlock();
      this.visitClassCodeContainer(statement);
      statement = node.getSetterBlock();
      this.visitClassCodeContainer(statement);
   }

   public void visitIfElse(IfStatement ifElse) {
      ifElse.setBooleanExpression((BooleanExpression)((BooleanExpression)this.transform(ifElse.getBooleanExpression())));
      ifElse.getIfBlock().visit(this);
      ifElse.getElseBlock().visit(this);
   }

   public Expression transform(Expression exp) {
      return exp == null ? null : exp.transformExpression(this);
   }

   public void visitAnnotations(AnnotatedNode node) {
      List<AnnotationNode> annotations = node.getAnnotations();
      if (!annotations.isEmpty()) {
         Iterator i$ = annotations.iterator();

         while(true) {
            AnnotationNode an;
            do {
               if (!i$.hasNext()) {
                  return;
               }

               an = (AnnotationNode)i$.next();
            } while(an.isBuiltIn());

            Iterator i$ = an.getMembers().entrySet().iterator();

            while(i$.hasNext()) {
               Entry<String, Expression> member = (Entry)i$.next();
               member.setValue(this.transform((Expression)member.getValue()));
            }
         }
      }
   }

   public void visitReturnStatement(ReturnStatement statement) {
      statement.setExpression(this.transform(statement.getExpression()));
   }

   public void visitAssertStatement(AssertStatement as) {
      as.setBooleanExpression((BooleanExpression)((BooleanExpression)this.transform(as.getBooleanExpression())));
      as.setMessageExpression(this.transform(as.getMessageExpression()));
   }

   public void visitCaseStatement(CaseStatement statement) {
      statement.setExpression(this.transform(statement.getExpression()));
      statement.getCode().visit(this);
   }

   public void visitDoWhileLoop(DoWhileStatement loop) {
      loop.setBooleanExpression((BooleanExpression)((BooleanExpression)this.transform(loop.getBooleanExpression())));
      super.visitDoWhileLoop(loop);
   }

   public void visitForLoop(ForStatement forLoop) {
      forLoop.setCollectionExpression(this.transform(forLoop.getCollectionExpression()));
      super.visitForLoop(forLoop);
   }

   public void visitSynchronizedStatement(SynchronizedStatement sync) {
      sync.setExpression(this.transform(sync.getExpression()));
      super.visitSynchronizedStatement(sync);
   }

   public void visitThrowStatement(ThrowStatement ts) {
      ts.setExpression(this.transform(ts.getExpression()));
   }

   public void visitWhileLoop(WhileStatement loop) {
      loop.setBooleanExpression((BooleanExpression)this.transform(loop.getBooleanExpression()));
      super.visitWhileLoop(loop);
   }

   public void visitExpressionStatement(ExpressionStatement es) {
      es.setExpression(this.transform(es.getExpression()));
   }
}
