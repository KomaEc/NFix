package org.codehaus.groovy.ast;

import groovy.lang.Mixin;
import java.util.Arrays;
import java.util.Iterator;
import org.codehaus.groovy.ast.expr.ClassExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.ListExpression;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.ast.expr.PropertyExpression;
import org.codehaus.groovy.ast.stmt.BlockStatement;
import org.codehaus.groovy.ast.stmt.ExpressionStatement;
import org.codehaus.groovy.control.CompilePhase;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.transform.ASTTransformation;
import org.codehaus.groovy.transform.GroovyASTTransformation;

@GroovyASTTransformation(
   phase = CompilePhase.CANONICALIZATION
)
public class MixinASTTransformation implements ASTTransformation {
   private static final ClassNode MY_TYPE = new ClassNode(Mixin.class);

   public void visit(ASTNode[] nodes, SourceUnit source) {
      if (nodes.length == 2 && nodes[0] instanceof AnnotationNode && nodes[1] instanceof AnnotatedNode) {
         AnnotationNode node = (AnnotationNode)nodes[0];
         AnnotatedNode parent = (AnnotatedNode)nodes[1];
         if (MY_TYPE.equals(node.getClassNode())) {
            Expression expr = node.getMember("value");
            if (expr != null) {
               Expression useClasses = null;
               if (expr instanceof ClassExpression) {
                  useClasses = expr;
               } else if (expr instanceof ListExpression) {
                  ListExpression listExpression = (ListExpression)expr;
                  Iterator i$ = listExpression.getExpressions().iterator();

                  while(i$.hasNext()) {
                     Expression ex = (Expression)i$.next();
                     if (!(ex instanceof ClassExpression)) {
                        return;
                     }
                  }

                  useClasses = expr;
               }

               if (useClasses != null) {
                  if (parent instanceof ClassNode) {
                     ClassNode annotatedClass = (ClassNode)parent;
                     Parameter[] noparams = new Parameter[0];
                     MethodNode clinit = annotatedClass.getDeclaredMethod("<clinit>", noparams);
                     if (clinit == null) {
                        clinit = annotatedClass.addMethod("<clinit>", 4105, ClassHelper.VOID_TYPE, noparams, (ClassNode[])null, new BlockStatement());
                        clinit.setSynthetic(true);
                     }

                     BlockStatement code = (BlockStatement)clinit.getCode();
                     code.addStatement(new ExpressionStatement(new MethodCallExpression(new PropertyExpression(new ClassExpression(annotatedClass), "metaClass"), "mixin", useClasses)));
                  }

               }
            }
         }
      } else {
         throw new RuntimeException("Internal error: expecting [AnnotationNode, AnnotatedNode] but got: " + Arrays.asList(nodes));
      }
   }
}
