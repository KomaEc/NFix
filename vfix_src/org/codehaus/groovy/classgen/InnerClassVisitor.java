package org.codehaus.groovy.classgen;

import groovyjarjarasm.asm.MethodVisitor;
import groovyjarjarasm.asm.Opcodes;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.codehaus.groovy.ast.ClassCodeVisitorSupport;
import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.ConstructorNode;
import org.codehaus.groovy.ast.FieldNode;
import org.codehaus.groovy.ast.InnerClassNode;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.Parameter;
import org.codehaus.groovy.ast.PropertyNode;
import org.codehaus.groovy.ast.VariableScope;
import org.codehaus.groovy.ast.expr.ArgumentListExpression;
import org.codehaus.groovy.ast.expr.AttributeExpression;
import org.codehaus.groovy.ast.expr.BinaryExpression;
import org.codehaus.groovy.ast.expr.ClassExpression;
import org.codehaus.groovy.ast.expr.ConstantExpression;
import org.codehaus.groovy.ast.expr.ConstructorCallExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.FieldExpression;
import org.codehaus.groovy.ast.expr.GStringExpression;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.ast.expr.PropertyExpression;
import org.codehaus.groovy.ast.expr.SpreadExpression;
import org.codehaus.groovy.ast.expr.TupleExpression;
import org.codehaus.groovy.ast.expr.VariableExpression;
import org.codehaus.groovy.ast.stmt.BlockStatement;
import org.codehaus.groovy.ast.stmt.ExpressionStatement;
import org.codehaus.groovy.ast.stmt.ReturnStatement;
import org.codehaus.groovy.ast.stmt.Statement;
import org.codehaus.groovy.control.CompilationUnit;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.syntax.Token;

public class InnerClassVisitor extends ClassCodeVisitorSupport implements Opcodes {
   private final SourceUnit sourceUnit;
   private ClassNode classNode;
   private static final int PUBLIC_SYNTHETIC = 4097;
   private FieldNode thisField = null;
   private MethodNode currentMethod;
   private FieldNode currentField;
   private boolean processingObjInitStatements;

   public InnerClassVisitor(CompilationUnit cu, SourceUnit su) {
      this.sourceUnit = su;
   }

   protected SourceUnit getSourceUnit() {
      return this.sourceUnit;
   }

   public void visitClass(ClassNode node) {
      this.classNode = node;
      this.thisField = null;
      InnerClassNode innerClass = null;
      if (!node.isEnum() && !node.isInterface() && node instanceof InnerClassNode) {
         innerClass = (InnerClassNode)node;
         if (!this.isStatic(innerClass) && innerClass.getVariableScope() == null) {
            this.thisField = innerClass.addField("this$0", 4097, node.getOuterClass(), (Expression)null);
         }

         if (innerClass.getVariableScope() == null && innerClass.getDeclaredConstructors().isEmpty()) {
            innerClass.addConstructor(4097, new Parameter[0], (ClassNode[])null, (Statement)null);
         }
      }

      super.visitClass(node);
      if (!node.isEnum() && !node.isInterface()) {
         this.addDispatcherMethods();
         if (innerClass != null) {
            if (node.getSuperClass().isInterface()) {
               node.addInterface(node.getUnresolvedSuperClass());
               node.setUnresolvedSuperClass(ClassHelper.OBJECT_TYPE);
            }

            this.addDefaultMethods(innerClass);
         }
      }
   }

   protected void visitObjectInitializerStatements(ClassNode node) {
      this.processingObjInitStatements = true;
      super.visitObjectInitializerStatements(node);
      this.processingObjInitStatements = false;
   }

   private boolean isStatic(InnerClassNode node) {
      VariableScope scope = node.getVariableScope();
      if (scope != null) {
         return scope.isInStaticContext();
      } else {
         return (node.getModifiers() & 8) != 0;
      }
   }

