package org.junit.internal.runners.rules;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.rules.MethodRule;
import org.junit.rules.TestRule;
import org.junit.runners.model.FrameworkMember;
import org.junit.runners.model.TestClass;

public class RuleMemberValidator {
   public static final RuleMemberValidator CLASS_RULE_VALIDATOR = classRuleValidatorBuilder().withValidator(new RuleMemberValidator.DeclaringClassMustBePublic()).withValidator(new RuleMemberValidator.MemberMustBeStatic()).withValidator(new RuleMemberValidator.MemberMustBePublic()).withValidator(new RuleMemberValidator.FieldMustBeATestRule()).build();
   public static final RuleMemberValidator RULE_VALIDATOR = testRuleValidatorBuilder().withValidator(new RuleMemberValidator.MemberMustBeNonStaticOrAlsoClassRule()).withValidator(new RuleMemberValidator.MemberMustBePublic()).withValidator(new RuleMemberValidator.FieldMustBeARule()).build();
   public static final RuleMemberValidator CLASS_RULE_METHOD_VALIDATOR = classRuleValidatorBuilder().forMethods().withValidator(new RuleMemberValidator.DeclaringClassMustBePublic()).withValidator(new RuleMemberValidator.MemberMustBeStatic()).withValidator(new RuleMemberValidator.MemberMustBePublic()).withValidator(new RuleMemberValidator.MethodMustBeATestRule()).build();
   public static final RuleMemberValidator RULE_METHOD_VALIDATOR = testRuleValidatorBuilder().forMethods().withValidator(new RuleMemberValidator.MemberMustBeNonStaticOrAlsoClassRule()).withValidator(new RuleMemberValidator.MemberMustBePublic()).withValidator(new RuleMemberValidator.MethodMustBeARule()).build();
   private final Class<? extends Annotation> annotation;
   private final boolean methods;
   private final List<RuleMemberValidator.RuleValidator> validatorStrategies;

   RuleMemberValidator(RuleMemberValidator.Builder builder) {
      this.annotation = builder.annotation;
      this.methods = builder.methods;
      this.validatorStrategies = builder.validators;
   }

   public void validate(TestClass target, List<Throwable> errors) {
      List<? extends FrameworkMember<?>> members = this.methods ? target.getAnnotatedMethods(this.annotation) : target.getAnnotatedFields(this.annotation);
      Iterator i$ = members.iterator();

      while(i$.hasNext()) {
         FrameworkMember<?> each = (FrameworkMember)i$.next();
         this.validateMember(each, errors);
      }

   }

   private void validateMember(FrameworkMember<?> member, List<Throwable> errors) {
      Iterator i$ = this.validatorStrategies.iterator();

      while(i$.hasNext()) {
         RuleMemberValidator.RuleValidator strategy = (RuleMemberValidator.RuleValidator)i$.next();
         strategy.validate(member, this.annotation, errors);
      }

   }

   private static RuleMemberValidator.Builder classRuleValidatorBuilder() {
      return new RuleMemberValidator.Builder(ClassRule.class);
   }

   private static RuleMemberValidator.Builder testRuleValidatorBuilder() {
      return new RuleMemberValidator.Builder(Rule.class);
   }

   private static boolean isRuleType(FrameworkMember<?> member) {
      return isMethodRule(member) || isTestRule(member);
   }

   private static boolean isTestRule(FrameworkMember<?> member) {
      return TestRule.class.isAssignableFrom(member.getType());
   }

   private static boolean isMethodRule(FrameworkMember<?> member) {
      return MethodRule.class.isAssignableFrom(member.getType());
   }

   private static final class FieldMustBeATestRule implements RuleMemberValidator.RuleValidator {
      private FieldMustBeATestRule() {
      }

      public void validate(FrameworkMember<?> member, Class<? extends Annotation> annotation, List<Throwable> errors) {
         if (!RuleMemberValidator.isTestRule(member)) {
            errors.add(new ValidationError(member, annotation, "must implement TestRule."));
         }

      }

      // $FF: synthetic method
      FieldMustBeATestRule(Object x0) {
         this();
      }
   }

   private static final class MethodMustBeATestRule implements RuleMemberValidator.RuleValidator {
      private MethodMustBeATestRule() {
      }

      public void validate(FrameworkMember<?> member, Class<? extends Annotation> annotation, List<Throwable> errors) {
         if (!RuleMemberValidator.isTestRule(member)) {
            errors.add(new ValidationError(member, annotation, "must return an implementation of TestRule."));
         }

      }

