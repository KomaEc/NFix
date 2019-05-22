package com.gzoltar.shaded.org.pitest.coverage.execute;

import com.gzoltar.shaded.org.pitest.boot.HotSwapAgent;
import com.gzoltar.shaded.org.pitest.classinfo.ClassName;
import com.gzoltar.shaded.org.pitest.classpath.ClassPathByteArraySource;
import com.gzoltar.shaded.org.pitest.coverage.CoverageTransformer;
import com.gzoltar.shaded.org.pitest.dependency.DependencyExtractor;
import com.gzoltar.shaded.org.pitest.functional.FCollection;
import com.gzoltar.shaded.org.pitest.functional.predicate.Predicate;
import com.gzoltar.shaded.org.pitest.help.PitHelpError;
import com.gzoltar.shaded.org.pitest.testapi.TestUnit;
import com.gzoltar.shaded.org.pitest.testapi.execute.FindTestUnits;
import com.gzoltar.shaded.org.pitest.util.ExitCode;
import com.gzoltar.shaded.org.pitest.util.Log;
import com.gzoltar.shaded.org.pitest.util.SafeDataInputStream;
import com.gzoltar.shaded.org.pitest.util.Unchecked;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.pitest.CodeCoverageStore;

public class CoverageSlave {
   private static final Logger LOG = Log.getLogger();

   public static void main(String[] args) {
      ExitCode exitCode = ExitCode.OK;
      Socket s = null;
      CoveragePipe invokeQueue = null;

      try {
         int port = Integer.valueOf(args[0]);
         s = new Socket("localhost", port);
         SafeDataInputStream dis = new SafeDataInputStream(s.getInputStream());
         CoverageOptions paramsFromParent = (CoverageOptions)dis.read(CoverageOptions.class);
         Log.setVerbose(paramsFromParent.isVerbose());
         invokeQueue = new CoveragePipe(new BufferedOutputStream(s.getOutputStream()));
         CodeCoverageStore.init(invokeQueue);
         LOG.info("Checking environment");
         if (paramsFromParent.getPitConfig().verifyEnvironment().hasSome()) {
            throw (PitHelpError)paramsFromParent.getPitConfig().verifyEnvironment().value();
         }

         HotSwapAgent.addTransformer(new CoverageTransformer(convertToJVMClassFilter(paramsFromParent.getFilter())));
         List<TestUnit> tus = getTestsFromParent(dis, paramsFromParent);
         LOG.info(tus.size() + " tests received");
         CoverageWorker worker = new CoverageWorker(invokeQueue, tus);
         worker.run();
      } catch (PitHelpError var18) {
         LOG.log(Level.SEVERE, var18.getMessage());
         exitCode = ExitCode.JUNIT_ISSUE;
      } catch (Throwable var19) {
         LOG.log(Level.SEVERE, "Error calculating coverage. Process will exit.", var19);
         exitCode = ExitCode.UNKNOWN_ERROR;
      } finally {
         if (invokeQueue != null) {
            invokeQueue.end(exitCode);
         }

         try {
            if (s != null) {
               s.close();
            }
         } catch (IOException var20) {
            throw Unchecked.translateCheckedException(var20);
         }

      }

      System.exit(exitCode.getCode());
   }

   private static Predicate<String> convertToJVMClassFilter(final Predicate<String> child) {
      return new Predicate<String>() {
         public Boolean apply(String a) {
            return (Boolean)child.apply(a.replace("/", "."));
         }
      };
   }

   private static List<TestUnit> getTestsFromParent(SafeDataInputStream dis, CoverageOptions paramsFromParent) throws IOException {
      List<ClassName> classes = receiveTestClassesFromParent(dis);
      Collections.sort(classes);
      List<TestUnit> tus = discoverTests(paramsFromParent, classes);
      DependencyFilter filter = new DependencyFilter(new DependencyExtractor(new ClassPathByteArraySource(), paramsFromParent.getDependencyAnalysisMaxDistance()), paramsFromParent.getFilter());
      List<TestUnit> filteredTus = filter.filterTestsByDependencyAnalysis(tus);
      LOG.info("Dependency analysis reduced number of potential tests by " + (tus.size() - filteredTus.size()));
      return filteredTus;
   }

   private static List<TestUnit> discoverTests(CoverageOptions paramsFromParent, List<ClassName> classes) {
      FindTestUnits finder = new FindTestUnits(paramsFromParent.getPitConfig());
      List<TestUnit> tus = finder.findTestUnitsForAllSuppliedClasses(FCollection.flatMap(classes, ClassName.nameToClass()));
      LOG.info("Found  " + tus.size() + " tests");
      return tus;
   }

   private static List<ClassName> receiveTestClassesFromParent(SafeDataInputStream dis) {
      int count = dis.readInt();
      LOG.fine("Expecting " + count + " tests classes from parent");
      List<ClassName> classes = new ArrayList(count);

      for(int i = 0; i != count; ++i) {
         classes.add(new ClassName(dis.readString()));
      }

      LOG.fine("Tests classes received");
      return classes;
   }
}