   private void addDefaultMethods(InnerClassNode node) {
      boolean isStatic = this.isStatic(node);
      final String classInternalName = BytecodeHelper.getClassInternalName((ClassNode)node);
      final String outerClassInternalName = this.getInternalName(node.getOuterClass(), isStatic);
      final String outerClassDescriptor = this.getTypeDescriptor(node.getOuterClass(), isStatic);
      final int objectDistance = this.getObjectDistance(node.getOuterClass());
      Parameter[] parameters = new Parameter[]{new Parameter(ClassHelper.STRING_TYPE, "name"), new Parameter(ClassHelper.OBJECT_TYPE, "args")};
      MethodNode method = node.addSyntheticMethod("methodMissing", 1, ClassHelper.OBJECT_TYPE, parameters, ClassNode.EMPTY_ARRAY, (Statement)null);
      BlockStatement block = new BlockStatement();
      if (isStatic) {
         this.setMethodDispatcherCode(block, new ClassExpression(node.getOuterClass()), parameters);
      } else {
         block.addStatement(new BytecodeSequence(new BytecodeInstruction() {
            public void visit(MethodVisitor mv) {
               mv.visitVarInsn(25, 0);
               mv.visitFieldInsn(180, classInternalName, "this$0", outerClassDescriptor);
               mv.visitVarInsn(25, 1);
               mv.visitVarInsn(25, 2);
               mv.visitMethodInsn(182, outerClassInternalName, "this$dist$invoke$" + objectDistance, "(Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/Object;");
               mv.visitInsn(176);
            }
         }));
      }

      method.setCode(block);
      parameters = new Parameter[]{new Parameter(ClassHelper.STRING_TYPE, "name"), new Parameter(ClassHelper.OBJECT_TYPE, "val")};
      method = node.addSyntheticMethod("propertyMissing", 1, ClassHelper.VOID_TYPE, parameters, ClassNode.EMPTY_ARRAY, (Statement)null);
      block = new BlockStatement();
      if (isStatic) {
         this.setPropertySetDispatcher(block, new ClassExpression(node.getOuterClass()), parameters);
      } else {
         block.addStatement(new BytecodeSequence(new BytecodeInstruction() {
            public void visit(MethodVisitor mv) {
               mv.visitVarInsn(25, 0);
               mv.visitFieldInsn(180, classInternalName, "this$0", outerClassDescriptor);
               mv.visitVarInsn(25, 1);
               mv.visitVarInsn(25, 2);
               mv.visitMethodInsn(182, outerClassInternalName, "this$dist$set$" + objectDistance, "(Ljava/lang/String;Ljava/lang/Object;)V");
               mv.visitInsn(177);
            }
         }));
      }

      method.setCode(block);
      parameters = new Parameter[]{new Parameter(ClassHelper.STRING_TYPE, "name")};
      method = node.addSyntheticMethod("propertyMissing", 1, ClassHelper.OBJECT_TYPE, parameters, ClassNode.EMPTY_ARRAY, (Statement)null);
      block = new BlockStatement();
      if (isStatic) {
         this.setPropertyGetterDispatcher(block, new ClassExpression(node.getOuterClass()), parameters);
      } else {
         block.addStatement(new BytecodeSequence(new BytecodeInstruction() {
            public void visit(MethodVisitor mv) {
               mv.visitVarInsn(25, 0);
               mv.visitFieldInsn(180, classInternalName, "this$0", outerClassDescriptor);
               mv.visitVarInsn(25, 1);
               mv.visitMethodInsn(182, outerClassInternalName, "this$dist$get$" + objectDistance, "(Ljava/lang/String;)Ljava/lang/Object;");
               mv.visitInsn(176);
            }
         }));
      }

      method.setCode(block);
   }

   private String getTypeDescriptor(ClassNode node, boolean isStatic) {
      return BytecodeHelper.getTypeDescription(this.getClassNode(node, isStatic));
   }

   private String getInternalName(ClassNode node, boolean isStatic) {
      return BytecodeHelper.getClassInternalName(this.getClassNode(node, isStatic));
   }

   public void visitConstructor(ConstructorNode node) {
      this.addThisReference(node);
      super.visitConstructor(node);
   }

   private boolean shouldHandleImplicitThisForInnerClass(ClassNode cn) {
      if (!cn.isEnum() && !cn.isInterface()) {
         if ((cn.getModifiers() & 8) != 0) {
            return false;
         } else if (!(cn instanceof InnerClassNode)) {
            return false;
         } else {
            InnerClassNode innerClass = (InnerClassNode)cn;
            if (innerClass.getVariableScope() != null) {
               return false;
            } else {
               return (innerClass.getModifiers() & 8) == 0;
            }
         }
      } else {
         return false;
      }
   }

