package org.codehaus.groovy.transform;

import groovy.lang.Immutable;
import groovy.lang.MetaClass;
import groovy.lang.MissingPropertyException;
import groovy.lang.ReadOnlyPropertyException;
import groovyjarjarasm.asm.Opcodes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.AnnotatedNode;
import org.codehaus.groovy.ast.AnnotationNode;
import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.ConstructorNode;
import org.codehaus.groovy.ast.FieldNode;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.Parameter;
import org.codehaus.groovy.ast.PropertyNode;
import org.codehaus.groovy.ast.expr.ArgumentListExpression;
import org.codehaus.groovy.ast.expr.BinaryExpression;
import org.codehaus.groovy.ast.expr.BooleanExpression;
import org.codehaus.groovy.ast.expr.CastExpression;
import org.codehaus.groovy.ast.expr.ClassExpression;
import org.codehaus.groovy.ast.expr.ConstantExpression;
import org.codehaus.groovy.ast.expr.ConstructorCallExpression;
import org.codehaus.groovy.ast.expr.DeclarationExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.MapExpression;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.ast.expr.PropertyExpression;
import org.codehaus.groovy.ast.expr.StaticMethodCallExpression;
import org.codehaus.groovy.ast.expr.TupleExpression;
import org.codehaus.groovy.ast.expr.VariableExpression;
import org.codehaus.groovy.ast.stmt.BlockStatement;
import org.codehaus.groovy.ast.stmt.EmptyStatement;
import org.codehaus.groovy.ast.stmt.ExpressionStatement;
import org.codehaus.groovy.ast.stmt.IfStatement;
import org.codehaus.groovy.ast.stmt.ReturnStatement;
import org.codehaus.groovy.ast.stmt.Statement;
import org.codehaus.groovy.ast.stmt.ThrowStatement;
import org.codehaus.groovy.control.CompilePhase;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.codehaus.groovy.runtime.InvokerHelper;
import org.codehaus.groovy.syntax.Token;
import org.codehaus.groovy.util.HashCodeHelper;

@GroovyASTTransformation(
   phase = CompilePhase.CANONICALIZATION
)
public class ImmutableASTTransformation implements ASTTransformation, Opcodes {
   private static List<String> immutableList = Arrays.asList("java.lang.Boolean", "java.lang.Byte", "java.lang.Character", "java.lang.Double", "java.lang.Float", "java.lang.Integer", "java.lang.Long", "java.lang.Short", "java.lang.String", "java.math.BigInteger", "java.math.BigDecimal", "java.awt.Color", "java.net.URI");
   private static final Class MY_CLASS = Immutable.class;
   private static final ClassNode MY_TYPE;
   private static final String MY_TYPE_NAME;
   private static final ClassNode OBJECT_TYPE;
   private static final ClassNode HASHMAP_TYPE;
   private static final ClassNode MAP_TYPE;
   private static final ClassNode DATE_TYPE;
   private static final ClassNode CLONEABLE_TYPE;
   private static final ClassNode COLLECTION_TYPE;
   private static final ClassNode HASHUTIL_TYPE;
   private static final ClassNode STRINGBUFFER_TYPE;
   private static final ClassNode READONLYEXCEPTION_TYPE;
   private static final ClassNode DGM_TYPE;
   private static final ClassNode INVOKER_TYPE;
   private static final ClassNode SELF_TYPE;
   private static final Token COMPARE_EQUAL;
   private static final Token COMPARE_NOT_EQUAL;
   private static final Token ASSIGN;

