package org.codehaus.groovy.classgen;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import groovy.lang.GroovyObjectSupport;
import groovy.lang.MetaClass;
import groovyjarjarasm.asm.Label;
import groovyjarjarasm.asm.MethodVisitor;
import groovyjarjarasm.asm.Opcodes;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.CodeVisitorSupport;
import org.codehaus.groovy.ast.ConstructorNode;
import org.codehaus.groovy.ast.FieldNode;
import org.codehaus.groovy.ast.GenericsType;
import org.codehaus.groovy.ast.GroovyClassVisitor;
import org.codehaus.groovy.ast.InnerClassNode;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.Parameter;
import org.codehaus.groovy.ast.PropertyNode;
import org.codehaus.groovy.ast.VariableScope;
import org.codehaus.groovy.ast.expr.ArgumentListExpression;
import org.codehaus.groovy.ast.expr.BinaryExpression;
import org.codehaus.groovy.ast.expr.CastExpression;
import org.codehaus.groovy.ast.expr.ClosureExpression;
import org.codehaus.groovy.ast.expr.ConstantExpression;
import org.codehaus.groovy.ast.expr.ConstructorCallExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.FieldExpression;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.ast.expr.VariableExpression;
import org.codehaus.groovy.ast.stmt.BlockStatement;
import org.codehaus.groovy.ast.stmt.ExpressionStatement;
import org.codehaus.groovy.ast.stmt.ReturnStatement;
import org.codehaus.groovy.ast.stmt.Statement;
import org.codehaus.groovy.reflection.ClassInfo;
import org.codehaus.groovy.runtime.MetaClassHelper;
import org.codehaus.groovy.syntax.RuntimeParserException;
import org.codehaus.groovy.syntax.Token;

public class Verifier implements GroovyClassVisitor, Opcodes {
   public static final String __TIMESTAMP = "__timeStamp";
   public static final String __TIMESTAMP__ = "__timeStamp__239_neverHappen";
   private static final Parameter[] INVOKE_METHOD_PARAMS;
   private static final Parameter[] SET_PROPERTY_PARAMS;
   private static final Parameter[] GET_PROPERTY_PARAMS;
   private static final Parameter[] SET_METACLASS_PARAMS;
   private ClassNode classNode;
   private MethodNode methodNode;

   public ClassNode getClassNode() {
      return this.classNode;
   }

   public MethodNode getMethodNode() {
      return this.methodNode;
   }

   private FieldNode setMetaClassFieldIfNotExists(ClassNode node, FieldNode metaClassField) {
      if (metaClassField != null) {
         return metaClassField;
      } else {
         final String classInternalName = BytecodeHelper.getClassInternalName(node);
         metaClassField = node.addField("metaClass", 4226, ClassHelper.METACLASS_TYPE, new BytecodeExpression() {
            public void visit(MethodVisitor mv) {
               mv.visitVarInsn(25, 0);
               mv.visitMethodInsn(182, classInternalName, "$getStaticMetaClass", "()Lgroovy/lang/MetaClass;");
            }

            public ClassNode getType() {
               return ClassHelper.METACLASS_TYPE;
            }
         });
         metaClassField.setSynthetic(true);
         return metaClassField;
      }
   }

   private FieldNode getMetaClassField(ClassNode node) {
      FieldNode ret = node.getDeclaredField("metaClass");
      ClassNode current;
      if (ret != null) {
         current = ret.getType();
         if (!current.equals(ClassHelper.METACLASS_TYPE)) {
            throw new RuntimeParserException("The class " + node.getName() + " cannot declare field 'metaClass' of type " + current.getName() + " as it needs to be of " + "the type " + ClassHelper.METACLASS_TYPE.getName() + " for internal groovy purposes", ret);
         } else {
            return ret;
         }
      } else {
         current = node;

         while(current != ClassHelper.OBJECT_TYPE) {
            current = current.getSuperClass();
            if (current == null) {
               break;
            }

            ret = current.getDeclaredField("metaClass");
            if (ret != null && !Modifier.isPrivate(ret.getModifiers())) {
               return ret;
            }
         }

         return null;
      }
   }

   public void visitClass(ClassNode node) {
      this.classNode = node;
      if ((this.classNode.getModifiers() & 512) > 0) {
         ConstructorNode dummy = new ConstructorNode(0, (Statement)null);
         this.addInitialization(node, dummy);
         node.visitContents(this);
      } else {
         ClassNode[] classNodes = this.classNode.getInterfaces();
         List interfaces = new ArrayList();

         for(int i = 0; i < classNodes.length; ++i) {
            ClassNode classNode = classNodes[i];
            interfaces.add(classNode.getName());
         }

         Set interfaceSet = new HashSet(interfaces);
         if (interfaceSet.size() != interfaces.size()) {
            throw new RuntimeParserException("Duplicate interfaces in implements list: " + interfaces, this.classNode);
         } else {
            this.addDefaultParameterMethods(node);
            this.addDefaultParameterConstructors(node);
            String classInternalName = BytecodeHelper.getClassInternalName(node);
            this.addStaticMetaClassField(node, classInternalName);
            boolean knownSpecialCase = node.isDerivedFrom(ClassHelper.GSTRING_TYPE) || node.isDerivedFrom(ClassHelper.make(GroovyObjectSupport.class));
            if (!knownSpecialCase) {
               this.addGroovyObjectInterfaceAndMethods(node, classInternalName);
            }

            this.addDefaultConstructor(node);
            if (!(node instanceof InnerClassNode)) {
               this.addTimeStamp(node);
            }

            this.addInitialization(node);
            this.checkReturnInObjectInitializer(node.getObjectInitializerStatements());
            node.getObjectInitializerStatements().clear();
            this.addCovariantMethods(node);
            node.visitContents(this);
         }
      }
   }

   private void addDefaultConstructor(ClassNode node) {
      if (node.getDeclaredConstructors().isEmpty()) {
         BlockStatement empty = new BlockStatement();
         empty.setSourcePosition(node);
         ConstructorNode constructor = new ConstructorNode(1, empty);
         constructor.setSourcePosition(node);
         constructor.setHasNoRealSourcePosition(true);
         node.addConstructor(constructor);
      }
   }

