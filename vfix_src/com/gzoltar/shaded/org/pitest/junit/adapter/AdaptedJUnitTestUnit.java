package com.gzoltar.shaded.org.pitest.junit.adapter;

import com.gzoltar.shaded.org.pitest.functional.Option;
import com.gzoltar.shaded.org.pitest.testapi.AbstractTestUnit;
import com.gzoltar.shaded.org.pitest.testapi.Description;
import com.gzoltar.shaded.org.pitest.testapi.ResultCollector;
import com.gzoltar.shaded.org.pitest.testapi.foreignclassloader.Events;
import com.gzoltar.shaded.org.pitest.util.ClassLoaderDetectionStrategy;
import com.gzoltar.shaded.org.pitest.util.IsolationUtils;
import com.gzoltar.shaded.org.pitest.util.Log;
import com.gzoltar.shaded.org.pitest.util.Unchecked;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.internal.builders.AllDefaultPossibilitiesBuilder;
import org.junit.internal.runners.ErrorReportingRunner;
import org.junit.runner.Runner;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.Filterable;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runners.model.RunnerBuilder;

public class AdaptedJUnitTestUnit extends AbstractTestUnit {
   private static final Logger LOG = Log.getLogger();
   private final ClassLoaderDetectionStrategy loaderDetection;
   private final Class<?> clazz;
   private final Option<Filter> filter;

   public AdaptedJUnitTestUnit(Class<?> clazz, Option<Filter> filter) {
      this(IsolationUtils.loaderDetectionStrategy(), clazz, filter);
   }

   AdaptedJUnitTestUnit(ClassLoaderDetectionStrategy loaderDetection, Class<?> clazz, Option<Filter> filter) {
      super(new Description(createName(clazz, filter), clazz));
      this.loaderDetection = loaderDetection;
      this.clazz = clazz;
      this.filter = filter;
   }

   private static String createName(Class<?> clazz, Option<Filter> filter) {
      return filter.hasSome() ? ((Filter)filter.value()).describe() : clazz.getName();
   }

   public void execute(ClassLoader loader, ResultCollector rc) {
      Runner runner = createRunner(this.clazz);
      this.checkForErrorRunner(runner);
      this.filterIfRequired(rc, runner);

      try {
         if (this.loaderDetection.fromDifferentLoader(runner.getClass(), loader)) {
            this.executeInDifferentClassLoader(loader, rc, runner);
         } else {
            CustomRunnerExecutor nativeCe = new CustomRunnerExecutor(this.getDescription(), runner, rc);
            nativeCe.run();
         }

      } catch (Exception var5) {
         LOG.log(Level.SEVERE, "Error while running adapter JUnit fixture " + this.clazz + " with filter " + this.filter, var5);
         throw Unchecked.translateCheckedException(var5);
      }
   }

   private void checkForErrorRunner(Runner runner) {
      if (runner instanceof ErrorReportingRunner) {
         LOG.warning("JUnit error for class " + this.clazz + " : " + runner.getDescription());
      }

   }

   private void filterIfRequired(ResultCollector rc, Runner runner) {
      if (this.filter.hasSome()) {
         if (!(runner instanceof Filterable)) {
            LOG.warning("Not able to filter " + runner.getDescription() + ". Mutation may have prevented JUnit from constructing test");
            return;
         }

         Filterable f = (Filterable)runner;

         try {
            f.filter((Filter)this.filter.value());
         } catch (NoTestsRemainException var5) {
            rc.notifySkipped(this.getDescription());
            return;
         }
      }

   }

   public static Runner createRunner(Class<?> clazz) {
      RunnerBuilder builder = createRunnerBuilder(clazz);

      try {
         return builder.runnerForClass(clazz);
      } catch (Throwable var3) {
         LOG.log(Level.SEVERE, "Error while creating runner for " + clazz, var3);
         throw Unchecked.translateCheckedException(var3);
      }
   }

   private static RunnerBuilder createRunnerBuilder(Class<?> clazz) {
      return new AllDefaultPossibilitiesBuilder(true);
   }

   private void executeInDifferentClassLoader(ClassLoader loader, ResultCollector rc, Runner runner) throws IllegalAccessException, InvocationTargetException {
      ForeignClassLoaderCustomRunnerExecutor ce = new ForeignClassLoaderCustomRunnerExecutor(runner);
      Callable foreignCe = (Callable)IsolationUtils.cloneForLoader(ce, loader);

      try {
         List<String> q = (List)foreignCe.call();
         this.convertStringsToResults(rc, q);
      } catch (Exception var7) {
         throw Unchecked.translateCheckedException(var7);
      }
   }

   private void convertStringsToResults(ResultCollector rc, List<String> q) {
      Events.applyEvents(q, rc, this.getDescription());
   }

   public String toString() {
      return "AdaptedJUnitTestUnit [clazz=" + this.clazz + ", filter=" + this.filter + "]";
   }
}