   public void visit(ASTNode[] nodes, SourceUnit source) {
      if (nodes.length == 2 && nodes[0] instanceof AnnotationNode && nodes[1] instanceof AnnotatedNode) {
         AnnotatedNode parent = (AnnotatedNode)nodes[1];
         AnnotationNode node = (AnnotationNode)nodes[0];
         if (MY_TYPE.equals(node.getClassNode())) {
            List<PropertyNode> newNodes = new ArrayList();
            if (parent instanceof ClassNode) {
               ClassNode cNode = (ClassNode)parent;
               String cName = cNode.getName();
               if (cNode.isInterface()) {
                  throw new RuntimeException("Error processing interface '" + cName + "'. " + MY_TYPE_NAME + " not allowed for interfaces.");
               }

               if ((cNode.getModifiers() & 16) == 0) {
                  cNode.setModifiers(cNode.getModifiers() | 16);
               }

               List<PropertyNode> pList = this.getInstanceProperties(cNode);
               Iterator i$ = pList.iterator();

               PropertyNode pNode;
               while(i$.hasNext()) {
                  pNode = (PropertyNode)i$.next();
                  this.adjustPropertyForImmutability(pNode, newNodes);
               }

               i$ = newNodes.iterator();

               while(i$.hasNext()) {
                  pNode = (PropertyNode)i$.next();
                  cNode.getProperties().remove(pNode);
                  this.addProperty(cNode, pNode);
               }

               List<FieldNode> fList = cNode.getFields();
               Iterator i$ = fList.iterator();

               while(i$.hasNext()) {
                  FieldNode fNode = (FieldNode)i$.next();
                  this.ensureNotPublic(cName, fNode);
               }

               this.createConstructor(cNode);
               this.createHashCode(cNode);
               this.createEquals(cNode);
               this.createToString(cNode);
            }

         }
      } else {
         throw new RuntimeException("Internal error: expecting [AnnotationNode, AnnotatedNode] but got: " + Arrays.asList(nodes));
      }
   }

   private boolean hasDeclaredMethod(ClassNode cNode, String name, int argsCount) {
      List<MethodNode> ms = cNode.getDeclaredMethods(name);
      Iterator i$ = ms.iterator();

      Parameter[] paras;
      do {
         if (!i$.hasNext()) {
            return false;
         }

         MethodNode m = (MethodNode)i$.next();
         paras = m.getParameters();
      } while(paras == null || paras.length != argsCount);

      return true;
   }

   private void ensureNotPublic(String cNode, FieldNode fNode) {
      String fName = fNode.getName();
      if (fNode.isPublic() && !fName.contains("$")) {
         throw new RuntimeException("Public field '" + fName + "' not allowed for " + MY_TYPE_NAME + " class '" + cNode + "'.");
      }
   }

   private void createHashCode(ClassNode cNode) {
      boolean hasExistingHashCode = this.hasDeclaredMethod(cNode, "hashCode", 0);
      if (!hasExistingHashCode || !this.hasDeclaredMethod(cNode, "_hashCode", 0)) {
         FieldNode hashField = cNode.addField("$hash$code", 4098, ClassHelper.int_TYPE, (Expression)null);
         BlockStatement body = new BlockStatement();
         Expression hash = new VariableExpression(hashField);
         List<PropertyNode> list = this.getInstanceProperties(cNode);
         body.addStatement(new IfStatement(this.isZeroExpr(hash), this.calculateHashStatements(hash, list), new EmptyStatement()));
         body.addStatement(new ReturnStatement(hash));
         cNode.addMethod(new MethodNode(hasExistingHashCode ? "_hashCode" : "hashCode", hasExistingHashCode ? 2 : 1, ClassHelper.int_TYPE, Parameter.EMPTY_ARRAY, ClassNode.EMPTY_ARRAY, body));
      }
   }

   private void createToString(ClassNode cNode) {
      boolean hasExistingToString = this.hasDeclaredMethod(cNode, "toString", 0);
      if (!hasExistingToString || !this.hasDeclaredMethod(cNode, "_toString", 0)) {
         BlockStatement body = new BlockStatement();
         List<PropertyNode> list = this.getInstanceProperties(cNode);
         Expression result = new VariableExpression("_result");
         Expression init = new ConstructorCallExpression(STRINGBUFFER_TYPE, MethodCallExpression.NO_ARGUMENTS);
         body.addStatement(new ExpressionStatement(new DeclarationExpression(result, ASSIGN, init)));
         body.addStatement(this.append(result, new ConstantExpression(cNode.getName())));
         body.addStatement(this.append(result, new ConstantExpression("(")));
         boolean first = true;
         Iterator i$ = list.iterator();

         while(i$.hasNext()) {
            PropertyNode pNode = (PropertyNode)i$.next();
            if (first) {
               first = false;
            } else {
               body.addStatement(this.append(result, new ConstantExpression(", ")));
            }

            body.addStatement(new IfStatement(new BooleanExpression(new VariableExpression(cNode.getField("$map$constructor"))), this.toStringPropertyName(result, pNode.getName()), new EmptyStatement()));
            Expression fieldExpr = new VariableExpression(pNode.getField());
            body.addStatement(this.append(result, new StaticMethodCallExpression(INVOKER_TYPE, "toString", fieldExpr)));
         }

         body.addStatement(this.append(result, new ConstantExpression(")")));
         body.addStatement(new ReturnStatement(new MethodCallExpression(result, "toString", MethodCallExpression.NO_ARGUMENTS)));
         cNode.addMethod(new MethodNode(hasExistingToString ? "_toString" : "toString", hasExistingToString ? 2 : 1, ClassHelper.STRING_TYPE, Parameter.EMPTY_ARRAY, ClassNode.EMPTY_ARRAY, body));
      }
   }

