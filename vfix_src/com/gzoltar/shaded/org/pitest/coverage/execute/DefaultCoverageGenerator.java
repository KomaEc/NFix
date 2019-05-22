package com.gzoltar.shaded.org.pitest.coverage.execute;

import com.gzoltar.shaded.org.pitest.classinfo.ClassInfo;
import com.gzoltar.shaded.org.pitest.classpath.CodeSource;
import com.gzoltar.shaded.org.pitest.coverage.CoverageData;
import com.gzoltar.shaded.org.pitest.coverage.CoverageExporter;
import com.gzoltar.shaded.org.pitest.coverage.CoverageGenerator;
import com.gzoltar.shaded.org.pitest.coverage.CoverageResult;
import com.gzoltar.shaded.org.pitest.coverage.analysis.LineMapper;
import com.gzoltar.shaded.org.pitest.functional.F;
import com.gzoltar.shaded.org.pitest.functional.FCollection;
import com.gzoltar.shaded.org.pitest.functional.SideEffect1;
import com.gzoltar.shaded.org.pitest.functional.prelude.Prelude;
import com.gzoltar.shaded.org.pitest.help.Help;
import com.gzoltar.shaded.org.pitest.help.PitHelpError;
import com.gzoltar.shaded.org.pitest.process.LaunchOptions;
import com.gzoltar.shaded.org.pitest.process.ProcessArgs;
import com.gzoltar.shaded.org.pitest.testapi.Configuration;
import com.gzoltar.shaded.org.pitest.util.ExitCode;
import com.gzoltar.shaded.org.pitest.util.Log;
import com.gzoltar.shaded.org.pitest.util.PitError;
import com.gzoltar.shaded.org.pitest.util.SocketFinder;
import com.gzoltar.shaded.org.pitest.util.Timings;
import com.gzoltar.shaded.org.pitest.util.Unchecked;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

public class DefaultCoverageGenerator implements CoverageGenerator {
   private static final Logger LOG = Log.getLogger();
   private final CoverageOptions coverageOptions;
   private final LaunchOptions launchOptions;
   private final CodeSource code;
   private final Timings timings;
   private final File workingDir;
   private final CoverageExporter exporter;
   private final boolean showProgress;

   public DefaultCoverageGenerator(File workingDir, CoverageOptions coverageOptions, LaunchOptions launchOptions, CodeSource code, CoverageExporter exporter, Timings timings, boolean showProgress) {
      this.coverageOptions = coverageOptions;
      this.code = code;
      this.launchOptions = launchOptions;
      this.timings = timings;
      this.workingDir = workingDir;
      this.exporter = exporter;
      this.showProgress = showProgress;
   }

   public CoverageData calculateCoverage() {
      try {
         long t0 = System.currentTimeMillis();
         this.timings.registerStart(Timings.Stage.SCAN_CLASS_PATH);
         Collection<ClassInfo> tests = this.code.getTests();
         this.timings.registerEnd(Timings.Stage.SCAN_CLASS_PATH);
         CoverageData coverage = new CoverageData(this.code, new LineMapper(this.code));
         this.timings.registerStart(Timings.Stage.COVERAGE);
         this.gatherCoverageData(tests, coverage);
         this.timings.registerEnd(Timings.Stage.COVERAGE);
         long time = (System.currentTimeMillis() - t0) / 1000L;
         LOG.info("Calculated coverage in " + time + " seconds.");
         this.verifyBuildSuitableForMutationTesting(coverage);
         this.exporter.recordCoverage(coverage.createCoverage());
         return coverage;
      } catch (PitHelpError var8) {
         throw var8;
      } catch (Exception var9) {
         throw Unchecked.translateCheckedException(var9);
      }
   }

   private void verifyBuildSuitableForMutationTesting(CoverageData coverage) {
      if (!coverage.allTestsGreen()) {
         throw new PitHelpError(Help.FAILING_TESTS, new Object[0]);
      }
   }

   private void gatherCoverageData(Collection<ClassInfo> tests, CoverageData coverage) throws IOException, InterruptedException, ExecutionException {
      List<String> filteredTests = FCollection.map(tests, classInfoToName());
      SideEffect1<CoverageResult> handler = this.resultProcessor(coverage);
      SocketFinder sf = new SocketFinder();
      ServerSocket socket = sf.getNextAvailableServerSocket();
      CoverageProcess process = new CoverageProcess(ProcessArgs.withClassPath(this.code.getClassPath()).andBaseDir(this.workingDir).andLaunchOptions(this.launchOptions).andStderr(this.logInfo()).andStdout(this.captureStandardOutIfVerbose()), this.coverageOptions, socket, filteredTests, handler);
      process.start();
      ExitCode exitCode = process.waitToDie();
      if (exitCode == ExitCode.JUNIT_ISSUE) {
         LOG.severe("Error generating coverage. Please check that your classpath contains JUnit 4.6 or above.");
         throw new PitError("Coverage generation slave exited abnormally. Please check the classpath.");
      } else if (!exitCode.isOk()) {
         LOG.severe("Coverage generator Slave exited abnormally due to " + exitCode);
         throw new PitError("Coverage generation slave exited abnormally!");
      } else {
         LOG.fine("Coverage generator Slave exited ok");
      }
   }

   private static F<ClassInfo, String> classInfoToName() {
      return new F<ClassInfo, String>() {
         public String apply(ClassInfo a) {
            return a.getName().asInternalName();
         }
      };
   }

   private SideEffect1<String> captureStandardOutIfVerbose() {
      return this.coverageOptions.isVerbose() ? this.log() : Prelude.noSideEffect(String.class);
   }

   private SideEffect1<String> logInfo() {
      return new SideEffect1<String>() {
         public void apply(String a) {
            DefaultCoverageGenerator.LOG.info("SLAVE : " + a);
         }
      };
   }

   private SideEffect1<String> log() {
      return new SideEffect1<String>() {
         public void apply(String a) {
            DefaultCoverageGenerator.LOG.fine("SLAVE : " + a);
         }
      };
   }

   private SideEffect1<CoverageResult> resultProcessor(final CoverageData coverage) {
      return new SideEffect1<CoverageResult>() {
         private final String[] spinner = new String[]{"\b/", "\b-", "\b\\", "\b|"};
         int i = 0;

         public void apply(CoverageResult cr) {
            coverage.calculateClassCoverage(cr);
            if (DefaultCoverageGenerator.this.showProgress) {
               System.out.printf("%s", this.spinner[this.i % this.spinner.length]);
            }

            ++this.i;
         }
      };
   }

   public Configuration getConfiguration() {
      return this.coverageOptions.getPitConfig();
   }

   public LaunchOptions getLaunchOptions() {
      return this.launchOptions;
   }
}
