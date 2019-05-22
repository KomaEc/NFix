package org.codehaus.groovy.transform;

import groovy.lang.GroovyObject;
import groovyjarjarasm.asm.Opcodes;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.codehaus.groovy.GroovyBugError;
import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.AnnotatedNode;
import org.codehaus.groovy.ast.AnnotationNode;
import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.FieldNode;
import org.codehaus.groovy.ast.GenericsType;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.Parameter;
import org.codehaus.groovy.ast.PropertyNode;
import org.codehaus.groovy.ast.expr.ArgumentListExpression;
import org.codehaus.groovy.ast.expr.BinaryExpression;
import org.codehaus.groovy.ast.expr.ConstantExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.ast.expr.PropertyExpression;
import org.codehaus.groovy.ast.expr.VariableExpression;
import org.codehaus.groovy.ast.stmt.ExpressionStatement;
import org.codehaus.groovy.ast.stmt.ReturnStatement;
import org.codehaus.groovy.classgen.Verifier;
import org.codehaus.groovy.control.CompilePhase;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.control.messages.SyntaxErrorMessage;
import org.codehaus.groovy.syntax.SyntaxException;
import org.codehaus.groovy.syntax.Token;

@GroovyASTTransformation(
   phase = CompilePhase.CANONICALIZATION
)
public class DelegateASTTransformation implements ASTTransformation, Opcodes {
   private static final ClassNode DEPRECATED_TYPE = new ClassNode(Deprecated.class);
   private static final ClassNode GROOVYOBJECT_TYPE = new ClassNode(GroovyObject.class);

   public void visit(ASTNode[] nodes, SourceUnit source) {
      if (nodes.length == 2 && nodes[0] instanceof AnnotationNode && nodes[1] instanceof AnnotatedNode) {
         AnnotatedNode parent = (AnnotatedNode)nodes[1];
         AnnotationNode node = (AnnotationNode)nodes[0];
         if (parent instanceof FieldNode) {
            FieldNode fieldNode = (FieldNode)parent;
            ClassNode type = fieldNode.getType();
            ClassNode owner = fieldNode.getOwner();
            if (type.equals(ClassHelper.OBJECT_TYPE) || type.equals(GROOVYOBJECT_TYPE)) {
               this.addError("@Delegate field '" + fieldNode.getName() + "' has an inappropriate type: " + type.getName() + ". Please add an explicit type but not java.lang.Object or groovy.lang.GroovyObject.", parent, source);
               return;
            }

            if (type.equals(owner)) {
               this.addError("@Delegate field '" + fieldNode.getName() + "' has an inappropriate type: " + type.getName() + ". Delegation to own type not supported. Please use a different type.", parent, source);
               return;
            }

            List<MethodNode> fieldMethods = this.getAllMethods(type);
            Expression deprecatedElement = node.getMember("deprecated");
            boolean deprecated = this.hasBooleanValue(deprecatedElement, true);
            Iterator i$ = fieldMethods.iterator();

            while(i$.hasNext()) {
               MethodNode mn = (MethodNode)i$.next();
               this.addDelegateMethod(fieldNode, owner, this.getAllMethods(owner), mn, deprecated);
            }

            i$ = type.getProperties().iterator();

            while(i$.hasNext()) {
               PropertyNode prop = (PropertyNode)i$.next();
               if (!prop.isStatic() && prop.isPublic()) {
                  String name = prop.getName();
                  this.addGetterIfNeeded(fieldNode, owner, prop, name);
                  this.addSetterIfNeeded(fieldNode, owner, prop, name);
               }
            }

            Expression interfacesElement = node.getMember("interfaces");
            if (this.hasBooleanValue(interfacesElement, false)) {
               return;
            }

            Set<ClassNode> allInterfaces = type.getAllInterfaces();
            Set<ClassNode> ownerIfaces = owner.getAllInterfaces();
            Iterator i$ = allInterfaces.iterator();

            while(i$.hasNext()) {
               ClassNode iface = (ClassNode)i$.next();
               if (Modifier.isPublic(iface.getModifiers()) && !ownerIfaces.contains(iface)) {
                  ClassNode[] ifaces = owner.getInterfaces();
                  ClassNode[] newIfaces = new ClassNode[ifaces.length + 1];
                  System.arraycopy(ifaces, 0, newIfaces, 0, ifaces.length);
                  newIfaces[ifaces.length] = iface;
                  owner.setInterfaces(newIfaces);
               }
            }
         }

      } else {
         throw new GroovyBugError("Internal error: expecting [AnnotationNode, AnnotatedNode] but got: " + Arrays.asList(nodes));
      }
   }

