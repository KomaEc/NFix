package org.codehaus.groovy.transform;

import groovy.transform.Synchronized;
import groovyjarjarasm.asm.Opcodes;
import java.util.Arrays;
import java.util.List;
import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.AnnotatedNode;
import org.codehaus.groovy.ast.AnnotationNode;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.FieldNode;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.expr.ArrayExpression;
import org.codehaus.groovy.ast.expr.ConstantExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.VariableExpression;
import org.codehaus.groovy.ast.stmt.Statement;
import org.codehaus.groovy.ast.stmt.SynchronizedStatement;
import org.codehaus.groovy.control.CompilePhase;
import org.codehaus.groovy.control.SourceUnit;

@GroovyASTTransformation(
   phase = CompilePhase.CANONICALIZATION
)
public class SynchronizedASTTransformation implements ASTTransformation, Opcodes {
   private static final Class MY_CLASS = Synchronized.class;
   private static final ClassNode MY_TYPE;
   private static final String MY_TYPE_NAME;
   private static final ClassNode OBJECT_TYPE;

   public void visit(ASTNode[] nodes, SourceUnit source) {
      if (nodes.length == 2 && nodes[0] instanceof AnnotationNode && nodes[1] instanceof AnnotatedNode) {
         AnnotatedNode parent = (AnnotatedNode)nodes[1];
         AnnotationNode node = (AnnotationNode)nodes[0];
         if (MY_TYPE.equals(node.getClassNode())) {
            Expression valueExpr = node.getMember("value");
            String value = null;
            if (valueExpr instanceof ConstantExpression) {
               ConstantExpression ce = (ConstantExpression)valueExpr;
               Object valueObject = ce.getValue();
               if (valueObject != null) {
                  value = valueObject.toString();
               }
            }

            if (parent instanceof MethodNode) {
               MethodNode mNode = (MethodNode)parent;
               ClassNode cNode = mNode.getDeclaringClass();
               String lockExpr = this.determineLock(value, cNode, mNode.isStatic());
               Statement origCode = mNode.getCode();
               Statement newCode = new SynchronizedStatement(new VariableExpression(lockExpr), origCode);
               mNode.setCode(newCode);
            }

         }
      } else {
         throw new RuntimeException("Internal error: expecting [AnnotationNode, AnnotatedNode] but got: " + Arrays.asList(nodes));
      }
   }

   private String determineLock(String value, ClassNode cNode, boolean isStatic) {
      if (value != null && value.length() > 0 && !value.equalsIgnoreCase("$lock")) {
         if (cNode.getDeclaredField(value) == null) {
            throw new RuntimeException("Error during " + MY_TYPE_NAME + " processing: lock field with name '" + value + "' not found in class " + cNode.getName());
         } else if (cNode.getDeclaredField(value).isStatic() != isStatic) {
            throw new RuntimeException("Error during " + MY_TYPE_NAME + " processing: lock field with name '" + value + "' should " + (isStatic ? "" : "not ") + "be static");
         } else {
            return value;
         }
      } else {
         FieldNode field;
         byte visibility;
         if (isStatic) {
            field = cNode.getDeclaredField("$LOCK");
            if (field == null) {
               visibility = 26;
               cNode.addField("$LOCK", visibility, OBJECT_TYPE, this.zeroLengthObjectArray());
            } else if (!field.isStatic()) {
               throw new RuntimeException("Error during " + MY_TYPE_NAME + " processing: $LOCK field must be static");
            }

            return "$LOCK";
         } else {
            field = cNode.getDeclaredField("$lock");
            if (field == null) {
               visibility = 18;
               cNode.addField("$lock", visibility, OBJECT_TYPE, this.zeroLengthObjectArray());
            } else if (field.isStatic()) {
               throw new RuntimeException("Error during " + MY_TYPE_NAME + " processing: $lock field must not be static");
            }

            return "$lock";
         }
      }
   }

   private Expression zeroLengthObjectArray() {
      return new ArrayExpression(OBJECT_TYPE, (List)null, Arrays.asList(new ConstantExpression(0)));
   }

   static {
      MY_TYPE = new ClassNode(MY_CLASS);
      MY_TYPE_NAME = "@" + MY_TYPE.getNameWithoutPackage();
      OBJECT_TYPE = new ClassNode(Object.class);
   }
}
