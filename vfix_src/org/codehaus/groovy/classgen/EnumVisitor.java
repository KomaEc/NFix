package org.codehaus.groovy.classgen;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.codehaus.groovy.ast.ClassCodeVisitorSupport;
import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.CodeVisitorSupport;
import org.codehaus.groovy.ast.ConstructorNode;
import org.codehaus.groovy.ast.EnumConstantClassNode;
import org.codehaus.groovy.ast.FieldNode;
import org.codehaus.groovy.ast.InnerClassNode;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.Parameter;
import org.codehaus.groovy.ast.expr.ArgumentListExpression;
import org.codehaus.groovy.ast.expr.ArrayExpression;
import org.codehaus.groovy.ast.expr.BinaryExpression;
import org.codehaus.groovy.ast.expr.BooleanExpression;
import org.codehaus.groovy.ast.expr.ClassExpression;
import org.codehaus.groovy.ast.expr.ConstantExpression;
import org.codehaus.groovy.ast.expr.ConstructorCallExpression;
import org.codehaus.groovy.ast.expr.DeclarationExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.FieldExpression;
import org.codehaus.groovy.ast.expr.ListExpression;
import org.codehaus.groovy.ast.expr.MapEntryExpression;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.ast.expr.SpreadExpression;
import org.codehaus.groovy.ast.expr.StaticMethodCallExpression;
import org.codehaus.groovy.ast.expr.TupleExpression;
import org.codehaus.groovy.ast.expr.VariableExpression;
import org.codehaus.groovy.ast.stmt.BlockStatement;
import org.codehaus.groovy.ast.stmt.EmptyStatement;
import org.codehaus.groovy.ast.stmt.ExpressionStatement;
import org.codehaus.groovy.ast.stmt.IfStatement;
import org.codehaus.groovy.ast.stmt.ReturnStatement;
import org.codehaus.groovy.ast.stmt.Statement;
import org.codehaus.groovy.control.CompilationUnit;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.control.messages.SyntaxErrorMessage;
import org.codehaus.groovy.syntax.SyntaxException;
import org.codehaus.groovy.syntax.Token;

public class EnumVisitor extends ClassCodeVisitorSupport {
   private static final int FS = 24;
   private static final int PS = 9;
   private static final int PUBLIC_FS = 25;
   private static final int PRIVATE_FS = 26;
   private final CompilationUnit compilationUnit;
   private final SourceUnit sourceUnit;

   public EnumVisitor(CompilationUnit cu, SourceUnit su) {
      this.compilationUnit = cu;
      this.sourceUnit = su;
   }

   public void visitClass(ClassNode node) {
      if (node.isEnum()) {
         this.completeEnum(node);
      }
   }

   protected SourceUnit getSourceUnit() {
      return this.sourceUnit;
   }

   private void completeEnum(ClassNode enumClass) {
      boolean isAic = this.isAnonymousInnerClass(enumClass);
      FieldNode minValue = null;
      FieldNode maxValue = null;
      FieldNode values = null;
      if (!isAic) {
         values = new FieldNode("$VALUES", 4122, enumClass.makeArray(), enumClass, (Expression)null);
         values.setSynthetic(true);
         this.addMethods(enumClass, values);
         minValue = new FieldNode("MIN_VALUE", 25, enumClass, enumClass, (Expression)null);
         maxValue = new FieldNode("MAX_VALUE", 25, enumClass, enumClass, (Expression)null);
      }

      this.addInit(enumClass, minValue, maxValue, values, isAic);
   }