   private void addStaticMetaClassField(ClassNode node, final String classInternalName) {
      final String _staticClassInfoFieldName;
      for(_staticClassInfoFieldName = "$staticClassInfo"; node.getDeclaredField(_staticClassInfoFieldName) != null; _staticClassInfoFieldName = _staticClassInfoFieldName + "$") {
      }

      FieldNode staticMetaClassField = node.addField(_staticClassInfoFieldName, 4106, ClassHelper.make(ClassInfo.class, false), (Expression)null);
      staticMetaClassField.setSynthetic(true);
      node.addSyntheticMethod("$getStaticMetaClass", 4, ClassHelper.make(MetaClass.class), Parameter.EMPTY_ARRAY, ClassNode.EMPTY_ARRAY, new BytecodeSequence(new BytecodeInstruction() {
         public void visit(MethodVisitor mv) {
            mv.visitVarInsn(25, 0);
            mv.visitMethodInsn(182, "java/lang/Object", "getClass", "()Ljava/lang/Class;");
            mv.visitMethodInsn(184, classInternalName, "$get$$class$" + classInternalName.replaceAll("\\/", "\\$"), "()Ljava/lang/Class;");
            Label l1 = new Label();
            mv.visitJumpInsn(166, l1);
            mv.visitVarInsn(25, 0);
            mv.visitMethodInsn(184, "org/codehaus/groovy/runtime/ScriptBytecodeAdapter", "initMetaClass", "(Ljava/lang/Object;)Lgroovy/lang/MetaClass;");
            mv.visitInsn(176);
            mv.visitLabel(l1);
            mv.visitFieldInsn(178, classInternalName, _staticClassInfoFieldName, "Lorg/codehaus/groovy/reflection/ClassInfo;");
            mv.visitVarInsn(58, 1);
            mv.visitVarInsn(25, 1);
            Label l0 = new Label();
            mv.visitJumpInsn(199, l0);
            mv.visitVarInsn(25, 0);
            mv.visitMethodInsn(182, "java/lang/Object", "getClass", "()Ljava/lang/Class;");
            mv.visitMethodInsn(184, "org/codehaus/groovy/reflection/ClassInfo", "getClassInfo", "(Ljava/lang/Class;)Lorg/codehaus/groovy/reflection/ClassInfo;");
            mv.visitInsn(89);
            mv.visitVarInsn(58, 1);
            mv.visitFieldInsn(179, classInternalName, _staticClassInfoFieldName, "Lorg/codehaus/groovy/reflection/ClassInfo;");
            mv.visitLabel(l0);
            mv.visitVarInsn(25, 1);
            mv.visitMethodInsn(182, "org/codehaus/groovy/reflection/ClassInfo", "getMetaClass", "()Lgroovy/lang/MetaClass;");
            mv.visitInsn(176);
         }
      }));
   }