   private void addThisReference(ConstructorNode node) {
      if (this.shouldHandleImplicitThisForInnerClass(this.classNode)) {
         Statement code = node.getCode();
         Parameter[] params = node.getParameters();
         Parameter[] newParams = new Parameter[params.length + 1];
         System.arraycopy(params, 0, newParams, 1, params.length);
         Parameter thisPara = new Parameter(this.classNode.getOuterClass(), this.getUniqueName(params, node));
         newParams[0] = thisPara;
         node.setParameters(newParams);
         Statement firstStatement = node.getFirstStatement();
         BlockStatement block = null;
         if (code == null) {
            block = new BlockStatement();
         } else if (!(code instanceof BlockStatement)) {
            block = new BlockStatement();
            block.addStatement(code);
         } else {
            block = (BlockStatement)code;
         }

         BlockStatement newCode = new BlockStatement();
         addFieldInit(thisPara, this.thisField, newCode);
         ConstructorCallExpression cce = this.getFirstIfSpecialConstructorCall(block);
         if (cce == null) {
            cce = new ConstructorCallExpression(ClassNode.SUPER, new TupleExpression());
            block.getStatements().add(0, new ExpressionStatement(cce));
         }

         if (this.shouldImplicitlyPassThisPara(cce)) {
            TupleExpression args = (TupleExpression)cce.getArguments();
            List<Expression> expressions = args.getExpressions();
            VariableExpression ve = new VariableExpression(thisPara.getName());
            ve.setAccessedVariable(thisPara);
            expressions.add(0, ve);
         }

         if (cce.isSuperCall()) {
            block.getStatements().add(1, newCode);
         }

         node.setCode(block);
      }
   }

   private boolean shouldImplicitlyPassThisPara(ConstructorCallExpression cce) {
      boolean pass = false;
      ClassNode superCN = this.classNode.getSuperClass();
      if (cce.isThisCall()) {
         pass = true;
      } else if (cce.isSuperCall() && !superCN.isEnum() && !superCN.isInterface() && superCN instanceof InnerClassNode) {
         InnerClassNode superInnerCN = (InnerClassNode)superCN;
         if (!this.isStatic(superInnerCN) && superCN.getOuterClass().equals(this.classNode.getOuterClass())) {
            pass = true;
         }
      }

      return pass;
   }

   private String getUniqueName(Parameter[] params, ConstructorNode node) {
      String namePrefix = "$p";

      label24:
      for(int i = 0; i < 100; ++i) {
         namePrefix = namePrefix + "$";
         Parameter[] arr$ = params;
         int len$ = params.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            Parameter p = arr$[i$];
            if (p.getName().equals(namePrefix)) {
               continue label24;
            }
         }

         return namePrefix;
      }

