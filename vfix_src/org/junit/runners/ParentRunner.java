package org.junit.runners;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.internal.AssumptionViolatedException;
import org.junit.internal.runners.model.EachTestNotifier;
import org.junit.internal.runners.rules.RuleMemberValidator;
import org.junit.internal.runners.statements.RunAfters;
import org.junit.internal.runners.statements.RunBefores;
import org.junit.rules.RunRules;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.Filterable;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.manipulation.Sortable;
import org.junit.runner.manipulation.Sorter;
import org.junit.runner.notification.RunNotifier;
import org.junit.runner.notification.StoppedByUserException;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerScheduler;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;
import org.junit.validator.AnnotationsValidator;
import org.junit.validator.PublicClassValidator;
import org.junit.validator.TestClassValidator;

public abstract class ParentRunner<T> extends Runner implements Filterable, Sortable {
   private static final List<TestClassValidator> VALIDATORS = Arrays.asList(new AnnotationsValidator(), new PublicClassValidator());
   private final Object childrenLock = new Object();
   private final TestClass testClass;
   private volatile Collection<T> filteredChildren = null;
   private volatile RunnerScheduler scheduler = new RunnerScheduler() {
      public void schedule(Runnable childStatement) {
         childStatement.run();
      }

      public void finished() {
      }
   };

   protected ParentRunner(Class<?> testClass) throws InitializationError {
      this.testClass = this.createTestClass(testClass);
      this.validate();
   }

   protected TestClass createTestClass(Class<?> testClass) {
      return new TestClass(testClass);
   }

   protected abstract List<T> getChildren();

   protected abstract Description describeChild(T var1);

   protected abstract void runChild(T var1, RunNotifier var2);

   protected void collectInitializationErrors(List<Throwable> errors) {
      this.validatePublicVoidNoArgMethods(BeforeClass.class, true, errors);
      this.validatePublicVoidNoArgMethods(AfterClass.class, true, errors);
      this.validateClassRules(errors);
      this.applyValidators(errors);
   }

   private void applyValidators(List<Throwable> errors) {
      if (this.getTestClass().getJavaClass() != null) {
         Iterator i$ = VALIDATORS.iterator();

         while(i$.hasNext()) {
            TestClassValidator each = (TestClassValidator)i$.next();
            errors.addAll(each.validateTestClass(this.getTestClass()));
         }
      }

   }

   protected void validatePublicVoidNoArgMethods(Class<? extends Annotation> annotation, boolean isStatic, List<Throwable> errors) {
      List<FrameworkMethod> methods = this.getTestClass().getAnnotatedMethods(annotation);
      Iterator i$ = methods.iterator();

      while(i$.hasNext()) {
         FrameworkMethod eachTestMethod = (FrameworkMethod)i$.next();
         eachTestMethod.validatePublicVoidNoArg(isStatic, errors);
      }

   }

   private void validateClassRules(List<Throwable> errors) {
      RuleMemberValidator.CLASS_RULE_VALIDATOR.validate(this.getTestClass(), errors);
      RuleMemberValidator.CLASS_RULE_METHOD_VALIDATOR.validate(this.getTestClass(), errors);
   }

   protected Statement classBlock(RunNotifier notifier) {
      Statement statement = this.childrenInvoker(notifier);
      if (!this.areAllChildrenIgnored()) {
         statement = this.withBeforeClasses(statement);
         statement = this.withAfterClasses(statement);
         statement = this.withClassRules(statement);
      }

      return statement;
   }

   private boolean areAllChildrenIgnored() {
      Iterator i$ = this.getFilteredChildren().iterator();

      Object child;
      do {
         if (!i$.hasNext()) {
            return true;
         }

         child = i$.next();
      } while(this.isIgnored(child));

      return false;
   }

   protected Statement withBeforeClasses(Statement statement) {
      List<FrameworkMethod> befores = this.testClass.getAnnotatedMethods(BeforeClass.class);
      return (Statement)(befores.isEmpty() ? statement : new RunBefores(statement, befores, (Object)null));
   }

   protected Statement withAfterClasses(Statement statement) {
      List<FrameworkMethod> afters = this.testClass.getAnnotatedMethods(AfterClass.class);
      return (Statement)(afters.isEmpty() ? statement : new RunAfters(statement, afters, (Object)null));
   }

   private Statement withClassRules(Statement statement) {
      List<TestRule> classRules = this.classRules();
      return (Statement)(classRules.isEmpty() ? statement : new RunRules(statement, classRules, this.getDescription()));
   }