   protected void addGroovyObjectInterfaceAndMethods(ClassNode node, final String classInternalName) {
      if (!node.isDerivedFromGroovyObject()) {
         node.addInterface(ClassHelper.make(GroovyObject.class));
      }

      FieldNode metaClassField = this.getMetaClassField(node);
      if (!node.hasMethod("getMetaClass", Parameter.EMPTY_ARRAY)) {
         metaClassField = this.setMetaClassFieldIfNotExists(node, metaClassField);
         this.addMethod(node, !Modifier.isAbstract(node.getModifiers()), "getMetaClass", 1, ClassHelper.METACLASS_TYPE, Parameter.EMPTY_ARRAY, ClassNode.EMPTY_ARRAY, new BytecodeSequence(new BytecodeInstruction() {
            public void visit(MethodVisitor mv) {
               Label nullLabel = new Label();
               mv.visitVarInsn(25, 0);
               mv.visitFieldInsn(180, classInternalName, "metaClass", "Lgroovy/lang/MetaClass;");
               mv.visitInsn(89);
               mv.visitJumpInsn(198, nullLabel);
               mv.visitInsn(176);
               mv.visitLabel(nullLabel);
               mv.visitInsn(87);
               mv.visitVarInsn(25, 0);
               mv.visitInsn(89);
               mv.visitMethodInsn(182, classInternalName, "$getStaticMetaClass", "()Lgroovy/lang/MetaClass;");
               mv.visitFieldInsn(181, classInternalName, "metaClass", "Lgroovy/lang/MetaClass;");
               mv.visitVarInsn(25, 0);
               mv.visitFieldInsn(180, classInternalName, "metaClass", "Lgroovy/lang/MetaClass;");
               mv.visitInsn(176);
            }
         }));
      }

      Parameter[] parameters = new Parameter[]{new Parameter(ClassHelper.METACLASS_TYPE, "mc")};
      if (!node.hasMethod("setMetaClass", parameters)) {
         metaClassField = this.setMetaClassFieldIfNotExists(node, metaClassField);
         Object setMetaClassCode;
         if (Modifier.isFinal(metaClassField.getModifiers())) {
            ConstantExpression text = new ConstantExpression("cannot set read-only meta class");
            ConstructorCallExpression cce = new ConstructorCallExpression(ClassHelper.make(IllegalArgumentException.class), text);
            setMetaClassCode = new ExpressionStatement(cce);
         } else {
            List list = new ArrayList();
            list.add(new BytecodeInstruction() {
               public void visit(MethodVisitor mv) {
                  mv.visitVarInsn(25, 0);
                  mv.visitVarInsn(25, 1);
                  mv.visitFieldInsn(181, classInternalName, "metaClass", "Lgroovy/lang/MetaClass;");
                  mv.visitInsn(177);
               }
            });
            setMetaClassCode = new BytecodeSequence(list);
         }

         this.addMethod(node, !Modifier.isAbstract(node.getModifiers()), "setMetaClass", 1, ClassHelper.VOID_TYPE, SET_METACLASS_PARAMS, ClassNode.EMPTY_ARRAY, (Statement)setMetaClassCode);
      }

      if (!node.hasMethod("invokeMethod", INVOKE_METHOD_PARAMS)) {
         VariableExpression vMethods = new VariableExpression("method");
         VariableExpression vArguments = new VariableExpression("arguments");
         VariableScope blockScope = new VariableScope();
         blockScope.putReferencedLocalVariable(vMethods);
         blockScope.putReferencedLocalVariable(vArguments);
         this.addMethod(node, !Modifier.isAbstract(node.getModifiers()), "invokeMethod", 1, ClassHelper.OBJECT_TYPE, INVOKE_METHOD_PARAMS, ClassNode.EMPTY_ARRAY, new BytecodeSequence(new BytecodeInstruction() {
            public void visit(MethodVisitor mv) {
               mv.visitVarInsn(25, 0);
               mv.visitMethodInsn(182, classInternalName, "getMetaClass", "()Lgroovy/lang/MetaClass;");
               mv.visitVarInsn(25, 0);
               mv.visitVarInsn(25, 1);
               mv.visitVarInsn(25, 2);
               mv.visitMethodInsn(185, "groovy/lang/MetaClass", "invokeMethod", "(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/Object;");
               mv.visitInsn(176);
            }
         }));
      }

      if (!node.hasMethod("getProperty", GET_PROPERTY_PARAMS)) {
         this.addMethod(node, !Modifier.isAbstract(node.getModifiers()), "getProperty", 1, ClassHelper.OBJECT_TYPE, GET_PROPERTY_PARAMS, ClassNode.EMPTY_ARRAY, new BytecodeSequence(new BytecodeInstruction() {
            public void visit(MethodVisitor mv) {
               mv.visitVarInsn(25, 0);
               mv.visitMethodInsn(182, classInternalName, "getMetaClass", "()Lgroovy/lang/MetaClass;");
               mv.visitVarInsn(25, 0);
               mv.visitVarInsn(25, 1);
               mv.visitMethodInsn(185, "groovy/lang/MetaClass", "getProperty", "(Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;");
               mv.visitInsn(176);
            }
         }));
      }

      if (!node.hasMethod("setProperty", SET_PROPERTY_PARAMS)) {
         this.addMethod(node, !Modifier.isAbstract(node.getModifiers()), "setProperty", 1, ClassHelper.VOID_TYPE, SET_PROPERTY_PARAMS, ClassNode.EMPTY_ARRAY, new BytecodeSequence(new BytecodeInstruction() {
            public void visit(MethodVisitor mv) {
               mv.visitVarInsn(25, 0);
               mv.visitMethodInsn(182, classInternalName, "getMetaClass", "()Lgroovy/lang/MetaClass;");
               mv.visitVarInsn(25, 0);
               mv.visitVarInsn(25, 1);
               mv.visitVarInsn(25, 2);
               mv.visitMethodInsn(185, "groovy/lang/MetaClass", "setProperty", "(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;)V");
               mv.visitInsn(177);
            }
         }));
      }

   }

   protected void addMethod(ClassNode node, boolean shouldBeSynthetic, String name, int modifiers, ClassNode returnType, Parameter[] parameters, ClassNode[] exceptions, Statement code) {
      if (shouldBeSynthetic) {
         node.addSyntheticMethod(name, modifiers, returnType, parameters, exceptions, code);
      } else {
         node.addMethod(name, modifiers & -4097, returnType, parameters, exceptions, code);
      }

   }

   protected void addTimeStamp(ClassNode node) {
      if (node.getDeclaredField("__timeStamp") == null) {
         FieldNode timeTagField = new FieldNode("__timeStamp", 4105, ClassHelper.Long_TYPE, node, new ConstantExpression(System.currentTimeMillis()));
         timeTagField.setSynthetic(true);
         node.addField(timeTagField);
         timeTagField = new FieldNode("__timeStamp__239_neverHappen" + String.valueOf(System.currentTimeMillis()), 4105, ClassHelper.Long_TYPE, node, new ConstantExpression(0L));
         timeTagField.setSynthetic(true);
         node.addField(timeTagField);
      }

   }

   private void checkReturnInObjectInitializer(List init) {
      CodeVisitorSupport cvs = new CodeVisitorSupport() {
         public void visitReturnStatement(ReturnStatement statement) {
            throw new RuntimeParserException("'return' is not allowed in object initializer", statement);
         }

         public void visitClosureExpression(ClosureExpression expression) {
         }
      };
      Iterator iterator = init.iterator();

      while(iterator.hasNext()) {
         Statement stm = (Statement)iterator.next();
         stm.visit(cvs);
      }

   }

   public void visitConstructor(ConstructorNode node) {
      CodeVisitorSupport checkSuper = new CodeVisitorSupport() {
         boolean firstMethodCall = true;
         String type = null;

         public void visitMethodCallExpression(MethodCallExpression call) {
            if (this.firstMethodCall) {
               this.firstMethodCall = false;
               String name = call.getMethodAsString();
               if (name != null) {
                  if (name.equals("super") || name.equals("this")) {
                     this.type = name;
                     call.getArguments().visit(this);
                     this.type = null;
                  }
               }
            }
         }

         public void visitConstructorCallExpression(ConstructorCallExpression call) {
            if (call.isSpecialCall()) {
               this.type = call.getText();
               call.getArguments().visit(this);
               this.type = null;
            }
         }

         public void visitVariableExpression(VariableExpression expression) {
            if (this.type != null) {
               String name = expression.getName();
               if (name.equals("this") || name.equals("super")) {
                  throw new RuntimeParserException("cannot reference " + name + " inside of " + this.type + "(....) before supertype constructor has been called", expression);
               }
            }
         }
      };
      Statement s = node.getCode();
      if (s != null) {
         s.visit(new VerifierCodeVisitor(this));
         s.visit(checkSuper);
      }
   }