   private Statement toStringPropertyName(Expression result, String fName) {
      BlockStatement body = new BlockStatement();
      body.addStatement(this.append(result, new ConstantExpression(fName)));
      body.addStatement(this.append(result, new ConstantExpression(":")));
      return body;
   }

   private ExpressionStatement append(Expression result, Expression expr) {
      return new ExpressionStatement(new MethodCallExpression(result, "append", expr));
   }

   private Statement calculateHashStatements(Expression hash, List<PropertyNode> list) {
      BlockStatement body = new BlockStatement();
      Expression result = new VariableExpression("_result");
      Expression init = new StaticMethodCallExpression(HASHUTIL_TYPE, "initHash", MethodCallExpression.NO_ARGUMENTS);
      body.addStatement(new ExpressionStatement(new DeclarationExpression(result, ASSIGN, init)));
      Iterator i$ = list.iterator();

      while(i$.hasNext()) {
         PropertyNode pNode = (PropertyNode)i$.next();
         Expression fieldExpr = new VariableExpression(pNode.getField());
         Expression args = new TupleExpression(result, fieldExpr);
         Expression current = new StaticMethodCallExpression(HASHUTIL_TYPE, "updateHash", args);
         body.addStatement(this.assignStatement(result, current));
      }

      body.addStatement(this.assignStatement(hash, result));
      return body;
   }

   private void createEquals(ClassNode cNode) {
      boolean hasExistingEquals = this.hasDeclaredMethod(cNode, "equals", 1);
      if (!hasExistingEquals || !this.hasDeclaredMethod(cNode, "_equals", 1)) {
         BlockStatement body = new BlockStatement();
         Expression other = new VariableExpression("other");
         body.addStatement(this.returnFalseIfNull(other));
         body.addStatement(this.returnFalseIfWrongType(cNode, other));
         body.addStatement(this.returnTrueIfIdentical(VariableExpression.THIS_EXPRESSION, other));
         body.addStatement(new ExpressionStatement(new BinaryExpression(other, ASSIGN, new CastExpression(cNode, other))));
         List<PropertyNode> list = this.getInstanceProperties(cNode);
         Iterator i$ = list.iterator();

         while(i$.hasNext()) {
            PropertyNode pNode = (PropertyNode)i$.next();
            body.addStatement(this.returnFalseIfPropertyNotEqual(pNode, other));
         }

         body.addStatement(new ReturnStatement(ConstantExpression.TRUE));
         Parameter[] params = new Parameter[]{new Parameter(OBJECT_TYPE, "other")};
         cNode.addMethod(new MethodNode(hasExistingEquals ? "_equals" : "equals", hasExistingEquals ? 2 : 1, ClassHelper.boolean_TYPE, params, ClassNode.EMPTY_ARRAY, body));
      }
   }

   private Statement returnFalseIfWrongType(ClassNode cNode, Expression other) {
      return new IfStatement(this.notEqualClasses(cNode, other), new ReturnStatement(ConstantExpression.FALSE), new EmptyStatement());
   }

   private IfStatement returnFalseIfNull(Expression other) {
      return new IfStatement(this.equalsNullExpr(other), new ReturnStatement(ConstantExpression.FALSE), new EmptyStatement());
   }

