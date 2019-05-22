package org.codehaus.groovy.classgen;

import groovyjarjarasm.asm.Opcodes;
import java.util.Iterator;
import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.CodeVisitorSupport;
import org.codehaus.groovy.ast.expr.ConstructorCallExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.FieldExpression;
import org.codehaus.groovy.ast.expr.ListExpression;
import org.codehaus.groovy.ast.expr.MapEntryExpression;
import org.codehaus.groovy.ast.expr.VariableExpression;
import org.codehaus.groovy.ast.stmt.ForStatement;
import org.codehaus.groovy.syntax.RuntimeParserException;

public class VerifierCodeVisitor extends CodeVisitorSupport implements Opcodes {
   private Verifier verifier;

   VerifierCodeVisitor(Verifier verifier) {
      this.verifier = verifier;
   }

   public void visitForLoop(ForStatement expression) {
      assertValidIdentifier(expression.getVariable().getName(), "for loop variable name", expression);
      super.visitForLoop(expression);
   }

   public void visitFieldExpression(FieldExpression expression) {
      if (!expression.getField().isSynthetic()) {
         assertValidIdentifier(expression.getFieldName(), "field name", expression);
      }

      super.visitFieldExpression(expression);
   }

   public void visitVariableExpression(VariableExpression expression) {
      assertValidIdentifier(expression.getName(), "variable name", expression);
      super.visitVariableExpression(expression);
   }

   public void visitListExpression(ListExpression expression) {
      Iterator i$ = expression.getExpressions().iterator();

      Expression element;
      do {
         if (!i$.hasNext()) {
            super.visitListExpression(expression);
            return;
         }

         element = (Expression)i$.next();
      } while(!(element instanceof MapEntryExpression));

      throw new RuntimeParserException("No map entry allowed at this place", element);
   }

   public void visitConstructorCallExpression(ConstructorCallExpression call) {
      ClassNode callType = call.getType();
      if (callType.isEnum() && !callType.equals(this.verifier.getClassNode())) {
         throw new RuntimeParserException("Enum constructor calls are only allowed inside the enum class", call);
      }
   }

   public static void assertValidIdentifier(String name, String message, ASTNode node) {
      int size = name.length();
      if (size <= 0) {
         throw new RuntimeParserException("Invalid " + message + ". Identifier must not be empty", node);
      } else {
         char firstCh = name.charAt(0);
         if (size == 1 && firstCh == '$') {
            throw new RuntimeParserException("Invalid " + message + ". Must include a letter but only found: " + name, node);
         } else if (!Character.isJavaIdentifierStart(firstCh)) {
            throw new RuntimeParserException("Invalid " + message + ". Must start with a letter but was: " + name, node);
         } else {
            for(int i = 1; i < size; ++i) {
               char ch = name.charAt(i);
               if (!Character.isJavaIdentifierPart(ch)) {
                  throw new RuntimeParserException("Invalid " + message + ". Invalid character at position: " + (i + 1) + " of value:  " + ch + " in name: " + name, node);
               }
            }

         }
      }
   }
}