   public void visitMethod(MethodNode node) {
      if (AsmClassGenerator.isMopMethod(node.getName())) {
         throw new RuntimeParserException("Found unexpected MOP methods in the class node for " + this.classNode.getName() + "(" + node.getName() + ")", this.classNode);
      } else {
         this.methodNode = node;
         this.adjustTypesIfStaticMainMethod(node);
         this.addReturnIfNeeded(node);
         Statement statement = node.getCode();
         if (statement != null) {
            statement.visit(new VerifierCodeVisitor(this));
         }

      }
   }

   private void adjustTypesIfStaticMainMethod(MethodNode node) {
      if (node.getName().equals("main") && node.isStatic()) {
         Parameter[] params = node.getParameters();
         if (params.length == 1) {
            Parameter param = params[0];
            if (param.getType() == null || param.getType() == ClassHelper.OBJECT_TYPE) {
               param.setType(ClassHelper.STRING_TYPE.makeArray());
               ClassNode returnType = node.getReturnType();
               if (returnType == ClassHelper.OBJECT_TYPE) {
                  node.setReturnType(ClassHelper.VOID_TYPE);
               }
            }
         }
      }

   }

   protected void addReturnIfNeeded(MethodNode node) {
      ReturnAdder.addReturnIfNeeded(node);
   }

   public void visitField(FieldNode node) {
   }

   private boolean methodNeedsReplacement(MethodNode m) {
      if (m == null) {
         return true;
      } else if (m.getDeclaringClass() == this.getClassNode()) {
         return false;
      } else {
         return (m.getModifiers() & 16) == 0;
      }
   }

   public void visitProperty(PropertyNode node) {
      String name = node.getName();
      FieldNode field = node.getField();
      int propNodeModifiers = node.getModifiers();
      String getterName = "get" + capitalize(name);
      String setterName = "set" + capitalize(name);
      if ((propNodeModifiers & 64) != 0) {
         propNodeModifiers -= 64;
      }

      if ((propNodeModifiers & 128) != 0) {
         propNodeModifiers -= 128;
      }

      Statement getterBlock = node.getGetterBlock();
      if (getterBlock == null) {
         MethodNode getter = this.classNode.getGetterMethod(getterName);
         if (getter == null && ClassHelper.boolean_TYPE == node.getType()) {
            String secondGetterName = "is" + capitalize(name);
            getter = this.classNode.getGetterMethod(secondGetterName);
         }

         if (!node.isPrivate() && this.methodNeedsReplacement(getter)) {
            getterBlock = this.createGetterBlock(node, field);
         }
      }

      Statement setterBlock = node.getSetterBlock();
      MethodNode getter;
      if (setterBlock == null) {
         getter = this.classNode.getSetterMethod(setterName);
         if (!node.isPrivate() && (propNodeModifiers & 16) == 0 && this.methodNeedsReplacement(getter)) {
            setterBlock = this.createSetterBlock(node, field);
         }
      }

      if (getterBlock != null) {
         getter = new MethodNode(getterName, propNodeModifiers, node.getType(), Parameter.EMPTY_ARRAY, ClassNode.EMPTY_ARRAY, getterBlock);
         getter.setSynthetic(true);
         this.addPropertyMethod(getter);
         this.visitMethod(getter);
         if (ClassHelper.boolean_TYPE == node.getType() || ClassHelper.Boolean_TYPE == node.getType()) {
            String secondGetterName = "is" + capitalize(name);
            MethodNode secondGetter = new MethodNode(secondGetterName, propNodeModifiers, node.getType(), Parameter.EMPTY_ARRAY, ClassNode.EMPTY_ARRAY, getterBlock);
            secondGetter.setSynthetic(true);
            this.addPropertyMethod(secondGetter);
            this.visitMethod(secondGetter);
         }
      }

      if (setterBlock != null) {
         Parameter[] setterParameterTypes = new Parameter[]{new Parameter(node.getType(), "value")};
         MethodNode setter = new MethodNode(setterName, propNodeModifiers, ClassHelper.VOID_TYPE, setterParameterTypes, ClassNode.EMPTY_ARRAY, setterBlock);
         setter.setSynthetic(true);
         this.addPropertyMethod(setter);
         this.visitMethod(setter);
      }

   }

   protected void addPropertyMethod(MethodNode method) {
      this.classNode.addMethod(method);
   }

   protected void addDefaultParameterMethods(final ClassNode node) {
      List methods = new ArrayList(node.getMethods());
      this.addDefaultParameters((List)methods, (Verifier.DefaultArgsAction)(new Verifier.DefaultArgsAction() {
         public void call(ArgumentListExpression arguments, Parameter[] newParams, MethodNode method) {
            MethodCallExpression expression = new MethodCallExpression(VariableExpression.THIS_EXPRESSION, method.getName(), arguments);
            expression.setImplicitThis(true);
            Statement code = null;
            if (method.isVoidMethod()) {
               code = new ExpressionStatement(expression);
            } else {
               code = new ReturnStatement(expression);
            }

            MethodNode newMethod = new MethodNode(method.getName(), method.getModifiers(), method.getReturnType(), newParams, method.getExceptions(), (Statement)code);
            MethodNode oldMethod = node.getDeclaredMethod(method.getName(), newParams);
            if (oldMethod != null) {
               throw new RuntimeParserException("The method with default parameters \"" + method.getTypeDescriptor() + "\" defines a method \"" + newMethod.getTypeDescriptor() + "\" that is already defined.", method);
            } else {
               Verifier.this.addPropertyMethod(newMethod);
               newMethod.setGenericsTypes(method.getGenericsTypes());
            }
         }
      }));
   }

   protected void addDefaultParameterConstructors(final ClassNode node) {
      List methods = new ArrayList(node.getDeclaredConstructors());
      this.addDefaultParameters((List)methods, (Verifier.DefaultArgsAction)(new Verifier.DefaultArgsAction() {
         public void call(ArgumentListExpression arguments, Parameter[] newParams, MethodNode method) {
            ConstructorNode ctor = (ConstructorNode)method;
            ConstructorCallExpression expression = new ConstructorCallExpression(ClassNode.THIS, arguments);
            Statement code = new ExpressionStatement(expression);
            Verifier.this.addConstructor(newParams, ctor, code, node);
         }
      }));
   }

