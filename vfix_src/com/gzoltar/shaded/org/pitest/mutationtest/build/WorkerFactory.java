package com.gzoltar.shaded.org.pitest.mutationtest.build;

import com.gzoltar.shaded.org.pitest.classinfo.ClassName;
import com.gzoltar.shaded.org.pitest.functional.SideEffect1;
import com.gzoltar.shaded.org.pitest.functional.prelude.Prelude;
import com.gzoltar.shaded.org.pitest.mutationtest.MutationConfig;
import com.gzoltar.shaded.org.pitest.mutationtest.TimeoutLengthStrategy;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.MutationDetails;
import com.gzoltar.shaded.org.pitest.mutationtest.execute.MutationTestProcess;
import com.gzoltar.shaded.org.pitest.mutationtest.execute.SlaveArguments;
import com.gzoltar.shaded.org.pitest.process.ProcessArgs;
import com.gzoltar.shaded.org.pitest.testapi.Configuration;
import com.gzoltar.shaded.org.pitest.util.Log;
import com.gzoltar.shaded.org.pitest.util.SocketFinder;
import java.io.File;
import java.util.Collection;

public class WorkerFactory {
   private final String classPath;
   private final File baseDir;
   private final Configuration pitConfig;
   private final TimeoutLengthStrategy timeoutStrategy;
   private final boolean verbose;
   private final MutationConfig config;

   public WorkerFactory(File baseDir, Configuration pitConfig, MutationConfig mutationConfig, TimeoutLengthStrategy timeoutStrategy, boolean verbose, String classPath) {
      this.pitConfig = pitConfig;
      this.timeoutStrategy = timeoutStrategy;
      this.verbose = verbose;
      this.classPath = classPath;
      this.baseDir = baseDir;
      this.config = mutationConfig;
   }

   public MutationTestProcess createWorker(Collection<MutationDetails> remainingMutations, Collection<ClassName> testClasses) {
      SlaveArguments fileArgs = new SlaveArguments(remainingMutations, testClasses, this.config.getEngine(), this.timeoutStrategy, Log.isVerbose(), this.pitConfig);
      ProcessArgs args = ProcessArgs.withClassPath(this.classPath).andLaunchOptions(this.config.getLaunchOptions()).andBaseDir(this.baseDir).andStdout(this.captureStdOutIfVerbose()).andStderr(Prelude.printWith("stderr "));
      SocketFinder sf = new SocketFinder();
      MutationTestProcess worker = new MutationTestProcess(sf.getNextAvailableServerSocket(), args, fileArgs);
      return worker;
   }

   private SideEffect1<String> captureStdOutIfVerbose() {
      return this.verbose ? Prelude.printWith("stdout ") : Prelude.noSideEffect(String.class);
   }
}
