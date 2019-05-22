package org.codehaus.groovy.transform;

import groovy.lang.Reference;
import groovyjarjarasm.asm.Opcodes;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.AnnotationNode;
import org.codehaus.groovy.ast.ClassCodeExpressionTransformer;
import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.FieldNode;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.Parameter;
import org.codehaus.groovy.ast.expr.ClassExpression;
import org.codehaus.groovy.ast.expr.ClosureExpression;
import org.codehaus.groovy.ast.expr.DeclarationExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.PropertyExpression;
import org.codehaus.groovy.ast.expr.TupleExpression;
import org.codehaus.groovy.ast.expr.VariableExpression;
import org.codehaus.groovy.ast.stmt.BlockStatement;
import org.codehaus.groovy.ast.stmt.ExpressionStatement;
import org.codehaus.groovy.ast.stmt.ForStatement;
import org.codehaus.groovy.control.CompilePhase;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.control.messages.SyntaxErrorMessage;
import org.codehaus.groovy.syntax.SyntaxException;

@GroovyASTTransformation(
   phase = CompilePhase.CANONICALIZATION
)
public class CategoryASTTransformation implements ASTTransformation, Opcodes {
   private static final VariableExpression THIS_EXPRESSION = new VariableExpression("$this");

   public void visit(ASTNode[] nodes, final SourceUnit source) {
      if (nodes.length == 2 && nodes[0] instanceof AnnotationNode && nodes[1] instanceof ClassNode) {
         AnnotationNode annotation = (AnnotationNode)nodes[0];
         ClassNode parent = (ClassNode)nodes[1];
         ClassNode targetClass = this.getTargetClass(source, annotation);
         final LinkedList<Set<String>> varStack = new LinkedList();
         Set<String> names = new HashSet();
         Iterator i$ = parent.getFields().iterator();

         while(i$.hasNext()) {
            FieldNode field = (FieldNode)i$.next();
            names.add(field.getName());
         }

         varStack.add(names);
         final Reference parameter = new Reference();
         ClassCodeExpressionTransformer expressionTransformer = new ClassCodeExpressionTransformer() {
            protected SourceUnit getSourceUnit() {
               return source;
            }

            private void addVariablesToStack(Parameter[] params) {
               Set<String> names = new HashSet();
               names.addAll((Collection)varStack.getLast());
               Parameter[] arr$ = params;
               int len$ = params.length;

               for(int i$ = 0; i$ < len$; ++i$) {
                  Parameter param = arr$[i$];
                  names.add(param.getName());
               }

               varStack.add(names);
            }

            public void visitMethod(MethodNode node) {
               this.addVariablesToStack(node.getParameters());
               super.visitMethod(node);
               varStack.removeLast();
            }

            public void visitBlockStatement(BlockStatement block) {
               Set<String> names = new HashSet();
               names.addAll((Collection)varStack.getLast());
               varStack.add(names);
               super.visitBlockStatement(block);
               varStack.remove(names);
            }

            public void visitClosureExpression(ClosureExpression ce) {
               this.addVariablesToStack(ce.getParameters());
               super.visitClosureExpression(ce);
               varStack.removeLast();
            }

            public void visitDeclarationExpression(DeclarationExpression expression) {
               if (expression.isMultipleAssignmentDeclaration()) {
                  TupleExpression te = (TupleExpression)expression.getLeftExpression();
                  List<Expression> list = te.getExpressions();
                  Iterator i$ = list.iterator();

                  while(i$.hasNext()) {
                     Expression arg = (Expression)i$.next();
                     VariableExpression ve = (VariableExpression)arg;
                     ((Set)varStack.getLast()).add(ve.getName());
                  }
               } else {
                  VariableExpression vex = expression.getVariableExpression();
                  ((Set)varStack.getLast()).add(vex.getName());
               }

               super.visitDeclarationExpression(expression);
            }

            public void visitForLoop(ForStatement forLoop) {
               Expression exp = forLoop.getCollectionExpression();
               exp.visit(this);
               Parameter loopParam = forLoop.getVariable();
               if (loopParam != null) {
                  ((Set)varStack.getLast()).add(loopParam.getName());
               }

               super.visitForLoop(forLoop);
            }

            public void visitExpressionStatement(ExpressionStatement es) {
               Expression exp = es.getExpression();
               if (exp instanceof DeclarationExpression) {
                  exp.visit(this);
               }

               super.visitExpressionStatement(es);
            }

            public Expression transform(Expression exp) {
               if (exp instanceof VariableExpression) {
                  VariableExpression ve = (VariableExpression)exp;
                  if (ve.getName().equals("this")) {
                     return CategoryASTTransformation.THIS_EXPRESSION;
                  }

                  if (!((Set)varStack.getLast()).contains(ve.getName())) {
                     return new PropertyExpression(CategoryASTTransformation.THIS_EXPRESSION, ve.getName());
                  }
               } else if (exp instanceof PropertyExpression) {
                  PropertyExpression pe = (PropertyExpression)exp;
                  if (pe.getObjectExpression() instanceof VariableExpression) {
                     VariableExpression vex = (VariableExpression)pe.getObjectExpression();
                     if (vex.isThisExpression()) {
                        pe.setObjectExpression(CategoryASTTransformation.THIS_EXPRESSION);
                        return pe;
                     }
                  }
               } else if (exp instanceof ClosureExpression) {
                  ClosureExpression ce = (ClosureExpression)exp;
                  ce.getVariableScope().putReferencedLocalVariable((Parameter)parameter.get());
                  Parameter[] params = ce.getParameters();
                  if (params == null) {
                     params = new Parameter[0];
                  } else if (params.length == 0) {
                     params = new Parameter[]{new Parameter(ClassHelper.OBJECT_TYPE, "it")};
                  }

                  this.addVariablesToStack(params);
                  ce.getCode().visit(this);
                  varStack.removeLast();
               }

               return super.transform(exp);
            }
         };
         Iterator i$ = parent.getMethods().iterator();

         while(i$.hasNext()) {
            MethodNode method = (MethodNode)i$.next();
            if (!method.isStatic()) {
               method.setModifiers(method.getModifiers() | 8);
               Parameter[] origParams = method.getParameters();
               Parameter[] newParams = new Parameter[origParams.length + 1];
               Parameter p = new Parameter(targetClass, "$this");
               p.setClosureSharedVariable(true);
               newParams[0] = p;
               parameter.set(p);
               System.arraycopy(origParams, 0, newParams, 1, origParams.length);
               method.setParameters(newParams);
               expressionTransformer.visitMethod(method);
            }
         }

      } else {
         throw new RuntimeException("Internal error: expecting [AnnotationNode, ClassNode] but got: " + Arrays.asList(nodes));
      }
   }

   private ClassNode getTargetClass(SourceUnit source, AnnotationNode annotation) {
      Expression value = annotation.getMember("value");
      if (value == null || !(value instanceof ClassExpression)) {
         source.getErrorCollector().addErrorAndContinue(new SyntaxErrorMessage(new SyntaxException("@groovy.lang.Category must define 'value' which is the class to apply this category to", annotation.getLineNumber(), annotation.getColumnNumber()), source));
      }

      return value != null ? value.getType() : null;
   }

   static {
      THIS_EXPRESSION.setClosureSharedVariable(true);
   }
}