   protected void addConstructor(Parameter[] newParams, ConstructorNode ctor, Statement code, ClassNode node) {
      node.addConstructor(ctor.getModifiers(), newParams, ctor.getExceptions(), code);
   }

   protected void addDefaultParameters(List methods, Verifier.DefaultArgsAction action) {
      Iterator iter = methods.iterator();

      while(iter.hasNext()) {
         MethodNode method = (MethodNode)iter.next();
         if (method.hasDefaultValue()) {
            this.addDefaultParameters(action, method);
         }
      }

   }

   protected void addDefaultParameters(Verifier.DefaultArgsAction action, MethodNode method) {
      Parameter[] parameters = method.getParameters();
      int counter = 0;
      List paramValues = new ArrayList();
      int size = parameters.length;

      int j;
      for(j = size - 1; j >= 0; --j) {
         Parameter parameter = parameters[j];
         if (parameter != null && parameter.hasInitialExpression()) {
            paramValues.add(j);
            paramValues.add(new CastExpression(parameter.getType(), parameter.getInitialExpression()));
            ++counter;
         }
      }

      for(j = 1; j <= counter; ++j) {
         Parameter[] newParams = new Parameter[parameters.length - j];
         ArgumentListExpression arguments = new ArgumentListExpression();
         int index = 0;
         int k = 1;

         for(int i = 0; i < parameters.length; ++i) {
            if (k > counter - j && parameters[i] != null && parameters[i].hasInitialExpression()) {
               arguments.addExpression(new CastExpression(parameters[i].getType(), parameters[i].getInitialExpression()));
               ++k;
            } else if (parameters[i] != null && parameters[i].hasInitialExpression()) {
               newParams[index++] = parameters[i];
               arguments.addExpression(new CastExpression(parameters[i].getType(), new VariableExpression(parameters[i].getName())));
               ++k;
            } else {
               newParams[index++] = parameters[i];
               arguments.addExpression(new CastExpression(parameters[i].getType(), new VariableExpression(parameters[i].getName())));
            }
         }

         action.call(arguments, newParams, method);
      }

      for(j = 0; j < parameters.length; ++j) {
         parameters[j].setInitialExpression((Expression)null);
      }

   }

   protected void addClosureCode(InnerClassNode node) {
   }

   protected void addInitialization(ClassNode node) {
      Iterator iter = node.getDeclaredConstructors().iterator();

      while(iter.hasNext()) {
         this.addInitialization(node, (ConstructorNode)iter.next());
      }

   }

   protected void addInitialization(ClassNode node, ConstructorNode constructorNode) {
      Statement firstStatement = constructorNode.getFirstStatement();
      if (!(firstStatement instanceof BytecodeSequence)) {
         ConstructorCallExpression first = this.getFirstIfSpecialConstructorCall(firstStatement);
         if (first == null || !first.isThisCall()) {
            List statements = new ArrayList();
            List staticStatements = new ArrayList();
            boolean isEnum = node.isEnum();
            List<Statement> initStmtsAfterEnumValuesInit = new ArrayList();
            Set explicitStaticPropsInEnum = new HashSet();
            Iterator i$;
            if (isEnum) {
               i$ = node.getProperties().iterator();

               while(i$.hasNext()) {
                  PropertyNode propNode = (PropertyNode)i$.next();
                  if (!propNode.isSynthetic() && propNode.getField().isStatic()) {
                     explicitStaticPropsInEnum.add(propNode.getField().getName());
                  }
               }

               i$ = node.getFields().iterator();

               while(i$.hasNext()) {
                  FieldNode fieldNode = (FieldNode)i$.next();
                  if (!fieldNode.isSynthetic() && fieldNode.isStatic() && fieldNode.getType() != node) {
                     explicitStaticPropsInEnum.add(fieldNode.getName());
                  }
               }
            }

            i$ = node.getFields().iterator();

            while(i$.hasNext()) {
               this.addFieldInitialization(statements, staticStatements, (FieldNode)i$.next(), isEnum, initStmtsAfterEnumValuesInit, explicitStaticPropsInEnum);
            }

            statements.addAll(node.getObjectInitializerStatements());
            if (!statements.isEmpty()) {
               Statement code = constructorNode.getCode();
               BlockStatement block = new BlockStatement();
               List otherStatements = block.getStatements();
               if (code instanceof BlockStatement) {
                  block = (BlockStatement)code;
                  otherStatements = block.getStatements();
               } else if (code != null) {
                  otherStatements.add(code);
               }

               if (!otherStatements.isEmpty()) {
                  if (first != null) {
                     otherStatements.remove(0);
                     statements.add(0, firstStatement);
                  }

                  Statement stmtThis$0 = this.getImplicitThis$0StmtIfInnerClass(otherStatements);
                  if (stmtThis$0 != null) {
                     statements.add(1, stmtThis$0);
                  }

                  statements.addAll(otherStatements);
               }

               BlockStatement newBlock = new BlockStatement(statements, block.getVariableScope());
               newBlock.setSourcePosition(block);
               constructorNode.setCode(newBlock);
            }

            if (!staticStatements.isEmpty()) {
               if (isEnum) {
                  staticStatements.removeAll(initStmtsAfterEnumValuesInit);
                  node.addStaticInitializerStatements(staticStatements, true);
                  if (!initStmtsAfterEnumValuesInit.isEmpty()) {
                     node.positionStmtsAfterEnumInitStmts(initStmtsAfterEnumValuesInit);
                  }
               } else {
                  node.addStaticInitializerStatements(staticStatements, true);
               }
            }

         }
      }
   }

