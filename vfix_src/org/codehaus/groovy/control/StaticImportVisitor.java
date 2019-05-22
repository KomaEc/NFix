package org.codehaus.groovy.control;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.codehaus.groovy.ast.AnnotatedNode;
import org.codehaus.groovy.ast.AnnotationNode;
import org.codehaus.groovy.ast.ClassCodeExpressionTransformer;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.DynamicVariable;
import org.codehaus.groovy.ast.FieldNode;
import org.codehaus.groovy.ast.ImportNode;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.ModuleNode;
import org.codehaus.groovy.ast.PropertyNode;
import org.codehaus.groovy.ast.Variable;
import org.codehaus.groovy.ast.expr.AnnotationConstantExpression;
import org.codehaus.groovy.ast.expr.ArgumentListExpression;
import org.codehaus.groovy.ast.expr.BinaryExpression;
import org.codehaus.groovy.ast.expr.ClassExpression;
import org.codehaus.groovy.ast.expr.ClosureExpression;
import org.codehaus.groovy.ast.expr.ConstantExpression;
import org.codehaus.groovy.ast.expr.ConstructorCallExpression;
import org.codehaus.groovy.ast.expr.EmptyExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.ListExpression;
import org.codehaus.groovy.ast.expr.MapEntryExpression;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.ast.expr.NamedArgumentListExpression;
import org.codehaus.groovy.ast.expr.PropertyExpression;
import org.codehaus.groovy.ast.expr.StaticMethodCallExpression;
import org.codehaus.groovy.ast.expr.TupleExpression;
import org.codehaus.groovy.ast.expr.VariableExpression;
import org.codehaus.groovy.ast.stmt.Statement;
import org.codehaus.groovy.runtime.MetaClassHelper;

public class StaticImportVisitor extends ClassCodeExpressionTransformer {
   private ClassNode currentClass;
   private MethodNode currentMethod;
   private SourceUnit source;
   private CompilationUnit compilationUnit;
   private boolean stillResolving;
   private boolean inSpecialConstructorCall;
   private boolean inClosure;
   private boolean inPropertyExpression;
   private Expression foundConstant;
   private Expression foundArgs;
   private boolean inAnnotation;
   private boolean inLeftExpression;

   public StaticImportVisitor(CompilationUnit cu) {
      this.compilationUnit = cu;
   }

   public void visitClass(ClassNode node, SourceUnit source) {
      this.currentClass = node;
      this.source = source;
      super.visitClass(node);
   }

   protected void visitConstructorOrMethod(MethodNode node, boolean isConstructor) {
      this.currentMethod = node;
      super.visitConstructorOrMethod(node, isConstructor);
      this.currentMethod = null;
   }

   public void visitAnnotations(AnnotatedNode node) {
      boolean oldInAnnotation = this.inAnnotation;
      this.inAnnotation = true;
      super.visitAnnotations(node);
      this.inAnnotation = oldInAnnotation;
   }

   public Expression transform(Expression exp) {
      if (exp == null) {
         return null;
      } else if (exp.getClass() == VariableExpression.class) {
         return this.transformVariableExpression((VariableExpression)exp);
      } else if (exp.getClass() == BinaryExpression.class) {
         return this.transformBinaryExpression((BinaryExpression)exp);
      } else if (exp.getClass() == PropertyExpression.class) {
         return this.transformPropertyExpression((PropertyExpression)exp);
      } else if (exp.getClass() == MethodCallExpression.class) {
         return this.transformMethodCallExpression((MethodCallExpression)exp);
      } else if (exp.getClass() == ClosureExpression.class) {
         return this.transformClosureExpression((ClosureExpression)exp);
      } else if (exp.getClass() == ConstructorCallExpression.class) {
         return this.transformConstructorCallExpression((ConstructorCallExpression)exp);
      } else {
         Expression result;
         if (exp.getClass() == ArgumentListExpression.class) {
            result = exp.transformExpression(this);
            if (this.inPropertyExpression) {
               this.foundArgs = result;
            }

            return result;
         } else if (exp instanceof ConstantExpression) {
            result = exp.transformExpression(this);
            if (this.inPropertyExpression) {
               this.foundConstant = result;
            }

            if (this.inAnnotation && exp instanceof AnnotationConstantExpression) {
               ConstantExpression ce = (ConstantExpression)result;
               if (ce.getValue() instanceof AnnotationNode) {
                  AnnotationNode an = (AnnotationNode)ce.getValue();
                  Map<String, Expression> attributes = an.getMembers();
                  Iterator i$ = attributes.entrySet().iterator();

                  while(i$.hasNext()) {
                     Entry entry = (Entry)i$.next();
                     Expression attrExpr = this.transform((Expression)entry.getValue());
                     entry.setValue(attrExpr);
                  }
               }
            }

            return result;
         } else {
            return exp.transformExpression(this);
         }
      }
   }