   private IfStatement returnTrueIfIdentical(Expression self, Expression other) {
      return new IfStatement(this.identicalExpr(self, other), new ReturnStatement(ConstantExpression.TRUE), new EmptyStatement());
   }

   private Statement returnFalseIfPropertyNotEqual(PropertyNode pNode, Expression other) {
      return new IfStatement(this.notEqualsExpr(pNode, other), new ReturnStatement(ConstantExpression.FALSE), new EmptyStatement());
   }

   private void addProperty(ClassNode cNode, PropertyNode pNode) {
      FieldNode fn = pNode.getField();
      cNode.getFields().remove(fn);
      cNode.addProperty(pNode.getName(), pNode.getModifiers() | 16, pNode.getType(), pNode.getInitialExpression(), pNode.getGetterBlock(), pNode.getSetterBlock());
      FieldNode newfn = cNode.getField(fn.getName());
      cNode.getFields().remove(newfn);
      cNode.addField(fn);
   }

   private void createConstructor(ClassNode cNode) {
      FieldNode constructorField = cNode.addField("$map$constructor", 4098, ClassHelper.boolean_TYPE, (Expression)null);
      Expression constructorStyle = new VariableExpression(constructorField);
      if (cNode.getDeclaredConstructors().size() != 0) {
         throw new RuntimeException("Explicit constructors not allowed for " + MY_TYPE_NAME + " class: " + cNode.getNameWithoutPackage());
      } else {
         List<PropertyNode> list = this.getInstanceProperties(cNode);
         boolean specialHashMapCase = list.size() == 1 && ((PropertyNode)list.get(0)).getField().getType().equals(HASHMAP_TYPE);
         if (specialHashMapCase) {
            this.createConstructorMapSpecial(cNode, constructorStyle, list);
         } else {
            this.createConstructorMap(cNode, constructorStyle, list);
            this.createConstructorOrdered(cNode, constructorStyle, list);
         }

      }
   }

   private List<PropertyNode> getInstanceProperties(ClassNode cNode) {
      List<PropertyNode> result = new ArrayList();
      Iterator i$ = cNode.getProperties().iterator();

      while(i$.hasNext()) {
         PropertyNode pNode = (PropertyNode)i$.next();
         if (!pNode.isStatic()) {
            result.add(pNode);
         }
      }

      return result;
   }

   private void createConstructorMapSpecial(ClassNode cNode, Expression constructorStyle, List<PropertyNode> list) {
      BlockStatement body = new BlockStatement();
      body.addStatement(this.createConstructorStatementMapSpecial(((PropertyNode)list.get(0)).getField()));
      this.createConstructorMapCommon(cNode, constructorStyle, body);
   }

   private void createConstructorMap(ClassNode cNode, Expression constructorStyle, List<PropertyNode> list) {
      BlockStatement body = new BlockStatement();
      Iterator i$ = list.iterator();

      while(i$.hasNext()) {
         PropertyNode pNode = (PropertyNode)i$.next();
         body.addStatement(this.createConstructorStatement(cNode, pNode));
      }

      Expression checkArgs = new ArgumentListExpression(new VariableExpression("this"), new VariableExpression("args"));
      body.addStatement(new ExpressionStatement(new StaticMethodCallExpression(SELF_TYPE, "checkPropNames", checkArgs)));
      this.createConstructorMapCommon(cNode, constructorStyle, body);
   }

   private void createConstructorMapCommon(ClassNode cNode, Expression constructorStyle, BlockStatement body) {
      List<FieldNode> fList = cNode.getFields();
      Iterator i$ = fList.iterator();

      while(true) {
         FieldNode fNode;
         do {
            do {
               do {
                  if (!i$.hasNext()) {
                     body.addStatement(this.assignStatement(constructorStyle, ConstantExpression.TRUE));
                     Parameter[] params = new Parameter[]{new Parameter(HASHMAP_TYPE, "args")};
                     cNode.addConstructor(new ConstructorNode(1, params, ClassNode.EMPTY_ARRAY, new IfStatement(this.equalsNullExpr(new VariableExpression("args")), new EmptyStatement(), body)));
                     return;
                  }

                  fNode = (FieldNode)i$.next();
               } while(fNode.isPublic());
            } while(cNode.getProperty(fNode.getName()) != null);
         } while(fNode.isFinal() && fNode.isStatic());

         if (!fNode.getName().contains("$")) {
            if (fNode.isFinal() && fNode.getInitialExpression() != null) {
               body.addStatement(this.checkFinalArgNotOverridden(cNode, fNode));
            }

            body.addStatement(this.createConstructorStatementDefault(fNode));
         }
      }
   }

