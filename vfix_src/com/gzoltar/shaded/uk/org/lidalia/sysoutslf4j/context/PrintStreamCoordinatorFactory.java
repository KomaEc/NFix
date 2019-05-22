package com.gzoltar.shaded.uk.org.lidalia.sysoutslf4j.context;

import com.gzoltar.shaded.uk.org.lidalia.sysoutslf4j.common.ExceptionUtils;
import com.gzoltar.shaded.uk.org.lidalia.sysoutslf4j.common.PrintStreamCoordinator;
import com.gzoltar.shaded.uk.org.lidalia.sysoutslf4j.common.ReflectionUtils;
import com.gzoltar.shaded.uk.org.lidalia.sysoutslf4j.system.PrintStreamCoordinatorImpl;
import java.util.concurrent.Callable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class PrintStreamCoordinatorFactory {
   private static final String LINE_END = System.getProperty("line.separator");
   private static final Logger LOG = LoggerFactory.getLogger(SysOutOverSLF4J.class);

   static PrintStreamCoordinator createPrintStreamCoordinator() {
      Class<?> candidateCoordinatorClass = getConfiguratorClassFromSLF4JPrintStreamClassLoader();
      if (candidateCoordinatorClass == null) {
         candidateCoordinatorClass = getConfiguratorClassFromSystemClassLoader();
      }

      if (candidateCoordinatorClass == null) {
         candidateCoordinatorClass = getConfiguratorClassFromCurrentClassLoader();
      }

      checkCoordinator(candidateCoordinatorClass);
      return makeCoordinator(candidateCoordinatorClass);
   }

   private static PrintStreamCoordinator makeCoordinator(final Class<?> coordinatorClass) {
      return (PrintStreamCoordinator)ExceptionUtils.doUnchecked(new Callable<PrintStreamCoordinator>() {
         public PrintStreamCoordinator call() throws InstantiationException, IllegalAccessException {
            Object coordinator = coordinatorClass.newInstance();
            return (PrintStreamCoordinator)ReflectionUtils.wrap(coordinator, PrintStreamCoordinator.class);
         }
      });
   }

   private static Class<?> getConfiguratorClassFromSLF4JPrintStreamClassLoader() {
      Class configuratorClass;
      if (SysOutOverSLF4J.systemOutputsAreSLF4JPrintStreams()) {
         ClassLoader classLoader = System.out.getClass().getClassLoader();
         configuratorClass = ClassLoaderUtils.loadClass(classLoader, PrintStreamCoordinatorImpl.class);
      } else {
         configuratorClass = null;
      }

      return configuratorClass;
   }

   private static Class<?> getConfiguratorClassFromSystemClassLoader() {
      Class configuratorClass = null;

      try {
         configuratorClass = ClassLoader.getSystemClassLoader().loadClass(PrintStreamCoordinatorImpl.class.getName());
      } catch (Exception var2) {
         LOG.debug("failed to load [" + PrintStreamCoordinatorImpl.class + "] from system class loader due to " + var2);
      }

      return configuratorClass;
   }

   private static void checkCoordinator(Class<?> candidateCoordinatorClass) {
      ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
      boolean usingSystemClassLoader = ClassLoader.getSystemClassLoader() == contextClassLoader;
      if (!usingSystemClassLoader && candidateCoordinatorClass.getClassLoader() == contextClassLoader) {
         reportFailureToAvoidClassLoaderLeak();
      }

   }

   private static void reportFailureToAvoidClassLoaderLeak() {
      LOG.warn("Unfortunately it is not possible to set up Sysout over SLF4J on this system without introducing a class loader memory leak." + LINE_END + "If you never need to discard the current class loader [" + Thread.currentThread().getContextClassLoader() + "] " + "this will not be a problem and you can suppress this warning." + LINE_END + "In the worst case discarding the current class loader may cause all subsequent attempts to print to " + "System.out or err to throw an exception.");
   }

   private static Class<PrintStreamCoordinatorImpl> getConfiguratorClassFromCurrentClassLoader() {
      return PrintStreamCoordinatorImpl.class;
   }

   private PrintStreamCoordinatorFactory() {
      throw new UnsupportedOperationException("Not instantiable");
   }
}