   private Expression transformMapEntryExpression(MapEntryExpression me, ClassNode constructorCallType) {
      Expression key = me.getKeyExpression();
      Expression value = me.getValueExpression();
      ModuleNode module = this.currentClass.getModule();
      if (module != null && key instanceof ConstantExpression) {
         Map<String, ImportNode> importNodes = module.getStaticImports();
         if (importNodes.containsKey(key.getText())) {
            ImportNode importNode = (ImportNode)importNodes.get(key.getText());
            if (importNode.getType().equals(constructorCallType)) {
               String newKey = importNode.getFieldName();
               return new MapEntryExpression(new ConstantExpression(newKey), value.transformExpression(this));
            }
         }
      }

      return me;
   }

   protected Expression transformBinaryExpression(BinaryExpression be) {
      int type = be.getOperation().getType();
      Expression right = this.transform(be.getRightExpression());
      be.setRightExpression(right);
      Expression left;
      if (type == 100) {
         boolean oldInLeftExpression = this.inLeftExpression;
         this.inLeftExpression = true;
         left = this.transform(be.getLeftExpression());
         this.inLeftExpression = oldInLeftExpression;
         if (left instanceof StaticMethodCallExpression) {
            StaticMethodCallExpression smce = (StaticMethodCallExpression)left;
            StaticMethodCallExpression result = new StaticMethodCallExpression(smce.getOwnerType(), smce.getMethod(), right);
            this.setSourcePosition(result, be);
            return result;
         }
      } else {
         left = this.transform(be.getLeftExpression());
      }

      be.setLeftExpression(left);
      return be;
   }

   protected Expression transformVariableExpression(VariableExpression ve) {
      Variable v = ve.getAccessedVariable();
      if (v != null && v instanceof DynamicVariable) {
         Expression result = this.findStaticFieldOrPropAccessorImportFromModule(v.getName());
         if (result != null) {
            this.setSourcePosition(result, ve);
            if (this.inAnnotation) {
               result = this.transformInlineConstants(result);
            }

            return result;
         }

         if (!this.inPropertyExpression || this.inSpecialConstructorCall) {
            this.addStaticVariableError(ve);
         }
      }

      return ve;
   }

   private void setSourcePosition(Expression toSet, Expression origNode) {
      toSet.setSourcePosition(origNode);
      if (toSet instanceof PropertyExpression) {
         ((PropertyExpression)toSet).getProperty().setSourcePosition(origNode);
      }

   }

   private Expression transformInlineConstants(Expression exp) {
      Expression constant;
      if (exp instanceof PropertyExpression) {
         PropertyExpression pe = (PropertyExpression)exp;
         if (pe.getObjectExpression() instanceof ClassExpression) {
            ClassExpression ce = (ClassExpression)pe.getObjectExpression();
            ClassNode type = ce.getType();
            if (type.isEnum()) {
               return exp;
            }

            constant = this.findConstant(type.getField(pe.getPropertyAsString()));
            if (constant != null) {
               return constant;
            }
         }
      } else if (exp instanceof ListExpression) {
         ListExpression le = (ListExpression)exp;
         ListExpression result = new ListExpression();
         Iterator i$ = le.getExpressions().iterator();

         while(i$.hasNext()) {
            constant = (Expression)i$.next();
            result.addExpression(this.transformInlineConstants(constant));
         }

         return result;
      }

      return exp;
   }