   private void addMethods(ClassNode enumClass, FieldNode values) {
      List methods = enumClass.getMethods();
      boolean hasNext = false;
      boolean hasPrevious = false;

      MethodNode valueOfMethod;
      for(int i = 0; i < methods.size(); ++i) {
         valueOfMethod = (MethodNode)methods.get(i);
         if (valueOfMethod.getName().equals("next") && valueOfMethod.getParameters().length == 0) {
            hasNext = true;
         }

         if (valueOfMethod.getName().equals("previous") && valueOfMethod.getParameters().length == 0) {
            hasPrevious = true;
         }

         if (hasNext && hasPrevious) {
            break;
         }
      }

      MethodNode valuesMethod = new MethodNode("values", 25, enumClass.makeArray(), new Parameter[0], ClassNode.EMPTY_ARRAY, (Statement)null);
      valuesMethod.setSynthetic(true);
      BlockStatement code = new BlockStatement();
      code.addStatement(new ReturnStatement(new MethodCallExpression(new FieldExpression(values), "clone", MethodCallExpression.NO_ARGUMENTS)));
      valuesMethod.setCode(code);
      enumClass.addMethod(valuesMethod);
      MethodNode nextMethod;
      BlockStatement code;
      BlockStatement ifStatement;
      Token assign;
      Token lt;
      if (!hasNext) {
         assign = Token.newSymbol(100, -1, -1);
         lt = Token.newSymbol(127, -1, -1);
         nextMethod = new MethodNode("next", 4097, enumClass, new Parameter[0], ClassNode.EMPTY_ARRAY, (Statement)null);
         nextMethod.setSynthetic(true);
         code = new BlockStatement();
         ifStatement = new BlockStatement();
         ifStatement.addStatement(new ExpressionStatement(new BinaryExpression(new VariableExpression("ordinal"), assign, new ConstantExpression(0))));
         code.addStatement(new ExpressionStatement(new DeclarationExpression(new VariableExpression("ordinal"), assign, new MethodCallExpression(new MethodCallExpression(VariableExpression.THIS_EXPRESSION, "ordinal", MethodCallExpression.NO_ARGUMENTS), "next", MethodCallExpression.NO_ARGUMENTS))));
         code.addStatement(new IfStatement(new BooleanExpression(new BinaryExpression(new VariableExpression("ordinal"), lt, new MethodCallExpression(new FieldExpression(values), "size", MethodCallExpression.NO_ARGUMENTS))), ifStatement, EmptyStatement.INSTANCE));
         code.addStatement(new ReturnStatement(new MethodCallExpression(new FieldExpression(values), "getAt", new VariableExpression("ordinal"))));
         nextMethod.setCode(code);
         enumClass.addMethod(nextMethod);
      }

      if (!hasPrevious) {
         assign = Token.newSymbol(100, -1, -1);
         lt = Token.newSymbol(124, -1, -1);
         nextMethod = new MethodNode("previous", 4097, enumClass, new Parameter[0], ClassNode.EMPTY_ARRAY, (Statement)null);
         nextMethod.setSynthetic(true);
         code = new BlockStatement();
         ifStatement = new BlockStatement();
         ifStatement.addStatement(new ExpressionStatement(new BinaryExpression(new VariableExpression("ordinal"), assign, new MethodCallExpression(new MethodCallExpression(new FieldExpression(values), "size", MethodCallExpression.NO_ARGUMENTS), "minus", new ConstantExpression(1)))));
         code.addStatement(new ExpressionStatement(new DeclarationExpression(new VariableExpression("ordinal"), assign, new MethodCallExpression(new MethodCallExpression(VariableExpression.THIS_EXPRESSION, "ordinal", MethodCallExpression.NO_ARGUMENTS), "previous", MethodCallExpression.NO_ARGUMENTS))));
         code.addStatement(new IfStatement(new BooleanExpression(new BinaryExpression(new VariableExpression("ordinal"), lt, new ConstantExpression(0))), ifStatement, EmptyStatement.INSTANCE));
         code.addStatement(new ReturnStatement(new MethodCallExpression(new FieldExpression(values), "getAt", new VariableExpression("ordinal"))));
         nextMethod.setCode(code);
         enumClass.addMethod(nextMethod);
      }

      Parameter stringParameter = new Parameter(ClassHelper.STRING_TYPE, "name");
      valueOfMethod = new MethodNode("valueOf", 9, enumClass, new Parameter[]{stringParameter}, ClassNode.EMPTY_ARRAY, (Statement)null);
      ArgumentListExpression callArguments = new ArgumentListExpression();
      callArguments.addExpression(new ClassExpression(enumClass));
      callArguments.addExpression(new VariableExpression("name"));
      code = new BlockStatement();
      code.addStatement(new ReturnStatement(new MethodCallExpression(new ClassExpression(ClassHelper.Enum_Type), "valueOf", callArguments)));
      valueOfMethod.setCode(code);
      valueOfMethod.setSynthetic(true);
      enumClass.addMethod(valueOfMethod);
   }