      this.addError("unable to find a unique prefix name for synthetic this reference", node);
      return namePrefix;
   }

   private ConstructorCallExpression getFirstIfSpecialConstructorCall(BlockStatement code) {
      if (code == null) {
         return null;
      } else {
         List<Statement> statementList = code.getStatements();
         if (statementList.isEmpty()) {
            return null;
         } else {
            Statement statement = (Statement)statementList.get(0);
            if (!(statement instanceof ExpressionStatement)) {
               return null;
            } else {
               Expression expression = ((ExpressionStatement)statement).getExpression();
               if (!(expression instanceof ConstructorCallExpression)) {
                  return null;
               } else {
                  ConstructorCallExpression cce = (ConstructorCallExpression)expression;
                  return cce.isSpecialCall() ? cce : null;
               }
            }
         }
      }
   }

   protected void visitConstructorOrMethod(MethodNode node, boolean isConstructor) {
      this.currentMethod = node;
      super.visitConstructorOrMethod(node, isConstructor);
      this.currentMethod = null;
   }

   public void visitField(FieldNode node) {
      this.currentField = node;
      super.visitField(node);
      this.currentField = null;
   }

   public void visitProperty(PropertyNode node) {
      FieldNode field = node.getField();
      Expression init = field.getInitialExpression();
      field.setInitialValueExpression((Expression)null);
      super.visitProperty(node);
      field.setInitialValueExpression(init);
   }

   public void visitConstructorCallExpression(ConstructorCallExpression call) {
      super.visitConstructorCallExpression(call);
      if (!call.isUsingAnonymousInnerClass()) {
         this.passThisReference(call);
      } else {
         InnerClassNode innerClass = (InnerClassNode)call.getType();
         if (innerClass.getDeclaredConstructors().isEmpty()) {
            if ((innerClass.getModifiers() & 8) == 0) {
               VariableScope scope = innerClass.getVariableScope();
               if (scope != null) {
                  boolean isStatic = scope.isInStaticContext();
                  List<Expression> expressions = ((TupleExpression)call.getArguments()).getExpressions();
                  BlockStatement block = new BlockStatement();
                  int additionalParamCount = 1 + scope.getReferencedLocalVariablesCount();
                  List parameters = new ArrayList(expressions.size() + additionalParamCount);
                  List superCallArguments = new ArrayList(expressions.size());
                  int pCount = additionalParamCount;
                  Iterator i$ = expressions.iterator();

                  Parameter thisParameter;
                  while(i$.hasNext()) {
                     Expression expr = (Expression)i$.next();
                     ++pCount;
                     thisParameter = new Parameter(ClassHelper.OBJECT_TYPE, "p" + pCount);
                     parameters.add(thisParameter);
                     superCallArguments.add(new VariableExpression(thisParameter));
                  }

                  ConstructorCallExpression cce = new ConstructorCallExpression(ClassNode.SUPER, new TupleExpression(superCallArguments));
                  block.addStatement(new ExpressionStatement(cce));
                  pCount = 0;
                  expressions.add(pCount, VariableExpression.THIS_EXPRESSION);
                  ClassNode outerClassType = this.getClassNode(innerClass.getOuterClass(), isStatic);
                  thisParameter = new Parameter(outerClassType, "p" + pCount);
                  parameters.add(pCount, thisParameter);
                  this.thisField = innerClass.addField("this$0", 4097, outerClassType, (Expression)null);
                  addFieldInit(thisParameter, this.thisField, block);
                  Iterator it = scope.getReferencedLocalVariablesIterator();

                  while(it.hasNext()) {
                     ++pCount;
                     org.codehaus.groovy.ast.Variable var = (org.codehaus.groovy.ast.Variable)it.next();
                     VariableExpression ve = new VariableExpression(var);
                     ve.setClosureSharedVariable(true);
                     ve.setUseReferenceDirectly(true);
                     expressions.add(pCount, ve);
                     Parameter p = new Parameter(ClassHelper.REFERENCE_TYPE, "p" + pCount);
                     parameters.add(pCount, p);
                     VariableExpression initial = new VariableExpression(p);
                     initial.setUseReferenceDirectly(true);
                     FieldNode pField = innerClass.addFieldFirst(ve.getName(), 4097, ClassHelper.REFERENCE_TYPE, initial);
                     pField.setHolder(true);
                  }

                  innerClass.addConstructor(4096, (Parameter[])((Parameter[])parameters.toArray(new Parameter[0])), ClassNode.EMPTY_ARRAY, block);
               }
            }
         }
      }
   }

   private void passThisReference(ConstructorCallExpression call) {
      ClassNode cn = call.getType().redirect();
      if (this.shouldHandleImplicitThisForInnerClass(cn)) {
         boolean isInStaticContext = true;
         if (this.currentMethod != null) {
            isInStaticContext = this.currentMethod.getVariableScope().isInStaticContext();
         } else if (this.currentField != null) {
            isInStaticContext = this.currentField.isStatic();
         } else if (this.processingObjInitStatements) {
            isInStaticContext = false;
         }

         if (isInStaticContext) {
            Expression args = call.getArguments();
            if (args instanceof TupleExpression && ((TupleExpression)args).getExpressions().isEmpty()) {
               this.addError("No enclosing instance passed in constructor call of a non-static inner class", call);
            }

         } else {
            ClassNode parent = this.classNode;

            int level;
            for(level = 0; parent != null && parent != cn.getOuterClass(); parent = parent.getOuterClass()) {
               ++level;
            }

            if (parent != null) {
               Expression argsExp = call.getArguments();
               if (argsExp instanceof TupleExpression) {
                  TupleExpression argsListExp = (TupleExpression)argsExp;
                  Expression this0 = VariableExpression.THIS_EXPRESSION;

                  for(int i = 0; i != level; ++i) {
                     this0 = new PropertyExpression((Expression)this0, "this$0");
                  }

                  argsListExp.getExpressions().add(0, this0);
               }

            }
         }
      }
   }

   private ClassNode getClassNode(ClassNode node, boolean isStatic) {
      if (isStatic) {
         node = ClassHelper.CLASS_Type;
      }

      return node;
   }

   private void addDispatcherMethods() {
      int objectDistance = this.getObjectDistance(this.classNode);
      Parameter[] parameters = new Parameter[]{new Parameter(ClassHelper.STRING_TYPE, "name"), new Parameter(ClassHelper.OBJECT_TYPE, "args")};
      MethodNode method = this.classNode.addSyntheticMethod("this$dist$invoke$" + objectDistance, 4097, ClassHelper.OBJECT_TYPE, parameters, ClassNode.EMPTY_ARRAY, (Statement)null);
      BlockStatement block = new BlockStatement();
      this.setMethodDispatcherCode(block, VariableExpression.THIS_EXPRESSION, parameters);
      method.setCode(block);
      parameters = new Parameter[]{new Parameter(ClassHelper.STRING_TYPE, "name"), new Parameter(ClassHelper.OBJECT_TYPE, "value")};
      method = this.classNode.addSyntheticMethod("this$dist$set$" + objectDistance, 4097, ClassHelper.VOID_TYPE, parameters, ClassNode.EMPTY_ARRAY, (Statement)null);
      block = new BlockStatement();
      this.setPropertySetDispatcher(block, VariableExpression.THIS_EXPRESSION, parameters);
      method.setCode(block);
      parameters = new Parameter[]{new Parameter(ClassHelper.STRING_TYPE, "name")};
      method = this.classNode.addSyntheticMethod("this$dist$get$" + objectDistance, 4097, ClassHelper.OBJECT_TYPE, parameters, ClassNode.EMPTY_ARRAY, (Statement)null);
      block = new BlockStatement();
      this.setPropertyGetterDispatcher(block, VariableExpression.THIS_EXPRESSION, parameters);
      method.setCode(block);
   }

   private void setPropertyGetterDispatcher(BlockStatement block, Expression thiz, Parameter[] parameters) {
      List gStringStrings = new ArrayList();
      gStringStrings.add(new ConstantExpression(""));
      gStringStrings.add(new ConstantExpression(""));
      List gStringValues = new ArrayList();
      gStringValues.add(new VariableExpression(parameters[0]));
      block.addStatement(new ReturnStatement(new AttributeExpression(thiz, new GStringExpression("$name", gStringStrings, gStringValues))));
   }

   private void setPropertySetDispatcher(BlockStatement block, Expression thiz, Parameter[] parameters) {
      List gStringStrings = new ArrayList();
      gStringStrings.add(new ConstantExpression(""));
      gStringStrings.add(new ConstantExpression(""));
      List gStringValues = new ArrayList();
      gStringValues.add(new VariableExpression(parameters[0]));
      block.addStatement(new ExpressionStatement(new BinaryExpression(new AttributeExpression(thiz, new GStringExpression("$name", gStringStrings, gStringValues)), Token.newSymbol(100, -1, -1), new VariableExpression(parameters[1]))));
   }

   private void setMethodDispatcherCode(BlockStatement block, Expression thiz, Parameter[] parameters) {
      List gStringStrings = new ArrayList();
      gStringStrings.add(new ConstantExpression(""));
      gStringStrings.add(new ConstantExpression(""));
      List gStringValues = new ArrayList();
      gStringValues.add(new VariableExpression(parameters[0]));
      block.addStatement(new ReturnStatement(new MethodCallExpression(thiz, new GStringExpression("$name", gStringStrings, gStringValues), new ArgumentListExpression(new SpreadExpression(new VariableExpression(parameters[1]))))));
   }

   private static void addFieldInit(Parameter p, FieldNode fn, BlockStatement block) {
      VariableExpression ve = new VariableExpression(p);
      FieldExpression fe = new FieldExpression(fn);
      block.addStatement(new ExpressionStatement(new BinaryExpression(fe, Token.newSymbol(100, -1, -1), ve)));
   }

   private int getObjectDistance(ClassNode node) {
      int count;
      for(count = 1; node != null && node != ClassHelper.OBJECT_TYPE; node = node.getSuperClass()) {
         ++count;
      }

      return count;
   }
}