   private Statement checkFinalArgNotOverridden(ClassNode cNode, FieldNode fNode) {
      String name = fNode.getName();
      Expression value = this.findArg(name);
      return new IfStatement(this.equalsNullExpr(value), new EmptyStatement(), new ThrowStatement(new ConstructorCallExpression(READONLYEXCEPTION_TYPE, new ArgumentListExpression(new ConstantExpression(name), new ConstantExpression(cNode.getName())))));
   }

   private void createConstructorOrdered(ClassNode cNode, Expression constructorStyle, List<PropertyNode> list) {
      MapExpression argMap = new MapExpression();
      Parameter[] orderedParams = new Parameter[list.size()];
      int index = 0;
      Iterator i$ = list.iterator();

      while(i$.hasNext()) {
         PropertyNode pNode = (PropertyNode)i$.next();
         orderedParams[index++] = new Parameter(pNode.getField().getType(), pNode.getField().getName());
         argMap.addMapEntryExpression(new ConstantExpression(pNode.getName()), new VariableExpression(pNode.getName()));
      }

      BlockStatement orderedBody = new BlockStatement();
      orderedBody.addStatement(new ExpressionStatement(new ConstructorCallExpression(ClassNode.THIS, new ArgumentListExpression(new CastExpression(HASHMAP_TYPE, argMap)))));
      orderedBody.addStatement(this.assignStatement(constructorStyle, ConstantExpression.FALSE));
      cNode.addConstructor(new ConstructorNode(1, orderedParams, ClassNode.EMPTY_ARRAY, orderedBody));
   }

   private Statement createConstructorStatement(ClassNode cNode, PropertyNode pNode) {
      FieldNode fNode = pNode.getField();
      ClassNode fieldType = fNode.getType();
      Statement statement;
      if (!fieldType.isArray() && !fieldType.implementsInterface(CLONEABLE_TYPE)) {
         if (fieldType.isDerivedFrom(DATE_TYPE)) {
            statement = this.createConstructorStatementDate(fNode);
         } else if (!this.isOrImplements(fieldType, COLLECTION_TYPE) && !this.isOrImplements(fieldType, MAP_TYPE)) {
            if (this.isKnownImmutable(fieldType)) {
               statement = this.createConstructorStatementDefault(fNode);
            } else {
               if (fieldType.isResolved()) {
                  throw new RuntimeException(createErrorMessage(cNode.getName(), fNode.getName(), fieldType.getName(), "compiling"));
               }

               statement = this.createConstructorStatementGuarded(cNode, fNode);
            }
         } else {
            statement = this.createConstructorStatementCollection(fNode);
         }
      } else {
         statement = this.createConstructorStatementArrayOrCloneable(fNode);
      }

      return statement;
   }

   private boolean isOrImplements(ClassNode fieldType, ClassNode interfaceType) {
      return fieldType.equals(interfaceType) || fieldType.implementsInterface(interfaceType);
   }

   private Statement createConstructorStatementGuarded(ClassNode cNode, FieldNode fNode) {
      Expression fieldExpr = new VariableExpression(fNode);
      Expression initExpr = fNode.getInitialValueExpression();
      if (initExpr == null) {
         initExpr = ConstantExpression.NULL;
      }

      Expression unknown = this.findArg(fNode.getName());
      return new IfStatement(this.equalsNullExpr(unknown), new IfStatement(this.equalsNullExpr((Expression)initExpr), new EmptyStatement(), this.assignStatement(fieldExpr, this.checkUnresolved(cNode, fNode, (Expression)initExpr))), this.assignStatement(fieldExpr, this.checkUnresolved(cNode, fNode, unknown)));
   }

   private Expression checkUnresolved(ClassNode cNode, FieldNode fNode, Expression value) {
      Expression args = new TupleExpression(new ConstantExpression(cNode.getName()), new ConstantExpression(fNode.getName()), value);
      return new StaticMethodCallExpression(SELF_TYPE, "checkImmutable", args);
   }

