package groovy.beans;

import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.beans.VetoableChangeSupport;
import java.util.Iterator;
import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.AnnotatedNode;
import org.codehaus.groovy.ast.AnnotationNode;
import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.FieldNode;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.Parameter;
import org.codehaus.groovy.ast.PropertyNode;
import org.codehaus.groovy.ast.expr.ArgumentListExpression;
import org.codehaus.groovy.ast.expr.BinaryExpression;
import org.codehaus.groovy.ast.expr.ConstantExpression;
import org.codehaus.groovy.ast.expr.ConstructorCallExpression;
import org.codehaus.groovy.ast.expr.DeclarationExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.FieldExpression;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.ast.expr.VariableExpression;
import org.codehaus.groovy.ast.stmt.BlockStatement;
import org.codehaus.groovy.ast.stmt.ExpressionStatement;
import org.codehaus.groovy.ast.stmt.ReturnStatement;
import org.codehaus.groovy.ast.stmt.Statement;
import org.codehaus.groovy.control.CompilePhase;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.control.messages.SimpleMessage;
import org.codehaus.groovy.control.messages.SyntaxErrorMessage;
import org.codehaus.groovy.runtime.MetaClassHelper;
import org.codehaus.groovy.syntax.SyntaxException;
import org.codehaus.groovy.syntax.Token;
import org.codehaus.groovy.transform.GroovyASTTransformation;

@GroovyASTTransformation(
   phase = CompilePhase.CANONICALIZATION
)
public class VetoableASTTransformation extends BindableASTTransformation {
   protected static ClassNode constrainedClassNode = new ClassNode(Vetoable.class);
   protected ClassNode vcsClassNode = new ClassNode(VetoableChangeSupport.class);

   public static boolean hasVetoableAnnotation(AnnotatedNode node) {
      Iterator i$ = node.getAnnotations().iterator();

      AnnotationNode annotation;
      do {
         if (!i$.hasNext()) {
            return false;
         }

         annotation = (AnnotationNode)i$.next();
      } while(!constrainedClassNode.equals(annotation.getClassNode()));

      return true;
   }

   public void visit(ASTNode[] nodes, SourceUnit source) {
      if (nodes[0] instanceof AnnotationNode && nodes[1] instanceof AnnotatedNode) {
         AnnotationNode node = (AnnotationNode)nodes[0];
         if (nodes[1] instanceof ClassNode) {
            this.addListenerToClass(source, node, (ClassNode)nodes[1]);
         } else {
            if ((((FieldNode)nodes[1]).getModifiers() & 16) != 0) {
               source.getErrorCollector().addErrorAndContinue(new SyntaxErrorMessage(new SyntaxException("@groovy.beans.Vetoable cannot annotate a final property.", node.getLineNumber(), node.getColumnNumber()), source));
            }

            this.addListenerToProperty(source, node, (AnnotatedNode)nodes[1]);
         }

      } else {
         throw new RuntimeException("Internal error: wrong types: $node.class / $parent.class");
      }
   }

   private void addListenerToProperty(SourceUnit source, AnnotationNode node, AnnotatedNode parent) {
      ClassNode declaringClass = parent.getDeclaringClass();
      FieldNode field = (FieldNode)parent;
      String fieldName = field.getName();
      Iterator i$ = declaringClass.getProperties().iterator();

      PropertyNode propertyNode;
      boolean bindable;
      do {
         if (!i$.hasNext()) {
            source.getErrorCollector().addErrorAndContinue(new SyntaxErrorMessage(new SyntaxException("@groovy.beans.Vetoable must be on a property, not a field.  Try removing the private, protected, or public modifier.", node.getLineNumber(), node.getColumnNumber()), source));
            return;
         }

         propertyNode = (PropertyNode)i$.next();
         bindable = BindableASTTransformation.hasBindableAnnotation(parent) || BindableASTTransformation.hasBindableAnnotation(parent.getDeclaringClass());
      } while(!propertyNode.getName().equals(fieldName));

      if (field.isStatic()) {
         source.getErrorCollector().addErrorAndContinue(new SyntaxErrorMessage(new SyntaxException("@groovy.beans.Vetoable cannot annotate a static property.", node.getLineNumber(), node.getColumnNumber()), source));
      } else {
         this.createListenerSetter(source, node, bindable, declaringClass, propertyNode);
      }

   }

   private void addListenerToClass(SourceUnit source, AnnotationNode node, ClassNode classNode) {
      boolean bindable = BindableASTTransformation.hasBindableAnnotation(classNode);
      Iterator i$ = classNode.getProperties().iterator();

      while(true) {
         PropertyNode propertyNode;
         do {
            do {
               do {
                  if (!i$.hasNext()) {
                     return;
                  }

                  propertyNode = (PropertyNode)i$.next();
               } while(hasVetoableAnnotation(propertyNode.getField()));
            } while((propertyNode.getField().getModifiers() & 16) != 0);
         } while(propertyNode.getField().isStatic());

         this.createListenerSetter(source, node, bindable || BindableASTTransformation.hasBindableAnnotation(propertyNode.getField()), classNode, propertyNode);
      }
   }

