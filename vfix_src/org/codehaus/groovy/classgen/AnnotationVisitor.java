package org.codehaus.groovy.classgen;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.AnnotationNode;
import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.FieldNode;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.expr.AnnotationConstantExpression;
import org.codehaus.groovy.ast.expr.ClassExpression;
import org.codehaus.groovy.ast.expr.ClosureExpression;
import org.codehaus.groovy.ast.expr.ConstantExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.ListExpression;
import org.codehaus.groovy.ast.expr.PropertyExpression;
import org.codehaus.groovy.ast.expr.VariableExpression;
import org.codehaus.groovy.ast.stmt.ReturnStatement;
import org.codehaus.groovy.control.ErrorCollector;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.control.messages.SyntaxErrorMessage;
import org.codehaus.groovy.syntax.SyntaxException;
import org.codehaus.groovy.vmplugin.VMPluginFactory;

public class AnnotationVisitor {
   private SourceUnit source;
   private ErrorCollector errorCollector;
   private AnnotationNode annotation;
   private ClassNode reportClass;

   public AnnotationVisitor(SourceUnit source, ErrorCollector errorCollector) {
      this.source = source;
      this.errorCollector = errorCollector;
   }

   public void setReportClass(ClassNode cn) {
      this.reportClass = cn;
   }

   public AnnotationNode visit(AnnotationNode node) {
      this.annotation = node;
      this.reportClass = node.getClassNode();
      if (!this.isValidAnnotationClass(node.getClassNode())) {
         this.addError("class " + node.getClassNode().getName() + " is not an annotation");
         return node;
      } else if (!this.checkIfMandatoryAnnotationValuesPassed(node)) {
         return node;
      } else if (!this.checkIfValidEnumConstsAreUsed(node)) {
         return node;
      } else {
         Map<String, Expression> attributes = node.getMembers();
         Iterator i$ = attributes.entrySet().iterator();

         while(i$.hasNext()) {
            Entry<String, Expression> entry = (Entry)i$.next();
            String attrName = (String)entry.getKey();
            Expression attrExpr = this.transformInlineConstants((Expression)entry.getValue());
            entry.setValue(attrExpr);
            ClassNode attrType = this.getAttributeType(node, attrName);
            this.visitExpression(attrName, attrExpr, attrType);
         }

         VMPluginFactory.getPlugin().configureAnnotation(node);
         return this.annotation;
      }
   }

   private boolean checkIfValidEnumConstsAreUsed(AnnotationNode node) {
      boolean ok = true;
      Map<String, Expression> attributes = node.getMembers();

      Entry entry;
      for(Iterator i$ = attributes.entrySet().iterator(); i$.hasNext(); ok &= this.validateEnumConstant((Expression)entry.getValue())) {
         entry = (Entry)i$.next();
      }

      return ok;
   }

   private boolean validateEnumConstant(Expression exp) {
      if (exp instanceof PropertyExpression) {
         PropertyExpression pe = (PropertyExpression)exp;
         String name = pe.getPropertyAsString();
         if (pe.getObjectExpression() instanceof ClassExpression && name != null) {
            ClassExpression ce = (ClassExpression)pe.getObjectExpression();
            ClassNode type = ce.getType();
            if (type.isEnum()) {
               boolean ok = false;

               try {
                  if (type.isResolved()) {
                     ok = Enum.valueOf(type.getTypeClass(), name) != null;
                  } else {
                     FieldNode enumField = type.getDeclaredField(name);
                     ok = enumField != null && enumField.getType().equals(type);
                  }
               } catch (Exception var8) {
               }

               if (!ok) {
                  this.addError("No enum const " + type.getName() + "." + name, pe);
                  return false;
               }
            }
         }
      }

      return true;
   }