   private Statement createConstructorStatementCollection(FieldNode fNode) {
      Expression fieldExpr = new VariableExpression(fNode);
      Expression initExpr = fNode.getInitialValueExpression();
      if (initExpr == null) {
         initExpr = ConstantExpression.NULL;
      }

      Expression collection = this.findArg(fNode.getName());
      return new IfStatement(this.equalsNullExpr(collection), new IfStatement(this.equalsNullExpr((Expression)initExpr), new EmptyStatement(), this.assignStatement(fieldExpr, this.cloneCollectionExpr((Expression)initExpr))), this.assignStatement(fieldExpr, this.cloneCollectionExpr(collection)));
   }

   private Statement createConstructorStatementMapSpecial(FieldNode fNode) {
      Expression fieldExpr = new VariableExpression(fNode);
      Expression initExpr = fNode.getInitialValueExpression();
      if (initExpr == null) {
         initExpr = ConstantExpression.NULL;
      }

      Expression namedArgs = this.findArg(fNode.getName());
      Expression baseArgs = new VariableExpression("args");
      return new IfStatement(this.equalsNullExpr(baseArgs), new IfStatement(this.equalsNullExpr((Expression)initExpr), new EmptyStatement(), this.assignStatement(fieldExpr, this.cloneCollectionExpr((Expression)initExpr))), new IfStatement(this.equalsNullExpr(namedArgs), new IfStatement(this.isTrueExpr(new MethodCallExpression(baseArgs, "containsKey", new ConstantExpression(fNode.getName()))), this.assignStatement(fieldExpr, namedArgs), this.assignStatement(fieldExpr, this.cloneCollectionExpr(baseArgs))), new IfStatement(this.isOneExpr(new MethodCallExpression(baseArgs, "size", MethodCallExpression.NO_ARGUMENTS)), this.assignStatement(fieldExpr, this.cloneCollectionExpr(namedArgs)), this.assignStatement(fieldExpr, this.cloneCollectionExpr(baseArgs)))));
   }

   private boolean isKnownImmutable(ClassNode fieldType) {
      if (!fieldType.isResolved()) {
         return false;
      } else {
         return fieldType.isEnum() || ClassHelper.isPrimitiveType(fieldType) || inImmutableList(fieldType.getName());
      }
   }

   private static boolean inImmutableList(String typeName) {
      return immutableList.contains(typeName);
   }

   private Statement createConstructorStatementDefault(FieldNode fNode) {
      String name = fNode.getName();
      Expression fieldExpr = new PropertyExpression(VariableExpression.THIS_EXPRESSION, name);
      Expression initExpr = fNode.getInitialValueExpression();
      if (initExpr == null) {
         initExpr = ConstantExpression.NULL;
      }

      Expression value = this.findArg(fNode.getName());
      return new IfStatement(this.equalsNullExpr(value), new IfStatement(this.equalsNullExpr((Expression)initExpr), new EmptyStatement(), this.assignStatement(fieldExpr, (Expression)initExpr)), this.assignStatement(fieldExpr, value));
   }

   private Statement createConstructorStatementArrayOrCloneable(FieldNode fNode) {
      Expression fieldExpr = new VariableExpression(fNode);
      Expression initExpr = fNode.getInitialValueExpression();
      if (initExpr == null) {
         initExpr = ConstantExpression.NULL;
      }

      Expression array = this.findArg(fNode.getName());
      return new IfStatement(this.equalsNullExpr(array), new IfStatement(this.equalsNullExpr((Expression)initExpr), this.assignStatement(fieldExpr, ConstantExpression.NULL), this.assignStatement(fieldExpr, this.cloneArrayOrCloneableExpr((Expression)initExpr))), this.assignStatement(fieldExpr, this.cloneArrayOrCloneableExpr(array)));
   }