   private Expression findConstant(FieldNode fn) {
      return fn != null && !fn.isEnum() && fn.isStatic() && fn.isFinal() && fn.getInitialValueExpression() instanceof ConstantExpression ? fn.getInitialValueExpression() : null;
   }

   protected Expression transformMethodCallExpression(MethodCallExpression mce) {
      Expression args = this.transform(mce.getArguments());
      Expression method = this.transform(mce.getMethod());
      Expression object = this.transform(mce.getObjectExpression());
      boolean isExplicitThisOrSuper = false;
      if (object instanceof VariableExpression) {
         VariableExpression ve = (VariableExpression)object;
         isExplicitThisOrSuper = !mce.isImplicitThis() && (ve.getName().equals("this") || ve.getName().equals("super"));
         boolean isExplicitSuper = ve.getName().equals("super");
         if (isExplicitSuper && this.currentMethod != null && this.currentMethod.isStatic()) {
            this.addError("'super' cannot be used in a static context, use the explicit class instead.", mce);
            return mce;
         }
      }

      MethodCallExpression ret;
      if (mce.isImplicitThis() || isExplicitThisOrSuper) {
         if (mce.isImplicitThis()) {
            Expression ret = this.findStaticMethodImportFromModule(method, args);
            if (ret != null) {
               this.setSourcePosition(ret, mce);
               return ret;
            }

            if (method instanceof ConstantExpression && !this.inLeftExpression) {
               String methodName = (String)((ConstantExpression)method).getValue();
               ret = this.findStaticFieldOrPropAccessorImportFromModule(methodName);
               if (ret != null) {
                  ret = new MethodCallExpression(ret, "call", args);
                  this.setSourcePosition(ret, mce);
                  return ret;
               }
            }
         }

         if (method instanceof ConstantExpression) {
            ConstantExpression ce = (ConstantExpression)method;
            Object value = ce.getValue();
            if (value instanceof String) {
               String methodName = (String)value;
               boolean lookForPossibleStaticMethod = !methodName.equals("call");
               if (this.currentMethod != null && !this.currentMethod.isStatic() && this.currentClass.hasPossibleMethod(methodName, args)) {
                  lookForPossibleStaticMethod = false;
               }

               if (this.inSpecialConstructorCall || lookForPossibleStaticMethod && this.currentClass.hasPossibleStaticMethod(methodName, args)) {
                  StaticMethodCallExpression smce = new StaticMethodCallExpression(this.currentClass, methodName, args);
                  this.setSourcePosition(smce, mce);
                  return smce;
               }
            }
         }
      }

      ret = new MethodCallExpression(object, method, args);
      ret.setSafe(mce.isSafe());
      ret.setImplicitThis(mce.isImplicitThis());
      ret.setSpreadSafe(mce.isSpreadSafe());
      this.setSourcePosition(ret, mce);
      return ret;
   }

   protected Expression transformConstructorCallExpression(ConstructorCallExpression cce) {
      this.inSpecialConstructorCall = cce.isSpecialCall();
      Expression expression = cce.getArguments();
      if (expression instanceof TupleExpression) {
         TupleExpression tuple = (TupleExpression)expression;
         if (tuple.getExpressions().size() == 1) {
            expression = tuple.getExpression(0);
            if (expression instanceof NamedArgumentListExpression) {
               NamedArgumentListExpression namedArgs = (NamedArgumentListExpression)expression;
               List<MapEntryExpression> entryExpressions = namedArgs.getMapEntryExpressions();

               for(int i = 0; i < entryExpressions.size(); ++i) {
                  entryExpressions.set(i, (MapEntryExpression)this.transformMapEntryExpression((MapEntryExpression)entryExpressions.get(i), cce.getType()));
               }
            }
         }
      }

      Expression ret = cce.transformExpression(this);
      this.inSpecialConstructorCall = false;
      return ret;
   }

