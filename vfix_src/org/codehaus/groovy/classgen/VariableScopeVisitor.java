package org.codehaus.groovy.classgen;

import java.beans.Introspector;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import org.codehaus.groovy.GroovyBugError;
import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.AnnotatedNode;
import org.codehaus.groovy.ast.AnnotationNode;
import org.codehaus.groovy.ast.ClassCodeVisitorSupport;
import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.DynamicVariable;
import org.codehaus.groovy.ast.FieldNode;
import org.codehaus.groovy.ast.InnerClassNode;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.Parameter;
import org.codehaus.groovy.ast.PropertyNode;
import org.codehaus.groovy.ast.VariableScope;
import org.codehaus.groovy.ast.expr.ArgumentListExpression;
import org.codehaus.groovy.ast.expr.ClosureExpression;
import org.codehaus.groovy.ast.expr.ConstantExpression;
import org.codehaus.groovy.ast.expr.ConstructorCallExpression;
import org.codehaus.groovy.ast.expr.DeclarationExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.FieldExpression;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.ast.expr.PropertyExpression;
import org.codehaus.groovy.ast.expr.VariableExpression;
import org.codehaus.groovy.ast.stmt.BlockStatement;
import org.codehaus.groovy.ast.stmt.CatchStatement;
import org.codehaus.groovy.ast.stmt.ForStatement;
import org.codehaus.groovy.ast.stmt.Statement;
import org.codehaus.groovy.control.SourceUnit;

public class VariableScopeVisitor extends ClassCodeVisitorSupport {
   private VariableScope currentScope = null;
   private VariableScope headScope = new VariableScope();
   private ClassNode currentClass = null;
   private SourceUnit source;
   private boolean inClosure = false;
   private boolean inPropertyExpression = false;
   private boolean isSpecialConstructorCall = false;
   private boolean inConstructor = false;
   private LinkedList stateStack = new LinkedList();

   public VariableScopeVisitor(SourceUnit source) {
      this.source = source;
      this.currentScope = this.headScope;
   }

   private void pushState(boolean isStatic) {
      this.stateStack.add(new VariableScopeVisitor.StateStackElement());
      this.currentScope = new VariableScope(this.currentScope);
      this.currentScope.setInStaticContext(isStatic);
   }

   private void pushState() {
      this.pushState(this.currentScope.isInStaticContext());
   }

   private void popState() {
      if (this.inClosure) {
         this.currentScope.setInStaticContext(false);
      }

      VariableScopeVisitor.StateStackElement element = (VariableScopeVisitor.StateStackElement)this.stateStack.removeLast();
      this.currentScope = element.scope;
      this.currentClass = element.clazz;
      this.inClosure = element.closure;
      this.inConstructor = element.inConstructor;
   }