   private List<MethodNode> getAllMethods(ClassNode type) {
      ClassNode node = type;

      ArrayList result;
      for(result = new ArrayList(); node != null; node = node.getSuperClass()) {
         result.addAll(node.getMethods());
      }

      return result;
   }

   private boolean hasBooleanValue(Expression expression, boolean bool) {
      return expression instanceof ConstantExpression && ((ConstantExpression)expression).getValue().equals(bool);
   }

   private void addSetterIfNeeded(FieldNode fieldNode, ClassNode owner, PropertyNode prop, String name) {
      String setterName = "set" + Verifier.capitalize(name);
      if ((prop.getModifiers() & 16) == 0 && owner.getSetterMethod(setterName) == null) {
         owner.addMethod(setterName, 1, ClassHelper.VOID_TYPE, new Parameter[]{new Parameter(this.nonGeneric(prop.getType()), "value")}, (ClassNode[])null, new ExpressionStatement(new BinaryExpression(new PropertyExpression(new VariableExpression(fieldNode), name), Token.newSymbol(100, -1, -1), new VariableExpression("value"))));
      }

   }

   private void addGetterIfNeeded(FieldNode fieldNode, ClassNode owner, PropertyNode prop, String name) {
      String getterName = "get" + Verifier.capitalize(name);
      if (owner.getGetterMethod(getterName) == null) {
         owner.addMethod(getterName, 1, this.nonGeneric(prop.getType()), Parameter.EMPTY_ARRAY, (ClassNode[])null, new ReturnStatement(new PropertyExpression(new VariableExpression(fieldNode), name)));
      }

   }

   private void addDelegateMethod(FieldNode fieldNode, ClassNode owner, List<MethodNode> ownMethods, MethodNode candidate, boolean deprecated) {
      if (candidate.isPublic() && !candidate.isStatic() && 0 == (candidate.getModifiers() & 4096)) {
         if (candidate.getAnnotations(DEPRECATED_TYPE).isEmpty() || deprecated) {
            Iterator i$ = GROOVYOBJECT_TYPE.getMethods().iterator();

            while(i$.hasNext()) {
               MethodNode mn = (MethodNode)i$.next();
               if (mn.getTypeDescriptor().equals(candidate.getTypeDescriptor())) {
                  return;
               }
            }

            MethodNode existingNode = null;
            Iterator i$ = ownMethods.iterator();

            while(i$.hasNext()) {
               MethodNode mn = (MethodNode)i$.next();
               if (mn.getTypeDescriptor().equals(candidate.getTypeDescriptor()) && !mn.isAbstract() && !mn.isStatic()) {
                  existingNode = mn;
                  break;
               }
            }

            if (existingNode == null || existingNode.getCode() == null) {
               ArgumentListExpression args = new ArgumentListExpression();
               Parameter[] params = candidate.getParameters();
               Parameter[] newParams = new Parameter[params.length];

               for(int i = 0; i < newParams.length; ++i) {
                  Parameter newParam = new Parameter(this.nonGeneric(params[i].getType()), params[i].getName());
                  newParam.setInitialExpression(params[i].getInitialExpression());
                  newParams[i] = newParam;
                  args.addExpression(new VariableExpression(newParam));
               }

               MethodNode newMethod = owner.addMethod(candidate.getName(), candidate.getModifiers() & -1025 & -257, this.nonGeneric(candidate.getReturnType()), newParams, candidate.getExceptions(), new ExpressionStatement(new MethodCallExpression(new VariableExpression(fieldNode), candidate.getName(), args)));
               newMethod.setGenericsTypes(candidate.getGenericsTypes());
            }

         }
      }
   }

   private ClassNode nonGeneric(ClassNode type) {
      if (type.isUsingGenerics()) {
         ClassNode nonGen = ClassHelper.makeWithoutCaching(type.getName());
         nonGen.setRedirect(type);
         nonGen.setGenericsTypes((GenericsType[])null);
         nonGen.setUsingGenerics(false);
         return nonGen;
      } else {
         return type;
      }
   }

   public void addError(String msg, ASTNode expr, SourceUnit source) {
      int line = expr.getLineNumber();
      int col = expr.getColumnNumber();
      source.getErrorCollector().addErrorAndContinue(new SyntaxErrorMessage(new SyntaxException(msg + '\n', line, col), source));
   }
}