   protected Expression transformClosureExpression(ClosureExpression ce) {
      boolean oldInClosure = this.inClosure;
      this.inClosure = true;
      Statement code = ce.getCode();
      if (code != null) {
         code.visit(this);
      }

      this.inClosure = oldInClosure;
      return ce;
   }

   protected Expression transformPropertyExpression(PropertyExpression pe) {
      boolean oldInPropertyExpression = this.inPropertyExpression;
      Expression oldFoundArgs = this.foundArgs;
      Expression oldFoundConstant = this.foundConstant;
      this.inPropertyExpression = true;
      this.foundArgs = null;
      this.foundConstant = null;
      Expression objectExpression = this.transform(pe.getObjectExpression());
      if (objectExpression instanceof VariableExpression) {
         VariableExpression ve = (VariableExpression)objectExpression;
         boolean isExplicitSuper = ve.getName().equals("super");
         if (isExplicitSuper && this.currentMethod != null && this.currentMethod.isStatic()) {
            this.addError("'super' cannot be used in a static context, use the explicit class instead.", pe);
            return null;
         }
      }

      if (this.foundArgs != null && this.foundConstant != null) {
         Expression result = this.findStaticMethodImportFromModule(this.foundConstant, this.foundArgs);
         if (result != null) {
            objectExpression = result;
         }
      }

      this.inPropertyExpression = oldInPropertyExpression;
      this.foundArgs = oldFoundArgs;
      this.foundConstant = oldFoundConstant;
      pe.setObjectExpression(objectExpression);
      if (!this.inSpecialConstructorCall) {
         this.checkStaticScope(pe);
      }

      return pe;
   }

   private void checkStaticScope(PropertyExpression pe) {
      if (!this.inClosure) {
         for(Object it = pe; it != null; it = ((PropertyExpression)it).getObjectExpression()) {
            if (!(it instanceof PropertyExpression)) {
               if (it instanceof VariableExpression) {
                  this.addStaticVariableError((VariableExpression)it);
               }

               return;
            }
         }

      }
   }

   private void addStaticVariableError(VariableExpression ve) {
      if (this.inSpecialConstructorCall || !this.inClosure && ve.isInStaticContext()) {
         if (!this.stillResolving) {
            if (!ve.isThisExpression() && !ve.isSuperExpression()) {
               if (this.currentMethod != null && this.currentMethod.isStatic()) {
                  FieldNode fieldNode = this.currentMethod.getDeclaringClass().getField(ve.getName());
                  if (fieldNode != null && fieldNode.isStatic()) {
                     return;
                  }
               }

               Variable v = ve.getAccessedVariable();
               if (v == null || v instanceof DynamicVariable || !v.isInStaticContext()) {
                  this.addError("Apparent variable '" + ve.getName() + "' was found in a static scope but doesn't refer" + " to a local variable, static field or class. Possible causes:\n" + "You attempted to reference a variable in the binding or an instance variable from a static context.\n" + "You misspelled a classname or statically imported field. Please check the spelling.\n" + "You attempted to use a method '" + ve.getName() + "' but left out brackets in a place not allowed by the grammar.", ve);
               }
            }
         }
      }
   }