   private void declare(Parameter[] parameters, ASTNode node) {
      Parameter[] arr$ = parameters;
      int len$ = parameters.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Parameter parameter = arr$[i$];
         if (parameter.hasInitialExpression()) {
            parameter.getInitialExpression().visit(this);
         }

         this.declare((org.codehaus.groovy.ast.Variable)parameter, node);
      }

   }

   private void declare(VariableExpression vex) {
      vex.setInStaticContext(this.currentScope.isInStaticContext());
      this.declare((org.codehaus.groovy.ast.Variable)vex, vex);
      vex.setAccessedVariable(vex);
   }

   private void declare(org.codehaus.groovy.ast.Variable var, ASTNode expr) {
      String scopeType = "scope";
      String variableType = "variable";
      if (expr.getClass() == FieldNode.class) {
         scopeType = "class";
         variableType = "field";
      } else if (expr.getClass() == PropertyNode.class) {
         scopeType = "class";
         variableType = "property";
      }

      StringBuffer msg = new StringBuffer();
      msg.append("The current ").append(scopeType);
      msg.append(" already contains a ").append(variableType);
      msg.append(" of the name ").append(var.getName());
      if (this.currentScope.getDeclaredVariable(var.getName()) != null) {
         this.addError(msg.toString(), expr);
      } else {
         for(VariableScope scope = this.currentScope.getParent(); scope != null && scope.getClassScope() == null; scope = scope.getParent()) {
            if (scope.getDeclaredVariable(var.getName()) != null) {
               this.addError(msg.toString(), expr);
               break;
            }
         }

         this.currentScope.putDeclaredVariable(var);
      }
   }

   protected SourceUnit getSourceUnit() {
      return this.source;
   }

   private org.codehaus.groovy.ast.Variable findClassMember(ClassNode cn, String name) {
      if (cn == null) {
         return null;
      } else if (cn.isScript()) {
         return new DynamicVariable(name, false);
      } else {
         Iterator i$ = cn.getFields().iterator();

         FieldNode fn;
         do {
            if (!i$.hasNext()) {
               i$ = cn.getMethods().iterator();

               String pName;
               MethodNode mn;
               do {
                  if (!i$.hasNext()) {
                     i$ = cn.getProperties().iterator();

                     PropertyNode pn;
                     do {
                        if (!i$.hasNext()) {
                           org.codehaus.groovy.ast.Variable ret = this.findClassMember(cn.getSuperClass(), name);
                           if (ret != null) {
                              return ret;
                           }

                           return this.findClassMember(cn.getOuterClass(), name);
                        }

                        pn = (PropertyNode)i$.next();
                     } while(!pn.getName().equals(name));

                     return pn;
                  }

                  mn = (MethodNode)i$.next();
                  pName = this.getPropertyName(mn);
               } while(pName == null || !pName.equals(name));

               return new PropertyNode(pName, mn.getModifiers(), this.getPropertyType(mn), cn, (Expression)null, (Statement)null, (Statement)null);
            }

            fn = (FieldNode)i$.next();
         } while(!fn.getName().equals(name));

         return fn;
      }
   }

   private ClassNode getPropertyType(MethodNode m) {
      return m.getReturnType() != ClassHelper.VOID_TYPE ? m.getReturnType() : m.getParameters()[0].getType();
   }

   private String getPropertyName(MethodNode m) {
      String name = m.getName();
      if (!name.startsWith("set") && !name.startsWith("get")) {
         return null;
      } else {
         String pname = name.substring(3);
         if (pname.length() == 0) {
            return null;
         } else {
            pname = Introspector.decapitalize(pname);
            if (name.startsWith("get") && (m.getReturnType() == ClassHelper.VOID_TYPE || m.getParameters().length != 0)) {
               return null;
            } else {
               return name.startsWith("set") && m.getParameters().length != 1 ? null : pname;
            }
         }
      }
   }

   private org.codehaus.groovy.ast.Variable checkVariableNameForDeclaration(String name, Expression expression) {
      if (!"super".equals(name) && !"this".equals(name)) {
         VariableScope scope = this.currentScope;
         Object var = new DynamicVariable(name, this.currentScope.isInStaticContext());

         while(true) {
            org.codehaus.groovy.ast.Variable var1 = scope.getDeclaredVariable(((org.codehaus.groovy.ast.Variable)var).getName());
            if (var1 != null) {
               var = var1;
               break;
            }

            var1 = scope.getReferencedLocalVariable(((org.codehaus.groovy.ast.Variable)var).getName());
            if (var1 != null) {
               var = var1;
               break;
            }

            var1 = scope.getReferencedClassVariable(((org.codehaus.groovy.ast.Variable)var).getName());
            if (var1 != null) {
               var = var1;
               break;
            }

            ClassNode classScope = scope.getClassScope();
            if (classScope != null) {
               org.codehaus.groovy.ast.Variable member = this.findClassMember(classScope, ((org.codehaus.groovy.ast.Variable)var).getName());
               if (member != null) {
                  boolean staticScope = this.currentScope.isInStaticContext() || this.isSpecialConstructorCall;
                  boolean staticMember = member.isInStaticContext();
                  if (!staticScope || staticMember) {
                     var = member;
                  }
               }
               break;
            }

            scope = scope.getParent();
         }

         VariableScope end = scope;

         for(scope = this.currentScope; scope != end; scope = scope.getParent()) {
            if (!end.isClassScope() && (!end.isReferencedClassVariable(name) || end.getDeclaredVariable(name) != null)) {
               ((org.codehaus.groovy.ast.Variable)var).setClosureSharedVariable(((org.codehaus.groovy.ast.Variable)var).isClosureSharedVariable() || this.inClosure);
               scope.putReferencedLocalVariable((org.codehaus.groovy.ast.Variable)var);
            } else {
               scope.putReferencedClassVariable((org.codehaus.groovy.ast.Variable)var);
            }
         }

         return (org.codehaus.groovy.ast.Variable)var;
      } else {
         return null;
      }
   }

   private void checkPropertyOnExplicitThis(PropertyExpression pe) {
      if (this.currentScope.isInStaticContext()) {
         Expression object = pe.getObjectExpression();
         if (object instanceof VariableExpression) {
            VariableExpression ve = (VariableExpression)object;
            if (ve.getName().equals("this")) {
               String name = pe.getPropertyAsString();
               if (!name.equals("class")) {
                  if (name != null) {
                     org.codehaus.groovy.ast.Variable member = this.findClassMember(this.currentClass, name);
                     if (member != null) {
                        this.checkVariableContextAccess(member, pe);
                     }
                  }
               }
            }
         }
      }
   }

   private void checkVariableContextAccess(org.codehaus.groovy.ast.Variable v, Expression expr) {
      if (!this.inPropertyExpression && !v.isInStaticContext() && this.currentScope.isInStaticContext()) {
         String msg = v.getName() + " is declared in a dynamic context, but you tried to" + " access it from a static context.";
         this.addError(msg, expr);
         DynamicVariable v2 = new DynamicVariable(v.getName(), this.currentScope.isInStaticContext());
         this.currentScope.putDeclaredVariable(v2);
      }
   }

   public void visitBlockStatement(BlockStatement block) {
      this.pushState();
      block.setVariableScope(this.currentScope);
      super.visitBlockStatement(block);
      this.popState();
   }

   public void visitForLoop(ForStatement forLoop) {
      this.pushState();
      forLoop.setVariableScope(this.currentScope);
      Parameter p = forLoop.getVariable();
      p.setInStaticContext(this.currentScope.isInStaticContext());
      if (p != ForStatement.FOR_LOOP_DUMMY) {
         this.declare((org.codehaus.groovy.ast.Variable)p, forLoop);
      }

      super.visitForLoop(forLoop);
      this.popState();
   }

   public void visitDeclarationExpression(DeclarationExpression expression) {
      expression.getRightExpression().visit(this);
      if (expression.isMultipleAssignmentDeclaration()) {
         ArgumentListExpression list = (ArgumentListExpression)expression.getLeftExpression();
         Iterator i$ = list.getExpressions().iterator();

         while(i$.hasNext()) {
            Expression e = (Expression)i$.next();
            VariableExpression exp = (VariableExpression)e;
            this.declare(exp);
         }
      } else {
         this.declare(expression.getVariableExpression());
      }

   }

   public void visitVariableExpression(VariableExpression expression) {
      String name = expression.getName();
      org.codehaus.groovy.ast.Variable v = this.checkVariableNameForDeclaration(name, expression);
      if (v != null) {
         expression.setAccessedVariable(v);
         this.checkVariableContextAccess(v, expression);
      }
   }

   public void visitPropertyExpression(PropertyExpression expression) {
      boolean ipe = this.inPropertyExpression;
      this.inPropertyExpression = true;
      expression.getObjectExpression().visit(this);
      this.inPropertyExpression = false;
      expression.getProperty().visit(this);
      this.checkPropertyOnExplicitThis(expression);
      this.inPropertyExpression = ipe;
   }

   public void visitClosureExpression(ClosureExpression expression) {
      this.pushState();
      this.inClosure = true;
      expression.setVariableScope(this.currentScope);
      if (expression.isParameterSpecified()) {
         Parameter[] parameters = expression.getParameters();
         Parameter[] arr$ = parameters;
         int len$ = parameters.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            Parameter parameter = arr$[i$];
            parameter.setInStaticContext(this.currentScope.isInStaticContext());
            if (parameter.hasInitialExpression()) {
               parameter.getInitialExpression().visit(this);
            }

            this.declare((org.codehaus.groovy.ast.Variable)parameter, expression);
         }
      } else if (expression.getParameters() != null) {
         Parameter var = new Parameter(ClassHelper.OBJECT_TYPE, "it");
         var.setInStaticContext(this.currentScope.isInStaticContext());
         this.currentScope.putDeclaredVariable(var);
      }

      super.visitClosureExpression(expression);
      this.popState();
   }

   public void visitCatchStatement(CatchStatement statement) {
      this.pushState();
      Parameter p = statement.getVariable();
      p.setInStaticContext(this.currentScope.isInStaticContext());
      this.declare((org.codehaus.groovy.ast.Variable)p, statement);
      super.visitCatchStatement(statement);
      this.popState();
   }

   public void visitFieldExpression(FieldExpression expression) {
      String name = expression.getFieldName();
      org.codehaus.groovy.ast.Variable v = this.checkVariableNameForDeclaration(name, expression);
      this.checkVariableContextAccess(v, expression);
   }

   public void visitClass(ClassNode node) {
      if (node instanceof InnerClassNode) {
         InnerClassNode in = (InnerClassNode)node;
         if (in.isAnonymous()) {
            return;
         }
      }

      this.pushState();
      this.currentClass = node;
      this.currentScope.setClassScope(node);
      super.visitClass(node);
      this.popState();
   }

   protected void visitConstructorOrMethod(MethodNode node, boolean isConstructor) {
      this.pushState(node.isStatic());
      this.inConstructor = isConstructor;
      node.setVariableScope(this.currentScope);
      Parameter[] parameters = node.getParameters();
      Parameter[] arr$ = parameters;
      int len$ = parameters.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Parameter parameter = arr$[i$];
         this.visitAnnotations(parameter);
      }

      this.declare((Parameter[])node.getParameters(), node);
      super.visitConstructorOrMethod(node, isConstructor);
      this.popState();
   }

   public void visitMethodCallExpression(MethodCallExpression call) {
      if (call.isImplicitThis() && call.getMethod() instanceof ConstantExpression) {
         ConstantExpression methodNameConstant = (ConstantExpression)call.getMethod();
         Object value = methodNameConstant.getText();
         if (!(value instanceof String)) {
            throw new GroovyBugError("tried to make a method call with a non-String constant method name.");
         }

         String methodName = (String)value;
         org.codehaus.groovy.ast.Variable v = this.checkVariableNameForDeclaration(methodName, call);
         if (v != null && !(v instanceof DynamicVariable)) {
            this.checkVariableContextAccess(v, call);
         }

         if (v instanceof VariableExpression || v instanceof Parameter) {
            VariableExpression object = new VariableExpression(v);
            object.setSourcePosition(methodNameConstant);
            call.setObjectExpression(object);
            ConstantExpression method = new ConstantExpression("call");
            method.setSourcePosition(methodNameConstant);
            call.setMethod(method);
         }
      }

      super.visitMethodCallExpression(call);
   }

   public void visitConstructorCallExpression(ConstructorCallExpression call) {
      this.isSpecialConstructorCall = call.isSpecialCall();
      super.visitConstructorCallExpression(call);
      this.isSpecialConstructorCall = false;
      if (call.isUsingAnonymousInnerClass()) {
         this.pushState();
         InnerClassNode innerClass = (InnerClassNode)call.getType();
         innerClass.setVariableScope(this.currentScope);
         Iterator i$ = innerClass.getMethods().iterator();

         while(i$.hasNext()) {
            MethodNode method = (MethodNode)i$.next();
            Parameter[] parameters = method.getParameters();
            if (parameters.length == 0) {
               parameters = null;
            }

            ClosureExpression cl = new ClosureExpression(parameters, method.getCode());
            this.visitClosureExpression(cl);
         }

         boolean ic = this.inClosure;
         this.inClosure = true;
         Iterator i$ = innerClass.getFields().iterator();

         while(i$.hasNext()) {
            FieldNode field = (FieldNode)i$.next();
            Expression expression = field.getInitialExpression();
            if (expression != null) {
               expression.visit(this);
            }
         }

         i$ = innerClass.getObjectInitializerStatements().iterator();

         while(i$.hasNext()) {
            Statement statement = (Statement)i$.next();
            statement.visit(this);
         }

         this.inClosure = ic;
         this.popState();
      }
   }

   public void visitProperty(PropertyNode node) {
      this.pushState(node.isStatic());
      super.visitProperty(node);
      this.popState();
   }

   public void visitField(FieldNode node) {
      this.pushState(node.isStatic());
      super.visitField(node);
      this.popState();
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
               Expression annMemberValue = (Expression)member.getValue();
               annMemberValue.visit(this);
            }
         }
      }
   }

   private class StateStackElement {
      VariableScope scope;
      ClassNode clazz;
      boolean closure;
      boolean inConstructor;

      StateStackElement() {
         this.scope = VariableScopeVisitor.this.currentScope;
         this.clazz = VariableScopeVisitor.this.currentClass;
         this.closure = VariableScopeVisitor.this.inClosure;
         this.inConstructor = VariableScopeVisitor.this.inConstructor;
      }
   }
}
