package org.codehaus.groovy.classgen;

import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.List;
import org.codehaus.groovy.ast.ClassCodeVisitorSupport;
import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.ConstructorNode;
import org.codehaus.groovy.ast.FieldNode;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.Parameter;
import org.codehaus.groovy.ast.PropertyNode;
import org.codehaus.groovy.ast.expr.BinaryExpression;
import org.codehaus.groovy.ast.expr.ConstantExpression;
import org.codehaus.groovy.ast.expr.DeclarationExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.GStringExpression;
import org.codehaus.groovy.ast.expr.MapEntryExpression;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.ast.expr.PropertyExpression;
import org.codehaus.groovy.ast.expr.TupleExpression;
import org.codehaus.groovy.ast.expr.VariableExpression;
import org.codehaus.groovy.ast.stmt.CatchStatement;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.runtime.MetaClassHelper;

public class ClassCompletionVerifier extends ClassCodeVisitorSupport {
   private ClassNode currentClass;
   private SourceUnit source;
   private boolean inConstructor = false;
   private boolean inStaticConstructor = false;

   public ClassCompletionVerifier(SourceUnit source) {
      this.source = source;
   }

   public ClassNode getClassNode() {
      return this.currentClass;
   }

   public void visitClass(ClassNode node) {
      ClassNode oldClass = this.currentClass;
      this.currentClass = node;
      this.checkImplementsAndExtends(node);
      if (this.source != null && !this.source.getErrorCollector().hasErrors()) {
         this.checkClassForIncorrectModifiers(node);
         this.checkClassForOverwritingFinal(node);
         this.checkMethodsForIncorrectModifiers(node);
         this.checkMethodsForOverridingFinal(node);
         this.checkNoAbstractMethodsNonabstractClass(node);
      }

      super.visitClass(node);
      this.currentClass = oldClass;
   }

   private void checkNoAbstractMethodsNonabstractClass(ClassNode node) {
      if (!Modifier.isAbstract(node.getModifiers())) {
         List<MethodNode> abstractMethods = node.getAbstractMethods();
         if (abstractMethods != null) {
            Iterator i$ = abstractMethods.iterator();

            while(i$.hasNext()) {
               MethodNode method = (MethodNode)i$.next();
               this.addError("Can't have an abstract method in a non-abstract class. The " + this.getDescription(node) + " must be declared abstract or" + " the " + this.getDescription(method) + " must be implemented.", node);
            }

         }
      }
   }

   private void checkClassForIncorrectModifiers(ClassNode node) {
      this.checkClassForAbstractAndFinal(node);
      this.checkClassForOtherModifiers(node);
   }

   private void checkClassForAbstractAndFinal(ClassNode node) {
      if (Modifier.isAbstract(node.getModifiers())) {
         if (Modifier.isFinal(node.getModifiers())) {
            if (node.isInterface()) {
               this.addError("The " + this.getDescription(node) + " must not be final. It is by definition abstract.", node);
            } else {
               this.addError("The " + this.getDescription(node) + " must not be both final and abstract.", node);
            }

         }
      }
   }

   private void checkClassForOtherModifiers(ClassNode node) {
      this.checkClassForModifier(node, Modifier.isTransient(node.getModifiers()), "transient");
      this.checkClassForModifier(node, Modifier.isVolatile(node.getModifiers()), "volatile");
      this.checkClassForModifier(node, Modifier.isNative(node.getModifiers()), "native");
   }

   private void checkMethodForModifier(MethodNode node, boolean condition, String modifierName) {
      if (condition) {
         this.addError("The " + this.getDescription(node) + " has an incorrect modifier " + modifierName + ".", node);
      }
   }

   private void checkClassForModifier(ClassNode node, boolean condition, String modifierName) {
      if (condition) {
         this.addError("The " + this.getDescription(node) + " has an incorrect modifier " + modifierName + ".", node);
      }
   }

   private String getDescription(ClassNode node) {
      return (node.isInterface() ? "interface" : "class") + " '" + node.getName() + "'";
   }

   private String getDescription(MethodNode node) {
      return "method '" + node.getTypeDescriptor() + "'";
   }

   private String getDescription(FieldNode node) {
      return "field '" + node.getName() + "'";
   }

