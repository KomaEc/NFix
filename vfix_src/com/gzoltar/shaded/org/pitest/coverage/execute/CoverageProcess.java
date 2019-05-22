package com.gzoltar.shaded.org.pitest.coverage.execute;

import com.gzoltar.shaded.org.pitest.coverage.CoverageResult;
import com.gzoltar.shaded.org.pitest.functional.SideEffect1;
import com.gzoltar.shaded.org.pitest.process.ProcessArgs;
import com.gzoltar.shaded.org.pitest.process.WrappingProcess;
import com.gzoltar.shaded.org.pitest.util.ExitCode;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;

public class CoverageProcess {
   private final WrappingProcess process;
   private final CoverageCommunicationThread crt;

   public CoverageProcess(ProcessArgs processArgs, CoverageOptions arguments, ServerSocket socket, List<String> testClases, SideEffect1<CoverageResult> handler) throws IOException {
      this.process = new WrappingProcess(socket.getLocalPort(), processArgs, CoverageSlave.class);
      this.crt = new CoverageCommunicationThread(socket, arguments, testClases, handler);
   }

   public void start() throws IOException, InterruptedException {
      this.crt.start();
      this.process.start();
   }

   public ExitCode waitToDie() throws InterruptedException {
      ExitCode var1;
      try {
         var1 = this.crt.waitToFinish();
      } finally {
         this.process.destroy();
      }

      return var1;
   }
}