   private void addInit(ClassNode enumClass, FieldNode minValue, FieldNode maxValue, FieldNode values, boolean isAic) {
      this.addConstructor(enumClass);
      Parameter[] parameter = new Parameter[]{new Parameter(ClassHelper.OBJECT_TYPE.makeArray(), "para")};
      MethodNode initMethod = new MethodNode("$INIT", 4121, enumClass, parameter, ClassNode.EMPTY_ARRAY, (Statement)null);
      initMethod.setSynthetic(true);
      ConstructorCallExpression cce = new ConstructorCallExpression(ClassNode.THIS, new ArgumentListExpression(new SpreadExpression(new VariableExpression("para"))));
      BlockStatement code = new BlockStatement();
      code.addStatement(new ReturnStatement(cce));
      initMethod.setCode(code);
      enumClass.addMethod(initMethod);
      List fields = enumClass.getFields();
      List arrayInit = new ArrayList();
      int value = -1;
      Token assign = Token.newSymbol(100, -1, -1);
      List block = new ArrayList();
      FieldNode tempMin = null;
      FieldNode tempMax = null;
      Iterator iterator = fields.iterator();

      while(true) {
         FieldNode field;
         do {
            if (!iterator.hasNext()) {
               if (!isAic) {
                  if (tempMin != null) {
                     block.add(new ExpressionStatement(new BinaryExpression(new FieldExpression(minValue), assign, new FieldExpression(tempMin))));
                     block.add(new ExpressionStatement(new BinaryExpression(new FieldExpression(maxValue), assign, new FieldExpression(tempMax))));
                     enumClass.addField(minValue);
                     enumClass.addField(maxValue);
                  }

                  block.add(new ExpressionStatement(new BinaryExpression(new FieldExpression(values), assign, new ArrayExpression(enumClass, arrayInit))));
                  enumClass.addField(values);
               }

               enumClass.addStaticInitializerStatements(block, true);
               return;
            }

            field = (FieldNode)iterator.next();
         } while((field.getModifiers() & 16384) == 0);

         ++value;
         if (tempMin == null) {
            tempMin = field;
         }

         tempMax = field;
         ClassNode enumBase = enumClass;
         ArgumentListExpression args = new ArgumentListExpression();
         args.addExpression(new ConstantExpression(field.getName()));
         args.addExpression(new ConstantExpression(value));
         if (field.getInitialExpression() != null) {
            ListExpression oldArgs = (ListExpression)field.getInitialExpression();
            Iterator oldArgsIterator = oldArgs.getExpressions().iterator();

            label56:
            while(true) {
               while(true) {
                  if (!oldArgsIterator.hasNext()) {
                     break label56;
                  }

                  Expression exp = (Expression)oldArgsIterator.next();
                  if (exp instanceof MapEntryExpression) {
                     String msg = "The usage of a map entry expression to initialize an Enum is currently not supported, please use an explicit map instead.";
                     this.sourceUnit.getErrorCollector().addErrorAndContinue(new SyntaxErrorMessage(new SyntaxException(msg + '\n', exp.getLineNumber(), exp.getColumnNumber()), this.sourceUnit));
                  } else {
                     InnerClassNode inner = null;
                     if (exp instanceof ClassExpression) {
                        ClassExpression clazzExp = (ClassExpression)exp;
                        ClassNode ref = clazzExp.getType();
                        if (ref instanceof EnumConstantClassNode) {
                           inner = (InnerClassNode)ref;
                        }
                     }

                     if (inner != null && inner.getVariableScope() == null) {
                        enumBase = inner;
                        initMethod.setModifiers(initMethod.getModifiers() & -17);
                     } else {
                        args.addExpression(exp);
                     }
                  }
               }
            }
         }

         field.setInitialValueExpression((Expression)null);
         block.add(new ExpressionStatement(new BinaryExpression(new FieldExpression(field), assign, new StaticMethodCallExpression((ClassNode)enumBase, "$INIT", args))));
         arrayInit.add(new FieldExpression(field));
      }
   }

