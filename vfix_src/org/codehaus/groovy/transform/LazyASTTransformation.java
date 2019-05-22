package org.codehaus.groovy.transform;

import groovyjarjarasm.asm.Opcodes;
import java.lang.ref.SoftReference;
import java.util.Arrays;
import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.AnnotatedNode;
import org.codehaus.groovy.ast.AnnotationNode;
import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.FieldNode;
import org.codehaus.groovy.ast.InnerClassNode;
import org.codehaus.groovy.ast.Parameter;
import org.codehaus.groovy.ast.expr.ArgumentListExpression;
import org.codehaus.groovy.ast.expr.BinaryExpression;
import org.codehaus.groovy.ast.expr.BooleanExpression;
import org.codehaus.groovy.ast.expr.ClassExpression;
import org.codehaus.groovy.ast.expr.ConstantExpression;
import org.codehaus.groovy.ast.expr.ConstructorCallExpression;
import org.codehaus.groovy.ast.expr.DeclarationExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.ast.expr.PropertyExpression;
import org.codehaus.groovy.ast.expr.VariableExpression;
import org.codehaus.groovy.ast.stmt.BlockStatement;
import org.codehaus.groovy.ast.stmt.ExpressionStatement;
import org.codehaus.groovy.ast.stmt.IfStatement;
import org.codehaus.groovy.ast.stmt.ReturnStatement;
import org.codehaus.groovy.ast.stmt.Statement;
import org.codehaus.groovy.ast.stmt.SynchronizedStatement;
import org.codehaus.groovy.control.CompilePhase;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.runtime.MetaClassHelper;
import org.codehaus.groovy.syntax.Token;

@GroovyASTTransformation(
   phase = CompilePhase.CANONICALIZATION
)
public class LazyASTTransformation implements ASTTransformation, Opcodes {
   private static final ClassNode SOFT_REF = ClassHelper.make(SoftReference.class);
   private static final Expression NULL_EXPR;
   private static final ClassNode OBJECT_TYPE;
   private static final Token ASSIGN;
   private static final Token COMPARE_NOT_EQUAL;

   public void visit(ASTNode[] nodes, SourceUnit source) {
      if (nodes.length == 2 && nodes[0] instanceof AnnotationNode && nodes[1] instanceof AnnotatedNode) {
         AnnotatedNode parent = (AnnotatedNode)nodes[1];
         AnnotationNode node = (AnnotationNode)nodes[0];
         if (parent instanceof FieldNode) {
            FieldNode fieldNode = (FieldNode)parent;
            Expression member = node.getMember("soft");
            Expression init = this.getInitExpr(fieldNode);
            fieldNode.rename("$" + fieldNode.getName());
            fieldNode.setModifiers(2 | fieldNode.getModifiers() & -6);
            if (member instanceof ConstantExpression && ((ConstantExpression)member).getValue().equals(true)) {
               this.createSoft(fieldNode, init);
            } else {
               this.create(fieldNode, init);
               if (ClassHelper.isPrimitiveType(fieldNode.getType())) {
                  fieldNode.setType(ClassHelper.getWrapper(fieldNode.getType()));
               }
            }
         }

      } else {
         throw new RuntimeException("Internal error: expecting [AnnotationNode, AnnotatedNode] but got: " + Arrays.asList(nodes));
      }
   }

   private void create(FieldNode fieldNode, Expression initExpr) {
      BlockStatement body = new BlockStatement();
      if (fieldNode.isStatic()) {
         this.addHolderClassIdiomBody(body, fieldNode, initExpr);
      } else if (this.isVolatile(fieldNode)) {
         this.addNonThreadSafeBody(body, fieldNode, initExpr);
      } else {
         this.addDoubleCheckedLockingBody(body, fieldNode, initExpr);
      }

      this.addMethod(fieldNode, body, fieldNode.getType());
   }

   private void addHolderClassIdiomBody(BlockStatement body, FieldNode fieldNode, Expression initExpr) {
      ClassNode declaringClass = fieldNode.getDeclaringClass();
      ClassNode fieldType = fieldNode.getType();
      int visibility = true;
      String fullName = declaringClass.getName() + "$" + fieldType.getNameWithoutPackage() + "Holder_" + fieldNode.getName().substring(1);
      InnerClassNode holderClass = new InnerClassNode(declaringClass, fullName, 10, OBJECT_TYPE);
      String innerFieldName = "INSTANCE";
      holderClass.addField("INSTANCE", 26, fieldType, initExpr);
      Expression innerField = new PropertyExpression(new ClassExpression(holderClass), "INSTANCE");
      declaringClass.getModule().addClass(holderClass);
      body.addStatement(new ReturnStatement(innerField));
   }

   private void addDoubleCheckedLockingBody(BlockStatement body, FieldNode fieldNode, Expression initExpr) {
      Expression fieldExpr = new VariableExpression(fieldNode);
      VariableExpression localVar = new VariableExpression(fieldNode.getName() + "_local", fieldNode.getType());
      body.addStatement(new ExpressionStatement(new DeclarationExpression(localVar, ASSIGN, fieldExpr)));
      body.addStatement(new IfStatement(new BooleanExpression(new BinaryExpression(localVar, COMPARE_NOT_EQUAL, NULL_EXPR)), new ReturnStatement(localVar), new SynchronizedStatement(this.synchTarget(fieldNode), new IfStatement(new BooleanExpression(new BinaryExpression(fieldExpr, COMPARE_NOT_EQUAL, NULL_EXPR)), new ReturnStatement(fieldExpr), new ReturnStatement(new BinaryExpression(fieldExpr, ASSIGN, initExpr))))));
   }