   private void wrapSetterMethod(ClassNode classNode, boolean bindable, String propertyName) {
      String getterName = "get" + MetaClassHelper.capitalize(propertyName);
      MethodNode setter = classNode.getSetterMethod("set" + MetaClassHelper.capitalize(propertyName));
      if (setter != null) {
         Statement code = setter.getCode();
         VariableExpression oldValue = new VariableExpression("$oldValue");
         VariableExpression newValue = new VariableExpression("$newValue");
         VariableExpression proposedValue = new VariableExpression(setter.getParameters()[0].getName());
         BlockStatement block = new BlockStatement();
         block.addStatement(new ExpressionStatement(new DeclarationExpression(oldValue, Token.newSymbol(100, 0, 0), new MethodCallExpression(VariableExpression.THIS_EXPRESSION, getterName, ArgumentListExpression.EMPTY_ARGUMENTS))));
         block.addStatement(new ExpressionStatement(new MethodCallExpression(VariableExpression.THIS_EXPRESSION, "fireVetoableChange", new ArgumentListExpression(new Expression[]{new ConstantExpression(propertyName), oldValue, proposedValue}))));
         block.addStatement(code);
         if (bindable) {
            block.addStatement(new ExpressionStatement(new DeclarationExpression(newValue, Token.newSymbol(100, 0, 0), new MethodCallExpression(VariableExpression.THIS_EXPRESSION, getterName, ArgumentListExpression.EMPTY_ARGUMENTS))));
            block.addStatement(new ExpressionStatement(new MethodCallExpression(VariableExpression.THIS_EXPRESSION, "firePropertyChange", new ArgumentListExpression(new Expression[]{new ConstantExpression(propertyName), oldValue, newValue}))));
         }

         setter.setCode(block);
      }

   }

   private void createListenerSetter(SourceUnit source, AnnotationNode node, boolean bindable, ClassNode declaringClass, PropertyNode propertyNode) {
      if (bindable && this.needsPropertyChangeSupport(declaringClass, source)) {
         this.addPropertyChangeSupport(declaringClass);
      }

      if (this.needsVetoableChangeSupport(declaringClass, source)) {
         this.addVetoableChangeSupport(declaringClass);
      }

      String setterName = "set" + MetaClassHelper.capitalize(propertyNode.getName());
      if (declaringClass.getMethods(setterName).isEmpty()) {
         Expression fieldExpression = new FieldExpression(propertyNode.getField());
         BlockStatement setterBlock = new BlockStatement();
         setterBlock.addStatement(this.createConstrainedStatement(propertyNode, fieldExpression));
         if (bindable) {
            setterBlock.addStatement(this.createBindableStatement(propertyNode, fieldExpression));
         } else {
            setterBlock.addStatement(this.createSetStatement(fieldExpression));
         }

         this.createSetterMethod(declaringClass, propertyNode, setterName, setterBlock);
      } else {
         this.wrapSetterMethod(declaringClass, bindable, propertyNode.getName());
      }

   }

   protected Statement createConstrainedStatement(PropertyNode propertyNode, Expression fieldExpression) {
      return new ExpressionStatement(new MethodCallExpression(VariableExpression.THIS_EXPRESSION, "fireVetoableChange", new ArgumentListExpression(new Expression[]{new ConstantExpression(propertyNode.getName()), fieldExpression, new VariableExpression("value")})));
   }

   protected Statement createSetStatement(Expression fieldExpression) {
      return new ExpressionStatement(new BinaryExpression(fieldExpression, Token.newSymbol(100, 0, 0), new VariableExpression("value")));
   }

   protected boolean needsVetoableChangeSupport(ClassNode declaringClass, SourceUnit sourceUnit) {
      boolean foundAdd = false;
      boolean foundRemove = false;
      boolean foundFire = false;

      ClassNode consideredClass;
      Iterator i$;
      for(consideredClass = declaringClass; consideredClass != null; consideredClass = consideredClass.getSuperClass()) {
         i$ = consideredClass.getMethods().iterator();

         while(i$.hasNext()) {
            MethodNode method = (MethodNode)i$.next();
            foundAdd = foundAdd || method.getName().equals("addVetoableChangeListener") && method.getParameters().length == 1;
            foundRemove = foundRemove || method.getName().equals("removeVetoableChangeListener") && method.getParameters().length == 1;
            foundFire = foundFire || method.getName().equals("fireVetoableChange") && method.getParameters().length == 3;
            if (foundAdd && foundRemove && foundFire) {
               return false;
            }
         }
      }

      for(consideredClass = declaringClass.getSuperClass(); consideredClass != null; consideredClass = consideredClass.getSuperClass()) {
         if (hasVetoableAnnotation(consideredClass)) {
            return false;
         }

         i$ = consideredClass.getFields().iterator();

         while(i$.hasNext()) {
            FieldNode field = (FieldNode)i$.next();
            if (hasVetoableAnnotation(field)) {
               return false;
            }
         }
      }

      if (!foundAdd && !foundRemove && !foundFire) {
         return true;
      } else {
         sourceUnit.getErrorCollector().addErrorAndContinue(new SimpleMessage("@Vetoable cannot be processed on " + declaringClass.getName() + " because some but not all of addVetoableChangeListener, removeVetoableChange, and fireVetoableChange were declared in the current or super classes.", sourceUnit));
         return false;
      }
   }

