package org.codehaus.groovy.transform;

import groovy.transform.IndexedProperty;
import groovyjarjarasm.asm.Opcodes;
import java.util.Arrays;
import java.util.List;
import org.codehaus.groovy.GroovyBugError;
import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.AnnotatedNode;
import org.codehaus.groovy.ast.AnnotationNode;
import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.FieldNode;
import org.codehaus.groovy.ast.Parameter;
import org.codehaus.groovy.ast.expr.BinaryExpression;
import org.codehaus.groovy.ast.expr.VariableExpression;
import org.codehaus.groovy.ast.stmt.BlockStatement;
import org.codehaus.groovy.ast.stmt.ExpressionStatement;
import org.codehaus.groovy.control.CompilePhase;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.control.messages.SyntaxErrorMessage;
import org.codehaus.groovy.runtime.MetaClassHelper;
import org.codehaus.groovy.syntax.SyntaxException;
import org.codehaus.groovy.syntax.Token;

@GroovyASTTransformation(
   phase = CompilePhase.CANONICALIZATION
)
public class IndexedPropertyASTTransformation implements ASTTransformation, Opcodes {
   private static final Class MY_CLASS = IndexedProperty.class;
   private static final ClassNode MY_TYPE;
   private static final String MY_TYPE_NAME;
   private static final ClassNode LIST_TYPE;
   private static final ClassNode OBJECT_TYPE;
   private static final Token ASSIGN;
   private static final Token INDEX;

   public void visit(ASTNode[] nodes, SourceUnit source) {
      if (nodes.length == 2 && nodes[0] instanceof AnnotationNode && nodes[1] instanceof AnnotatedNode) {
         AnnotatedNode parent = (AnnotatedNode)nodes[1];
         AnnotationNode node = (AnnotationNode)nodes[0];
         if (MY_TYPE.equals(node.getClassNode())) {
            if (parent instanceof FieldNode) {
               FieldNode fNode = (FieldNode)parent;
               ClassNode cNode = fNode.getDeclaringClass();
               if (cNode.getProperty(fNode.getName()) == null) {
                  this.addError("Error during " + MY_TYPE_NAME + " processing. Field '" + fNode.getName() + "' doesn't appear to be a property; incorrect visibility?", fNode, source);
                  return;
               }

               ClassNode fType = fNode.getType();
               if (fType.isArray()) {
                  this.addArraySetter(fNode);
                  this.addArrayGetter(fNode);
               } else if (fType.isDerivedFrom(LIST_TYPE)) {
                  this.addListSetter(fNode);
                  this.addListGetter(fNode);
               } else {
                  this.addError("Error during " + MY_TYPE_NAME + " processing. Non-Indexable property '" + fNode.getName() + "' found. Type must be array or list but found " + fType.getName(), fNode, source);
               }
            }

         }
      } else {
         throw new GroovyBugError("Internal error: expecting [AnnotationNode, AnnotatedNode] but got: " + Arrays.asList(nodes));
      }
   }

   private void addListGetter(FieldNode fNode) {
      this.addGetter(fNode, this.getComponentTypeForList(fNode.getType()));
   }

   private void addListSetter(FieldNode fNode) {
      this.addSetter(fNode, this.getComponentTypeForList(fNode.getType()));
   }

   private void addArrayGetter(FieldNode fNode) {
      this.addGetter(fNode, fNode.getType().getComponentType());
   }

   private void addArraySetter(FieldNode fNode) {
      this.addSetter(fNode, fNode.getType().getComponentType());
   }

   private void addGetter(FieldNode fNode, ClassNode componentType) {
      ClassNode cNode = fNode.getDeclaringClass();
      BlockStatement body = new BlockStatement();
      Parameter[] params = new Parameter[]{new Parameter(ClassHelper.int_TYPE, "index")};
      body.addStatement(new ExpressionStatement(new BinaryExpression(new VariableExpression(fNode.getName()), INDEX, new VariableExpression(params[0]))));
      cNode.addMethod(this.makeName(fNode, "get"), this.getModifiers(fNode), componentType, params, (ClassNode[])null, body);
   }

   private void addSetter(FieldNode fNode, ClassNode componentType) {
      ClassNode cNode = fNode.getDeclaringClass();
      BlockStatement body = new BlockStatement();
      Parameter[] params = new Parameter[]{new Parameter(ClassHelper.int_TYPE, "index"), new Parameter(componentType, "value")};
      body.addStatement(new ExpressionStatement(new BinaryExpression(new BinaryExpression(new VariableExpression(fNode.getName()), INDEX, new VariableExpression(params[0])), ASSIGN, new VariableExpression(params[1]))));
      cNode.addMethod(this.makeName(fNode, "set"), this.getModifiers(fNode), ClassHelper.VOID_TYPE, params, (ClassNode[])null, body);
   }

   private ClassNode getComponentTypeForList(ClassNode fType) {
      return fType.isUsingGenerics() && fType.getGenericsTypes().length == 1 ? fType.getGenericsTypes()[0].getType() : OBJECT_TYPE;
   }

   private int getModifiers(FieldNode fNode) {
      int mods = 1;
      if (fNode.isStatic()) {
         mods |= 8;
      }

      return mods;
   }

   private String makeName(FieldNode fNode, String prefix) {
      return prefix + MetaClassHelper.capitalize(fNode.getName());
   }

   private void addError(String msg, ASTNode expr, SourceUnit source) {
      int line = expr.getLineNumber();
      int col = expr.getColumnNumber();
      source.getErrorCollector().addErrorAndContinue(new SyntaxErrorMessage(new SyntaxException(msg + '\n', line, col), source));
   }

   static {
      MY_TYPE = new ClassNode(MY_CLASS);
      MY_TYPE_NAME = "@" + MY_TYPE.getNameWithoutPackage();
      LIST_TYPE = new ClassNode(List.class);
      OBJECT_TYPE = new ClassNode(Object.class);
      ASSIGN = Token.newSymbol("=", -1, -1);
      INDEX = Token.newSymbol("[", -1, -1);
   }
}
