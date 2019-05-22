package com.github.javaparser.ast.validator.chunks;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.AnnotationDeclaration;
import com.github.javaparser.ast.body.AnnotationMemberDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.modules.ModuleRequiresStmt;
import com.github.javaparser.ast.nodeTypes.NodeWithModifiers;
import com.github.javaparser.ast.nodeTypes.NodeWithTokenRange;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.validator.ProblemReporter;
import com.github.javaparser.ast.validator.VisitorValidator;
import com.github.javaparser.utils.SeparatedItemStringBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ModifierValidator extends VisitorValidator {
   private final Modifier[] interfaceWithNothingSpecial;
   private final Modifier[] interfaceWithStaticAndDefault;
   private final Modifier[] interfaceWithStaticAndDefaultAndPrivate;
   private final boolean hasStrictfp;
   private final boolean hasDefaultAndStaticInterfaceMethods;
   private final boolean hasPrivateInterfaceMethods;

   public ModifierValidator(boolean hasStrictfp, boolean hasDefaultAndStaticInterfaceMethods, boolean hasPrivateInterfaceMethods) {
      this.interfaceWithNothingSpecial = new Modifier[]{Modifier.PUBLIC, Modifier.PROTECTED, Modifier.ABSTRACT, Modifier.FINAL, Modifier.SYNCHRONIZED, Modifier.NATIVE, Modifier.STRICTFP};
      this.interfaceWithStaticAndDefault = new Modifier[]{Modifier.PUBLIC, Modifier.PROTECTED, Modifier.ABSTRACT, Modifier.STATIC, Modifier.FINAL, Modifier.SYNCHRONIZED, Modifier.NATIVE, Modifier.STRICTFP, Modifier.DEFAULT};
      this.interfaceWithStaticAndDefaultAndPrivate = new Modifier[]{Modifier.PUBLIC, Modifier.PROTECTED, Modifier.PRIVATE, Modifier.ABSTRACT, Modifier.STATIC, Modifier.FINAL, Modifier.SYNCHRONIZED, Modifier.NATIVE, Modifier.STRICTFP, Modifier.DEFAULT};
      this.hasStrictfp = hasStrictfp;
      this.hasDefaultAndStaticInterfaceMethods = hasDefaultAndStaticInterfaceMethods;
      this.hasPrivateInterfaceMethods = hasPrivateInterfaceMethods;
   }

   public void visit(ClassOrInterfaceDeclaration n, ProblemReporter reporter) {
      if (n.isInterface()) {
         this.validateInterfaceModifiers(n, reporter);
      } else {
         this.validateClassModifiers(n, reporter);
      }

      super.visit(n, reporter);
   }

   private void validateClassModifiers(ClassOrInterfaceDeclaration n, ProblemReporter reporter) {
      if (n.isTopLevelType()) {
         this.validateModifiers(n, reporter, Modifier.PUBLIC, Modifier.ABSTRACT, Modifier.FINAL, Modifier.STRICTFP);
      } else if (n.isNestedType()) {
         this.validateModifiers(n, reporter, Modifier.PUBLIC, Modifier.PROTECTED, Modifier.PRIVATE, Modifier.ABSTRACT, Modifier.STATIC, Modifier.FINAL, Modifier.STRICTFP);
      } else if (n.isLocalClassDeclaration()) {
         this.validateModifiers(n, reporter, Modifier.ABSTRACT, Modifier.FINAL, Modifier.STRICTFP);
      }

   }

   private void validateInterfaceModifiers(TypeDeclaration<?> n, ProblemReporter reporter) {
      if (n.isTopLevelType()) {
         this.validateModifiers(n, reporter, Modifier.PUBLIC, Modifier.ABSTRACT, Modifier.STRICTFP);
      } else if (n.isNestedType()) {
         this.validateModifiers(n, reporter, Modifier.PUBLIC, Modifier.PROTECTED, Modifier.PRIVATE, Modifier.ABSTRACT, Modifier.STATIC, Modifier.STRICTFP);
      }

   }

   public void visit(EnumDeclaration n, ProblemReporter reporter) {
      if (n.isTopLevelType()) {
         this.validateModifiers(n, reporter, Modifier.PUBLIC, Modifier.STRICTFP);
      } else if (n.isNestedType()) {
         this.validateModifiers(n, reporter, Modifier.PUBLIC, Modifier.PROTECTED, Modifier.PRIVATE, Modifier.STATIC, Modifier.STRICTFP);
      }

      super.visit(n, reporter);
   }

   public void visit(AnnotationDeclaration n, ProblemReporter reporter) {
      this.validateInterfaceModifiers(n, reporter);
      super.visit(n, reporter);
   }

   public void visit(AnnotationMemberDeclaration n, ProblemReporter reporter) {
      this.validateModifiers(n, reporter, Modifier.PUBLIC, Modifier.ABSTRACT);
      super.visit(n, reporter);
   }

   public void visit(ConstructorDeclaration n, ProblemReporter reporter) {
      this.validateModifiers(n, reporter, Modifier.PUBLIC, Modifier.PROTECTED, Modifier.PRIVATE);
      n.getParameters().forEach((p) -> {
         this.validateModifiers(p, reporter, Modifier.FINAL);
      });
      super.visit(n, reporter);
   }

   public void visit(FieldDeclaration n, ProblemReporter reporter) {
      this.validateModifiers(n, reporter, Modifier.PUBLIC, Modifier.PROTECTED, Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL, Modifier.TRANSIENT, Modifier.VOLATILE);
      super.visit(n, reporter);
   }

   public void visit(MethodDeclaration n, ProblemReporter reporter) {
      if (n.isAbstract()) {
         SeparatedItemStringBuilder builder = new SeparatedItemStringBuilder("Cannot be 'abstract' and also '", "', '", "'.");
         Iterator var4 = Arrays.asList(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL, Modifier.NATIVE, Modifier.STRICTFP, Modifier.SYNCHRONIZED).iterator();

         while(var4.hasNext()) {
            Modifier m = (Modifier)var4.next();
            if (n.getModifiers().contains(m)) {
               builder.append(m.asString());
            }
         }

         if (builder.hasItems()) {
            reporter.report((NodeWithTokenRange)n, builder.toString());
         }
      }

      if (n.getParentNode().isPresent() && n.getParentNode().get() instanceof ClassOrInterfaceDeclaration) {
         if (((ClassOrInterfaceDeclaration)n.getParentNode().get()).isInterface()) {
            if (this.hasDefaultAndStaticInterfaceMethods) {
               if (this.hasPrivateInterfaceMethods) {
                  this.validateModifiers(n, reporter, this.interfaceWithStaticAndDefaultAndPrivate);
               } else {
                  this.validateModifiers(n, reporter, this.interfaceWithStaticAndDefault);
               }
            } else {
               this.validateModifiers(n, reporter, this.interfaceWithNothingSpecial);
            }
         } else {
            this.validateModifiers(n, reporter, Modifier.PUBLIC, Modifier.PROTECTED, Modifier.PRIVATE, Modifier.ABSTRACT, Modifier.STATIC, Modifier.FINAL, Modifier.SYNCHRONIZED, Modifier.NATIVE, Modifier.STRICTFP);
         }
      }

      n.getParameters().forEach((p) -> {
         this.validateModifiers(p, reporter, Modifier.FINAL);
      });
      super.visit(n, reporter);
   }

   public void visit(LambdaExpr n, ProblemReporter reporter) {
      n.getParameters().forEach((p) -> {
         this.validateModifiers(p, reporter, Modifier.FINAL);
      });
      super.visit(n, reporter);
   }

   public void visit(CatchClause n, ProblemReporter reporter) {
      this.validateModifiers(n.getParameter(), reporter, Modifier.FINAL);
      super.visit(n, reporter);
   }

   public void visit(VariableDeclarationExpr n, ProblemReporter reporter) {
      this.validateModifiers(n, reporter, Modifier.FINAL);
      super.visit(n, reporter);
   }

   public void visit(ModuleRequiresStmt n, ProblemReporter reporter) {
      this.validateModifiers(n, reporter, Modifier.TRANSITIVE, Modifier.STATIC);
      super.visit(n, reporter);
   }

   private <T extends NodeWithModifiers<?> & NodeWithTokenRange<?>> void validateModifiers(T n, ProblemReporter reporter, Modifier... allowedModifiers) {
      this.validateAtMostOneOf(n, reporter, Modifier.PUBLIC, Modifier.PROTECTED, Modifier.PRIVATE);
      this.validateAtMostOneOf(n, reporter, Modifier.FINAL, Modifier.ABSTRACT);
      if (this.hasStrictfp) {
         this.validateAtMostOneOf(n, reporter, Modifier.NATIVE, Modifier.STRICTFP);
      } else {
         allowedModifiers = this.removeModifierFromArray(Modifier.STRICTFP, allowedModifiers);
      }

      Iterator var4 = n.getModifiers().iterator();

      while(var4.hasNext()) {
         Modifier m = (Modifier)var4.next();
         if (!this.arrayContains(allowedModifiers, m)) {
            reporter.report((NodeWithTokenRange)n, "'%s' is not allowed here.", m.asString());
         }
      }

   }

   private Modifier[] removeModifierFromArray(Modifier m, Modifier[] allowedModifiers) {
      List<Modifier> newModifiers = new ArrayList(Arrays.asList(allowedModifiers));
      newModifiers.remove(m);
      allowedModifiers = (Modifier[])newModifiers.toArray(new Modifier[0]);
      return allowedModifiers;
   }

   private boolean arrayContains(Object[] items, Object searchItem) {
      Object[] var3 = items;
      int var4 = items.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Object o = var3[var5];
         if (o == searchItem) {
            return true;
         }
      }

      return false;
   }

   private <T extends NodeWithModifiers<?> & NodeWithTokenRange<?>> void validateAtMostOneOf(T t, ProblemReporter reporter, Modifier... modifiers) {
      List<Modifier> foundModifiers = new ArrayList();
      Modifier[] var5 = modifiers;
      int var6 = modifiers.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         Modifier m = var5[var7];
         if (t.getModifiers().contains(m)) {
            foundModifiers.add(m);
         }
      }

      if (foundModifiers.size() > 1) {
         SeparatedItemStringBuilder builder = new SeparatedItemStringBuilder("Can have only one of '", "', '", "'.");
         Iterator var10 = foundModifiers.iterator();

         while(var10.hasNext()) {
            Modifier m = (Modifier)var10.next();
            builder.append(m.asString());
         }

         reporter.report((NodeWithTokenRange)t, builder.toString());
      }

   }
}