   private boolean isAnonymousInnerClass(ClassNode enumClass) {
      if (!(enumClass instanceof EnumConstantClassNode)) {
         return false;
      } else {
         InnerClassNode ic = (InnerClassNode)enumClass;
         return ic.getVariableScope() == null;
      }
   }

   private void addConstructor(ClassNode enumClass) {
      List ctors = new ArrayList(enumClass.getDeclaredConstructors());
      if (ctors.size() == 0) {
         ConstructorNode init = new ConstructorNode(1, new Parameter[0], ClassNode.EMPTY_ARRAY, new BlockStatement());
         enumClass.addConstructor(init);
         ctors.add(init);
      }

      Iterator iterator = ctors.iterator();

      while(true) {
         boolean chainedThisConstructorCall;
         ConstructorNode ctor;
         ConstructorCallExpression cce;
         while(true) {
            if (!iterator.hasNext()) {
               return;
            }

            chainedThisConstructorCall = false;
            ctor = (ConstructorNode)iterator.next();
            cce = null;
            if (!ctor.firstStatementIsSpecialConstructorCall()) {
               break;
            }

            Statement code = ctor.getFirstStatement();
            cce = (ConstructorCallExpression)((ExpressionStatement)code).getExpression();
            if (!cce.isSuperCall()) {
               chainedThisConstructorCall = true;
               break;
            }
         }

         Parameter[] oldP = ctor.getParameters();
         Parameter[] newP = new Parameter[oldP.length + 2];
         String stringParameterName = this.getUniqueVariableName("__str", ctor.getCode());
         newP[0] = new Parameter(ClassHelper.STRING_TYPE, stringParameterName);
         String intParameterName = this.getUniqueVariableName("__int", ctor.getCode());
         newP[1] = new Parameter(ClassHelper.int_TYPE, intParameterName);
         System.arraycopy(oldP, 0, newP, 2, oldP.length);
         ctor.setParameters(newP);
         if (chainedThisConstructorCall) {
            TupleExpression args = (TupleExpression)cce.getArguments();
            List<Expression> argsExprs = args.getExpressions();
            argsExprs.add(0, new VariableExpression(stringParameterName));
            argsExprs.add(1, new VariableExpression(intParameterName));
         } else {
            cce = new ConstructorCallExpression(ClassNode.SUPER, new ArgumentListExpression(new VariableExpression(stringParameterName), new VariableExpression(intParameterName)));
            BlockStatement code = new BlockStatement();
            code.addStatement(new ExpressionStatement(cce));
            Statement oldCode = ctor.getCode();
            if (oldCode != null) {
               code.addStatement(oldCode);
            }

            ctor.setCode(code);
         }
      }
   }

   private String getUniqueVariableName(final String name, Statement code) {
      if (code == null) {
         return name;
      } else {
         final Object[] found = new Object[1];
         CodeVisitorSupport cv = new CodeVisitorSupport() {
            public void visitVariableExpression(VariableExpression expression) {
               if (expression.getName().equals(name)) {
                  found[0] = Boolean.TRUE;
               }

            }
         };
         code.visit(cv);
         return found[0] != null ? this.getUniqueVariableName("_" + name, code) : name;
      }
   }
}