   private void addNonThreadSafeBody(BlockStatement body, FieldNode fieldNode, Expression initExpr) {
      Expression fieldExpr = new VariableExpression(fieldNode);
      body.addStatement(new IfStatement(new BooleanExpression(new BinaryExpression(fieldExpr, COMPARE_NOT_EQUAL, NULL_EXPR)), new ExpressionStatement(fieldExpr), new ExpressionStatement(new BinaryExpression(fieldExpr, ASSIGN, initExpr))));
   }

   private void addMethod(FieldNode fieldNode, BlockStatement body, ClassNode type) {
      int visibility = 1;
      if (fieldNode.isStatic()) {
         visibility |= 8;
      }

      String name = "get" + MetaClassHelper.capitalize(fieldNode.getName().substring(1));
      fieldNode.getDeclaringClass().addMethod(name, visibility, type, Parameter.EMPTY_ARRAY, ClassNode.EMPTY_ARRAY, body);
   }

   private void createSoft(FieldNode fieldNode, Expression initExpr) {
      ClassNode type = fieldNode.getType();
      fieldNode.setType(SOFT_REF);
      this.createSoftGetter(fieldNode, initExpr, type);
      this.createSoftSetter(fieldNode, type);
   }

   private void createSoftGetter(FieldNode fieldNode, Expression initExpr, ClassNode type) {
      BlockStatement body = new BlockStatement();
      Expression fieldExpr = new VariableExpression(fieldNode);
      Expression resExpr = new VariableExpression("res", type);
      MethodCallExpression callExpression = new MethodCallExpression(new VariableExpression(fieldNode), "get", new ArgumentListExpression());
      callExpression.setSafe(true);
      body.addStatement(new ExpressionStatement(new DeclarationExpression(resExpr, ASSIGN, callExpression)));
      BlockStatement elseBlock = new BlockStatement();
      elseBlock.addStatement(new ExpressionStatement(new BinaryExpression(resExpr, ASSIGN, initExpr)));
      elseBlock.addStatement(new ExpressionStatement(new BinaryExpression(fieldExpr, ASSIGN, new ConstructorCallExpression(SOFT_REF, resExpr))));
      elseBlock.addStatement(new ExpressionStatement(resExpr));
      Statement mainIf = new IfStatement(new BooleanExpression(new BinaryExpression(resExpr, COMPARE_NOT_EQUAL, NULL_EXPR)), new ExpressionStatement(resExpr), elseBlock);
      if (this.isVolatile(fieldNode)) {
         body.addStatement(mainIf);
      } else {
         body.addStatement(new IfStatement(new BooleanExpression(new BinaryExpression(resExpr, COMPARE_NOT_EQUAL, NULL_EXPR)), new ExpressionStatement(resExpr), new SynchronizedStatement(this.synchTarget(fieldNode), mainIf)));
      }

      this.addMethod(fieldNode, body, type);
   }

   private void createSoftSetter(FieldNode fieldNode, ClassNode type) {
      BlockStatement body = new BlockStatement();
      Expression fieldExpr = new VariableExpression(fieldNode);
      String name = "set" + MetaClassHelper.capitalize(fieldNode.getName().substring(1));
      Parameter parameter = new Parameter(type, "value");
      Expression paramExpr = new VariableExpression(parameter);
      body.addStatement(new IfStatement(new BooleanExpression(new BinaryExpression(paramExpr, COMPARE_NOT_EQUAL, NULL_EXPR)), new ExpressionStatement(new BinaryExpression(fieldExpr, ASSIGN, new ConstructorCallExpression(SOFT_REF, paramExpr))), new ExpressionStatement(new BinaryExpression(fieldExpr, ASSIGN, NULL_EXPR))));
      int visibility = 1;
      if (fieldNode.isStatic()) {
         visibility |= 8;
      }

      fieldNode.getDeclaringClass().addMethod(name, visibility, ClassHelper.VOID_TYPE, new Parameter[]{parameter}, ClassNode.EMPTY_ARRAY, body);
   }

   private Expression synchTarget(FieldNode fieldNode) {
      return (Expression)(fieldNode.isStatic() ? new ClassExpression(fieldNode.getDeclaringClass()) : VariableExpression.THIS_EXPRESSION);
   }

   private boolean isVolatile(FieldNode fieldNode) {
      return (fieldNode.getModifiers() & 64) == 0;
   }

   private Expression getInitExpr(FieldNode fieldNode) {
      Expression initExpr = fieldNode.getInitialValueExpression();
      fieldNode.setInitialValueExpression((Expression)null);
      if (initExpr == null) {
         initExpr = new ConstructorCallExpression(fieldNode.getType(), new ArgumentListExpression());
      }

      return (Expression)initExpr;
   }

   static {
      NULL_EXPR = ConstantExpression.NULL;
      OBJECT_TYPE = new ClassNode(Object.class);
      ASSIGN = Token.newSymbol("=", -1, -1);
      COMPARE_NOT_EQUAL = Token.newSymbol("!=", -1, -1);
   }
}