      // $FF: synthetic method
      MethodMustBeATestRule(Object x0) {
         this();
      }
   }

   private static final class MethodMustBeARule implements RuleMemberValidator.RuleValidator {
      private MethodMustBeARule() {
      }

      public void validate(FrameworkMember<?> member, Class<? extends Annotation> annotation, List<Throwable> errors) {
         if (!RuleMemberValidator.isRuleType(member)) {
            errors.add(new ValidationError(member, annotation, "must return an implementation of MethodRule or TestRule."));
         }

      }

      // $FF: synthetic method
      MethodMustBeARule(Object x0) {
         this();
      }
   }

   private static final class FieldMustBeARule implements RuleMemberValidator.RuleValidator {
      private FieldMustBeARule() {
      }

      public void validate(FrameworkMember<?> member, Class<? extends Annotation> annotation, List<Throwable> errors) {
         if (!RuleMemberValidator.isRuleType(member)) {
            errors.add(new ValidationError(member, annotation, "must implement MethodRule or TestRule."));
         }

      }

      // $FF: synthetic method
      FieldMustBeARule(Object x0) {
         this();
      }
   }

   private static final class MemberMustBePublic implements RuleMemberValidator.RuleValidator {
      private MemberMustBePublic() {
      }

      public void validate(FrameworkMember<?> member, Class<? extends Annotation> annotation, List<Throwable> errors) {
         if (!member.isPublic()) {
            errors.add(new ValidationError(member, annotation, "must be public."));
         }

      }

      // $FF: synthetic method
      MemberMustBePublic(Object x0) {
         this();
      }
   }

   private static final class DeclaringClassMustBePublic implements RuleMemberValidator.RuleValidator {
      private DeclaringClassMustBePublic() {
      }

      public void validate(FrameworkMember<?> member, Class<? extends Annotation> annotation, List<Throwable> errors) {
         if (!this.isDeclaringClassPublic(member)) {
            errors.add(new ValidationError(member, annotation, "must be declared in a public class."));
         }

      }

      private boolean isDeclaringClassPublic(FrameworkMember<?> member) {
         return Modifier.isPublic(member.getDeclaringClass().getModifiers());
      }

      // $FF: synthetic method
      DeclaringClassMustBePublic(Object x0) {
         this();
      }
   }

   private static final class MemberMustBeStatic implements RuleMemberValidator.RuleValidator {
      private MemberMustBeStatic() {
      }

      public void validate(FrameworkMember<?> member, Class<? extends Annotation> annotation, List<Throwable> errors) {
         if (!member.isStatic()) {
            errors.add(new ValidationError(member, annotation, "must be static."));
         }

      }

      // $FF: synthetic method
      MemberMustBeStatic(Object x0) {
         this();
      }
   }

   private static final class MemberMustBeNonStaticOrAlsoClassRule implements RuleMemberValidator.RuleValidator {
      private MemberMustBeNonStaticOrAlsoClassRule() {
      }

      public void validate(FrameworkMember<?> member, Class<? extends Annotation> annotation, List<Throwable> errors) {
         boolean isMethodRuleMember = RuleMemberValidator.isMethodRule(member);
         boolean isClassRuleAnnotated = member.getAnnotation(ClassRule.class) != null;
         if (member.isStatic() && (isMethodRuleMember || !isClassRuleAnnotated)) {
            String message;
            if (RuleMemberValidator.isMethodRule(member)) {
               message = "must not be static.";
            } else {
               message = "must not be static or it must be annotated with @ClassRule.";
            }

            errors.add(new ValidationError(member, annotation, message));
         }

      }

      // $FF: synthetic method
      MemberMustBeNonStaticOrAlsoClassRule(Object x0) {
         this();
      }
   }

   interface RuleValidator {
      void validate(FrameworkMember<?> var1, Class<? extends Annotation> var2, List<Throwable> var3);
   }

   private static class Builder {
      private final Class<? extends Annotation> annotation;
      private boolean methods;
      private final List<RuleMemberValidator.RuleValidator> validators;

      private Builder(Class<? extends Annotation> annotation) {
         this.annotation = annotation;
         this.methods = false;
         this.validators = new ArrayList();
      }

      RuleMemberValidator.Builder forMethods() {
         this.methods = true;
         return this;
      }

      RuleMemberValidator.Builder withValidator(RuleMemberValidator.RuleValidator validator) {
         this.validators.add(validator);
         return this;
      }

      RuleMemberValidator build() {
         return new RuleMemberValidator(this);
      }

      // $FF: synthetic method
      Builder(Class x0, Object x1) {
         this(x0);
      }
   }
}
