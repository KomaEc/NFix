package org.codehaus.groovy.transform;

import groovy.lang.Newify;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.AnnotatedNode;
import org.codehaus.groovy.ast.AnnotationNode;
import org.codehaus.groovy.ast.ClassCodeExpressionTransformer;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.FieldNode;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.expr.ClassExpression;
import org.codehaus.groovy.ast.expr.ConstantExpression;
import org.codehaus.groovy.ast.expr.ConstructorCallExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.ListExpression;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.ast.expr.VariableExpression;
import org.codehaus.groovy.control.CompilePhase;
import org.codehaus.groovy.control.SourceUnit;

@GroovyASTTransformation(
   phase = CompilePhase.CANONICALIZATION
)
public class NewifyASTTransformation extends ClassCodeExpressionTransformer implements ASTTransformation {
   private static final ClassNode MY_TYPE = new ClassNode(Newify.class);
   private static final String MY_NAME;
   private SourceUnit source;
   private ListExpression classesToNewify;
   private boolean auto;

   public void visit(ASTNode[] nodes, SourceUnit source) {
      this.source = source;
      if (nodes.length != 2 || !(nodes[0] instanceof AnnotationNode) || !(nodes[1] instanceof AnnotatedNode)) {
         this.internalError("Expecting [AnnotationNode, AnnotatedClass] but got: " + Arrays.asList(nodes));
      }

      AnnotatedNode parent = (AnnotatedNode)nodes[1];
      AnnotationNode node = (AnnotationNode)nodes[0];
      if (!MY_TYPE.equals(node.getClassNode())) {
         this.internalError("Transformation called from wrong annotation: " + node.getClassNode().getName());
      }

      boolean autoFlag = this.determineAutoFlag(node.getMember("auto"));
      ListExpression list = this.determineClassesToNewify(node.getMember("value"));
      if (parent instanceof ClassNode) {
         this.newifyClass(parent, autoFlag, list);
      } else if (parent instanceof MethodNode || parent instanceof FieldNode) {
         this.newifyMethodOrField(parent, autoFlag, list);
      }

   }

   private boolean determineAutoFlag(Expression autoExpr) {
      return !(autoExpr instanceof ConstantExpression) || !((ConstantExpression)autoExpr).getValue().equals(false);
   }

   private ListExpression determineClassesToNewify(Expression expr) {
      ListExpression list = new ListExpression();
      if (expr instanceof ClassExpression) {
         list.addExpression(expr);
      } else if (expr instanceof ListExpression) {
         list = (ListExpression)expr;
         List<Expression> expressions = list.getExpressions();
         Iterator i$ = expressions.iterator();

         while(i$.hasNext()) {
            Expression ex = (Expression)i$.next();
            if (!(ex instanceof ClassExpression)) {
               throw new RuntimeException("Error during @" + MY_NAME + " processing. Annotation parameter must be a list of classes.");
            }
         }

         this.checkDuplicateNameClashes(list);
      }

      return list;
   }

   public Expression transform(Expression expr) {
      if (expr == null) {
         return null;
      } else if (expr instanceof MethodCallExpression) {
         MethodCallExpression mce = (MethodCallExpression)expr;
         Expression args = this.transform(mce.getArguments());
         Expression method = this.transform(mce.getMethod());
         Expression object = this.transform(mce.getObjectExpression());
         return (Expression)(this.isNewifyCandidate(mce) ? this.transformMethodCall(mce, args) : new MethodCallExpression(object, method, args));
      } else {
         return expr.transformExpression(this);
      }
   }

   private void newifyClass(AnnotatedNode parent, boolean autoFlag, ListExpression list) {
      ClassNode cNode = (ClassNode)parent;
      String cName = cNode.getName();
      if (cNode.isInterface()) {
         throw new RuntimeException("Error processing interface '" + cName + "'. @" + MY_NAME + " not allowed for interfaces.");
      } else {
         this.classesToNewify = list;
         this.auto = autoFlag;
         super.visitClass(cNode);
      }
   }