   private Statement createConstructorStatementDate(FieldNode fNode) {
      Expression fieldExpr = new VariableExpression(fNode);
      Expression initExpr = fNode.getInitialValueExpression();
      if (initExpr == null) {
         initExpr = ConstantExpression.NULL;
      }

      Expression date = this.findArg(fNode.getName());
      return new IfStatement(this.equalsNullExpr(date), new IfStatement(this.equalsNullExpr((Expression)initExpr), this.assignStatement(fieldExpr, ConstantExpression.NULL), this.assignStatement(fieldExpr, this.cloneDateExpr((Expression)initExpr))), this.assignStatement(fieldExpr, this.cloneDateExpr(date)));
   }

   private Expression cloneDateExpr(Expression origDate) {
      return new ConstructorCallExpression(DATE_TYPE, new MethodCallExpression(origDate, "getTime", MethodCallExpression.NO_ARGUMENTS));
   }

   private Statement assignStatement(Expression fieldExpr, Expression value) {
      return new ExpressionStatement(this.assignExpr(fieldExpr, value));
   }

   private Expression assignExpr(Expression fieldExpr, Expression value) {
      return new BinaryExpression(fieldExpr, ASSIGN, value);
   }

   private BooleanExpression equalsNullExpr(Expression argExpr) {
      return new BooleanExpression(new BinaryExpression(argExpr, COMPARE_EQUAL, ConstantExpression.NULL));
   }

   private BooleanExpression isTrueExpr(Expression argExpr) {
      return new BooleanExpression(new BinaryExpression(argExpr, COMPARE_EQUAL, ConstantExpression.TRUE));
   }

   private BooleanExpression isZeroExpr(Expression expr) {
      return new BooleanExpression(new BinaryExpression(expr, COMPARE_EQUAL, new ConstantExpression(0)));
   }

   private BooleanExpression isOneExpr(Expression expr) {
      return new BooleanExpression(new BinaryExpression(expr, COMPARE_EQUAL, new ConstantExpression(1)));
   }

   private BooleanExpression notEqualsExpr(PropertyNode pNode, Expression other) {
      Expression fieldExpr = new VariableExpression(pNode.getField());
      Expression otherExpr = new PropertyExpression(other, pNode.getField().getName());
      return new BooleanExpression(new BinaryExpression(fieldExpr, COMPARE_NOT_EQUAL, otherExpr));
   }

   private BooleanExpression identicalExpr(Expression self, Expression other) {
      return new BooleanExpression(new MethodCallExpression(self, "is", new ArgumentListExpression(other)));
   }

   private BooleanExpression notEqualClasses(ClassNode cNode, Expression other) {
      return new BooleanExpression(new BinaryExpression(new ClassExpression(cNode), COMPARE_NOT_EQUAL, new MethodCallExpression(other, "getClass", MethodCallExpression.NO_ARGUMENTS)));
   }

   private Expression findArg(String fName) {
      return new PropertyExpression(new VariableExpression("args"), fName);
   }

   private void adjustPropertyForImmutability(PropertyNode pNode, List<PropertyNode> newNodes) {
      FieldNode fNode = pNode.getField();
      fNode.setModifiers(pNode.getModifiers() & -2 | 16 | 2);
      this.adjustPropertyNode(pNode, this.createGetterBody(fNode));
      newNodes.add(pNode);
   }

   private void adjustPropertyNode(PropertyNode pNode, Statement getterBody) {
      pNode.setSetterBlock((Statement)null);
      pNode.setGetterBlock(getterBody);
   }

   private Statement createGetterBody(FieldNode fNode) {
      BlockStatement body = new BlockStatement();
      ClassNode fieldType = fNode.getType();
      Statement statement;
      if (!fieldType.isArray() && !fieldType.implementsInterface(CLONEABLE_TYPE)) {
         if (fieldType.isDerivedFrom(DATE_TYPE)) {
            statement = this.createGetterBodyDate(fNode);
         } else {
            statement = this.createGetterBodyDefault(fNode);
         }
      } else {
         statement = this.createGetterBodyArrayOrCloneable(fNode);
      }

      body.addStatement(statement);
      return body;
   }

   private Statement createGetterBodyDefault(FieldNode fNode) {
      Expression fieldExpr = new VariableExpression(fNode);
      return new ExpressionStatement(fieldExpr);
   }

