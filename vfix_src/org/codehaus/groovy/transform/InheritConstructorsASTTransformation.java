package org.codehaus.groovy.transform;

import groovy.transform.InheritConstructors;
import groovyjarjarasm.asm.Opcodes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.codehaus.groovy.GroovyBugError;
import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.AnnotatedNode;
import org.codehaus.groovy.ast.AnnotationNode;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.ConstructorNode;
import org.codehaus.groovy.ast.Parameter;
import org.codehaus.groovy.ast.expr.ArgumentListExpression;
import org.codehaus.groovy.ast.expr.ConstructorCallExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.VariableExpression;
import org.codehaus.groovy.ast.stmt.BlockStatement;
import org.codehaus.groovy.ast.stmt.ExpressionStatement;
import org.codehaus.groovy.control.CompilePhase;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.control.messages.SyntaxErrorMessage;
import org.codehaus.groovy.syntax.SyntaxException;

@GroovyASTTransformation(
   phase = CompilePhase.CANONICALIZATION
)
public class InheritConstructorsASTTransformation implements ASTTransformation, Opcodes {
   private static final Class MY_CLASS = InheritConstructors.class;
   private static final ClassNode MY_TYPE;
   private static final String MY_TYPE_NAME;

   public void visit(ASTNode[] nodes, SourceUnit source) {
      if (nodes.length == 2 && nodes[0] instanceof AnnotationNode && nodes[1] instanceof AnnotatedNode) {
         AnnotatedNode parent = (AnnotatedNode)nodes[1];
         AnnotationNode node = (AnnotationNode)nodes[0];
         if (MY_TYPE.equals(node.getClassNode())) {
            if (parent instanceof ClassNode) {
               ClassNode cNode = (ClassNode)parent;
               if (cNode.isInterface()) {
                  this.addError("Error processing interface '" + cNode.getName() + "'. " + MY_TYPE_NAME + " only allowed for classes.", cNode, source);
               } else {
                  ClassNode sNode = cNode.getSuperClass();
                  Iterator i$ = sNode.getDeclaredConstructors().iterator();

                  while(true) {
                     ConstructorNode cn;
                     Parameter[] params;
                     do {
                        if (!i$.hasNext()) {
                           return;
                        }

                        cn = (ConstructorNode)i$.next();
                        params = cn.getParameters();
                     } while(cn.isPrivate());

                     Parameter[] pcopy = new Parameter[params.length];
                     List<Expression> args = new ArrayList();

                     for(int i = 0; i < params.length; ++i) {
                        Parameter p = params[i];
                        pcopy[i] = p.hasInitialExpression() ? new Parameter(p.getType(), p.getName(), p.getInitialExpression()) : new Parameter(p.getType(), p.getName());
                        args.add(new VariableExpression(p.getName(), p.getType()));
                     }

                     if (!this.isClashing(cNode, pcopy)) {
                        BlockStatement body = new BlockStatement();
                        body.addStatement(new ExpressionStatement(new ConstructorCallExpression(ClassNode.SUPER, new ArgumentListExpression(args))));
                        cNode.addConstructor(cn.getModifiers(), pcopy, cn.getExceptions(), body);
                     }
                  }
               }
            }
         }
      } else {
         throw new GroovyBugError("Internal error: expecting [AnnotationNode, AnnotatedNode] but got: " + Arrays.asList(nodes));
      }
   }

   private boolean isClashing(ClassNode cNode, Parameter[] pcopy) {
      Iterator i$ = cNode.getDeclaredConstructors().iterator();

      ConstructorNode cn;
      do {
         if (!i$.hasNext()) {
            return false;
         }

         cn = (ConstructorNode)i$.next();
      } while(!this.conflictingTypes(pcopy, cn.getParameters()));

      return true;
   }

   private boolean conflictingTypes(Parameter[] pcopy, Parameter[] parameters) {
      if (pcopy.length != parameters.length) {
         return false;
      } else {
         for(int i = 0; i < pcopy.length; ++i) {
            if (!pcopy[i].getType().equals(parameters[i].getType())) {
               return false;
            }
         }

         return true;
      }
   }

   private void addError(String msg, ASTNode expr, SourceUnit source) {
      int line = expr.getLineNumber();
      int col = expr.getColumnNumber();
      source.getErrorCollector().addErrorAndContinue(new SyntaxErrorMessage(new SyntaxException(msg + '\n', line, col), source));
   }

   static {
      MY_TYPE = new ClassNode(MY_CLASS);
      MY_TYPE_NAME = "@" + MY_TYPE.getNameWithoutPackage();
   }
}