   private void checkAbstractDeclaration(MethodNode methodNode) {
      if (Modifier.isAbstract(methodNode.getModifiers())) {
         if (!Modifier.isAbstract(this.currentClass.getModifiers())) {
            this.addError("Can't have an abstract method in a non-abstract class. The " + this.getDescription(this.currentClass) + " must be declared abstract or the method '" + methodNode.getTypeDescriptor() + "' must not be abstract.", methodNode);
         }
      }
   }

   private void checkClassForOverwritingFinal(ClassNode cn) {
      ClassNode superCN = cn.getSuperClass();
      if (superCN != null) {
         if (Modifier.isFinal(superCN.getModifiers())) {
            StringBuffer msg = new StringBuffer();
            msg.append("You are not allowed to overwrite the final ");
            msg.append(this.getDescription(superCN));
            msg.append(".");
            this.addError(msg.toString(), cn);
         }
      }
   }

   private void checkImplementsAndExtends(ClassNode node) {
      ClassNode cn = node.getSuperClass();
      if (cn.isInterface() && !node.isInterface()) {
         this.addError("You are not allowed to extend the " + this.getDescription(cn) + ", use implements instead.", node);
      }

      ClassNode[] arr$ = node.getInterfaces();
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         ClassNode anInterface = arr$[i$];
         if (!anInterface.isInterface()) {
            this.addError("You are not allowed to implement the " + this.getDescription(anInterface) + ", use extends instead.", node);
         }
      }

   }

   private void checkMethodsForIncorrectModifiers(ClassNode cn) {
      if (cn.isInterface()) {
         Iterator i$ = cn.getMethods().iterator();

         while(i$.hasNext()) {
            MethodNode method = (MethodNode)i$.next();
            if (Modifier.isFinal(method.getModifiers())) {
               this.addError("The " + this.getDescription(method) + " from " + this.getDescription(cn) + " must not be final. It is by definition abstract.", method);
            }

            if (Modifier.isStatic(method.getModifiers()) && !this.isConstructor(method)) {
               this.addError("The " + this.getDescription(method) + " from " + this.getDescription(cn) + " must not be static. Only fields may be static in an interface.", method);
            }
         }

      }
   }

   private boolean isConstructor(MethodNode method) {
      return method.getName().equals("<clinit>");
   }

   private void checkMethodsForOverridingFinal(ClassNode cn) {
      Iterator i$ = cn.getMethods().iterator();

      MethodNode method;
      Parameter[] params;
      MethodNode superMethod;
      label26:
      do {
         while(i$.hasNext()) {
            method = (MethodNode)i$.next();
            params = method.getParameters();
            Iterator i$ = cn.getSuperClass().getMethods(method.getName()).iterator();

            while(i$.hasNext()) {
               superMethod = (MethodNode)i$.next();
               Parameter[] superParams = superMethod.getParameters();
               if (this.hasEqualParameterTypes(params, superParams)) {
                  continue label26;
               }
            }
         }

         return;
      } while(!Modifier.isFinal(superMethod.getModifiers()));

      this.addInvalidUseOfFinalError(method, params, superMethod.getDeclaringClass());
   }

   private void addInvalidUseOfFinalError(MethodNode method, Parameter[] parameters, ClassNode superCN) {
      StringBuffer msg = new StringBuffer();
      msg.append("You are not allowed to override the final method ").append(method.getName());
      msg.append("(");
      boolean needsComma = false;
      Parameter[] arr$ = parameters;
      int len$ = parameters.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Parameter parameter = arr$[i$];
         if (needsComma) {
            msg.append(",");
         } else {
            needsComma = true;
         }

         msg.append(parameter.getType());
      }

      msg.append(") from ").append(this.getDescription(superCN));
      msg.append(".");
      this.addError(msg.toString(), method);
   }

   private boolean hasEqualParameterTypes(Parameter[] first, Parameter[] second) {
      if (first.length != second.length) {
         return false;
      } else {
         for(int i = 0; i < first.length; ++i) {
            String ft = first[i].getType().getName();
            String st = second[i].getType().getName();
            if (!ft.equals(st)) {
               return false;
            }
         }

         return true;
      }
   }

   protected SourceUnit getSourceUnit() {
      return this.source;
   }

   public void visitMethod(MethodNode node) {
      this.inConstructor = false;
      this.inStaticConstructor = node.isStaticConstructor();
      this.checkAbstractDeclaration(node);
      this.checkRepetitiveMethod(node);
      this.checkOverloadingPrivateAndPublic(node);
      this.checkMethodModifiers(node);
      super.visitMethod(node);
   }

   private void checkMethodModifiers(MethodNode node) {
      if ((this.currentClass.getModifiers() & 512) != 0) {
         this.checkMethodForModifier(node, Modifier.isStrict(node.getModifiers()), "strictfp");
         this.checkMethodForModifier(node, Modifier.isSynchronized(node.getModifiers()), "synchronized");
         this.checkMethodForModifier(node, Modifier.isNative(node.getModifiers()), "native");
      }

   }

   private void checkOverloadingPrivateAndPublic(MethodNode node) {
      if (!this.isConstructor(node)) {
         boolean hasPrivate = false;
         boolean hasPublic = false;
         Iterator i$ = this.currentClass.getMethods(node.getName()).iterator();

         while(true) {
            while(true) {
               MethodNode method;
               do {
                  do {
                     if (!i$.hasNext()) {
                        if (hasPrivate && hasPublic) {
                           this.addError("Mixing private and public/protected methods of the same name causes multimethods to be disabled and is forbidden to avoid surprising behaviour. Renaming the private methods will solve the problem.", node);
                        }

                        return;
                     }

                     method = (MethodNode)i$.next();
                  } while(method == node);
               } while(!method.getDeclaringClass().equals(node.getDeclaringClass()));

               int modifiers = method.getModifiers();
               if (!Modifier.isPublic(modifiers) && !Modifier.isProtected(modifiers)) {
                  hasPrivate = true;
               } else {
                  hasPublic = true;
               }
            }
         }
      }
   }

   private void checkRepetitiveMethod(MethodNode node) {
      if (!this.isConstructor(node)) {
         Iterator i$ = this.currentClass.getMethods(node.getName()).iterator();

         while(i$.hasNext()) {
            MethodNode method = (MethodNode)i$.next();
            if (method != node && method.getDeclaringClass().equals(node.getDeclaringClass())) {
               Parameter[] p1 = node.getParameters();
               Parameter[] p2 = method.getParameters();
               if (p1.length == p2.length) {
                  this.addErrorIfParamsAndReturnTypeEqual(p2, p1, node, method);
               }
            }
         }

      }
   }

   private void addErrorIfParamsAndReturnTypeEqual(Parameter[] p2, Parameter[] p1, MethodNode node, MethodNode element) {
      boolean isEqual = true;

      for(int i = 0; i < p2.length; ++i) {
         isEqual &= p1[i].getType().equals(p2[i].getType());
      }

      isEqual &= node.getReturnType().equals(element.getReturnType());
      if (isEqual) {
         this.addError("Repetitive method name/signature for " + this.getDescription(node) + " in " + this.getDescription(this.currentClass) + ".", node);
      }

   }

   public void visitField(FieldNode node) {
      if (this.currentClass.getDeclaredField(node.getName()) != node) {
         this.addError("The " + this.getDescription(node) + " is declared multiple times.", node);
      }

      this.checkInterfaceFieldModifiers(node);
      super.visitField(node);
   }

   public void visitProperty(PropertyNode node) {
      this.checkDuplicateProperties(node);
      super.visitProperty(node);
   }

   private void checkDuplicateProperties(PropertyNode node) {
      ClassNode cn = node.getDeclaringClass();
      String name = node.getName();
      String getterName = "get" + MetaClassHelper.capitalize(name);
      if (Character.isUpperCase(name.charAt(0))) {
         Iterator i$ = cn.getProperties().iterator();

         while(i$.hasNext()) {
            PropertyNode propNode = (PropertyNode)i$.next();
            String otherName = propNode.getField().getName();
            String otherGetterName = "get" + MetaClassHelper.capitalize(otherName);
            if (node != propNode && getterName.equals(otherGetterName)) {
               String msg = "The field " + name + " and " + otherName + " on the class " + cn.getName() + " will result in duplicate JavaBean properties, which is not allowed";
               this.addError(msg, node);
            }
         }
      }

   }

   private void checkInterfaceFieldModifiers(FieldNode node) {
      if (this.currentClass.isInterface()) {
         if ((node.getModifiers() & 25) == 0) {
            this.addError("The " + this.getDescription(node) + " is not 'public final static' but is defined in the " + this.getDescription(this.currentClass) + ".", node);
         }

      }
   }

   public void visitBinaryExpression(BinaryExpression expression) {
      if (expression.getOperation().getType() == 30 && expression.getRightExpression() instanceof MapEntryExpression) {
         this.addError("You tried to use a map entry for an index operation, this is not allowed. Maybe something should be set in parentheses or a comma is missing?", expression.getRightExpression());
      }

      super.visitBinaryExpression(expression);
      switch(expression.getOperation().getType()) {
      case 100:
      case 210:
      case 211:
      case 212:
      case 213:
      case 214:
      case 215:
      case 216:
      case 285:
      case 286:
      case 287:
      case 350:
      case 351:
      case 352:
         this.checkFinalFieldAccess(expression.getLeftExpression());
      default:
      }
   }

   private void checkFinalFieldAccess(Expression expression) {
      if (expression instanceof VariableExpression || expression instanceof PropertyExpression) {
         org.codehaus.groovy.ast.Variable v = null;
         if (expression instanceof VariableExpression) {
            VariableExpression ve = (VariableExpression)expression;
            v = ve.getAccessedVariable();
         } else {
            PropertyExpression propExp = (PropertyExpression)expression;
            Expression objectExpression = propExp.getObjectExpression();
            if (objectExpression instanceof VariableExpression) {
               VariableExpression varExp = (VariableExpression)objectExpression;
               if (varExp.isThisExpression()) {
                  v = this.currentClass.getDeclaredField(propExp.getPropertyAsString());
               }
            }
         }

         if (v instanceof FieldNode) {
            FieldNode fn = (FieldNode)v;
            int modifiers = fn.getModifiers();
            boolean isFinal = (modifiers & 16) != 0;
            boolean isStatic = (modifiers & 8) != 0;
            boolean error = isFinal && (isStatic && !this.inStaticConstructor || !isStatic && !this.inConstructor);
            if (error) {
               this.addError("cannot modify" + (isStatic ? " static" : "") + " final field '" + fn.getName() + "' outside of " + (isStatic ? "static initialization block." : "constructor."), expression);
            }
         }

      }
   }

   public void visitConstructor(ConstructorNode node) {
      this.inConstructor = true;
      this.inStaticConstructor = node.isStaticConstructor();
      super.visitConstructor(node);
   }

   public void visitCatchStatement(CatchStatement cs) {
      if (!cs.getExceptionType().isDerivedFrom(ClassHelper.make(Throwable.class))) {
         this.addError("Catch statement parameter type is not a subclass of Throwable.", cs);
      }

      super.visitCatchStatement(cs);
   }

   public void visitMethodCallExpression(MethodCallExpression mce) {
      super.visitMethodCallExpression(mce);
      Expression aexp = mce.getArguments();
      if (aexp instanceof TupleExpression) {
         TupleExpression arguments = (TupleExpression)aexp;
         Iterator i$ = arguments.getExpressions().iterator();

         while(i$.hasNext()) {
            Expression e = (Expression)i$.next();
            this.checkForInvalidDeclaration(e);
         }
      } else {
         this.checkForInvalidDeclaration(aexp);
      }

   }

   private void checkForInvalidDeclaration(Expression exp) {
      if (exp instanceof DeclarationExpression) {
         this.addError("Invalid use of declaration inside method call.", exp);
      }
   }

   public void visitConstantExpression(ConstantExpression expression) {
      super.visitConstantExpression(expression);
      this.checkStringExceedingMaximumLength(expression);
   }

   public void visitGStringExpression(GStringExpression expression) {
      super.visitGStringExpression(expression);
      Iterator i$ = expression.getStrings().iterator();

      while(i$.hasNext()) {
         ConstantExpression ce = (ConstantExpression)i$.next();
         this.checkStringExceedingMaximumLength(ce);
      }

   }

   private void checkStringExceedingMaximumLength(ConstantExpression expression) {
      Object value = expression.getValue();
      if (value instanceof String) {
         String s = (String)value;
         if (s.length() > 65535) {
            this.addError("String too long. The given string is " + s.length() + " Unicode code units long, but only a maximum of 65535 is allowed.", expression);
         }
      }

   }
}