   private Statement getImplicitThis$0StmtIfInnerClass(List<Statement> otherStatements) {
      if (!(this.classNode instanceof InnerClassNode)) {
         return null;
      } else {
         Iterator i$ = otherStatements.iterator();

         while(true) {
            Statement stmt;
            do {
               if (!i$.hasNext()) {
                  return null;
               }

               stmt = (Statement)i$.next();
            } while(!(stmt instanceof BlockStatement));

            List<Statement> stmts = ((BlockStatement)stmt).getStatements();
            Iterator i$ = stmts.iterator();

            while(i$.hasNext()) {
               Statement bstmt = (Statement)i$.next();
               if (bstmt instanceof ExpressionStatement) {
                  Expression expr = ((ExpressionStatement)bstmt).getExpression();
                  if (expr instanceof BinaryExpression) {
                     Expression lExpr = ((BinaryExpression)expr).getLeftExpression();
                     if (lExpr instanceof FieldExpression && "this$0".equals(((FieldExpression)lExpr).getFieldName())) {
                        stmts.remove(bstmt);
                        return bstmt;
                     }
                  }
               }
            }
         }
      }
   }

   private ConstructorCallExpression getFirstIfSpecialConstructorCall(Statement code) {
      if (code != null && code instanceof ExpressionStatement) {
         Expression expression = ((ExpressionStatement)code).getExpression();
         if (!(expression instanceof ConstructorCallExpression)) {
            return null;
         } else {
            ConstructorCallExpression cce = (ConstructorCallExpression)expression;
            return cce.isSpecialCall() ? cce : null;
         }
      } else {
         return null;
      }
   }

   protected void addFieldInitialization(List list, List staticList, FieldNode fieldNode, boolean isEnumClassNode, List initStmtsAfterEnumValuesInit, Set explicitStaticPropsInEnum) {
      Expression expression = fieldNode.getInitialExpression();
      if (expression != null) {
         FieldExpression fe = new FieldExpression(fieldNode);
         if (fieldNode.getType().equals(ClassHelper.REFERENCE_TYPE) && (fieldNode.getModifiers() & 4096) != 0) {
            fe.setUseReferenceDirectly(true);
         }

         ExpressionStatement statement = new ExpressionStatement(new BinaryExpression(fe, Token.newSymbol(100, fieldNode.getLineNumber(), fieldNode.getColumnNumber()), expression));
         if (fieldNode.isStatic()) {
            if (expression instanceof ConstantExpression) {
               staticList.add(0, statement);
            } else {
               staticList.add(statement);
            }

            fieldNode.setInitialValueExpression((Expression)null);
            if (isEnumClassNode && explicitStaticPropsInEnum.contains(fieldNode.getName())) {
               initStmtsAfterEnumValuesInit.add(statement);
            }
         } else {
            list.add(statement);
         }
      }

   }

   public static String capitalize(String name) {
      return MetaClassHelper.capitalize(name);
   }

   protected Statement createGetterBlock(PropertyNode propertyNode, final FieldNode field) {
      return new BytecodeSequence(new BytecodeInstruction() {
         public void visit(MethodVisitor mv) {
            if (field.isStatic()) {
               mv.visitFieldInsn(178, BytecodeHelper.getClassInternalName(Verifier.this.classNode), field.getName(), BytecodeHelper.getTypeDescription(field.getType()));
            } else {
               mv.visitVarInsn(25, 0);
               mv.visitFieldInsn(180, BytecodeHelper.getClassInternalName(Verifier.this.classNode), field.getName(), BytecodeHelper.getTypeDescription(field.getType()));
            }

            BytecodeHelper helper = new BytecodeHelper(mv);
            helper.doReturn(field.getType());
         }
      });
   }

   protected Statement createSetterBlock(PropertyNode propertyNode, final FieldNode field) {
      return new BytecodeSequence(new BytecodeInstruction() {
         public void visit(MethodVisitor mv) {
            BytecodeHelper helper = new BytecodeHelper(mv);
            if (field.isStatic()) {
               helper.load(field.getType(), 0);
               mv.visitFieldInsn(179, BytecodeHelper.getClassInternalName(Verifier.this.classNode), field.getName(), BytecodeHelper.getTypeDescription(field.getType()));
            } else {
               mv.visitVarInsn(25, 0);
               helper.load(field.getType(), 1);
               mv.visitFieldInsn(181, BytecodeHelper.getClassInternalName(Verifier.this.classNode), field.getName(), BytecodeHelper.getTypeDescription(field.getType()));
            }

            mv.visitInsn(177);
         }
      });
   }

   public void visitGenericType(GenericsType genericsType) {
   }

   public static long getTimestamp(Class clazz) {
      if (clazz.getClassLoader() instanceof GroovyClassLoader.InnerLoader) {
         GroovyClassLoader.InnerLoader innerLoader = (GroovyClassLoader.InnerLoader)clazz.getClassLoader();
         return innerLoader.getTimeStamp();
      } else {
         Field[] fields = clazz.getFields();

         for(int i = 0; i != fields.length; ++i) {
            if (Modifier.isStatic(fields[i].getModifiers())) {
               String name = fields[i].getName();
               if (name.startsWith("__timeStamp__239_neverHappen")) {
                  try {
                     return Long.decode(name.substring("__timeStamp__239_neverHappen".length()));
                  } catch (NumberFormatException var5) {
                     return Long.MAX_VALUE;
                  }
               }
            }
         }

         return Long.MAX_VALUE;
      }
   }