   private Expression findStaticFieldOrPropAccessorImportFromModule(String name) {
      ModuleNode module = this.currentClass.getModule();
      if (module == null) {
         return null;
      } else {
         Map<String, ImportNode> importNodes = module.getStaticImports();
         this.stillResolving = false;
         String accessorName = this.getAccessorName(name);
         ImportNode importNode;
         Expression expression;
         if (importNodes.containsKey(accessorName)) {
            importNode = (ImportNode)importNodes.get(accessorName);
            expression = this.findStaticPropertyAccessorByFullName(importNode.getType(), importNode.getFieldName());
            if (expression != null) {
               return expression;
            }

            expression = this.findStaticPropertyAccessor(importNode.getType(), this.getPropNameForAccessor(importNode.getFieldName()));
            if (expression != null) {
               return expression;
            }
         }

         if (accessorName.startsWith("get")) {
            accessorName = "is" + accessorName.substring(3);
            if (importNodes.containsKey(accessorName)) {
               importNode = (ImportNode)importNodes.get(accessorName);
               expression = this.findStaticPropertyAccessorByFullName(importNode.getType(), importNode.getFieldName());
               if (expression != null) {
                  return expression;
               }

               expression = this.findStaticPropertyAccessor(importNode.getType(), this.getPropNameForAccessor(importNode.getFieldName()));
               if (expression != null) {
                  return expression;
               }
            }
         }

         if (importNodes.containsKey(name)) {
            importNode = (ImportNode)importNodes.get(name);
            expression = this.findStaticPropertyAccessor(importNode.getType(), importNode.getFieldName());
            if (expression != null) {
               return expression;
            }

            expression = this.findStaticField(importNode.getType(), importNode.getFieldName());
            if (expression != null) {
               return expression;
            }
         }

         Iterator i$ = module.getStaticStarImports().values().iterator();

         do {
            if (!i$.hasNext()) {
               return null;
            }

            ImportNode importNode = (ImportNode)i$.next();
            ClassNode node = importNode.getType();
            expression = this.findStaticPropertyAccessor(node, name);
            if (expression != null) {
               return expression;
            }

            expression = this.findStaticField(node, name);
         } while(expression == null);

         return expression;
      }
   }

   private Expression findStaticMethodImportFromModule(Expression method, Expression args) {
      ModuleNode module = this.currentClass.getModule();
      if (module != null && method instanceof ConstantExpression) {
         Map<String, ImportNode> importNodes = module.getStaticImports();
         ConstantExpression ce = (ConstantExpression)method;
         Object value = ce.getValue();
         if (!(value instanceof String)) {
            return null;
         } else {
            String name = (String)value;
            Expression expression;
            if (importNodes.containsKey(name)) {
               ImportNode importNode = (ImportNode)importNodes.get(name);
               expression = this.findStaticMethod(importNode.getType(), importNode.getFieldName(), args);
               if (expression != null) {
                  return expression;
               }

               expression = this.findStaticPropertyAccessorGivenArgs(importNode.getType(), this.getPropNameForAccessor(importNode.getFieldName()), args);
               if (expression != null) {
                  return new StaticMethodCallExpression(importNode.getType(), importNode.getFieldName(), args);
               }
            }

            if (this.validPropName(name)) {
               String propName = this.getPropNameForAccessor(name);
               if (importNodes.containsKey(propName)) {
                  ImportNode importNode = (ImportNode)importNodes.get(propName);
                  expression = this.findStaticMethod(importNode.getType(), this.prefix(name) + MetaClassHelper.capitalize(importNode.getFieldName()), args);
                  if (expression != null) {
                     return expression;
                  }

                  expression = this.findStaticPropertyAccessorGivenArgs(importNode.getType(), importNode.getFieldName(), args);
                  if (expression != null) {
                     return new StaticMethodCallExpression(importNode.getType(), this.prefix(name) + MetaClassHelper.capitalize(importNode.getFieldName()), args);
                  }
               }
            }

            Map<String, ImportNode> starImports = module.getStaticStarImports();
            ClassNode starImportType;
            if (this.isEnum(this.currentClass) && starImports.containsKey(this.currentClass.getName())) {
               ImportNode importNode = (ImportNode)starImports.get(this.currentClass.getName());
               starImportType = importNode == null ? null : importNode.getType();
               expression = this.findStaticMethod(starImportType, name, args);
               if (expression != null) {
                  return expression;
               }
            } else {
               Iterator i$ = starImports.values().iterator();

               while(i$.hasNext()) {
                  ImportNode importNode = (ImportNode)i$.next();
                  starImportType = importNode == null ? null : importNode.getType();
                  expression = this.findStaticMethod(starImportType, name, args);
                  if (expression != null) {
                     return expression;
                  }

                  expression = this.findStaticPropertyAccessorGivenArgs(starImportType, this.getPropNameForAccessor(name), args);
                  if (expression != null) {
                     return new StaticMethodCallExpression(starImportType, name, args);
                  }
               }
            }

            return null;
         }
      } else {
         return null;
      }
   }