   private static String createErrorMessage(String className, String fieldName, String typeName, String mode) {
      return MY_TYPE_NAME + " processor doesn't know how to handle field '" + fieldName + "' of type '" + prettyTypeName(typeName) + "' while " + mode + " class " + className + ".\n" + MY_TYPE_NAME + " classes currently only support properties with known immutable types " + "or types where special handling achieves immutable behavior, including:\n" + "- Strings, primitive types, wrapper types, BigInteger and BigDecimal, enums\n" + "- other " + MY_TYPE_NAME + " classes and known immutables (java.awt.Color, java.net.URI)\n" + "- Cloneable classes, collections, maps and arrays, and other classes with special handling (java.util.Date)\n" + "Other restrictions apply, please see the groovydoc for " + MY_TYPE_NAME + " for further details";
   }

   private static String prettyTypeName(String name) {
      return name.equals("java.lang.Object") ? name + " or def" : name;
   }

   private Statement createGetterBodyArrayOrCloneable(FieldNode fNode) {
      Expression fieldExpr = new VariableExpression(fNode);
      Expression expression = this.cloneArrayOrCloneableExpr(fieldExpr);
      return this.safeExpression(fieldExpr, expression);
   }

   private Expression cloneArrayOrCloneableExpr(Expression fieldExpr) {
      return new MethodCallExpression(fieldExpr, "clone", MethodCallExpression.NO_ARGUMENTS);
   }

   private Expression cloneCollectionExpr(Expression fieldExpr) {
      return new StaticMethodCallExpression(DGM_TYPE, "asImmutable", fieldExpr);
   }

   private Statement createGetterBodyDate(FieldNode fNode) {
      Expression fieldExpr = new VariableExpression(fNode);
      Expression expression = this.cloneDateExpr(fieldExpr);
      return this.safeExpression(fieldExpr, expression);
   }

   private Statement safeExpression(Expression fieldExpr, Expression expression) {
      return new IfStatement(this.equalsNullExpr(fieldExpr), new ExpressionStatement(fieldExpr), new ExpressionStatement(expression));
   }

   public static Object checkImmutable(String className, String fieldName, Object field) {
      if (field != null && !(field instanceof Enum) && !inImmutableList(field.getClass().getName())) {
         if (field instanceof Collection) {
            return DefaultGroovyMethods.asImmutable((Collection)field);
         } else if (field.getClass().getAnnotation(MY_CLASS) != null) {
            return field;
         } else {
            String typeName = field.getClass().getName();
            throw new RuntimeException(createErrorMessage(className, fieldName, typeName, "constructing"));
         }
      } else {
         return field;
      }
   }

   public static void checkPropNames(Object instance, Map<String, Object> args) {
      MetaClass metaClass = InvokerHelper.getMetaClass(instance);
      Iterator i$ = args.keySet().iterator();

      String k;
      do {
         if (!i$.hasNext()) {
            return;
         }

         k = (String)i$.next();
      } while(metaClass.hasProperty(instance, k) != null);

      throw new MissingPropertyException(k, instance.getClass());
   }

   static {
      MY_TYPE = new ClassNode(MY_CLASS);
      MY_TYPE_NAME = "@" + MY_TYPE.getNameWithoutPackage();
      OBJECT_TYPE = new ClassNode(Object.class);
      HASHMAP_TYPE = new ClassNode(HashMap.class);
      MAP_TYPE = new ClassNode(Map.class);
      DATE_TYPE = new ClassNode(Date.class);
      CLONEABLE_TYPE = new ClassNode(Cloneable.class);
      COLLECTION_TYPE = new ClassNode(Collection.class);
      HASHUTIL_TYPE = new ClassNode(HashCodeHelper.class);
      STRINGBUFFER_TYPE = new ClassNode(StringBuffer.class);
      READONLYEXCEPTION_TYPE = new ClassNode(ReadOnlyPropertyException.class);
      DGM_TYPE = new ClassNode(DefaultGroovyMethods.class);
      INVOKER_TYPE = new ClassNode(InvokerHelper.class);
      SELF_TYPE = new ClassNode(ImmutableASTTransformation.class);
      COMPARE_EQUAL = Token.newSymbol(123, -1, -1);
      COMPARE_NOT_EQUAL = Token.newSymbol(120, -1, -1);
      ASSIGN = Token.newSymbol(100, -1, -1);
   }
}