   protected void addCovariantMethods(ClassNode classNode) {
      Map methodsToAdd = new HashMap();
      Map genericsSpec = new HashMap();
      Map abstractMethods = new HashMap();
      Map<String, MethodNode> allInterfaceMethods = new HashMap();
      ClassNode[] interfaces = classNode.getInterfaces();

      for(int i = 0; i < interfaces.length; ++i) {
         ClassNode iface = interfaces[i];
         Map ifaceMethodsMap = iface.getDeclaredMethodsMap();
         abstractMethods.putAll(ifaceMethodsMap);
         allInterfaceMethods.putAll(ifaceMethodsMap);
      }

      this.collectSuperInterfaceMethods(classNode, allInterfaceMethods);
      List declaredMethods = new ArrayList(classNode.getMethods());
      Iterator methodsIterator = declaredMethods.iterator();

      MethodNode intfMethod;
      MethodNode m;
      do {
         if (!methodsIterator.hasNext()) {
            this.addCovariantMethods(classNode, declaredMethods, abstractMethods, methodsToAdd, genericsSpec);
            Map declaredMethodsMap = new HashMap();
            Iterator it;
            if (methodsToAdd.size() > 0) {
               it = declaredMethods.iterator();

               while(it.hasNext()) {
                  intfMethod = (MethodNode)it.next();
                  declaredMethodsMap.put(intfMethod.getTypeDescriptor(), intfMethod);
               }
            }

            it = methodsToAdd.entrySet().iterator();

            while(true) {
               MethodNode method;
               MethodNode mn;
               do {
                  if (!it.hasNext()) {
                     return;
                  }

                  Entry entry = (Entry)it.next();
                  method = (MethodNode)entry.getValue();
                  mn = (MethodNode)declaredMethodsMap.get(entry.getKey());
               } while(mn != null && mn.getDeclaringClass().equals(classNode));

               this.addPropertyMethod(method);
            }
         }

         m = (MethodNode)methodsIterator.next();
         abstractMethods.remove(m.getTypeDescriptor());
         if (m.isStatic() || !m.isPublic() && !m.isProtected()) {
            methodsIterator.remove();
         }

         intfMethod = (MethodNode)allInterfaceMethods.get(m.getTypeDescriptor());
      } while(intfMethod == null || (m.getModifiers() & 4096) != 0 || m.isPublic() || m.isStaticConstructor());

      throw new RuntimeParserException("The method " + m.getName() + " should be public as it implements the corresponding method from interface " + intfMethod.getDeclaringClass(), m);
   }

   private void collectSuperInterfaceMethods(ClassNode cn, Map allInterfaceMethods) {
      List cnInterfaces = Arrays.asList(cn.getInterfaces());

      for(ClassNode sn = cn.getSuperClass(); !sn.equals(ClassHelper.OBJECT_TYPE); sn = sn.getSuperClass()) {
         ClassNode[] interfaces = sn.getInterfaces();

         for(int i = 0; i < interfaces.length; ++i) {
            ClassNode iface = interfaces[i];
            if (!cnInterfaces.contains(iface)) {
               Map ifaceMethodsMap = iface.getDeclaredMethodsMap();
               allInterfaceMethods.putAll(ifaceMethodsMap);
            }
         }
      }

   }

   private void addCovariantMethods(ClassNode classNode, List declaredMethods, Map abstractMethods, Map methodsToAdd, Map oldGenericsSpec) {
      ClassNode sn = classNode.getUnresolvedSuperClass(false);
      if (sn != null) {
         Map genericsSpec = this.createGenericsSpec(sn, oldGenericsSpec);
         List classMethods = sn.getMethods();
         Iterator it = declaredMethods.iterator();

         MethodNode method;
         while(it.hasNext()) {
            method = (MethodNode)it.next();
            if (!method.isStatic()) {
               this.storeMissingCovariantMethods(classMethods, method, methodsToAdd, genericsSpec);
            }
         }

         if (!abstractMethods.isEmpty()) {
            it = classMethods.iterator();

            while(it.hasNext()) {
               method = (MethodNode)it.next();
               if (!method.isStatic()) {
                  this.storeMissingCovariantMethods(abstractMethods.values(), method, methodsToAdd, Collections.EMPTY_MAP);
               }
            }
         }

         this.addCovariantMethods(sn.redirect(), declaredMethods, abstractMethods, methodsToAdd, genericsSpec);
      }

      ClassNode[] interfaces = classNode.getInterfaces();

      for(int i = 0; i < interfaces.length; ++i) {
         List interfacesMethods = interfaces[i].getMethods();
         Map genericsSpec = this.createGenericsSpec(interfaces[i], oldGenericsSpec);
         Iterator it = declaredMethods.iterator();

         while(it.hasNext()) {
            MethodNode method = (MethodNode)it.next();
            if (!method.isStatic()) {
               this.storeMissingCovariantMethods(interfacesMethods, method, methodsToAdd, genericsSpec);
            }
         }

         this.addCovariantMethods(interfaces[i], declaredMethods, abstractMethods, methodsToAdd, genericsSpec);
      }

   }

   private MethodNode getCovariantImplementation(final MethodNode oldMethod, final MethodNode overridingMethod, Map genericsSpec) {
      if (!oldMethod.getName().equals(overridingMethod.getName())) {
         return null;
      } else if ((overridingMethod.getModifiers() & 64) != 0) {
         return null;
      } else {
         boolean normalEqualParameters = this.equalParametersNormal(overridingMethod, oldMethod);
         boolean genericEqualParameters = this.equalParametersWithGenerics(overridingMethod, oldMethod, genericsSpec);
         if (!normalEqualParameters && !genericEqualParameters) {
            return null;
         } else {
            ClassNode mr = overridingMethod.getReturnType();
            ClassNode omr = oldMethod.getReturnType();
            boolean equalReturnType = mr.equals(omr);
            if (equalReturnType && normalEqualParameters) {
               return null;
            } else {
               ClassNode testmr = this.correctToGenericsSpec(genericsSpec, omr);
               if (!this.isAssignable(mr, testmr)) {
                  throw new RuntimeParserException("The return type of " + overridingMethod.getTypeDescriptor() + " in " + overridingMethod.getDeclaringClass().getName() + " is incompatible with " + oldMethod.getTypeDescriptor() + " in " + oldMethod.getDeclaringClass().getName(), overridingMethod);
               } else if ((oldMethod.getModifiers() & 16) != 0) {
                  throw new RuntimeParserException("Cannot override final method " + oldMethod.getTypeDescriptor() + " in " + oldMethod.getDeclaringClass().getName(), overridingMethod);
               } else if (oldMethod.isStatic() != overridingMethod.isStatic()) {
                  throw new RuntimeParserException("Cannot override method " + oldMethod.getTypeDescriptor() + " in " + oldMethod.getDeclaringClass().getName() + " with disparate static modifier", overridingMethod);
               } else {
                  MethodNode newMethod = new MethodNode(oldMethod.getName(), overridingMethod.getModifiers() | 4096 | 64, oldMethod.getReturnType().getPlainNodeReference(), this.cleanParameters(oldMethod.getParameters()), oldMethod.getExceptions(), (Statement)null);
                  List instructions = new ArrayList(1);
                  instructions.add(new BytecodeInstruction() {
                     public void visit(MethodVisitor mv) {
                        BytecodeHelper helper = new BytecodeHelper(mv);
                        mv.visitVarInsn(25, 0);
                        Parameter[] para = oldMethod.getParameters();
                        Parameter[] goal = overridingMethod.getParameters();

                        for(int i = 0; i < para.length; ++i) {
                           helper.load(para[i].getType(), i + 1);
                           if (!para[i].getType().equals(goal[i].getType())) {
                              helper.doCast(goal[i].getType());
                           }
                        }

                        mv.visitMethodInsn(182, BytecodeHelper.getClassInternalName(Verifier.this.classNode), overridingMethod.getName(), BytecodeHelper.getMethodDescriptor(overridingMethod.getReturnType(), overridingMethod.getParameters()));
                        helper.doReturn(oldMethod.getReturnType());
                     }
                  });
                  newMethod.setCode(new BytecodeSequence(instructions));
                  return newMethod;
               }
            }
         }
      }
   }