   private String prefix(String name) {
      return name.startsWith("is") ? "is" : name.substring(0, 3);
   }

   private String getPropNameForAccessor(String fieldName) {
      int prefixLength = fieldName.startsWith("is") ? 2 : 3;
      if (fieldName.length() < prefixLength + 1) {
         return fieldName;
      } else {
         return !this.validPropName(fieldName) ? fieldName : String.valueOf(fieldName.charAt(prefixLength)).toLowerCase() + fieldName.substring(prefixLength + 1);
      }
   }

   private boolean validPropName(String propName) {
      return propName.startsWith("get") || propName.startsWith("is") || propName.startsWith("set");
   }

   private String getAccessorName(String name) {
      return (this.inLeftExpression ? "set" : "get") + MetaClassHelper.capitalize(name);
   }

   private Expression findStaticPropertyAccessorGivenArgs(ClassNode staticImportType, String propName, Expression args) {
      return this.findStaticPropertyAccessor(staticImportType, propName);
   }

   private Expression findStaticPropertyAccessor(ClassNode staticImportType, String propName) {
      String accessorName = this.getAccessorName(propName);
      Expression accessor = this.findStaticPropertyAccessorByFullName(staticImportType, accessorName);
      if (accessor == null && accessorName.startsWith("get")) {
         accessor = this.findStaticPropertyAccessorByFullName(staticImportType, "is" + accessorName.substring(3));
      }

      if (accessor == null && this.hasStaticProperty(staticImportType, propName)) {
         if (this.inLeftExpression) {
            accessor = new StaticMethodCallExpression(staticImportType, accessorName, ArgumentListExpression.EMPTY_ARGUMENTS);
         } else {
            accessor = new PropertyExpression(new ClassExpression(staticImportType), propName);
         }
      }

      return (Expression)accessor;
   }

   private boolean hasStaticProperty(ClassNode staticImportType, String propName) {
      for(ClassNode classNode = staticImportType; classNode != null; classNode = classNode.getSuperClass()) {
         Iterator i$ = classNode.getProperties().iterator();

         while(i$.hasNext()) {
            PropertyNode pn = (PropertyNode)i$.next();
            if (pn.getName().equals(propName) && pn.isStatic()) {
               return true;
            }
         }
      }

      return false;
   }

   private Expression findStaticPropertyAccessorByFullName(ClassNode staticImportType, String accessorMethodName) {
      ArgumentListExpression dummyArgs = new ArgumentListExpression();
      dummyArgs.addExpression(new EmptyExpression());
      return this.findStaticMethod(staticImportType, accessorMethodName, this.inLeftExpression ? dummyArgs : ArgumentListExpression.EMPTY_ARGUMENTS);
   }

   private Expression findStaticField(ClassNode staticImportType, String fieldName) {
      if (!staticImportType.isPrimaryClassNode() && !staticImportType.isResolved()) {
         this.stillResolving = true;
      } else {
         staticImportType.getFields();
         FieldNode field = staticImportType.getField(fieldName);
         if (field != null && field.isStatic()) {
            return new PropertyExpression(new ClassExpression(staticImportType), fieldName);
         }
      }

      return null;
   }

   private Expression findStaticMethod(ClassNode staticImportType, String methodName, Expression args) {
      return (staticImportType.isPrimaryClassNode() || staticImportType.isResolved()) && staticImportType.hasPossibleStaticMethod(methodName, args) ? new StaticMethodCallExpression(staticImportType, methodName, args) : null;
   }

   protected SourceUnit getSourceUnit() {
      return this.source;
   }

   private boolean isEnum(ClassNode node) {
      return (node.getModifiers() & 16384) != 0;
   }
}
