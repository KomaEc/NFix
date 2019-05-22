package com.gzoltar.shaded.org.pitest.mutationtest.execute;

import com.gzoltar.shaded.org.pitest.mutationtest.MutationStatusMap;
import com.gzoltar.shaded.org.pitest.mutationtest.MutationStatusTestPair;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.MutationDetails;
import com.gzoltar.shaded.org.pitest.process.ProcessArgs;
import com.gzoltar.shaded.org.pitest.process.WrappingProcess;
import com.gzoltar.shaded.org.pitest.util.ExitCode;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Iterator;

public class MutationTestProcess {
   private final WrappingProcess process;
   private final MutationTestCommunicationThread thread;

   public MutationTestProcess(ServerSocket socket, ProcessArgs processArgs, SlaveArguments arguments) {
      this.process = new WrappingProcess(socket.getLocalPort(), processArgs, MutationTestSlave.class);
      this.thread = new MutationTestCommunicationThread(socket, arguments, new HashMap());
   }

   public void start() throws IOException, InterruptedException {
      this.thread.start();
      this.process.start();
   }

   public void results(MutationStatusMap allmutations) throws IOException {
      Iterator i$ = allmutations.allMutations().iterator();

      while(i$.hasNext()) {
         MutationDetails each = (MutationDetails)i$.next();
         MutationStatusTestPair status = this.thread.getStatus(each.getId());
         if (status != null) {
            allmutations.setStatusForMutation(each, status);
         }
      }

   }

   public ExitCode waitToDie() {
      ExitCode var1;
      try {
         var1 = this.thread.waitToFinish();
      } finally {
         this.process.destroy();
      }

      return var1;
   }
}