   protected List<TestRule> classRules() {
      List<TestRule> result = this.testClass.getAnnotatedMethodValues((Object)null, ClassRule.class, TestRule.class);
      result.addAll(this.testClass.getAnnotatedFieldValues((Object)null, ClassRule.class, TestRule.class));
      return result;
   }

   protected Statement childrenInvoker(final RunNotifier notifier) {
      return new Statement() {
         public void evaluate() {
            ParentRunner.this.runChildren(notifier);
         }
      };
   }

   protected boolean isIgnored(T child) {
      return false;
   }

   private void runChildren(final RunNotifier notifier) {
      RunnerScheduler currentScheduler = this.scheduler;

      try {
         Iterator i$ = this.getFilteredChildren().iterator();

         while(i$.hasNext()) {
            final T each = i$.next();
            currentScheduler.schedule(new Runnable() {
               public void run() {
                  ParentRunner.this.runChild(each, notifier);
               }
            });
         }
      } finally {
         currentScheduler.finished();
      }

   }

   protected String getName() {
      return this.testClass.getName();
   }

   public final TestClass getTestClass() {
      return this.testClass;
   }

   protected final void runLeaf(Statement statement, Description description, RunNotifier notifier) {
      EachTestNotifier eachNotifier = new EachTestNotifier(notifier, description);
      eachNotifier.fireTestStarted();

      try {
         statement.evaluate();
      } catch (AssumptionViolatedException var10) {
         eachNotifier.addFailedAssumption(var10);
      } catch (Throwable var11) {
         eachNotifier.addFailure(var11);
      } finally {
         eachNotifier.fireTestFinished();
      }

   }

   protected Annotation[] getRunnerAnnotations() {
      return this.testClass.getAnnotations();
   }

   public Description getDescription() {
      Description description = Description.createSuiteDescription(this.getName(), this.getRunnerAnnotations());
      Iterator i$ = this.getFilteredChildren().iterator();

      while(i$.hasNext()) {
         T child = i$.next();
         description.addChild(this.describeChild(child));
      }

      return description;
   }

   public void run(RunNotifier notifier) {
      EachTestNotifier testNotifier = new EachTestNotifier(notifier, this.getDescription());

      try {
         Statement statement = this.classBlock(notifier);
         statement.evaluate();
      } catch (AssumptionViolatedException var4) {
         testNotifier.addFailedAssumption(var4);
      } catch (StoppedByUserException var5) {
         throw var5;
      } catch (Throwable var6) {
         testNotifier.addFailure(var6);
      }

   }

   public void filter(Filter filter) throws NoTestsRemainException {
      synchronized(this.childrenLock) {
         List<T> children = new ArrayList(this.getFilteredChildren());
         Iterator iter = children.iterator();

         while(iter.hasNext()) {
            T each = iter.next();
            if (this.shouldRun(filter, each)) {
               try {
                  filter.apply(each);
               } catch (NoTestsRemainException var8) {
                  iter.remove();
               }
            } else {
               iter.remove();
            }
         }

         this.filteredChildren = Collections.unmodifiableCollection(children);
         if (this.filteredChildren.isEmpty()) {
            throw new NoTestsRemainException();
         }
      }
   }

   public void sort(Sorter sorter) {
      synchronized(this.childrenLock) {
         Iterator i$ = this.getFilteredChildren().iterator();

         while(i$.hasNext()) {
            T each = i$.next();
            sorter.apply(each);
         }

         List<T> sortedChildren = new ArrayList(this.getFilteredChildren());
         Collections.sort(sortedChildren, this.comparator(sorter));
         this.filteredChildren = Collections.unmodifiableCollection(sortedChildren);
      }
   }

   private void validate() throws InitializationError {
      List<Throwable> errors = new ArrayList();
      this.collectInitializationErrors(errors);
      if (!errors.isEmpty()) {
         throw new InitializationError(errors);
      }
   }

   private Collection<T> getFilteredChildren() {
      if (this.filteredChildren == null) {
         synchronized(this.childrenLock) {
            if (this.filteredChildren == null) {
               this.filteredChildren = Collections.unmodifiableCollection(this.getChildren());
            }
         }
      }

      return this.filteredChildren;
   }

   private boolean shouldRun(Filter filter, T each) {
      return filter.shouldRun(this.describeChild(each));
   }

   private Comparator<? super T> comparator(final Sorter sorter) {
      return new Comparator<T>() {
         public int compare(T o1, T o2) {
            return sorter.compare(ParentRunner.this.describeChild(o1), ParentRunner.this.describeChild(o2));
         }
      };
   }

   public void setScheduler(RunnerScheduler scheduler) {
      this.scheduler = scheduler;
   }
}
