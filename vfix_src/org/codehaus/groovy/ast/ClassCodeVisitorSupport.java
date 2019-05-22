package org.codehaus.groovy.ast;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.VariableExpression;
import org.codehaus.groovy.ast.stmt.AssertStatement;
import org.codehaus.groovy.ast.stmt.BlockStatement;
import org.codehaus.groovy.ast.stmt.BreakStatement;
import org.codehaus.groovy.ast.stmt.CaseStatement;
import org.codehaus.groovy.ast.stmt.CatchStatement;
import org.codehaus.groovy.ast.stmt.ContinueStatement;
import org.codehaus.groovy.ast.stmt.DoWhileStatement;
import org.codehaus.groovy.ast.stmt.ExpressionStatement;
import org.codehaus.groovy.ast.stmt.ForStatement;
import org.codehaus.groovy.ast.stmt.IfStatement;
import org.codehaus.groovy.ast.stmt.ReturnStatement;
import org.codehaus.groovy.ast.stmt.Statement;
import org.codehaus.groovy.ast.stmt.SwitchStatement;
import org.codehaus.groovy.ast.stmt.SynchronizedStatement;
import org.codehaus.groovy.ast.stmt.ThrowStatement;
import org.codehaus.groovy.ast.stmt.TryCatchStatement;
import org.codehaus.groovy.ast.stmt.WhileStatement;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.control.messages.SyntaxErrorMessage;
import org.codehaus.groovy.syntax.SyntaxException;

public abstract class ClassCodeVisitorSupport extends CodeVisitorSupport implements GroovyClassVisitor {
   public void visitClass(ClassNode node) {
      this.visitAnnotations(node);
      this.visitPackage(node.getPackage());
      this.visitImports(node.getModule());
      node.visitContents(this);
      this.visitObjectInitializerStatements(node);
   }

   protected void visitObjectInitializerStatements(ClassNode node) {
      Iterator i$ = node.getObjectInitializerStatements().iterator();

      while(i$.hasNext()) {
         Statement element = (Statement)i$.next();
         element.visit(this);
      }

   }

   public void visitPackage(PackageNode node) {
      if (node != null) {
         this.visitAnnotations(node);
         node.visit(this);
      }

   }

   public void visitImports(ModuleNode node) {
      if (node != null) {
         Iterator i$ = node.getImports().iterator();

         ImportNode importStaticStarNode;
         while(i$.hasNext()) {
            importStaticStarNode = (ImportNode)i$.next();
            this.visitAnnotations(importStaticStarNode);
            importStaticStarNode.visit(this);
         }

         i$ = node.getStarImports().iterator();

         while(i$.hasNext()) {
            importStaticStarNode = (ImportNode)i$.next();
            this.visitAnnotations(importStaticStarNode);
            importStaticStarNode.visit(this);
         }

         i$ = node.getStaticImports().values().iterator();

         while(i$.hasNext()) {
            importStaticStarNode = (ImportNode)i$.next();
            this.visitAnnotations(importStaticStarNode);
            importStaticStarNode.visit(this);
         }

         i$ = node.getStaticStarImports().values().iterator();

         while(i$.hasNext()) {
            importStaticStarNode = (ImportNode)i$.next();
            this.visitAnnotations(importStaticStarNode);
            importStaticStarNode.visit(this);
         }
      }

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
               ((Expression)member.getValue()).visit(this);
            }
         }
      }
   }

   protected void visitClassCodeContainer(Statement code) {
      if (code != null) {
         code.visit(this);
      }

   }

   public void visitVariableExpression(VariableExpression expression) {
      this.visitAnnotations(expression);
      super.visitVariableExpression(expression);
   }

   protected void visitConstructorOrMethod(MethodNode node, boolean isConstructor) {
      this.visitAnnotations(node);
      this.visitClassCodeContainer(node.getCode());
      Parameter[] arr$ = node.getParameters();
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Parameter param = arr$[i$];
         this.visitAnnotations(param);
      }

   }

   public void visitConstructor(ConstructorNode node) {
      this.visitConstructorOrMethod(node, true);
   }

   public void visitMethod(MethodNode node) {
      this.visitConstructorOrMethod(node, false);
   }

   public void visitField(FieldNode node) {
      this.visitAnnotations(node);
      Expression init = node.getInitialExpression();
      if (init != null) {
         init.visit(this);
      }

   }

   public void visitProperty(PropertyNode node) {
      this.visitAnnotations(node);
      Statement statement = node.getGetterBlock();
      this.visitClassCodeContainer(statement);
      statement = node.getSetterBlock();
      this.visitClassCodeContainer(statement);
      Expression init = node.getInitialExpression();
      if (init != null) {
         init.visit(this);
      }

   }

   protected void addError(String msg, ASTNode expr) {
      int line = expr.getLineNumber();
      int col = expr.getColumnNumber();
      SourceUnit source = this.getSourceUnit();
      source.getErrorCollector().addErrorAndContinue(new SyntaxErrorMessage(new SyntaxException(msg + '\n', line, col), source));
   }

   protected abstract SourceUnit getSourceUnit();

   protected void visitStatement(Statement statement) {
   }

   public void visitAssertStatement(AssertStatement statement) {
      this.visitStatement(statement);
      super.visitAssertStatement(statement);
   }

   public void visitBlockStatement(BlockStatement block) {
      this.visitStatement(block);
      super.visitBlockStatement(block);
   }

   public void visitBreakStatement(BreakStatement statement) {
      this.visitStatement(statement);
      super.visitBreakStatement(statement);
   }

   public void visitCaseStatement(CaseStatement statement) {
      this.visitStatement(statement);
      super.visitCaseStatement(statement);
   }

   public void visitCatchStatement(CatchStatement statement) {
      this.visitStatement(statement);
      super.visitCatchStatement(statement);
   }

   public void visitContinueStatement(ContinueStatement statement) {
      this.visitStatement(statement);
      super.visitContinueStatement(statement);
   }

   public void visitDoWhileLoop(DoWhileStatement loop) {
      this.visitStatement(loop);
      super.visitDoWhileLoop(loop);
   }

   public void visitExpressionStatement(ExpressionStatement statement) {
      this.visitStatement(statement);
      super.visitExpressionStatement(statement);
   }

   public void visitForLoop(ForStatement forLoop) {
      this.visitStatement(forLoop);
      super.visitForLoop(forLoop);
   }

   public void visitIfElse(IfStatement ifElse) {
      this.visitStatement(ifElse);
      super.visitIfElse(ifElse);
   }

   public void visitReturnStatement(ReturnStatement statement) {
      this.visitStatement(statement);
      super.visitReturnStatement(statement);
   }

   public void visitSwitch(SwitchStatement statement) {
      this.visitStatement(statement);
      super.visitSwitch(statement);
   }

   public void visitSynchronizedStatement(SynchronizedStatement statement) {
      this.visitStatement(statement);
      super.visitSynchronizedStatement(statement);
   }

   public void visitThrowStatement(ThrowStatement statement) {
      this.visitStatement(statement);
      super.visitThrowStatement(statement);
   }

   public void visitTryCatchFinally(TryCatchStatement statement) {
      this.visitStatement(statement);
      super.visitTryCatchFinally(statement);
   }

   public void visitWhileLoop(WhileStatement loop) {
      this.visitStatement(loop);
      super.visitWhileLoop(loop);
   }
}