   protected void createSetterMethod(ClassNode declaringClass, PropertyNode propertyNode, String setterName, Statement setterBlock) {
      Parameter[] setterParameterTypes = new Parameter[]{new Parameter(propertyNode.getType(), "value")};
      ClassNode[] exceptions = new ClassNode[]{new ClassNode(PropertyVetoException.class)};
      MethodNode setter = new MethodNode(setterName, propertyNode.getModifiers(), ClassHelper.VOID_TYPE, setterParameterTypes, exceptions, setterBlock);
      setter.setSynthetic(true);
      declaringClass.addMethod(setter);
   }

   protected void addVetoableChangeSupport(ClassNode declaringClass) {
      ClassNode vcsClassNode = ClassHelper.make(VetoableChangeSupport.class);
      ClassNode vclClassNode = ClassHelper.make(VetoableChangeListener.class);
      FieldNode vcsField = declaringClass.addField("this$vetoableChangeSupport", 4114, vcsClassNode, new ConstructorCallExpression(vcsClassNode, new ArgumentListExpression(new Expression[]{new VariableExpression("this")})));
      declaringClass.addMethod(new MethodNode("addVetoableChangeListener", 4097, ClassHelper.VOID_TYPE, new Parameter[]{new Parameter(vclClassNode, "listener")}, ClassNode.EMPTY_ARRAY, new ExpressionStatement(new MethodCallExpression(new FieldExpression(vcsField), "addVetoableChangeListener", new ArgumentListExpression(new Expression[]{new VariableExpression("listener")})))));
      declaringClass.addMethod(new MethodNode("addVetoableChangeListener", 4097, ClassHelper.VOID_TYPE, new Parameter[]{new Parameter(ClassHelper.STRING_TYPE, "name"), new Parameter(vclClassNode, "listener")}, ClassNode.EMPTY_ARRAY, new ExpressionStatement(new MethodCallExpression(new FieldExpression(vcsField), "addVetoableChangeListener", new ArgumentListExpression(new Expression[]{new VariableExpression("name"), new VariableExpression("listener")})))));
      declaringClass.addMethod(new MethodNode("removeVetoableChangeListener", 4097, ClassHelper.VOID_TYPE, new Parameter[]{new Parameter(vclClassNode, "listener")}, ClassNode.EMPTY_ARRAY, new ExpressionStatement(new MethodCallExpression(new FieldExpression(vcsField), "removeVetoableChangeListener", new ArgumentListExpression(new Expression[]{new VariableExpression("listener")})))));
      declaringClass.addMethod(new MethodNode("removeVetoableChangeListener", 4097, ClassHelper.VOID_TYPE, new Parameter[]{new Parameter(ClassHelper.STRING_TYPE, "name"), new Parameter(vclClassNode, "listener")}, ClassNode.EMPTY_ARRAY, new ExpressionStatement(new MethodCallExpression(new FieldExpression(vcsField), "removeVetoableChangeListener", new ArgumentListExpression(new Expression[]{new VariableExpression("name"), new VariableExpression("listener")})))));
      declaringClass.addMethod(new MethodNode("fireVetoableChange", 4097, ClassHelper.VOID_TYPE, new Parameter[]{new Parameter(ClassHelper.STRING_TYPE, "name"), new Parameter(ClassHelper.OBJECT_TYPE, "oldValue"), new Parameter(ClassHelper.OBJECT_TYPE, "newValue")}, new ClassNode[]{new ClassNode(PropertyVetoException.class)}, new ExpressionStatement(new MethodCallExpression(new FieldExpression(vcsField), "fireVetoableChange", new ArgumentListExpression(new Expression[]{new VariableExpression("name"), new VariableExpression("oldValue"), new VariableExpression("newValue")})))));
      declaringClass.addMethod(new MethodNode("getVetoableChangeListeners", 4097, vclClassNode.makeArray(), Parameter.EMPTY_ARRAY, ClassNode.EMPTY_ARRAY, new ReturnStatement(new ExpressionStatement(new MethodCallExpression(new FieldExpression(vcsField), "getVetoableChangeListeners", ArgumentListExpression.EMPTY_ARGUMENTS)))));
      declaringClass.addMethod(new MethodNode("getVetoableChangeListeners", 4097, vclClassNode.makeArray(), new Parameter[]{new Parameter(ClassHelper.STRING_TYPE, "name")}, ClassNode.EMPTY_ARRAY, new ReturnStatement(new ExpressionStatement(new MethodCallExpression(new FieldExpression(vcsField), "getVetoableChangeListeners", new ArgumentListExpression(new Expression[]{new VariableExpression("name")}))))));
   }
}