   private boolean isAssignable(ClassNode node, ClassNode testNode) {
      if (testNode.isInterface()) {
         if (node.isInterface()) {
            if (node.isDerivedFrom(testNode)) {
               return true;
            }
         } else if (node.implementsInterface(testNode)) {
            return true;
         }
      } else if (node.isDerivedFrom(testNode)) {
         return true;
      }

      return false;
   }

   private Parameter[] cleanParameters(Parameter[] parameters) {
      Parameter[] params = new Parameter[parameters.length];

      for(int i = 0; i < params.length; ++i) {
         params[i] = new Parameter(parameters[i].getType().getPlainNodeReference(), parameters[i].getName());
      }

      return params;
   }

   private void storeMissingCovariantMethods(Collection methods, MethodNode method, Map methodsToAdd, Map genericsSpec) {
      Iterator sit = methods.iterator();

      MethodNode bridgeMethod;
      do {
         if (!sit.hasNext()) {
            return;
         }

         MethodNode toOverride = (MethodNode)sit.next();
         bridgeMethod = this.getCovariantImplementation(toOverride, method, genericsSpec);
      } while(bridgeMethod == null);

      methodsToAdd.put(bridgeMethod.getTypeDescriptor(), bridgeMethod);
   }

   private ClassNode correctToGenericsSpec(Map genericsSpec, GenericsType type) {
      ClassNode ret = null;
      if (type.isPlaceholder()) {
         String name = type.getName();
         ret = (ClassNode)genericsSpec.get(name);
      }

      if (ret == null) {
         ret = type.getType();
      }

      return ret;
   }

   private ClassNode correctToGenericsSpec(Map genericsSpec, ClassNode type) {
      if (type.isGenericsPlaceHolder()) {
         String name = type.getGenericsTypes()[0].getName();
         type = (ClassNode)genericsSpec.get(name);
      }

      if (type == null) {
         type = ClassHelper.OBJECT_TYPE;
      }

      return type;
   }

   private boolean equalParametersNormal(MethodNode m1, MethodNode m2) {
      Parameter[] p1 = m1.getParameters();
      Parameter[] p2 = m2.getParameters();
      if (p1.length != p2.length) {
         return false;
      } else {
         for(int i = 0; i < p2.length; ++i) {
            ClassNode type = p2[i].getType();
            ClassNode parameterType = p1[i].getType();
            if (!parameterType.equals(type)) {
               return false;
            }
         }

         return true;
      }
   }

   private boolean equalParametersWithGenerics(MethodNode m1, MethodNode m2, Map genericsSpec) {
      Parameter[] p1 = m1.getParameters();
      Parameter[] p2 = m2.getParameters();
      if (p1.length != p2.length) {
         return false;
      } else {
         for(int i = 0; i < p2.length; ++i) {
            ClassNode type = p2[i].getType();
            ClassNode genericsType = this.correctToGenericsSpec(genericsSpec, type);
            ClassNode parameterType = p1[i].getType();
            if (!parameterType.equals(genericsType)) {
               return false;
            }
         }

         return true;
      }
   }

   private Map createGenericsSpec(ClassNode current, Map oldSpec) {
      Map ret = new HashMap(oldSpec);
      GenericsType[] sgts = current.getGenericsTypes();
      if (sgts != null) {
         ClassNode[] spec = new ClassNode[sgts.length];

         for(int i = 0; i < spec.length; ++i) {
            spec[i] = this.correctToGenericsSpec(ret, (GenericsType)sgts[i]);
         }

         GenericsType[] newGts = current.redirect().getGenericsTypes();
         if (newGts == null) {
            return ret;
         }

         ret.clear();

         for(int i = 0; i < spec.length; ++i) {
            ret.put(newGts[i].getName(), spec[i]);
         }
      }

      return ret;
   }

   static {
      INVOKE_METHOD_PARAMS = new Parameter[]{new Parameter(ClassHelper.STRING_TYPE, "method"), new Parameter(ClassHelper.OBJECT_TYPE, "arguments")};
      SET_PROPERTY_PARAMS = new Parameter[]{new Parameter(ClassHelper.STRING_TYPE, "property"), new Parameter(ClassHelper.OBJECT_TYPE, "value")};
      GET_PROPERTY_PARAMS = new Parameter[]{new Parameter(ClassHelper.STRING_TYPE, "property")};
      SET_METACLASS_PARAMS = new Parameter[]{new Parameter(ClassHelper.METACLASS_TYPE, "mc")};
   }

   public interface DefaultArgsAction {
      void call(ArgumentListExpression var1, Parameter[] var2, MethodNode var3);
   }
}