   private Expression transformInlineConstants(Expression exp) {
      if (exp instanceof PropertyExpression) {
         PropertyExpression pe = (PropertyExpression)exp;
         if (pe.getObjectExpression() instanceof ClassExpression) {
            ClassExpression ce = (ClassExpression)pe.getObjectExpression();
            ClassNode type = ce.getType();
            if (type.isEnum() || !type.isResolved()) {
               return exp;
            }

            try {
               type.getFields();
               Field field = type.getTypeClass().getField(pe.getPropertyAsString());
               if (field != null && Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers())) {
                  return new ConstantExpression(field.get((Object)null));
               }
            } catch (Exception var6) {
            }
         }
      } else if (exp instanceof ListExpression) {
         ListExpression le = (ListExpression)exp;
         ListExpression result = new ListExpression();
         Iterator i$ = le.getExpressions().iterator();

         while(i$.hasNext()) {
            Expression e = (Expression)i$.next();
            result.addExpression(this.transformInlineConstants(e));
         }

         return result;
      }

      return exp;
   }

   private boolean checkIfMandatoryAnnotationValuesPassed(AnnotationNode node) {
      boolean ok = true;
      Map attributes = node.getMembers();
      ClassNode classNode = node.getClassNode();
      Iterator i$ = classNode.getMethods().iterator();

      while(i$.hasNext()) {
         MethodNode mn = (MethodNode)i$.next();
         String methodName = mn.getName();
         if (mn.getCode() == null && !attributes.containsKey(methodName)) {
            this.addError("No explicit/default value found for annotation attribute '" + methodName + "' in annotation " + classNode, node);
            ok = false;
         }
      }

      return ok;
   }

   private ClassNode getAttributeType(AnnotationNode node, String attrName) {
      ClassNode classNode = node.getClassNode();
      List methods = classNode.getMethods(attrName);
      if (methods.size() == 0) {
         this.addError("'" + attrName + "'is not part of the annotation " + classNode, node);
         return ClassHelper.OBJECT_TYPE;
      } else {
         MethodNode method = (MethodNode)methods.get(0);
         return method.getReturnType();
      }
   }

   private boolean isValidAnnotationClass(ClassNode node) {
      return node.implementsInterface(ClassHelper.Annotation_TYPE);
   }

   protected void visitExpression(String attrName, Expression attrExp, ClassNode attrType) {
      if (attrType.isArray()) {
         ListExpression listExp;
         if (attrExp instanceof ListExpression) {
            listExp = (ListExpression)attrExp;
            this.visitListExpression(attrName, listExp, attrType.getComponentType());
         } else if (attrExp instanceof ClosureExpression) {
            this.addError("Annotation list attributes must use Groovy notation [el1, el2]", attrExp);
         } else {
            listExp = new ListExpression();
            listExp.addExpression(attrExp);
            if (this.annotation != null) {
               this.annotation.setMember(attrName, listExp);
            }

            this.visitExpression(attrName, listExp, attrType);
         }
      } else if (ClassHelper.isPrimitiveType(attrType)) {
         this.visitConstantExpression(attrName, this.getConstantExpression(attrExp, attrType), ClassHelper.getWrapper(attrType));
      } else if (ClassHelper.STRING_TYPE.equals(attrType)) {
         this.visitConstantExpression(attrName, this.getConstantExpression(attrExp, attrType), ClassHelper.STRING_TYPE);
      } else if (ClassHelper.CLASS_Type.equals(attrType)) {
         if (!(attrExp instanceof ClassExpression)) {
            this.addError("Only classes can be used for attribute '" + attrName + "'", attrExp);
         }
      } else if (attrType.isDerivedFrom(ClassHelper.Enum_Type)) {
         if (attrExp instanceof PropertyExpression) {
            this.visitEnumExpression(attrName, (PropertyExpression)attrExp, attrType);
         } else {
            this.addError("Expected enum value for attribute " + attrName, attrExp);
         }
      } else if (this.isValidAnnotationClass(attrType)) {
         if (attrExp instanceof AnnotationConstantExpression) {
            this.visitAnnotationExpression(attrName, (AnnotationConstantExpression)attrExp, attrType);
         } else {
            this.addError("Expected annotation of type '" + attrType.getName() + "' for attribute " + attrName, attrExp);
         }
      } else {
         this.addError("Unexpected type " + attrType.getName(), attrExp);
      }

   }

   public void checkReturnType(ClassNode attrType, ASTNode node) {
      if (attrType.isArray()) {
         this.checkReturnType(attrType.getComponentType(), node);
      } else {
         if (ClassHelper.isPrimitiveType(attrType)) {
            return;
         }

         if (ClassHelper.STRING_TYPE.equals(attrType)) {
            return;
         }

         if (ClassHelper.CLASS_Type.equals(attrType)) {
            return;
         }

         if (attrType.isDerivedFrom(ClassHelper.Enum_Type)) {
            return;
         }

         if (this.isValidAnnotationClass(attrType)) {
            return;
         }

         this.addError("Unexpected return type " + attrType.getName(), node);
      }

   }

   private ConstantExpression getConstantExpression(Expression exp, ClassNode attrType) {
      if (exp instanceof ConstantExpression) {
         return (ConstantExpression)exp;
      } else {
         String base = "expected '" + exp.getText() + "' to be an inline constant of type " + attrType.getName();
         if (exp instanceof PropertyExpression) {
            this.addError(base + " not a property expression", exp);
         } else if (exp instanceof VariableExpression && ((VariableExpression)exp).getAccessedVariable() instanceof FieldNode) {
            this.addError(base + " not a field expression", exp);
         } else {
            this.addError(base, exp);
         }

         return ConstantExpression.EMPTY_EXPRESSION;
      }
   }

   protected void visitAnnotationExpression(String attrName, AnnotationConstantExpression expression, ClassNode attrType) {
      AnnotationNode annotationNode = (AnnotationNode)expression.getValue();
      AnnotationVisitor visitor = new AnnotationVisitor(this.source, this.errorCollector);
      visitor.visit(annotationNode);
   }

   protected void visitListExpression(String attrName, ListExpression listExpr, ClassNode elementType) {
      Iterator i$ = listExpr.getExpressions().iterator();

      while(i$.hasNext()) {
         Expression expression = (Expression)i$.next();
         this.visitExpression(attrName, expression, elementType);
      }

   }

   protected void visitConstantExpression(String attrName, ConstantExpression constExpr, ClassNode attrType) {
      if (!constExpr.getType().isDerivedFrom(attrType)) {
         this.addError("Attribute '" + attrName + "' should have type '" + attrType.getName() + "'; " + "but found type '" + constExpr.getType().getName() + "'", constExpr);
      }

   }

   protected void visitEnumExpression(String attrName, PropertyExpression propExpr, ClassNode attrType) {
      if (!propExpr.getObjectExpression().getType().isDerivedFrom(attrType)) {
         this.addError("Attribute '" + attrName + "' should have type '" + attrType.getName() + "' (Enum), but found " + propExpr.getObjectExpression().getType().getName(), propExpr);
      }

   }

   protected void addError(String msg) {
      this.addError(msg, this.annotation);
   }

   protected void addError(String msg, ASTNode expr) {
      this.errorCollector.addErrorAndContinue(new SyntaxErrorMessage(new SyntaxException(msg + " in @" + this.reportClass.getName() + '\n', expr.getLineNumber(), expr.getColumnNumber()), this.source));
   }

   public void checkCircularReference(ClassNode searchClass, ClassNode attrType, Expression startExp) {
      if (this.isValidAnnotationClass(attrType)) {
         if (!(startExp instanceof AnnotationConstantExpression)) {
            this.addError("Found '" + startExp.getText() + "' when expecting an Annotation Constant", startExp);
         } else {
            AnnotationConstantExpression ace = (AnnotationConstantExpression)startExp;
            AnnotationNode annotationNode = (AnnotationNode)ace.getValue();
            if (annotationNode.getClassNode().equals(searchClass)) {
               this.addError("Circular reference discovered in " + searchClass.getName(), startExp);
            } else {
               ClassNode cn = annotationNode.getClassNode();
               Iterator i$ = cn.getMethods().iterator();

               while(i$.hasNext()) {
                  MethodNode method = (MethodNode)i$.next();
                  if (method.getReturnType().equals(searchClass)) {
                     this.addError("Circular reference discovered in " + cn.getName(), startExp);
                  }

                  ReturnStatement code = (ReturnStatement)method.getCode();
                  if (code != null) {
                     this.checkCircularReference(searchClass, method.getReturnType(), code.getExpression());
                  }
               }

            }
         }
      }
   }
}