   private void newifyMethodOrField(AnnotatedNode parent, boolean autoFlag, ListExpression list) {
      ListExpression oldClassesToNewify = this.classesToNewify;
      boolean oldAuto = this.auto;
      this.checkClassLevelClashes(list);
      this.checkAutoClash(autoFlag);
      this.classesToNewify = list;
      this.auto = autoFlag;
      if (parent instanceof FieldNode) {
         super.visitField((FieldNode)parent);
      } else {
         super.visitMethod((MethodNode)parent);
      }

      this.classesToNewify = oldClassesToNewify;
      this.auto = oldAuto;
   }

   private void checkDuplicateNameClashes(ListExpression list) {
      Set<String> seen = new HashSet();
      List<ClassExpression> classes = list.getExpressions();
      Iterator i$ = classes.iterator();

      while(i$.hasNext()) {
         ClassExpression ce = (ClassExpression)i$.next();
         String name = ce.getType().getNameWithoutPackage();
         if (seen.contains(name)) {
            throw new RuntimeException("Duplicate name '" + name + "' found during @" + MY_NAME + " processing.");
         }

         seen.add(name);
      }

   }

   private void checkAutoClash(boolean autoFlag) {
      if (this.auto && !autoFlag) {
         throw new RuntimeException("Error during @" + MY_NAME + " processing. The 'auto' flag can't be false at method/constructor/field level if it is true at the class level.");
      }
   }

   private void checkClassLevelClashes(ListExpression list) {
      List<ClassExpression> classes = list.getExpressions();
      Iterator i$ = classes.iterator();

      String name;
      do {
         if (!i$.hasNext()) {
            return;
         }

         ClassExpression ce = (ClassExpression)i$.next();
         name = ce.getType().getNameWithoutPackage();
      } while(!this.findClassWithMatchingBasename(name));

      throw new RuntimeException("Error during @" + MY_NAME + " processing. Class '" + name + "' can't appear at method/constructor/field level if it already appears at the class level.");
   }

   private boolean findClassWithMatchingBasename(String nameWithoutPackage) {
      if (this.classesToNewify == null) {
         return false;
      } else {
         List<ClassExpression> classes = this.classesToNewify.getExpressions();
         Iterator i$ = classes.iterator();

         ClassExpression ce;
         do {
            if (!i$.hasNext()) {
               return false;
            }

            ce = (ClassExpression)i$.next();
         } while(!ce.getType().getNameWithoutPackage().equals(nameWithoutPackage));

         return true;
      }
   }

   private boolean isNewifyCandidate(MethodCallExpression mce) {
      return mce.getObjectExpression() == VariableExpression.THIS_EXPRESSION || this.auto && this.isNewMethodStyle(mce);
   }

   private boolean isNewMethodStyle(MethodCallExpression mce) {
      Expression obj = mce.getObjectExpression();
      Expression meth = mce.getMethod();
      return obj instanceof ClassExpression && meth instanceof ConstantExpression && ((ConstantExpression)meth).getValue().equals("new");
   }

   private Expression transformMethodCall(MethodCallExpression mce, Expression args) {
      ClassNode classType;
      if (this.isNewMethodStyle(mce)) {
         classType = mce.getObjectExpression().getType();
      } else {
         classType = this.findMatchingCandidateClass(mce);
      }

      if (classType != null) {
         return new ConstructorCallExpression(classType, args);
      } else {
         mce.setArguments(args);
         return mce;
      }
   }

   private ClassNode findMatchingCandidateClass(MethodCallExpression mce) {
      if (this.classesToNewify == null) {
         return null;
      } else {
         List<ClassExpression> classes = this.classesToNewify.getExpressions();
         Iterator i$ = classes.iterator();

         ClassNode type;
         do {
            if (!i$.hasNext()) {
               return null;
            }

            ClassExpression ce = (ClassExpression)i$.next();
            type = ce.getType();
         } while(!type.getNameWithoutPackage().equals(mce.getMethodAsString()));

         return type;
      }
   }

   private void internalError(String message) {
      throw new RuntimeException("Internal error: " + message);
   }

   protected SourceUnit getSourceUnit() {
      return this.source;
   }

   static {
      MY_NAME = MY_TYPE.getNameWithoutPackage();
   }
}
