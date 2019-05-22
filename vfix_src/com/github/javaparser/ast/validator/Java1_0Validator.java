package com.github.javaparser.ast.validator;

import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.body.AnnotationDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.ClassExpr;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.modules.ModuleDeclaration;
import com.github.javaparser.ast.nodeTypes.NodeWithTokenRange;
import com.github.javaparser.ast.nodeTypes.NodeWithTypeArguments;
import com.github.javaparser.ast.nodeTypes.NodeWithTypeParameters;
import com.github.javaparser.ast.stmt.AssertStmt;
import com.github.javaparser.ast.stmt.ForeachStmt;
import com.github.javaparser.ast.stmt.SwitchEntryStmt;
import com.github.javaparser.ast.stmt.TryStmt;
import com.github.javaparser.ast.type.UnionType;
import com.github.javaparser.ast.validator.chunks.CommonValidators;
import com.github.javaparser.ast.validator.chunks.ModifierValidator;
import com.github.javaparser.ast.validator.chunks.NoBinaryIntegerLiteralsValidator;
import com.github.javaparser.ast.validator.chunks.NoUnderscoresInIntegerLiteralsValidator;

public class Java1_0Validator extends Validators {
   protected final Validator modifiersWithoutStrictfpAndDefaultAndStaticInterfaceMethodsAndPrivateInterfaceMethods = new ModifierValidator(false, false, false);
   protected final Validator noAssertKeyword = new SimpleValidator(AssertStmt.class, (n) -> {
      return true;
   }, (n, reporter) -> {
      reporter.report((NodeWithTokenRange)n, "'assert' keyword is not supported.");
   });
   protected final Validator noInnerClasses = new SimpleValidator(ClassOrInterfaceDeclaration.class, (n) -> {
      return !n.isTopLevelType();
   }, (n, reporter) -> {
      reporter.report((NodeWithTokenRange)n, "inner classes or interfaces are not supported.");
   });
   protected final Validator noReflection = new SimpleValidator(ClassExpr.class, (n) -> {
      return true;
   }, (n, reporter) -> {
      reporter.report((NodeWithTokenRange)n, "Reflection is not supported.");
   });
   protected final Validator noGenerics = new TreeVisitorValidator((node, reporter) -> {
      if (node instanceof NodeWithTypeArguments && ((NodeWithTypeArguments)node).getTypeArguments().isPresent()) {
         reporter.report((NodeWithTokenRange)node, "Generics are not supported.");
      }

      if (node instanceof NodeWithTypeParameters && ((NodeWithTypeParameters)node).getTypeParameters().isNonEmpty()) {
         reporter.report((NodeWithTokenRange)node, "Generics are not supported.");
      }

   });
   protected final SingleNodeTypeValidator<TryStmt> tryWithoutResources = new SingleNodeTypeValidator(TryStmt.class, (n, reporter) -> {
      if (n.getCatchClauses().isEmpty() && !n.getFinallyBlock().isPresent()) {
         reporter.report((NodeWithTokenRange)n, "Try has no finally and no catch.");
      }

      if (n.getResources().isNonEmpty()) {
         reporter.report((NodeWithTokenRange)n, "Catch with resource is not supported.");
      }

   });
   protected final Validator noAnnotations = new TreeVisitorValidator((node, reporter) -> {
      if (node instanceof AnnotationExpr || node instanceof AnnotationDeclaration) {
         reporter.report((NodeWithTokenRange)node, "Annotations are not supported.");
      }

   });
   protected final Validator noEnums = new SimpleValidator(EnumDeclaration.class, (n) -> {
      return true;
   }, (n, reporter) -> {
      reporter.report((NodeWithTokenRange)n, "Enumerations are not supported.");
   });
   protected final Validator noVarargs = new SimpleValidator(Parameter.class, Parameter::isVarArgs, (n, reporter) -> {
      reporter.report((NodeWithTokenRange)n, "Varargs are not supported.");
   });
   protected final Validator noForEach = new SimpleValidator(ForeachStmt.class, (n) -> {
      return true;
   }, (n, reporter) -> {
      reporter.report((NodeWithTokenRange)n, "For-each loops are not supported.");
   });
   protected final Validator noStaticImports = new SimpleValidator(ImportDeclaration.class, ImportDeclaration::isStatic, (n, reporter) -> {
      reporter.report((NodeWithTokenRange)n, "Static imports are not supported.");
   });
   protected final Validator noStringsInSwitch = new SimpleValidator(SwitchEntryStmt.class, (n) -> {
      return (Boolean)n.getLabel().map((l) -> {
         return l instanceof StringLiteralExpr;
      }).orElse(false);
   }, (n, reporter) -> {
      reporter.report((NodeWithTokenRange)n.getLabel().get(), "Strings in switch statements are not supported.");
   });
   protected final Validator noBinaryIntegerLiterals = new NoBinaryIntegerLiteralsValidator();
   protected final Validator noUnderscoresInIntegerLiterals = new NoUnderscoresInIntegerLiteralsValidator();
   protected final Validator noMultiCatch = new SimpleValidator(UnionType.class, (n) -> {
      return true;
   }, (n, reporter) -> {
      reporter.report((NodeWithTokenRange)n, "Multi-catch is not supported.");
   });
   protected final Validator noLambdas = new SimpleValidator(LambdaExpr.class, (n) -> {
      return true;
   }, (n, reporter) -> {
      reporter.report((NodeWithTokenRange)n, "Lambdas are not supported.");
   });
   protected final Validator noModules = new SimpleValidator(ModuleDeclaration.class, (n) -> {
      return true;
   }, (n, reporter) -> {
      reporter.report((NodeWithTokenRange)n, "Modules are not supported.");
   });

   public Java1_0Validator() {
      super(new CommonValidators());
      this.add(this.modifiersWithoutStrictfpAndDefaultAndStaticInterfaceMethodsAndPrivateInterfaceMethods);
      this.add(this.noAssertKeyword);
      this.add(this.noInnerClasses);
      this.add(this.noReflection);
      this.add(this.noGenerics);
      this.add(this.tryWithoutResources);
      this.add(this.noAnnotations);
      this.add(this.noEnums);
      this.add(this.noVarargs);
      this.add(this.noForEach);
      this.add(this.noStaticImports);
      this.add(this.noStringsInSwitch);
      this.add(this.noBinaryIntegerLiterals);
      this.add(this.noUnderscoresInIntegerLiterals);
      this.add(this.noMultiCatch);
      this.add(this.noLambdas);
      this.add(this.noModules);
   }
}
